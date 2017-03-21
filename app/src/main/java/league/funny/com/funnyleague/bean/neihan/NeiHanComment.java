package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

public class NeiHanComment {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("avatar_url")
    private String userImage;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("digg_count")
    private String dingCount;
    @SerializedName("text")
    private String commentContent;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDingCount() {
        return dingCount;
    }

    public void setDingCount(String dingCount) {
        this.dingCount = dingCount;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
