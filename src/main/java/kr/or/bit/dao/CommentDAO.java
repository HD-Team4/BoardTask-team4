package kr.or.bit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kr.or.bit.dto.CommentDTO;
import kr.or.bit.utils.DBConnection;

public class CommentDAO {

    public List<CommentDTO> list(int boardId) {
        String sql = "select comment_id, board_id, writer, password, content, " +
                "to_char(created_at, 'yyyy-mm-dd hh24:mi:ss') created_at, " +
                "to_char(updated_at, 'yyyy-mm-dd hh24:mi:ss') updated_at " +
                "from reply where board_id = ? order by comment_id desc";
        List<CommentDTO> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new CommentDTO(
                            rs.getInt("comment_id"),
                            rs.getInt("board_id"),
                            rs.getString("writer"),
                            rs.getString("password"),
                            rs.getString("content"),
                            rs.getString("created_at"),
                            rs.getString("updated_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int write(CommentDTO comment) {
        String sql = "insert into reply (comment_id, board_id, writer, password, content) " +
                "values (seq_reply_id.nextval, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, comment.getBoardId());
            pstmt.setString(2, comment.getWriter());
            pstmt.setString(3, comment.getPassword());
            pstmt.setString(4, comment.getContent());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int modify(CommentDTO comment) {
        String sql = "update reply set content = ?, writer = ?, updated_at = sysdate " +
                "where comment_id = ? and password = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, comment.getContent());
            pstmt.setString(2, comment.getWriter());
            pstmt.setInt(3, comment.getCommentId());
            pstmt.setString(4, comment.getPassword());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int delete(int commentId, String password) {
        String sql = "delete from reply where comment_id = ? and password = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);
            pstmt.setString(2, password);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
