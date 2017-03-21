package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

public class NeiHanResponse {
    @SerializedName("data")
    private NeiHanData data;

    public NeiHanData getData() {
        return data;
    }

    public void setData(NeiHanData data) {
        this.data = data;
    }
}
