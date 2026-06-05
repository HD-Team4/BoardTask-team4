package kr.or.bit.action.comment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dto.CommentDTO;
import kr.or.bit.service.CommentService;
import kr.or.bit.utils.HtmlEscapeUtil;

public class CommentWriteAjaxAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String boardIdStr = request.getParameter("boardId");
        String writer = request.getParameter("writer");
        String password = request.getParameter("password");
        String content = request.getParameter("content");

        if (boardIdStr == null || boardIdStr.trim().isEmpty() || writer == null || password == null || content == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        int boardId = Integer.parseInt(boardIdStr);

        // XSS 방지 처리
        writer = HtmlEscapeUtil.escape(writer);
        content = HtmlEscapeUtil.escape(content);

        CommentDTO comment = new CommentDTO();
        comment.setBoardId(boardId);
        comment.setWriter(writer);
        comment.setPassword(password);
        comment.setContent(content);

        CommentService service = CommentService.getInstance();
        int result = service.write(comment);

        response.setContentType("application/json;charset=UTF-8");
        if (result > 0) {
            response.getWriter().print("{\"result\":\"success\"}");
        } else {
            response.getWriter().print("{\"result\":\"fail\"}");
        }
        return null;
    }
}
