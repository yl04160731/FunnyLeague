package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

public class NeiHanItem {
    @SerializedName("id")
    private String id;
    @SerializedName("content")
    private String content;
    @SerializedName("comment_count")
    private String comment_count;
    @SerializedName("digg_count")
    private String digg_count;
    @SerializedName("bury_count")
    private String bury_count;
    @SerializedName("repin_count")
    private String repin_count;
    @SerializedName("share_count")
    private String share_count;
    @SerializedName("user")
    private NeiHanUser user;


}
