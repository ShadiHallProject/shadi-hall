package org.by9steps.shadihall.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.by9steps.shadihall.model.salepur2data.SalePur2;

import java.util.List;

public class Account5customGroup1Data {

    @SerializedName("Account5customGroup1")
    @Expose
    private List<Account5customGroup1> account5customGroup1List = null;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Account5customGroup1> getAccount5customGroup1List() {
        return account5customGroup1List;
    }

    public void setAccount5customGroup1List(List<Account5customGroup1> account5customGroup1List) {
        this.account5customGroup1List = account5customGroup1List;
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
