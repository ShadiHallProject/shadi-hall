package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Account3Name;

import java.util.List;

public class DialogForAccountingTypeItem extends DialogFragment implements View.OnClickListener {


    private CustomDialogOnDismisListener listener;

    View customView;
    TextInputLayout name_layout;
    TextInputEditText name;
    TextView countinsertion;
    String[] sp_items = {"NotAllowLogin", "Admin", "Custom Rights"};
    String groupID=null;
    int spPosition;
    Button add,cancel;
    public static final String DIALOG_EDIT_TEXT_TITLE ="Edit";
    String dialogtype;

    //////////////Count for item insertion in database
    int count=0;
    DatabaseHelper databaseHelper;
    String groupName="";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=new DatabaseHelper(getContext());
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        customView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_for_accounting, null);
        String head=getArguments().getString("head");
        spPosition=0;
        groupID=getArguments().getString("groupID");
        listener= (CustomDialogOnDismisListener) getContext();

        groupName=getArguments().getString("groupName");
        AssigningIDsFromView(customView);
        dialogtype = getArguments().getString("type");
        Log.e("keyy",getContext().getClass().getName());
        if(dialogtype!=null && dialogtype.equals(DIALOG_EDIT_TEXT_TITLE)){
            String dbpkID=getArguments().getString("dbpkid");
            String acnameid=getArguments().getString("acnameid");
            return  openEditTypeDialog(dbpkID,acnameid);
        }else{
            return new AlertDialog.Builder(getContext())
                    .setView(customView)
                    .setTitle(groupName)
                    .setOnDismissListener(this)
                    .setCancelable(false)
                    .create();
        }


    }
    private Dialog openEditTypeDialog(String dbpkID, String acnameid) {
        Log.e("editdialog1","PK:"+dbpkID+" acnameid :"+acnameid);
        List<Account3Name> chartOfAcc;
        String query;
        if(acnameid.equals("0"))
        {
            query = "SELECT * FROM Account3Name WHERE ID = " + dbpkID;

        }else
            query = "SELECT * FROM Account3Name WHERE AcNameID = " + acnameid;

        chartOfAcc = databaseHelper.getAccount3Name(query);
        for (Account3Name c : chartOfAcc) {

            name.setText(c.getAcName());

            add.setText(getString(R.string.update));
        }
        return new AlertDialog.Builder(getActivity())
                .setView(customView)
                .setTitle("Edit "+groupName)
                .setCancelable(false)
                .create();
    }

    private void AssigningIDsFromView(View customView) {
        name_layout = customView.findViewById(R.id.name_layout);
        countinsertion=customView.findViewById(R.id.counttxt);
        name = customView.findViewById(R.id.name);
        add = customView.findViewById(R.id.add);add.setOnClickListener(this);
        cancel=customView.findViewById(R.id.cancel);cancel.setOnClickListener(this);
    }
    public void onClick(View v) {

        String dbpkID=getArguments().getString("dbpkid");
        String acnameid=getArguments().getString("acnameid");
        if(v.getId() == R.id.add && dialogtype.equals(DIALOG_EDIT_TEXT_TITLE)){
            Log.e("editdialog2","PK:"+dbpkID+" acnameid :"+acnameid);
            Prefrence prefrence=new Prefrence(getActivity());
            String querydb,query1db;
            int dbpkid=Integer.parseInt(dbpkID.trim());

            querydb = "SELECT AcName FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " AND AcName = '" + name.getText().toString() + "' AND ID != " + dbpkid;

            if (databaseHelper.findAccount3Name(querydb)) {
                Log.e("key","Flag1-------------------");
                Toast.makeText(getActivity(), "Name Already Register", Toast.LENGTH_SHORT).show();
            }  else {
                Log.e("key","Flag3-------------------");

                querydb = "Update Account3Name SET AcName = '" + name.getText().toString() + "', AcAddress = '" + GenericConstants.NullFieldStandardText  + "', AcMobileNo = '" + "null"
                        + "', AcContactNo ='" + GenericConstants.NullFieldStandardText + "', AcEmailAddress = '" + GenericConstants.NullFieldStandardText + "', AcPassward = '" + GenericConstants.NullFieldStandardText
                        + "', SecurityRights = '" + spPosition + "', Salary = '" + "0" + "', UpdatedDate = 'Null' WHERE AcNameID = " + acnameid+" AND ID = "+dbpkID;
                databaseHelper.updateAccount3Name(querydb);
                MNotificationClass.ShowToast(getContext(),"Updated Data");
                dismiss();


            }

        }
        else if (v.getId() == R.id.add && dialogtype!=DIALOG_EDIT_TEXT_TITLE) {
            InputValidation inputValidation = new InputValidation(getContext());
            if (!inputValidation.isInputEditTextFilled(name, name_layout, getString(R.string.error_message_c_name))) {
                MNotificationClass.ShowToast(getContext(), "Some Field May empty");

            } else{
                Prefrence prefrence = new Prefrence(getContext());
                String query;
                query = "SELECT AcName FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " AND AcName = '" + name.getText().toString() + "'";

                if (databaseHelper.findAccount3Name(query)) {
                    Toast.makeText(getContext(), "Name Already Register", Toast.LENGTH_SHORT).show();
                }  else {

                    Account3Name account3Nametem = new Account3Name(
                            "0",
                            name.getText().toString(),
                            groupID,
                            GenericConstants.NullFieldStandardText,
                            "",
                            GenericConstants.NullFieldStandardText,
                            GenericConstants.NullFieldStandardText,
                            "0", "0",
                            "",
                            prefrence.getClientIDSession(),
                           prefrence.getClientUserIDSession(),
                            "0", "0",
                            GenericConstants.NullFieldStandardText,
                            "0",
                            sp_items[0],///SecurityRights
                            "0",///UserRights
                            "0");

                    databaseHelper.createAccount3Name(account3Nametem);
                    count++;
                    countinsertion.setText(""+count);
                    countinsertion.setVisibility(View.VISIBLE);
                    SetFieldsEmpty();
                    MNotificationClass.ShowToast(getContext(),"Added ");
                }
            }
            // int seriolNo = databaseHelper.getMaxValue("SELECT max(SerialNo) FROM Account3Name") + 1;
            // int seriolNo = databaseHelper.getMaxValue("SELECT max(SerialNo) FROM Account3Name") + 1;




        }
        else if(v.getId()==R.id.cancel){
            this.dismiss();
        }
    }

    private void SetFieldsEmpty() {
        name.setText("");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onDismissListener("key");
    }


}
