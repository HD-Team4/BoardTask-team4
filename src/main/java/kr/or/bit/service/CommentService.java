package kr.or.bit.service;

import java.util.List;
import kr.or.bit.dao.CommentDAO;
import kr.or.bit.dto.CommentDTO;

public class CommentService {
    private static final CommentService instance = new CommentService();
    private final CommentDAO dao;

    private CommentService() {
        this.dao = new CommentDAO();
    }

    public static CommentService getInstance() {
        return instance;
    }

    public List<CommentDTO> list(int boardId) {
        return dao.list(boardId);
    }

    public int write(CommentDTO comment) {
        return dao.write(comment);
    }

    public int modify(CommentDTO comment) {
        return dao.modify(comment);
    }

    public int delete(int commentId, String password) {
        return dao.delete(commentId, password);
    }
}
