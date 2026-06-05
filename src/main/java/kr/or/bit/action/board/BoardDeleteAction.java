package kr.or.bit.action.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.service.BoardService;

public class BoardDeleteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String boardIdStr = request.getParameter("boardId");
        String password = request.getParameter("password");
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

        int result = service.delete(boardId, password);

        ActionForward forward = new ActionForward();
        if (result > 0) {
            forward.setRedirect(true);
            forward.setPath(request.getContextPath() + "/board/list.do?cp=" + cpage + "&ps=" + pagesize);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script>alert('게시글 삭제에 실패했습니다.'); history.back();</script>");
            return null;
        }
        return forward;
    }
}
