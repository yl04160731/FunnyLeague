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
    @SerializedName("large_image")
    private NeiHanImage image;

    public NeiHanImage getImage() {
        return image;
    }

    public void setImage(NeiHanImage image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(String digg_count) {
        this.digg_count = digg_count;
    }

    public String getBury_count() {
        return bury_count;
    }

    public void setBury_count(String bury_count) {
        this.bury_count = bury_count;
    }

    public String getRepin_count() {
        return repin_count;
    }

    public void setRepin_count(String repin_count) {
        this.repin_count = repin_count;
    }

    public String getShare_count() {
        return share_count;
    }

    public void setShare_count(String share_count) {
        this.share_count = share_count;
    }

    public NeiHanUser getUser() {
        return user;
    }

    public void setUser(NeiHanUser user) {
        this.user = user;
    }

}
