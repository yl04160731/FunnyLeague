package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

public class NeiHanUser {
    @SerializedName("avatar_url")
    private String userImage;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("name")
    private String userName;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
