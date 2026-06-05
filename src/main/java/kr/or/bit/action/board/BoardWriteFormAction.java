package kr.or.bit.action.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;

public class BoardWriteFormAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = new ActionForward();
        forward.setRedirect(false);
        forward.setPath("/WEB-INF/views/board/write.jsp");
        return forward;
    }
}
