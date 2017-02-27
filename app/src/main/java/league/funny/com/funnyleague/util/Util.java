package league.funny.com.funnyleague.util;

/**
 * Created by inno-y on 2017/2/24.
 */

public class Util {
    public static String replaceHtmlSign(String arg){
        if(arg == null){
            return null;
        }
        arg = arg.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&")
                .replace("<br/>",System.getProperty("line.separator"))
                .replace("<br>",System.getProperty("line.separator"));
        return arg;
    }

}
