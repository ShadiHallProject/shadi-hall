package org.by9steps.shadihall.model;

public class Vehicle1Group {
    private String VehicleGroupID;
    private String VehicleGroupName;

    public Vehicle1Group() {
    }

    public Vehicle1Group(String vehicleGroupID, String vehicleGroupName) {

        VehicleGroupID = vehicleGroupID;
        VehicleGroupName = vehicleGroupName;
    }



    public String getVehicleGroupID() {
        return VehicleGroupID;
    }

    public String getVehicleGroupName() {
        return VehicleGroupName;
    }



    public void setVehicleGroupID(String vehicleGroupID) {
        VehicleGroupID = vehicleGroupID;
    }

    public void setVehicleGroupName(String vehicleGroupName) {
        VehicleGroupName = vehicleGroupName;
    }
}
