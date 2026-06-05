package kr.or.bit.action.comment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.service.CommentService;

public class CommentDeleteAjaxAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String commentIdStr = request.getParameter("commentId");
        String password = request.getParameter("password");

        if (commentIdStr == null || commentIdStr.trim().isEmpty() || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        int commentId = Integer.parseInt(commentIdStr);

        CommentService service = CommentService.getInstance();
        int result = service.delete(commentId, password);

        response.setContentType("application/json;charset=UTF-8");
        if (result > 0) {
            response.getWriter().print("{\"result\":\"success\"}");
        } else {
            response.getWriter().print("{\"result\":\"fail\"}");
        }
        return null;
    }
}
