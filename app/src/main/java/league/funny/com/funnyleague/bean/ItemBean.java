package league.funny.com.funnyleague.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by inno-y on 2017/2/21.
 */

public class ItemBean implements Serializable {
    private int id;
    private String userId;
    private String userName;
    private String userImage;
    private String userUrl;
    private String userAge;
    private String userSex;
    private String itemContentTitle;
    private String itemContent;
    private String itemImage;
    private String itemContentUrl;
    private String smileCount;
    private String commentCount;
    private String commentGoodName;
    private String commentGoodContent;
    private String commentGoodCount;
    private String ding;
    private String cai;
    private String share;
    private String repin;

    public String getRepin() {
        return repin;
    }

    public void setRepin(String repin) {
        this.repin = repin;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    private ArrayList<CommentBean> qbcbList = new ArrayList<>();

    public String getDing() {
        return ding;
    }

    public void setDing(String ding) {
        this.ding = ding;
    }

    public String getCai() {
        return cai;
    }

    public void setCai(String cai) {
        this.cai = cai;
    }

    public String getItemContentTitle() {
        return itemContentTitle;
    }

    public void setItemContentTitle(String itemContentTitle) {
        this.itemContentTitle = itemContentTitle;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
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

    public String getCommentGoodName() {
        return commentGoodName;
    }

    public void setCommentGoodName(String commentGoodName) {
        this.commentGoodName = commentGoodName;
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

    public ArrayList<CommentBean> getQbcbList() {
        return qbcbList;
    }

    public void setQbcbList(ArrayList<CommentBean> qbcbList) {
        this.qbcbList = qbcbList;
    }

    public String getItemContentUrl() {
        return itemContentUrl;
    }

    public void setItemContentUrl(String itemContentUrl) {
        this.itemContentUrl = itemContentUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}
