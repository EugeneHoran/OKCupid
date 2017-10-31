package exercise.okcupid.com.model;


import java.util.List;

public class UpdateDataHolder {
    public List<UserData> userDataList;
    public int whichUpdate;

    public Integer startRange;
    public Integer rangeLength;

    public UpdateDataHolder() {
    }

    public UpdateDataHolder(List<UserData> userDataList, int whichUpdate, Integer startRange, Integer rangeLength) {
        this.userDataList = userDataList;
        this.whichUpdate = whichUpdate;
        this.startRange = startRange;
        this.rangeLength = rangeLength;
    }

    public List<UserData> getUserDataList() {
        return userDataList;
    }

    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }

    public int getWhichUpdate() {
        return whichUpdate;
    }

    public void setWhichUpdate(int whichUpdate) {
        this.whichUpdate = whichUpdate;
    }

    public Integer getStartRange() {
        return startRange;
    }

    public void setStartRange(Integer startRange) {
        this.startRange = startRange;
    }

    public Integer getRangeLength() {
        return rangeLength;
    }

    public void setRangeLength(Integer rangeLength) {
        this.rangeLength = rangeLength;
    }
}
