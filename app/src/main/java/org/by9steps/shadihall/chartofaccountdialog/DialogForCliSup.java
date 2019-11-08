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
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class DialogForCliSup extends DialogFragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {
    public static final String DIALOG_EDIT_TEXT_TITLE ="Edit";
    public CustomDialogOnDismisListener listener;

    View customView;
    TextInputLayout name_layout;
    TextInputEditText name;
    TextInputLayout address_layout;
    TextInputEditText address;
    TextInputLayout mobile_layout;
    TextInputEditText mobile;
    TextInputLayout email_layout;
    TextInputEditText email;
    //    TextInputLayout salary_layout;
//    TextInputEditText salary;
    TextInputLayout login_mobile_layout;
    TextInputEditText login_mobile;
    TextInputLayout password_layout;
    TextInputEditText password;
    TextView loginInfo;
    Spinner spinner;
    int spPosition;
    TextView countinsertion;
    Button add, cancel;
    DatabaseHelper databaseHelper;
    String[] sp_items = {"NotAllowLogin", "Admin", "Custom Rights"};
    String groupID = null;
int count=0;
//////////////////////////Check either edit or add
String dialogtype;
    String groupName="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        customView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_clisup, null);
        String head = getArguments().getString("head");
        spPosition = 0;
        listener= (CustomDialogOnDismisListener) getContext();
        groupID = getArguments().getString("groupID");
        dialogtype = getArguments().getString("type");
        AssigningIDsFromView(customView);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, sp_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




        setCancelable(false);
        groupName=getArguments().getString("groupName");
        if(dialogtype!=null && dialogtype.equals(DIALOG_EDIT_TEXT_TITLE)){
            String dbpkID=getArguments().getString("dbpkid");
            String acnameid=getArguments().getString("acnameid");
            return  openEditTypeDialog(dbpkID,acnameid);
        }else{
            return new AlertDialog.Builder(getActivity())
                    .setView(customView)
                    .setTitle(groupName)
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
            address.setText(c.getAcAddress());
            mobile.setText(c.getAcContactNo());
            email.setText(c.getAcEmailAddress());
            login_mobile.setText(c.getAcMobileNo());
            password.setText(c.getAcPassward());
            Log.e("CCCCCC", c.getSecurityRights());
            if (c.getSecurityRights().equals("0")) {
                spinner.setSelection(0);
            } else if (c.getSecurityRights().equals("1")) {
                spinner.setSelection(1);
            } else if (c.getSecurityRights().equals("2")) {
                spinner.setSelection(2);
            }
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
        name = customView.findViewById(R.id.name);
        address_layout = customView.findViewById(R.id.address_layout);
        address = customView.findViewById(R.id.address);
        mobile_layout = customView.findViewById(R.id.mobile_layout);
        mobile = customView.findViewById(R.id.mobile);
        email_layout = customView.findViewById(R.id.email_layout);
        email = customView.findViewById(R.id.email);
//        salary_layout = customView.findViewById(R.id.salary_layout);
//        salary = customView.findViewById(R.id.salary);
        loginInfo = customView.findViewById(R.id.login_info);
        login_mobile_layout = customView.findViewById(R.id.login_mobile_layout);
        login_mobile = customView.findViewById(R.id.login_mobile);
        password_layout = customView.findViewById(R.id.password_layout);
        password = customView.findViewById(R.id.password);
        spinner = customView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        add = customView.findViewById(R.id.add);
        add.setOnClickListener(this);
        cancel = customView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        countinsertion=customView.findViewById(R.id.counttxt);

    }

    @Override
    public void onClick(View v) {
        String dbpkID=getArguments().getString("dbpkid");
        String acnameid=getArguments().getString("acnameid");

        if(v.getId() == R.id.add && dialogtype.equals(DIALOG_EDIT_TEXT_TITLE)){
            Log.e("editdialog2","PK:"+dbpkID+" acnameid :"+acnameid);
            Prefrence prefrence=new Prefrence(getActivity());
            String querydb,query1db;
            int dbpkid=Integer.parseInt(dbpkID.trim());
            String mobileNo=login_mobile.getText().toString();
            querydb = "SELECT AcName FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " AND AcName = '" + name.getText().toString() + "' AND ID != " + dbpkid;
            query1db = "SELECT AcMobileNo FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " AND AcMobileNo = '" + mobileNo + "' AND ID != " + dbpkid;

            if (databaseHelper.findAccount3Name(querydb)) {
                Log.e("key","Flag1-------------------");
                Toast.makeText(getActivity(), "Name Already Register", Toast.LENGTH_SHORT).show();
            } else if (databaseHelper.findAccount3Name(query1db)&& !mobileNo.equals("null") ) {
                Log.e("key","Flag2-------------------");

                Toast.makeText(getActivity(), "Login Number Already Register", Toast.LENGTH_SHORT).show();

            } else {
                Log.e("key","Flag3-------------------");

                querydb = "Update Account3Name SET AcName = '" + name.getText().toString() + "', AcAddress = '" + address.getText().toString() + "', AcMobileNo = '" + login_mobile.getText().toString()
                            + "', AcContactNo ='" + mobile.getText().toString() + "', AcEmailAddress = '" + email.getText().toString() + "', AcPassward = '" + password.getText().toString()
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

            }
//            else if (!inputValidation.isInputEditTextFilled(address, address_layout, getString(R.string.error_message_c_address))) {
//                MNotificationClass.ShowToast(getContext(), "Some Field May empty");
//
//            }
//            else if (!inputValidation.isInputEditTextFilled(mobile, mobile_layout, getString(R.string.error_message_c_number))) {
//                MNotificationClass.ShowToast(getContext(), "Some Field May empty");
//
//            }
//            else if (!inputValidation.isInputEditTextFilled(email, email_layout, getString(R.string.error_message_email))) {
//                MNotificationClass.ShowToast(getContext(), "Some Field May empty");
//
//            }
//            if (!inputValidation.isInputEditTextFilled(salary, salary_layout, getString(R.string.error_message_salary))) {
//                MNotificationClass.ShowToast(getContext(), "Some Field May empty");
//
//            }
//            else if (!inputValidation.isInputEditTextFilled(login_mobile, login_mobile_layout, getString(R.string.error_message_login_number))) {
//                MNotificationClass.ShowToast(getContext(), "Some Field May empty");
//
            else {
                Prefrence prefrence = new Prefrence(getContext());
                String query, query1;
                query = "SELECT AcName FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " AND AcName = '" + name.getText().toString() + "'";
                query1 = "SELECT AcMobileNo FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " AND AcMobileNo = '" + login_mobile.getText().toString() + "'";

                if (databaseHelper.findAccount3Name(query)) {
                    Toast.makeText(getContext(), "Name Already Register", Toast.LENGTH_SHORT).show();
                } else if (databaseHelper.findAccount3Name(query1)) {
                    Toast.makeText(getContext(), "Login Number Already Register", Toast.LENGTH_SHORT).show();
                } else {
                    int maxacnameid=databaseHelper.getMaxAccount3NameMaxAcNameID(prefrence.getClientIDSession());

                    Account3Name account3Nametem = new Account3Name(
                            maxacnameid+"",
                            name.getText().toString(),
                            groupID,
                            address.getText().toString(),
                            login_mobile.getText().toString(),
                            mobile.getText().toString(),
                            email.getText().toString(),
                            "0", "0",
                            password.getText().toString(),
                            prefrence.getClientIDSession(),
                            prefrence.getClientUserIDSession(),
                            "0",
                            "0",
                            GenericConstants.NullFieldStandardText,
                            "0",
                            sp_items[spPosition],///User Rights
                            String.valueOf(spPosition),///SecurityRights
                            "0");

                    databaseHelper.createAccount3Name(account3Nametem);
                    count++;
                    countinsertion.setText(""+count);
                    countinsertion.setVisibility(View.VISIBLE);
                    SetFieldsEmpty();
                    MNotificationClass.ShowToast(getContext(), "Added ");
                }
            }
            // int seriolNo = databaseHelper.getMaxValue("SELECT max(SerialNo) FROM Account3Name") + 1;
            // int seriolNo = databaseHelper.getMaxValue("SELECT max(SerialNo) FROM Account3Name") + 1;


        } else if (v.getId() == R.id.cancel) {
            this.dismiss();
        }
    }

    private void SetFieldsEmpty() {
        name = customView.findViewById(R.id.name);name.setText("");
        address = customView.findViewById(R.id.address);address.setText("");
        mobile = customView.findViewById(R.id.mobile);mobile.setText("");
        email = customView.findViewById(R.id.email);email.setText("");
        loginInfo = customView.findViewById(R.id.login_info);loginInfo.setText("");
        login_mobile = customView.findViewById(R.id.login_mobile);login_mobile.setText("");
        password = customView.findViewById(R.id.password);password.setText("");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spPosition = position;
        Log.e("kye", "Pos" + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.onDismissListener("key");
    }
}
