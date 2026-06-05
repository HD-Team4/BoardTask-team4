package kr.or.bit.action.comment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dto.CommentDTO;
import kr.or.bit.service.CommentService;
import kr.or.bit.utils.HtmlEscapeUtil;

public class CommentModifyAjaxAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String commentIdStr = request.getParameter("commentId");
        String writer = request.getParameter("writer");
        String password = request.getParameter("password");
        String content = request.getParameter("content");

        if (commentIdStr == null || commentIdStr.trim().isEmpty() || writer == null || password == null || content == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        int commentId = Integer.parseInt(commentIdStr);

        writer = HtmlEscapeUtil.escape(writer);
        content = HtmlEscapeUtil.escape(content);

        CommentDTO comment = new CommentDTO();
        comment.setCommentId(commentId);
        comment.setWriter(writer);
        comment.setPassword(password);
        comment.setContent(content);

        CommentService service = CommentService.getInstance();
        int result = service.modify(comment);

        response.setContentType("application/json;charset=UTF-8");
        if (result > 0) {
            response.getWriter().print("{\"result\":\"success\"}");
        } else {
            response.getWriter().print("{\"result\":\"fail\"}");
        }
        return null;
    }
}
