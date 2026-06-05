package kr.or.bit.action.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dto.BoardDTO;
import kr.or.bit.service.BoardService;

public class BoardReplyFormAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String boardIdStr = request.getParameter("boardId");
        ActionForward forward = new ActionForward();

        if (boardIdStr == null || boardIdStr.trim().isEmpty()) {
            forward.setRedirect(true);
            forward.setPath(request.getContextPath() + "/board/list.do");
            return forward;
        }

        int boardId = Integer.parseInt(boardIdStr);
        String cpage = request.getParameter("cp");
        String pagesize = request.getParameter("ps");

        BoardService service = BoardService.getInstance();
        BoardDTO parentBoard = service.getContent(boardId);

        if (parentBoard == null) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>alert('부모 게시글을 찾을 수 없습니다.'); history.back();</script>");
            return null;
        }

        request.setAttribute("boardId", boardId);
        request.setAttribute("cp", cpage);
        request.setAttribute("ps", pagesize);
        request.setAttribute("parentBoard", parentBoard);

        forward.setRedirect(false);
        forward.setPath("/WEB-INF/views/board/reply.jsp");
        return forward;
    }
}
