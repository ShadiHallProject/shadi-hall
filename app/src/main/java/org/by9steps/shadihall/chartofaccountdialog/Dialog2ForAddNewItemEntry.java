package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Item1Type;
import org.by9steps.shadihall.model.Item2Group;

import java.util.List;

public class Dialog2ForAddNewItemEntry extends DialogFragment implements
        AdapterView.OnItemSelectedListener, View.OnClickListener{
    public static final String DIALOG_EDIT_TEXT_TITLE ="Edit";
    Button add,cancel;
    View customView;
    Spinner spinner;
    String[] country ;
    EditText EditTextGroupName;
    DatabaseHelper helper;
    List<Item1Type>list;
    int itemselectindex = -99, serialcount = -1;

    String dialogtype=null;
    String itemtype,groupName,s3;
    int keyId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        customView = LayoutInflater.from(getContext()).inflate(R.layout.item_entry_dialog2, null);

        AssignIDsToFields(customView);

        dialogtype = getArguments().getString("type");
        keyId=getArguments().getInt("keyid");
        itemtype=getArguments().getString("item1typeid");
        groupName=getArguments().getString("item2groupName");



        //  WHERE AcNameID = " + acnameid+" AND ID = "+dbpkID;

        Prefrence prefrence=new Prefrence(getContext());
        int value =Integer.parseInt(prefrence.getClientIDSession());

        list = refdb.TableItem1.GetItem1TypeList(helper,"Select * from Item1Type");  //string
        country=new String[list.size()];
        for (int i = 0; i <list.size() ; i++) {
            country[i]=list.get(i).getItemType();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                country);
        spinner.setAdapter(dataAdapter);

        if(dialogtype!=null && dialogtype.equals(DIALOG_EDIT_TEXT_TITLE)){

            EditTextGroupName.setText(groupName);
            return openEditTypeDialog();
        }else {

            return new AlertDialog.Builder(getContext())
                    .setView(customView)
                    .setTitle("Item Entry")
                    .setOnDismissListener(this)
                    .setCancelable(false)
                    .create();
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper=new DatabaseHelper(getContext());
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.addEntry && dialogtype.equals(DIALOG_EDIT_TEXT_TITLE)){
            updateData();
        }
        else if (view.getId() == R.id.addEntry && dialogtype!=DIALOG_EDIT_TEXT_TITLE) {
            SaveData();
        }
        else {
            this.dismiss();
        }



    }

    public  void AssignIDsToFields(View customView){

        EditTextGroupName=customView.findViewById(R.id.EditGroupName);
        add=customView.findViewById(R.id.addEntry);
        add.setOnClickListener(this);

        cancel=customView.findViewById(R.id.cancelEntry);
        cancel.setOnClickListener(this);


        spinner = customView.findViewById(R.id.SPitemType);
        spinner.setOnItemSelectedListener(this);

    }

    public Dialog openEditTypeDialog(){

        return new AlertDialog.Builder(getActivity())
                .setView(customView)
                .setTitle("Edit "+groupName)
                .setCancelable(false)
                .create();
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        itemselectindex = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void SaveData() {
        if(EditTextGroupName.getText().toString().isEmpty())
        {
            MNotificationClass.ShowToast(getContext(),"Some Fields Empty");
            return;
        }



        Item2Group item2Group=new Item2Group();
        Prefrence prefrence=new Prefrence(getContext());

        int maxid=helper.getMaxValueOfItem2Group(prefrence.getClientIDSession());
            MNotificationClass.ShowToastTem(getContext()," "+maxid);

        item2Group.setItem2GroupID(String.valueOf(maxid)); //--/++
        item2Group.setItem1TypeID(list.get(itemselectindex).getItem1TypeID()); //itemid save
        item2Group.setItem2GroupName(EditTextGroupName.getText().toString());
        item2Group.setClientID(prefrence.getClientIDSession());
        item2Group.setClientUserID(prefrence.getClientUserIDSession());
        item2Group.setNetCode("0");
        item2Group.setSysCode("0");
        item2Group.setUpdatedDate(GenericConstants.NullFieldStandardText);
        long status= refdb.Table2Group.AddItem2Group(helper,item2Group);
        if(status!=-1)
        {
            MNotificationClass.ShowToast(getContext(),"Item Added");
            serialcount--;

            setFieldsToEmpty();

        }else{
            MNotificationClass.ShowToast(getContext(),"Item Not Added");
        }


    }
    public void updateData(){
        if(EditTextGroupName.getText().toString().isEmpty() || itemselectindex < 0  )
        {
            Toast.makeText(getContext(), "Some field are Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String query = "Update Item2Group SET UpdatedDate = 'Null' , Item1TypeID = '" + list.get(itemselectindex).getItem1TypeID() +  "', Item2GroupName = '" + EditTextGroupName.getText().toString() + "' WHERE  ID = "+keyId;
            helper.updateItem2Group(query);
            MNotificationClass.ShowToast(getContext(),"Updated Data");
            dismiss();
        }
    }
    public void setFieldsToEmpty(){
        EditTextGroupName.setText("");
        itemselectindex=0;

    }

}
