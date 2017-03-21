package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NeiHanSubData {

    @SerializedName("group")
    private NeiHanItem groups;

    @SerializedName("comments")
    private ArrayList<NeiHanComment> comments;

    public NeiHanItem getGroups() {
        return groups;
    }

    public void setGroups(NeiHanItem groups) {
        this.groups = groups;
    }

    public ArrayList<NeiHanComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<NeiHanComment> comments) {
        this.comments = comments;
    }
}
