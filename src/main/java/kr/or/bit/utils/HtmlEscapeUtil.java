package kr.or.bit.utils;

public class HtmlEscapeUtil {
    public static String escape(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\'':
                    builder.append("&#x27;");
                    break;
                case '/':
                    builder.append("&#x2F;");
                    break;
                default:
                    builder.append(c);
            }
        }
        return builder.toString();
    }
}
