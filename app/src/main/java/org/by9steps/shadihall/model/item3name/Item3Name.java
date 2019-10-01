
package org.by9steps.shadihall.model.item3name;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item3Name {

    @SerializedName("Item3Name")
    @Expose
    private List<Item3Name_> item3Name = null;
    @SerializedName("success")
    @Expose
    private int success;

    public List<Item3Name_> getItem3Name() {
        return item3Name;
    }

    public void setItem3Name(List<Item3Name_> item3Name) {
        this.item3Name = item3Name;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

}
