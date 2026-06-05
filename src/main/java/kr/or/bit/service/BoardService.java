package kr.or.bit.service;

import java.util.List;
import kr.or.bit.dao.BoardDAO;
import kr.or.bit.dto.BoardDTO;

public class BoardService {
    private static final BoardService instance = new BoardService();
    private final BoardDAO dao;

    private BoardService() {
        this.dao = new BoardDAO();
    }

    public static BoardService getInstance() {
        return instance;
    }

    public List<BoardDTO> list(int cpage, int pagesize) {
        return dao.list(cpage, pagesize);
    }

    public int totalBoardCount() {
        return dao.totalBoardCount();
    }

    public int write(BoardDTO board) {
        return dao.write(board);
    }

    public int reply(int parentId, BoardDTO child) {
        return dao.reply(parentId, child);
    }

    public BoardDTO getContent(int boardId) {
        return dao.getContent(boardId);
    }

    public boolean addReadCount(int boardId) {
        return dao.addReadCount(boardId);
    }

    public int modify(BoardDTO board) {
        return dao.modify(board);
    }

    public int delete(int boardId, String password) {
        return dao.delete(boardId, password);
    }

    public boolean verifyPassword(int boardId, String password) {
        return dao.verifyPassword(boardId, password);
    }
}
