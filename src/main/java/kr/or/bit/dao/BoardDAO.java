package kr.or.bit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import kr.or.bit.dto.BoardDTO;
import kr.or.bit.utils.DBConnection;

public class BoardDAO {

    public BoardDAO() {
        // 테이블 자동 이관/초기화 감지 코드
        try (Connection conn = DBConnection.getConnection()) {
            boolean rebuildNeeded = false;
            try (PreparedStatement pstmt = conn.prepareStatement("select board_id from jspboard where 1=0")) {
                pstmt.executeQuery();
            } catch (SQLException e) {
                rebuildNeeded = true;
            }

            if (rebuildNeeded) {
                System.out.println("Tomcat Board App: Rebuilding database tables to match the new DTO/schema requirements...");
                try (Statement stmt = conn.createStatement()) {
                    try { stmt.execute("DROP TABLE reply CASCADE CONSTRAINTS"); } catch (Exception ignored) {}
                    try { stmt.execute("DROP TABLE jspboard CASCADE CONSTRAINTS"); } catch (Exception ignored) {}
                    try { stmt.execute("DROP SEQUENCE seq_jspboard_id"); } catch (Exception ignored) {}
                    try { stmt.execute("DROP SEQUENCE seq_reply_id"); } catch (Exception ignored) {}

                    stmt.execute("CREATE TABLE jspboard (" +
                                 "    board_id NUMBER PRIMARY KEY," +
                                 "    writer VARCHAR2(50) NOT NULL," +
                                 "    password VARCHAR2(50) NOT NULL," +
                                 "    title VARCHAR2(200) NOT NULL," +
                                 "    content VARCHAR2(4000) NOT NULL," +
                                 "    read_count NUMBER DEFAULT 0," +
                                 "    ref NUMBER DEFAULT 0," +
                                 "    re_step NUMBER DEFAULT 0," +
                                 "    re_level NUMBER DEFAULT 0," +
                                 "    created_at DATE DEFAULT SYSDATE," +
                                 "    updated_at DATE DEFAULT SYSDATE" +
                                 ")");

                    stmt.execute("CREATE SEQUENCE seq_jspboard_id START WITH 1 INCREMENT BY 1 NOCACHE");

                    stmt.execute("CREATE TABLE reply (" +
                                 "    comment_id NUMBER PRIMARY KEY," +
                                 "    board_id NUMBER NOT NULL," +
                                 "    writer VARCHAR2(50) NOT NULL," +
                                 "    password VARCHAR2(50) NOT NULL," +
                                 "    content VARCHAR2(1000) NOT NULL," +
                                 "    created_at DATE DEFAULT SYSDATE," +
                                 "    updated_at DATE DEFAULT SYSDATE," +
                                 "    CONSTRAINT fk_jspboard_board_id FOREIGN KEY (board_id) REFERENCES jspboard(board_id) ON DELETE CASCADE" +
                                 ")");

                    stmt.execute("CREATE SEQUENCE seq_reply_id START WITH 1 INCREMENT BY 1 NOCACHE");
                    System.out.println("Tomcat Board App: Database schema successfully initialized.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 1. 게시글 목록 조회 (페이징 + 계층 정렬)
    public List<BoardDTO> list(int cpage, int pagesize) {
        int start = cpage * pagesize - (pagesize - 1);
        int end = cpage * pagesize;
        String sql = "select * from (select rownum rn, b.* from " +
                "(select board_id, writer, password, title, content, read_count, ref, re_step, re_level, " +
                "to_char(created_at, 'yyyy-mm-dd hh24:mi:ss') created_at, " +
                "to_char(updated_at, 'yyyy-mm-dd hh24:mi:ss') updated_at from jspboard " +
                "order by ref desc, re_step asc) b where rownum <= ?) where rn >= ?";

        List<BoardDTO> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, end);
            pstmt.setInt(2, start);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBoard(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. 전체 게시글 개수 조회
    public int totalBoardCount() {
        String sql = "select count(*) cnt from jspboard";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getInt("cnt") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 3. 원글 쓰기
    public int write(BoardDTO board) {
        String seqSql = "select seq_jspboard_id.nextval from dual";
        String insertSql = "insert into jspboard(board_id, writer, password, title, content, ref, re_step, re_level) " +
                "values(?, ?, ?, ?, ?, ?, 0, 0)";

        try (Connection conn = DBConnection.getConnection()) {
            int nextId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(seqSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    nextId = rs.getInt(1);
                }
            }

            if (nextId == 0) return 0;

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, nextId);
                pstmt.setString(2, board.getWriter());
                pstmt.setString(3, board.getPassword());
                pstmt.setString(4, board.getTitle());
                pstmt.setString(5, board.getContent());
                pstmt.setInt(6, nextId); // ref = board_id
                return pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 4. 답글 쓰기
    public int reply(int parentId, BoardDTO child) {
        String selectSql = "select ref, re_step, re_level from jspboard where board_id = ?";
        String updateSql = "update jspboard set re_step = re_step + 1 where ref = ? and re_step > ?";
        String insertSql = "insert into jspboard(board_id, writer, password, title, content, ref, re_step, re_level) " +
                "values(seq_jspboard_id.nextval, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            int ref = 0;
            int reStep = 0;
            int reLevel = 0;

            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setInt(1, parentId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        ref = rs.getInt("ref");
                        reStep = rs.getInt("re_step");
                        reLevel = rs.getInt("re_level");
                    } else {
                        conn.rollback();
                        return 0;
                    }
                }
            }

            // 부모 아래의 다른 답글들의 순서를 뒤로 미룸
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, ref);
                pstmt.setInt(2, reStep);
                pstmt.executeUpdate();
            }

            // 새 답글 등록
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, child.getWriter());
                pstmt.setString(2, child.getPassword());
                pstmt.setString(3, child.getTitle());
                pstmt.setString(4, child.getContent());
                pstmt.setInt(5, ref);
                pstmt.setInt(6, reStep + 1);
                pstmt.setInt(7, reLevel + 1);
                int row = pstmt.executeUpdate();
                conn.commit();
                return row;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 5. 게시글 상세 조회
    public BoardDTO getContent(int boardId) {
        String sql = "select board_id, writer, password, title, content, read_count, ref, re_step, re_level, " +
                "to_char(created_at, 'yyyy-mm-dd hh24:mi:ss') created_at, " +
                "to_char(updated_at, 'yyyy-mm-dd hh24:mi:ss') updated_at from jspboard where board_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? mapBoard(rs) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 6. 조회물 증가
    public boolean addReadCount(int boardId) {
        String sql = "update jspboard set read_count = read_count + 1 where board_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 7. 게시글 수정
    public int modify(BoardDTO board) {
        String sql = "update jspboard set title = ?, content = ?, writer = ?, updated_at = sysdate " +
                "where board_id = ? and password = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setString(3, board.getWriter());
            pstmt.setInt(4, board.getBoardId());
            pstmt.setString(5, board.getPassword());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 8. 게시글 삭제 (비밀번호 인증 및 논리/물리 삭제 분기 + 상위 논리 삭제 게시글 재귀 정리)
    public int delete(int boardId, String password) {
        String selectSql = "select ref, re_step, re_level from jspboard where board_id = ? and password = ?";
        String checkChildSql = "select re_level from jspboard where ref = ? and re_step > ? order by re_step asc";
        String deleteSql = "delete from jspboard where board_id = ?";
        String updateSql = "update jspboard set title = '삭제된 게시물입니다.', content = '삭제된 게시물입니다.', writer = '(삭제됨)', password = '_DELETED_', updated_at = sysdate where board_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            int ref = 0;
            int reStep = 0;
            int reLevel = 0;
            boolean exists = false;

            try {
                try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                    pstmt.setInt(1, boardId);
                    pstmt.setString(2, password);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            ref = rs.getInt("ref");
                            reStep = rs.getInt("re_step");
                            reLevel = rs.getInt("re_level");
                            exists = true;
                        }
                    }
                }

                if (!exists) {
                    conn.rollback();
                    return 0; // 패스워드 불일치 또는 미존재
                }

                boolean hasChild = false;
                try (PreparedStatement pstmt = conn.prepareStatement(checkChildSql)) {
                    pstmt.setInt(1, ref);
                    pstmt.setInt(2, reStep);
                    pstmt.setMaxRows(1);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            int nextReLevel = rs.getInt("re_level");
                            if (nextReLevel > reLevel) {
                                hasChild = true;
                            }
                        }
                    }
                }

                int result = 0;
                if (hasChild) {
                    try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                        pstmt.setInt(1, boardId);
                        result = pstmt.executeUpdate();
                    }
                } else {
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                        pstmt.setInt(1, boardId);
                        result = pstmt.executeUpdate();
                    }
                }

                // 하위 답글이 없는 논리 삭제된 부모 글들의 재귀적 물리 삭제 처리
                boolean cleaned;
                do {
                    cleaned = false;
                    String findDeletedSql = "select board_id, re_step, re_level from jspboard where ref = ? and password = '_DELETED_'";
                    List<BoardDTO> deletedPosts = new ArrayList<>();
                    try (PreparedStatement pstmt = conn.prepareStatement(findDeletedSql)) {
                        pstmt.setInt(1, ref);
                        try (ResultSet rs = pstmt.executeQuery()) {
                            while (rs.next()) {
                                BoardDTO p = new BoardDTO();
                                p.setBoardId(rs.getInt("board_id"));
                                p.setReStep(rs.getInt("re_step"));
                                p.setReLevel(rs.getInt("re_level"));
                                deletedPosts.add(p);
                            }
                        }
                    }

                    for (BoardDTO p : deletedPosts) {
                        boolean hasDescendant = false;
                        try (PreparedStatement pstmt = conn.prepareStatement(checkChildSql)) {
                            pstmt.setInt(1, ref);
                            pstmt.setInt(2, p.getReStep());
                            pstmt.setMaxRows(1);
                            try (ResultSet rs = pstmt.executeQuery()) {
                                if (rs.next()) {
                                    int nextReLevel = rs.getInt("re_level");
                                    if (nextReLevel > p.getReLevel()) {
                                        hasDescendant = true;
                                    }
                                }
                            }
                        }

                        if (!hasDescendant) {
                            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                                pstmt.setInt(1, p.getBoardId());
                                pstmt.executeUpdate();
                            }
                            cleaned = true;
                            break;
                        }
                    }
                } while (cleaned);

                conn.commit();
                return result;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 9. 비밀번호 검증
    public boolean verifyPassword(int boardId, String password) {
        String sql = "select count(*) cnt from jspboard where board_id = ? and password = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardId);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private BoardDTO mapBoard(ResultSet rs) throws SQLException {
        return new BoardDTO(
                rs.getInt("board_id"),
                rs.getString("writer"),
                rs.getString("password"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getInt("read_count"),
                rs.getInt("ref"),
                rs.getInt("re_step"),
                rs.getInt("re_level"),
                rs.getString("created_at"),
                rs.getString("updated_at")
        );
    }
}
