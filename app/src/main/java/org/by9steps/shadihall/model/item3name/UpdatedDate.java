
package org.by9steps.shadihall.model.item3name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatedDate {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("timezone_type")
    @Expose
    private int timezoneType;
    @SerializedName("timezone")
    @Expose
    private String timezone;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTimezoneType() {
        return timezoneType;
    }

    public void setTimezoneType(int timezoneType) {
        this.timezoneType = timezoneType;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}
