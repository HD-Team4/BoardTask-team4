package kr.or.bit.action.board;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.dto.BoardDTO;
import kr.or.bit.service.BoardService;
import kr.or.bit.utils.PageUtil;

public class BoardListAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BoardService service = BoardService.getInstance();
        int totalboardcount = service.totalBoardCount();

        String ps = request.getParameter("ps");
        String cp = request.getParameter("cp");
        if (ps == null || ps.trim().isEmpty()) {
            ps = "5";
        }
        if (cp == null || cp.trim().isEmpty()) {
            cp = "1";
        }

        int pagesize = Integer.parseInt(ps);
        int cpage = Integer.parseInt(cp);
        int pagecount = PageUtil.getPageCount(totalboardcount, pagesize);
        List<BoardDTO> list = service.list(cpage, pagesize);

        request.setAttribute("totalboardcount", totalboardcount);
        request.setAttribute("pagesize", pagesize);
        request.setAttribute("cpage", cpage);
        request.setAttribute("pagecount", pagecount);
        request.setAttribute("list", list);

        ActionForward forward = new ActionForward();
        forward.setRedirect(false);
        forward.setPath("/WEB-INF/views/board/list.jsp");
        return forward;
    }
}
