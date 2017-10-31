
package exercise.okcupid.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Photo extends RealmObject {

    @SerializedName("full_paths")
    private FullPaths fullPaths;
    @SerializedName("base_path")
    private String basePath;
    @SerializedName("original_size")
    private OriginalSize originalSize;
    @SerializedName("ordinal")
    private Integer ordinal;
    @SerializedName("id")
    private String id;
    @SerializedName("crop_rect")
    private CropRect cropRect;
    @SerializedName("caption")
    private String caption;
    @SerializedName("thumb_paths")
    private ThumbPaths thumbPaths;

    public FullPaths getFullPaths() {
        return fullPaths;
    }

    public void setFullPaths(FullPaths fullPaths) {
        this.fullPaths = fullPaths;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public OriginalSize getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(OriginalSize originalSize) {
        this.originalSize = originalSize;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CropRect getCropRect() {
        return cropRect;
    }

    public void setCropRect(CropRect cropRect) {
        this.cropRect = cropRect;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ThumbPaths getThumbPaths() {
        return thumbPaths;
    }

    public void setThumbPaths(ThumbPaths thumbPaths) {
        this.thumbPaths = thumbPaths;
    }

}
