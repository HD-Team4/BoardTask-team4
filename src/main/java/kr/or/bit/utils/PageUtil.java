package kr.or.bit.utils;

public class PageUtil {
    public static int getPageCount(int totalCount, int pageSize) {
        if (totalCount == 0) {
            return 1;
        }
        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
