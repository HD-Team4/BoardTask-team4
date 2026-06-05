package kr.or.bit.action.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dto.BoardDTO;
import kr.or.bit.service.BoardService;
import kr.or.bit.utils.HtmlEscapeUtil;

public class BoardWriteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String writer = request.getParameter("writer");
        String password = request.getParameter("password");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // XSS 방지를 위한 HTML Escaping
        title = HtmlEscapeUtil.escape(title);
        content = HtmlEscapeUtil.escape(content);
        writer = HtmlEscapeUtil.escape(writer);

        BoardDTO board = new BoardDTO();
        board.setWriter(writer);
        board.setPassword(password);
        board.setTitle(title);
        board.setContent(content);

        BoardService service = BoardService.getInstance();
        int result = service.write(board);

        ActionForward forward = new ActionForward();
        if (result > 0) {
            forward.setRedirect(true);
            forward.setPath(request.getContextPath() + "/board/list.do");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>alert('글 등록에 실패했습니다.'); history.back();</script>");
            return null;
        }
        return forward;
    }
}
