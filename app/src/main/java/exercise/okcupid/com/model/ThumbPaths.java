
package exercise.okcupid.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ThumbPaths extends RealmObject {

    @SerializedName("large")
    private String large;
    @SerializedName("desktop_match")
    private String desktopMatch;
    @SerializedName("small")
    private String small;
    @SerializedName("medium")
    private String medium;

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getDesktopMatch() {
        return desktopMatch;
    }

    public void setDesktopMatch(String desktopMatch) {
        this.desktopMatch = desktopMatch;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

}
