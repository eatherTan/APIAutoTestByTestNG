package util;

import java.util.Date;
import java.util.HashMap;

public class HeaderMap {
    /**
     * 添加Header
     * 需要什么header，可以自行定义
     * @return
     */
    public static HashMap<String, String> addHeader() {
        HashMap<String, String> headermap = new HashMap<String, String>();
        Long timestamp = new Date().getTime();
        String Authkey = "tan-1234";
        String Timestamp = timestamp.toString();
        headermap.put("Authkey", Authkey);
        headermap.put("Timestamp", Timestamp);
        headermap.put("UserId", "1");
        return headermap;

    }
}
