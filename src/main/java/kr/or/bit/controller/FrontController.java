package kr.or.bit.controller;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import kr.or.bit.action.Action;
import kr.or.bit.action.ActionForward;
import kr.or.bit.action.board.*;
import kr.or.bit.action.comment.*;

@WebServlet(urlPatterns = {"*.do", "*.ajax"})
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String urlCommand = requestURI.substring(contextPath.length());

        Action action = null;
        ActionForward forward = null;

        try {
            // 게시판 관련 요청
            if (urlCommand.equals("/board/list.do")) {
                action = new BoardListAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/board/detail.do")) {
                action = new BoardDetailAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/board/writeForm.do")) {
                action = new BoardWriteFormAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/board/write.do")) {
                action = new BoardWriteAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/board/modifyForm.do")) {
                action = new BoardModifyFormAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/board/modify.do")) {
                action = new BoardModifyAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/board/delete.do")) {
                action = new BoardDeleteAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/board/replyForm.do")) {
                action = new BoardReplyFormAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/board/reply.do")) {
                action = new BoardReplyAction();
                forward = action.execute(request, response);
            }
            // 댓글 관련 비동기 요청
            else if (urlCommand.equals("/comment/list.ajax")) {
                action = new CommentListAjaxAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/comment/write.ajax")) {
                action = new CommentWriteAjaxAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/comment/modify.ajax")) {
                action = new CommentModifyAjaxAction();
                forward = action.execute(request, response);
            } else if (urlCommand.equals("/comment/delete.ajax")) {
                action = new CommentDeleteAjaxAction();
                forward = action.execute(request, response);
            }

            if (forward != null) {
                if (forward.isRedirect()) {
                    response.sendRedirect(forward.getPath());
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
                    dispatcher.forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }
}
