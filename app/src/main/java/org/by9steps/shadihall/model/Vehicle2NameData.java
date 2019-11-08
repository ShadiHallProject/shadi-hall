package org.by9steps.shadihall.model;

import java.util.List;

public class Vehicle2NameData {

    private List<Vehicle2Name> vehicle2Names=null;
    private int success;

    public List<Vehicle2Name> getVehicle2Names() {
        return vehicle2Names;
    }

    public void setVehicle2Names(List<Vehicle2Name> vehicle2Names) {
        this.vehicle2Names = vehicle2Names;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
