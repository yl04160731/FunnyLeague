package league.funny.com.funnyleague.bean.neihan;

import com.google.gson.annotations.SerializedName;

import league.funny.com.funnyleague.bean.image.ImageData;

public class NeiHanResponse {
    @SerializedName("data")
    private ImageData data;

    public ImageData getData() {
        return data;
    }

    public void setData(ImageData data) {
        this.data = data;
    }
}
