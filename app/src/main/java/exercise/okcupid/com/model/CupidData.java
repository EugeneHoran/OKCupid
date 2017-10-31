
package exercise.okcupid.com.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CupidData {

    @SerializedName("total_matches")
    private Integer totalMatches;

    @SerializedName("data")
    private List<UserData> userDataList = null;

    public Integer getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Integer totalMatches) {
        this.totalMatches = totalMatches;
    }

    public List<UserData> getUserList() {
        return userDataList;
    }

    public void setUserList(List<UserData> data) {
        this.userDataList = data;
    }

}
