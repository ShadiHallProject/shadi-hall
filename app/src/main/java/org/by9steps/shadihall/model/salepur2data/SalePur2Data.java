
package org.by9steps.shadihall.model.salepur2data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalePur2Data {

    @SerializedName("SalePur2")
    @Expose
    private List<SalePur2> salePur2 = null;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<SalePur2> getSalePur2() {
        return salePur2;
    }

    public void setSalePur2(List<SalePur2> salePur2) {
        this.salePur2 = salePur2;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
