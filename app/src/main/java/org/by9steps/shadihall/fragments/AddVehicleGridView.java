package org.by9steps.shadihall.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.AddVehicleAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.JoinQueryAddVehicle;

import java.util.List;

public class AddVehicleGridView extends Fragment {

    private AddVehicleAdapter vehicleAdapter;
    Prefrence prefrence;
    DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    List<JoinQueryAddVehicle> mylist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_vehicle_gridview,container,false);

        prefrence=new Prefrence(getContext());
        databaseHelper=new DatabaseHelper(getContext());
        recyclerView=view.findViewById(R.id.AddVehicleRecyclerView);

        String query1="SELECT        Vehicle2Name.VehicleID, Vehicle2Name.VehicleGroupID, Vehicle2Name.VehicleName, Vehicle2Name.Brand, Vehicle2Name.Model, Vehicle2Name.Colour, \n" +
                "                         Vehicle2Name.RegistrationNo, Vehicle2Name.Status, Vehicle2Name.ContactNo, Vehicle2Name.ClientID, Vehicle2Name.UpdatedDate, Vehicle2Name.Account3ID, \n" +
                "                         Account3Name.AcName AS DriverName\n" +
                "FROM            Vehicle2Name INNER JOIN\n" +
                "                         Account3Name ON Vehicle2Name.Account3ID = Account3Name.AcNameID AND Vehicle2Name.ClientID = Account3Name.ClientID\n" +
                "WHERE        (Vehicle2Name.ClientID = '"+prefrence.getClientIDSession()+"')";

        mylist=databaseHelper.GetDataFromJoinQueryAddVehicle(query1);
        vehicleAdapter=new AddVehicleAdapter(getContext(),mylist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(vehicleAdapter);




        return  view;
    }


}