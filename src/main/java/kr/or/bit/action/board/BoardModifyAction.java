package kr.or.bit.action.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dto.BoardDTO;
import kr.or.bit.service.BoardService;
import kr.or.bit.utils.HtmlEscapeUtil;

public class BoardModifyAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String boardIdStr = request.getParameter("boardId");
        String writer = request.getParameter("writer");
        String password = request.getParameter("password");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String cpage = request.getParameter("cp");
        String pagesize = request.getParameter("ps");

        if (boardIdStr == null || boardIdStr.trim().isEmpty()) {
            ActionForward forward = new ActionForward();
            forward.setRedirect(true);
            forward.setPath(request.getContextPath() + "/board/list.do");
            return forward;
        }

        int boardId = Integer.parseInt(boardIdStr);
        BoardService service = BoardService.getInstance();

        // 비밀번호 검증
        if (!service.verifyPassword(boardId, password)) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
            return null;
        }

        // XSS 방지를 위한 HTML Escaping
        title = HtmlEscapeUtil.escape(title);
        content = HtmlEscapeUtil.escape(content);
        writer = HtmlEscapeUtil.escape(writer);

        BoardDTO board = new BoardDTO();
        board.setBoardId(boardId);
        board.setWriter(writer);
        board.setPassword(password);
        board.setTitle(title);
        board.setContent(content);

        int result = service.modify(board);

        ActionForward forward = new ActionForward();
        if (result > 0) {
            forward.setRedirect(true);
            forward.setPath(request.getContextPath() + "/board/detail.do?boardId=" + boardId + "&cp=" + cpage + "&ps=" + pagesize);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>alert('게시글 수정에 실패했습니다.'); history.back();</script>");
            return null;
        }
        return forward;
    }
}
