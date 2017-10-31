
package exercise.okcupid.com.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CropRect extends RealmObject {

    @SerializedName("height")
    private Integer height;
    @SerializedName("y")
    private Integer y;
    @SerializedName("width")
    private Integer width;
    @SerializedName("x")
    private Integer x;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

}
