package kr.or.bit.action.comment;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dto.CommentDTO;
import kr.or.bit.service.CommentService;

public class CommentListAjaxAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String boardIdStr = request.getParameter("boardId");
        if (boardIdStr == null || boardIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        int boardId = Integer.parseInt(boardIdStr);
        CommentService service = CommentService.getInstance();
        List<CommentDTO> list = service.list(boardId);

        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < list.size(); i++) {
            CommentDTO comment = list.get(i);
            json.append("{");
            json.append("\"commentId\":").append(comment.getCommentId()).append(",");
            json.append("\"boardId\":").append(comment.getBoardId()).append(",");
            json.append("\"writer\":\"").append(escapeJson(comment.getWriter())).append("\",");
            json.append("\"content\":\"").append(escapeJson(comment.getContent())).append("\",");
            json.append("\"createdAt\":\"").append(comment.getCreatedAt()).append("\",");
            json.append("\"updatedAt\":\"").append(comment.getUpdatedAt()).append("\"");
            json.append("}");
            if (i < list.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(json.toString());
        return null;
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\r", "\\r")
                    .replace("\n", "\\n")
                    .replace("\t", "\\t");
    }
}
