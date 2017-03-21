package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NeiHanUser {
    @SerializedName("group")
    private ArrayList<NeiHanItem> groups;

    public ArrayList<NeiHanItem> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<NeiHanItem> groups) {
        this.groups = groups;
    }
}
