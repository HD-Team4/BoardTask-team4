package kr.or.bit.service;

import java.util.List;

import javax.naming.NamingException;
import jakarta.servlet.http.HttpServletRequest;
import kr.or.bit.dao.BoardDAO;
import kr.or.bit.dto.BoardDTO;
import kr.or.bit.dto.CommentDTO;

//JSP 諛쏅뒗 ?붿껌 (?쒕퉬??瑜??ㅽ뻾?섎뒗 遺遺?
public class BoardService {
	private static BoardService instance = new BoardService();
	private BoardService() {}
	public static BoardService getInBoardService() {
		return instance; 
	}
	
	//?쒕퉬???붿껌(湲?곌린)
		public int writeOk(BoardDTO boarddata) throws Exception {
			BoardDAO dao = new BoardDAO();
			int result = dao.writeok(boarddata);
			return result;
		}
		
		//?쒕퉬???붿껌(湲紐⑸줉 蹂댁뿬二쇨린)
		public List<BoardDTO> list(int cpage, int pagesize) throws Exception{
			BoardDAO dao = new BoardDAO();
			return dao.list(cpage, pagesize);
		}
		
		//?쒕퉬???붿껌(湲紐⑸줉 寃뚯떆臾?珥?嫄댁닔)
		public int totalBoardCount() throws Exception {
			BoardDAO dao = new BoardDAO();
			return dao.totalBoardCount();
		}
		
		//?쒕퉬???붿껌(湲 ?곸꽭蹂닿린)
		public BoardDTO content(int idx) throws NamingException {
			return new BoardDAO().getContent(idx);
		}
		
		//?쒕퉬???붿껌 (湲 ?곸꽭蹂닿린 ??議고쉶??利앷??섍린)
		public boolean  addReadNum(String idx) throws NamingException {
			return new BoardDAO().getReadNum(idx);
		}
		
		//?쒕퉬???붿껌(寃뚯떆湲 ??젣?섍린) : jspboard , CommentDTO 
		public int board_Delete(String idx , String pwd) throws NamingException {
			return new BoardDAO().deleteOk(idx, pwd);
		}
		
		//?쒕퉬???붿껌(?볤? ?낅젰?섍린)
		public int replyWrite(int idx_fk,String writer,String userid, String content,String pwd) throws NamingException {
			return new BoardDAO().replywrite(idx_fk, writer, userid, content, pwd);
		}
		
		//?쒕퉬???붿껌(?볤? 紐⑸줉 議고쉶?섍린)
		public List<CommentDTO> replyList(String idx_fk) throws NamingException{
			return new BoardDAO().replylist(idx_fk);
		}
		
		//?쒕퉬???붿껌(?볤? ??젣?섍린)
		public int replyDelete(String no, String pwd) throws NamingException {
			return new BoardDAO().replyDelete(no, pwd);
		}
		
		//?쒕퉬???붿껌(寃뚯떆臾??곸꽭議고쉶  > ?듦? ?곌린(rewriteok)
		public int rewriteok(BoardDTO boardata) throws Exception {
			return new BoardDAO().reWriteOk(boardata);
		}
		
		//?쒕퉬?? ?붿껌(?섏젙 ?곗씠??議고쉶 )
		public BoardDTO board_EditContent(String idx) throws NamingException {
			return new BoardDAO().getEditContent(idx);
		}
		
		//?ㅻⅨ ?뚯뒪??(BoardDTO DTO Parameter ?ъ슜)
		//request ?붿껌 媛앹껜瑜?Parameter ?ъ슜 (?μ젏 : view 肄붾뱶 媛먯냼)
		public int board_Edit(HttpServletRequest req) throws NamingException {
			
			return new BoardDAO().boardEdit(req);
		}
	
}








