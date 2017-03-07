package league.funny.com.funnyleague.bean.image;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImageData {
    @SerializedName("images")
    private ArrayList<ImageItem> images;

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageItem> images) {
        this.images = images;
    }
}
