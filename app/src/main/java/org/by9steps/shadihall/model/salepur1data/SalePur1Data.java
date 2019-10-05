
package org.by9steps.shadihall.model.salepur1data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalePur1Data {

    @SerializedName("Salepur1")
    @Expose
    private List<Salepur1> salepur1 = null;
    @SerializedName("success")
    @Expose
    private int success;

    public List<Salepur1> getSalepur1() {
        return salepur1;
    }

    public void setSalepur1(List<Salepur1> salepur1) {
        this.salepur1 = salepur1;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

}
