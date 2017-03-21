package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NeiHanData {

    @SerializedName("data")
    private ArrayList<NeiHanSubData> subData;

    public ArrayList<NeiHanSubData> getSubData() {
        return subData;
    }

    public void setSubData(ArrayList<NeiHanSubData> subData) {
        this.subData = subData;
    }
}
