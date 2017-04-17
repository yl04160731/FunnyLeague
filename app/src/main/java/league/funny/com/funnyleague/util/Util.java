package league.funny.com.funnyleague.util;

import android.content.Context;

/**
 * Created by inno-y on 2017/2/24.
 */

public class Util {
    public static String replaceHtmlSign(String arg) {
        if (arg == null) {
            return null;
        }
        arg = arg.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("&nbsp;", "")
                .replace("<br/>", System.getProperty("line.separator"))
                .replace("<br />", System.getProperty("line.separator"))
                .replace("<br>", System.getProperty("line.separator"))
                .replace("<b>", "").replace("</b>", "")
                .replace("<p>", "").replace("</p>", "");
        return arg;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
