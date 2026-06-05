package kr.or.bit.utils;

public class PageUtil {
	 
	 private int pageSize;//???섏씠吏???곗씠??媛쒖닔
	 private int pagerSize;//踰덊샇濡?蹂댁뿬二쇰뒗 ?섏씠吏 Link 媛쒖닔
	 private int dataCount;//珥??곗씠????
	 private int currentPage;//?꾩옱 ?섏씠吏 踰덊샇
	 private int pageCount;//珥??섏씠吏 ??
	 
	 private String linkUrl;//?섏씠?媛 ?ы븿?섎뒗 ?섏씠吏??二쇱냼
	 
	 public PageUtil(int dataCount, int currentPage, 
	  int pageSize, int pagerSize, String linkUrl) {
	  
	  this.linkUrl = linkUrl;
	  
	  this.dataCount = dataCount;
	  this.pageSize = pageSize;
	  this.pagerSize = pagerSize;
	  this.currentPage = currentPage;  
	  pageCount = 
	   (dataCount / pageSize) + ((dataCount % pageSize) > 0 ? 1 : 0); 
	 }
	 
	 public String toString(){
	  StringBuffer linkString = new StringBuffer();
	  
	  //1. 泥섏쓬, ?댁쟾 ??ぉ 留뚮뱾湲?
	  if (currentPage > 1) {
	   linkString.append(
	    String.format("[<a href='%s?pageno=1'>泥섏쓬</a>]",linkUrl));
	   linkString.append("&nbsp;");
	   linkString.append("&nbsp;");
	   linkString.append(String.format(
	    "[<a href='%s?pageno=%d'>?댁쟾</a>]", linkUrl, currentPage - 1));
	   linkString.append("&nbsp;");
	  }
	  
	  //2. ?섏씠吏 踰덊샇 Link 留뚮뱾湲?
	  int pagerBlock = (currentPage - 1) / pagerSize;
	  int start = (pagerBlock * pagerSize) + 1;
	  int end = start + pagerSize;
	  for (int i = start; i < end; i++) {
	   if (i > pageCount) break;
	   linkString.append("&nbsp;");
	   if(i == currentPage) {
	    linkString.append(String.format("[%d]", i));
	   } else { 
	    linkString.append(String.format(
	     "<a href='%s?pageno=%d'>%d</a>", linkUrl, i, i));
	   }
	   linkString.append("&nbsp;");
	  }
	  
	  //3. ?ㅼ쓬, 留덉?留???ぉ 留뚮뱾湲?
	  if (currentPage < pageCount) {
	   linkString.append("&nbsp;");
	   linkString.append(String.format(
	    "[<a href='%s?pageno=%d'>?ㅼ쓬</a>]",linkUrl, currentPage + 1));
	   linkString.append("&nbsp;");
	   linkString.append("&nbsp;");
	   linkString.append(String.format(
	    "[<a href='%s?pageno=%d'>留덉?留?/a>]", linkUrl, pageCount));
	  }
	  
	  return linkString.toString();
	 }
	 
	}
