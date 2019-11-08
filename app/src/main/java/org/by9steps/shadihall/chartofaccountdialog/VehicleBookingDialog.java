package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.AddVehicleAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Vehicle1Group;
import org.by9steps.shadihall.model.Vehicle2Name;

import java.util.List;

public class VehicleBookingDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private AddVehicleAdapter vehicleAdapter;
   private static final String TAG = "VehicleBookingDialog";
   public static final String ENABLE_EDIT_MODE="Edit";
   Spinner  spVehicleCat,spDriver;
   String [] spData;
   List<Vehicle1Group> spList;
   DatabaseHelper databaseHelper;
   int itemselectindex;
   TextInputEditText VehicleName,colour,model,RegNo,brand,contactNo;
   Button btnCancle,btnAdd;

   String[] sp2Data;
   List<Account3Name> sp2List;
   int item2sp;
   int VehicleID;
   String type;

    Prefrence prefrence;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view= LayoutInflater.from(getContext()).inflate(R.layout.vehicle_booking_dialog,null);

        databaseHelper=new DatabaseHelper(getContext());
        prefrence=new Prefrence(getContext());
        AssignIDToView(view);



        //spinner Vehicle Category data
        String query="select * from Vehicle1Group";
        spList=databaseHelper.getVehicle1Group(query);
        spData=new String[spList.size()];
        for (int i = 0; i < spList.size(); i++) {
            spData[i]=spList.get(i).getVehicleGroupName();
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,spData);
        spVehicleCat.setAdapter(arrayAdapter);
        spVehicleCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemselectindex=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //spinner Driver data;
        String query2="select * from Account3Name where ClientID = "+prefrence.getClientIDSession();
        sp2List=databaseHelper.getAccount3Name(query2);
        sp2Data=new String[sp2List.size()];
        for (int j = 0; j < sp2List.size(); j++) {
            sp2Data[j]=sp2List.get(j).getAcName();
        }
        ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,sp2Data);
        spDriver.setAdapter(arrayAdapter2);
        spDriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item2sp=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        type=getArguments().getString("type");

        if(type==ENABLE_EDIT_MODE){
            VehicleID=Integer.parseInt(getArguments().getString("VehicleID"));
            VehicleName.setText(getArguments().getString("VehicleName"));
            colour.setText(getArguments().getString("colour"));
            model.setText(getArguments().getString("model"));
            RegNo.setText(getArguments().getString("RegistrationNo"));
            contactNo.setText(getArguments().getString("ContactNo"));
            brand.setText(getArguments().getString("brand"));
            //spinner Catagegory
            String id=getArguments().getString("VehicleGroupID");
            String query3="select VehicleGroupName from Vehicle1Group where VehicleGroupID = '"+ id +"'";
            String value=databaseHelper.getVehicle1GroupSpinnerVehicleGroupName(query3);
            int selectedID=arrayAdapter.getPosition(value);
            spVehicleCat.setSelection(selectedID);
          //  MNotificationClass.ShowToastTem(getContext(),"position "+selectedID+" value "+value );
            itemselectindex=selectedID;

            //spinner Driver
          //  MNotificationClass.ShowToastTem(getContext(),"ARG "+getArguments().getString("DriverName"));
//            int selectedDriver=arrayAdapter2.getPosition(getArguments().getString("DriverName"));
//            String query4="";
//            MNotificationClass.ShowToastTem(getContext(),""+selectedDriver);
//            spDriver.setSelection(selectedDriver);
//            item2sp=selectedDriver;

            btnAdd.setText("update");
         return    EditDialog(view);
        }else {

            return new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle("Add Vehicle")
                    .setCancelable(false)
                    .setOnDismissListener(this)
                    .create();
        }


    }

    public void AssignIDToView(View view){

        spVehicleCat=view.findViewById(R.id.spinnerVehicleCategory);
        spDriver=view.findViewById(R.id.spinnerDriver);
        VehicleName=view.findViewById(R.id.tvVehicleName);
        colour=view.findViewById(R.id.tvColour);
        model=view.findViewById(R.id.tvModel);
        RegNo=view.findViewById(R.id.tvRegistrationNo);
        brand=view.findViewById(R.id.tvBrand);
        contactNo=view.findViewById(R.id.tvContactNo);
        btnAdd=view.findViewById(R.id.btnVechicleAdd);btnAdd.setOnClickListener(this);
        btnCancle=view.findViewById(R.id.btnVechiclecancel);btnCancle.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.btnVechicleAdd && type==ENABLE_EDIT_MODE){
            updateDate();
        }else if(v.getId()== R.id.btnVechicleAdd && type!=ENABLE_EDIT_MODE){
            saveData();
        }else {
            this.dismiss();
        }

    }

    public void saveData(){

        MNotificationClass.ShowToast(getContext(),"sp cat "+itemselectindex);
        MNotificationClass.ShowToast(getContext(),"sp driver "+item2sp);
        if(VehicleName.getText().toString().isEmpty() || colour.getText().toString().isEmpty() || model.getText().toString().isEmpty() || RegNo.getText().toString().isEmpty() || brand.getText().toString().isEmpty() || contactNo.getText().toString().isEmpty() || brand.getText().toString().isEmpty() || contactNo.getText().toString().isEmpty() ) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }


        int maxId=databaseHelper.getMaxValueOfVehicle2Name(prefrence.getClientIDSession());

      //  MNotificationClass.ShowToastTem(getContext()," maxid "+maxId);

        Prefrence prefrence=new Prefrence(getContext());
        Vehicle2Name vehicle2Name=new Vehicle2Name();

        vehicle2Name.setVehicleID(String.valueOf(maxId));
        vehicle2Name.setVehicleGroupID(spList.get(itemselectindex).getVehicleGroupID()); //spinner1 cat
        vehicle2Name.setVehicleName(VehicleName.getText().toString());
        vehicle2Name.setBrand(brand.getText().toString());
        vehicle2Name.setModel(model.getText().toString());
        vehicle2Name.setColour(colour.getText().toString());
        vehicle2Name.setRegistrationNo(RegNo.getText().toString());
        vehicle2Name.setAccount3ID(sp2List.get(item2sp).getAcNameID());//spinner2 driver
        vehicle2Name.setStatus("offline"); //offline
        vehicle2Name.setLng("0");
        vehicle2Name.setLat("0");
        vehicle2Name.setContactNo(contactNo.getText().toString());
        vehicle2Name.setSerialNo("1"); //filhal
        vehicle2Name.setClientID(prefrence.getClientIDSession());
        vehicle2Name.setClientUserID(prefrence.getClientUserIDSession());
        vehicle2Name.setUpdatedDate(GenericConstants.NullFieldStandardText);
        vehicle2Name.setNetCode("0");
        vehicle2Name.setSysCode("0");


        long status=databaseHelper.createVehicle2Name(vehicle2Name);
        if(status!=-1){
            MNotificationClass.ShowToast(getContext(),"Data Added");
            setFieldsToEmpty();

        }else {
            MNotificationClass.ShowToast(getContext(),"Sorry Error Found..!");
        }


    }

    public void updateDate(){

        if(VehicleName.getText().toString().isEmpty() || colour.getText().toString().isEmpty() || model.getText().toString().isEmpty() || RegNo.getText().toString().isEmpty() || brand.getText().toString().isEmpty() || contactNo.getText().toString().isEmpty() || brand.getText().toString().isEmpty() || contactNo.getText().toString().isEmpty() ) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }else {

          //  String a=a;
            //String q="update table name set vehiclename = '"+a+"' grod ='"+ a +"' where id ='"+a+"' ";
            String query = "Update Vehicle2Name SET VehicleName = '" + VehicleName.getText().toString() + "', Colour = '" + colour.getText().toString() + "', Model = '" + model.getText().toString() + "', RegistrationNo = '"+RegNo.getText().toString()+"', ContactNo = '"+contactNo.getText().toString()+"' ,Brand = '"+brand.getText().toString()+"' ,VehicleGroupID = '"+spList.get(itemselectindex).getVehicleGroupID()+"' ,UpdatedDate = '" + GenericConstants.NullFieldStandardText + "' WHERE VehicleID = '"+ VehicleID+"'";

            Log.e(TAG, "updateDate: "+query);

            databaseHelper.updateVehicle2NameLocal(query);
            MNotificationClass.ShowToast(getContext(),"Updated Data");
            dismiss();
        }

    }

    public Dialog EditDialog(View view){
        return new AlertDialog.Builder(getContext())
                .setTitle("Edit Vehicle")
                .setView(view)
                .setCancelable(false)
                .setOnDismissListener(null)
                .create();

    }

    private void setFieldsToEmpty() {
        VehicleName.setText("");
        colour.setText("");
        model.setText("");
        RegNo.setText("");
        brand.setText("");
        contactNo.setText("");

    }

}
