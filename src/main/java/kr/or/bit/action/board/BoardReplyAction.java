package kr.or.bit.action.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dto.BoardDTO;
import kr.or.bit.service.BoardService;
import kr.or.bit.utils.HtmlEscapeUtil;

public class BoardReplyAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parentIdStr = request.getParameter("boardId");
        String writer = request.getParameter("writer");
        String password = request.getParameter("password");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String cpage = request.getParameter("cp");
        String pagesize = request.getParameter("ps");

        if (parentIdStr == null || parentIdStr.trim().isEmpty()) {
            ActionForward forward = new ActionForward();
            forward.setRedirect(true);
            forward.setPath(request.getContextPath() + "/board/list.do");
            return forward;
        }

        int parentId = Integer.parseInt(parentIdStr);

        // XSS 방지를 위한 HTML Escaping
        title = HtmlEscapeUtil.escape(title);
        content = HtmlEscapeUtil.escape(content);
        writer = HtmlEscapeUtil.escape(writer);

        BoardDTO child = new BoardDTO();
        child.setWriter(writer);
        child.setPassword(password);
        child.setTitle(title);
        child.setContent(content);

        BoardService service = BoardService.getInstance();
        int result = service.reply(parentId, child);

        ActionForward forward = new ActionForward();
        if (result > 0) {
            forward.setRedirect(true);
            forward.setPath(request.getContextPath() + "/board/list.do?cp=" + cpage + "&ps=" + pagesize);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>alert('답글 등록에 실패했습니다.'); history.back();</script>");
            return null;
        }
        return forward;
    }
}
