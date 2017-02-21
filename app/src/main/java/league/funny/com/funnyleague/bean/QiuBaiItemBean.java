package league.funny.com.funnyleague.bean;

import java.util.ArrayList;

/**
 * Created by inno-y on 2017/2/21.
 */

public class QiuBaiItemBean {
    private int id;
    private String userId;
    private String userName;
    private String userImage;
    private String userAge;
    private String userSex;
    private String itemContent;
    private String smileCount;
    private String commentCount;
    private String commentGood;
    private String commentGoodContent;
    private String commentGoodCount;
    private ArrayList<QiuBaiCommentBean> qbcbList = new ArrayList<>();

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

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public String getSmileCount() {
        return smileCount;
    }

    public void setSmileCount(String smileCount) {
        this.smileCount = smileCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentGood() {
        return commentGood;
    }

    public void setCommentGood(String commentGood) {
        this.commentGood = commentGood;
    }

    public String getCommentGoodContent() {
        return commentGoodContent;
    }

    public void setCommentGoodContent(String commentGoodContent) {
        this.commentGoodContent = commentGoodContent;
    }

    public String getCommentGoodCount() {
        return commentGoodCount;
    }

    public void setCommentGoodCount(String commentGoodCount) {
        this.commentGoodCount = commentGoodCount;
    }

    public ArrayList<QiuBaiCommentBean> getQbcbList() {
        return qbcbList;
    }

    public void setQbcbList(ArrayList<QiuBaiCommentBean> qbcbList) {
        this.qbcbList = qbcbList;
    }
}
