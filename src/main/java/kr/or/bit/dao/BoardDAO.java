package kr.or.bit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.bit.dto.BoardDTO;
import kr.or.bit.dto.CommentDTO;

public class BoardDAO {
    private final DataSource ds;

    public BoardDAO() throws NamingException {
        Context context = new InitialContext();
        ds = (DataSource) context.lookup("java:comp/env/jdbc/oracle");
    }

    public int writeok(BoardDTO boarddata) {
        String sql = "insert into jspboard(idx, writer, pwd, subject, content, email, homepage, writedate, readnum, filename, filesize, refer) "
                + "values(jspboard_idx.nextval,?,?,?,?,?,?,sysdate,0,?,0,?)";
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, boarddata.getWriter());
            pstmt.setString(2, boarddata.getPwd());
            pstmt.setString(3, boarddata.getSubject());
            pstmt.setString(4, boarddata.getContent());
            pstmt.setString(5, boarddata.getEmail());
            pstmt.setString(6, boarddata.getHomepage());
            pstmt.setString(7, boarddata.getFilename());
            pstmt.setInt(8, getMaxRefer(conn) + 1);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getMaxRefer(Connection conn) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("select nvl(max(refer),0) from jspboard");
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public List<BoardDTO> list(int cpage, int pagesize) {
        int start = cpage * pagesize - (pagesize - 1);
        int end = cpage * pagesize;
        String sql = "select * from (select rownum rn, b.* from "
                + "(select * from jspboard order by refer desc, step asc) b where rownum <= ?) where rn >= ?";
        List<BoardDTO> list = new ArrayList<>();
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    public int totalBoardCount() {
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("select count(*) cnt from jspboard");
             ResultSet rs = pstmt.executeQuery()) {
            return rs.next() ? rs.getInt("cnt") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public BoardDTO getContent(int idx) {
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("select * from jspboard where idx=?")) {
            pstmt.setInt(1, idx);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? mapBoard(rs) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getReadNum(String idx) {
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("update jspboard set readnum = readnum + 1 where idx=?")) {
            pstmt.setString(1, idx);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int deleteOk(String idx, String pwd) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            if (!matchesPassword(conn, idx, pwd)) {
                conn.rollback();
                return 0;
            }
            try (PreparedStatement replyDelete = conn.prepareStatement("delete from reply where idx_fk=?");
                 PreparedStatement boardDelete = conn.prepareStatement("delete from jspboard where idx=?")) {
                replyDelete.setString(1, idx);
                replyDelete.executeUpdate();
                boardDelete.setString(1, idx);
                int row = boardDelete.executeUpdate();
                conn.commit();
                return row;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean matchesPassword(Connection conn, String idx, String pwd) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("select pwd from jspboard where idx=?")) {
            pstmt.setString(1, idx);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getString("pwd").equals(pwd);
            }
        }
    }

    public int replywrite(int idx_fk, String writer, String userid, String content, String pwd) {
        String sql = "insert into reply(no, writer, userid, content, pwd, idx_fk) values(reply_no.nextval,?,?,?,?,?)";
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, writer);
            pstmt.setString(2, userid);
            pstmt.setString(3, content);
            pstmt.setString(4, pwd);
            pstmt.setInt(5, idx_fk);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<CommentDTO> replylist(String idx_fk) {
        List<CommentDTO> list = new ArrayList<>();
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("select * from reply where idx_fk=? order by no desc")) {
            pstmt.setString(1, idx_fk);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new CommentDTO(
                            rs.getInt("no"),
                            rs.getString("writer"),
                            rs.getString("userid"),
                            rs.getString("pwd"),
                            rs.getString("content"),
                            rs.getDate("writedate"),
                            rs.getInt("idx_fk")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int replyDelete(String no, String pwd) {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement select = conn.prepareStatement("select pwd from reply where no=?")) {
                select.setString(1, no);
                try (ResultSet rs = select.executeQuery()) {
                    if (!rs.next() || !rs.getString("pwd").equals(pwd)) {
                        return 0;
                    }
                }
            }
            try (PreparedStatement delete = conn.prepareStatement("delete from reply where no=?")) {
                delete.setString(1, no);
                return delete.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int reWriteOk(BoardDTO boarddata) {
        String selectSql = "select refer, depth, step from jspboard where idx=?";
        String updateSql = "update jspboard set step = step + 1 where step > ? and refer = ?";
        String insertSql = "insert into jspboard(idx, writer, pwd, subject, content, email, homepage, writedate, readnum, filename, filesize, refer, depth, step) "
                + "values(jspboard_idx.nextval,?,?,?,?,?,?,sysdate,0,?,0,?,?,?)";
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            int refer;
            int depth;
            int step;
            try (PreparedStatement select = conn.prepareStatement(selectSql)) {
                select.setInt(1, boarddata.getIdx());
                try (ResultSet rs = select.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return 0;
                    }
                    refer = rs.getInt("refer");
                    depth = rs.getInt("depth");
                    step = rs.getInt("step");
                }
            }
            try (PreparedStatement update = conn.prepareStatement(updateSql)) {
                update.setInt(1, step);
                update.setInt(2, refer);
                update.executeUpdate();
            }
            try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                insert.setString(1, boarddata.getWriter());
                insert.setString(2, boarddata.getPwd());
                insert.setString(3, boarddata.getSubject());
                insert.setString(4, boarddata.getContent());
                insert.setString(5, boarddata.getEmail());
                insert.setString(6, boarddata.getHomepage());
                insert.setString(7, boarddata.getFilename());
                insert.setInt(8, refer);
                insert.setInt(9, depth + 1);
                insert.setInt(10, step + 1);
                int row = insert.executeUpdate();
                conn.commit();
                return row;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public BoardDTO getEditContent(String idx) {
        try {
            return getContent(Integer.parseInt(idx));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int boardEdit(HttpServletRequest boarddata) {
        String idx = boarddata.getParameter("idx");
        String pwd = boarddata.getParameter("pwd");
        String sql = "update jspboard set writer=?, email=?, homepage=?, subject=?, content=?, filename=? where idx=? and pwd=?";
        try (Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, boarddata.getParameter("writer"));
            pstmt.setString(2, boarddata.getParameter("email"));
            pstmt.setString(3, boarddata.getParameter("homepage"));
            pstmt.setString(4, boarddata.getParameter("subject"));
            pstmt.setString(5, boarddata.getParameter("content"));
            pstmt.setString(6, boarddata.getParameter("filename"));
            pstmt.setString(7, idx);
            pstmt.setString(8, pwd);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private BoardDTO mapBoard(ResultSet rs) throws SQLException {
        return new BoardDTO(
                rs.getInt("idx"),
                rs.getString("writer"),
                rs.getString("pwd"),
                rs.getString("subject"),
                rs.getString("content"),
                rs.getDate("writedate"),
                rs.getInt("readnum"),
                rs.getString("filename"),
                rs.getInt("filesize"),
                rs.getString("homepage"),
                rs.getString("email"),
                rs.getInt("refer"),
                rs.getInt("depth"),
                rs.getInt("step"));
    }
}
