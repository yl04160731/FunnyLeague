package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NeiHanImage {
    @SerializedName("url_list")
    private ArrayList<NeiHanUrlList> NeiHanUrlList;

    public ArrayList<NeiHanUrlList> getNeiHanUrlList() {
        return NeiHanUrlList;
    }

    public void setNeiHanUrlList(ArrayList<NeiHanUrlList> neiHanUrlList) {
        NeiHanUrlList = neiHanUrlList;
    }
}
