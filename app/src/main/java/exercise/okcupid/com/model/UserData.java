
package exercise.okcupid.com.model;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserData extends RealmObject {

    @PrimaryKey
    @SerializedName("userid")
    private String userId;
    @SerializedName("liked")
    private boolean isLiked;
    @SerializedName("match")
    private Integer match;
    @SerializedName("enemy")
    private Integer enemy;
    @SerializedName("relative")
    private Long relative; // 64 bit
    @SerializedName("last_login")
    private Integer lastLogin;
    @SerializedName("gender")
    private Integer gender;
    @SerializedName("location")
    private UserLocation userLocation;
    @SerializedName("state_code")
    private String stateCode;
    @SerializedName("orientation")
    private Integer orientation;
    @SerializedName("country_name")
    private String countryName;
    @SerializedName("photo")
    private Photo photo;
    @SerializedName("state_name")
    private String stateName;
    @SerializedName("age")
    private Integer age;
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("friend")
    private Integer friend;
    @SerializedName("is_online")
    private Integer isOnline;
    @SerializedName("username")
    private String username;
    @SerializedName("city_name")
    private String cityName;
    @SerializedName("stoplight_color")
    private String stoplightColor;

    /**
     * Format Texts
     */
    public String getPercentageFormatted() {
        return String.format("%s%% Match", String.valueOf(Math.round((float) match / 100)));
    }

    public String getAgeLocationFormatted() {
        return String.format("%s · %s, %s",
                String.valueOf(age),
                userLocation.getCityName(),
                userLocation.getStateCode());
    }


    /**
     * Save list change
     *
     * @param id userId
     */
    public static void setOnLike(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        UserData userDataUpdate = realm.where(UserData.class).contains("userId", id).findFirst();
        if (userDataUpdate == null) {
            realm.close();
            return;
        }
        userDataUpdate.setLiked(!userDataUpdate.getLiked());
        realm.copyToRealmOrUpdate(userDataUpdate);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * Getters and setters
     */
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Integer getEnemy() {
        return enemy;
    }

    public void setEnemy(Integer enemy) {
        this.enemy = enemy;
    }

    public Long getRelative() {
        return relative;
    }

    public void setRelative(Long relative) {
        this.relative = relative;
    }

    public Integer getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Integer lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public Integer getMatch() {
        return match;
    }

    public void setMatch(Integer match) {
        this.match = match;
    }

    public boolean getLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getFriend() {
        return friend;
    }

    public void setFriend(Integer friend) {
        this.friend = friend;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStoplightColor() {
        return stoplightColor;
    }

    public void setStoplightColor(String stoplightColor) {
        this.stoplightColor = stoplightColor;
    }
}
