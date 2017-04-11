package league.funny.com.funnyleague.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by inno-y on 2017/2/21.
 */

public class CommentBean implements Serializable {
    private int id;
    private String userId;
    private String userUrl;
    private String userName;
    private String userImage;
    private String userAge;
    private String userSex;
    private String commentContent;
    private String goodCount;
    private String floor;
    private String replyUser;
    private ArrayList<CommentBean> commentbeanList;

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }

    public ArrayList<CommentBean> getCommentbeanList() {
        return commentbeanList;
    }

    public void setCommentbeanList(ArrayList<CommentBean> commentbeanList) {
        this.commentbeanList = commentbeanList;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(String goodCount) {
        this.goodCount = goodCount;
    }
}
