package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.fragments.SelectDateFragment;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.CBUpdate;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.ChartOfAcc;
import org.by9steps.shadihall.model.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CashBookEntryDialog extends DialogFragment implements View.OnClickListener  {
    View customView;
    TextInputLayout date_layout;
    public static TextView date;
    TextInputLayout description_layout;
    TextInputEditText description;
    TextInputLayout amount_layout;
    TextInputEditText amount;
    TextInputLayout debit_account_layout, credit_account_layout;
    TextView debit_account, credit_account, tv_cr, tv_db;
    Button add;

    InputValidation inputValidation;
    Prefrence prefrence;

    String tableID, spinnerType, viewType, cbID;
    //////////////////Check wehter this entry is Income,Expense,Default
    String entrytype="";
 public static String entrytypelist[]={"Income","Expense","Default"};
    ProgressDialog pDialog;

    ArrayList<String> listDebit = new ArrayList<>();
    ArrayList<String> listCredit = new ArrayList<>();
    ArrayList<String> mDebit = new ArrayList<>();
    ArrayList<String> mCredit = new ArrayList<>();
    SpinnerDialog debitDialog, creditDialog;

    DatabaseHelper databaseHelper;
    List<ChartOfAcc> chartOfAccList;
    String query = "", currentDate, dDebit = null, cCredit = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        customView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_for_booking_receiving, null);
//        if (getSupportActionBar() != null) {
//            getActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getActivity().getSupportActionBar().setTitle("Cash Collection");
//
//        }

       // Intent intent = getActivity().getIntent();
        Bundle bb=getArguments();
        Log.e("TAGDialog",getTag());

        //if (intent != null) {
            tableID = bb.getString("BookingID");
            spinnerType = bb.getString("Spinner");
//            spinnerType="Hide";
        entrytype=bb.getString("EntryType");
        MNotificationClass.ShowToastTem(getContext(),entrytype);
            viewType = bb.getString("Type");
            cbID = bb.getString("CashBookID");
       // }

        inputValidation = new InputValidation(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());


        Date da = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sf.format(da);


        query = "SELECT * FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession();

        List<org.by9steps.shadihall.model.Spinner> li = databaseHelper.getSpinnerAcName(query);
        for (Spinner a : li){
            listCredit.add(a.getName());
            mCredit.add(a.getAcId());
            listDebit.add(a.getName());
            mDebit.add(a.getAcId());
        }

        date_layout = customView.findViewById(R.id.date_layout);
        date = customView.findViewById(R.id.date);
        description_layout = customView.findViewById(R.id.description_layout);
        description = customView.findViewById(R.id.description);
        amount_layout = customView.findViewById(R.id.enter_amount_layout);
        amount = customView.findViewById(R.id.enter_amount);
        debit_account = customView.findViewById(R.id.debit_account);
        debit_account_layout = customView.findViewById(R.id.debit_account_layout);
        credit_account = customView.findViewById(R.id.credit_account);
        credit_account_layout = customView.findViewById(R.id.credit_account_layout);
        tv_cr = customView.findViewById(R.id.tv_cr);
        tv_db = customView.findViewById(R.id.tv_db);
        add = customView.findViewById(R.id.add);

        date.setText(currentDate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.date = "CashBook";
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });


        if (!spinnerType.equals("Hide")){
            debit_account.setOnClickListener(this);
            credit_account.setOnClickListener(this);
            debit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
            credit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
        }else {

            credit_account.setText("Income");
            debit_account.setText("Cash");
            cCredit = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Booking Income'");
            dDebit = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Cash'");
        }

        add.setOnClickListener(this);

        if (viewType.equals("Edit")){
            String query = "SELECT * FROM CashBook WHERE CashBookID = "+ cbID;
            List<CashBook> cashBook = databaseHelper.getCashBook(query);
            for (CashBook c : cashBook){
                date.setText(c.getCBDate());
                dDebit = c.getDebitAccount();
                cCredit = c.getCreditAccount();
                debit_account.setText(databaseHelper.getAccountName("SELECT * FROM Account3Name WHERE AcNameID ="+c.getDebitAccount()));
                credit_account.setText(databaseHelper.getAccountName("SELECT * FROM Account3Name WHERE AcNameID ="+c.getCreditAccount()));
                description.setText(c.getCBRemarks());
                amount.setText(c.getAmount());
                add.setText("Update");
            }

        }

        return new AlertDialog.Builder(getActivity())
                .setView(customView)
                .setTitle(getTag())
                .setCancelable(false)
                .create();
        
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.debit_account:
                Log.e("CLICK","CLICK");
                debitDialog = new SpinnerDialog(getActivity(),listDebit,"Select Debit");
                debitDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        Toast.makeText(getContext(),"Item Selected",Toast.LENGTH_LONG).show();
                        dDebit = mDebit.get(i);
                        debit_account.setText(s);
                    }
                });
                debitDialog.showSpinerDialog();
                break;
            case R.id.credit_account:
                Log.e("CLICK","CLICK");
                creditDialog = new SpinnerDialog(getActivity(),listCredit,"Select Credit");
                creditDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        Toast.makeText(getContext(),"Item Selected",Toast.LENGTH_LONG).show();
                        credit_account.setText(s);
                        cCredit = mCredit.get(i);
                    }
                });
                creditDialog.showSpinerDialog();
                break;
            case R.id.add:

                if (cCredit == null){
                    Toast.makeText(getContext(),"Select Credit",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dDebit == null){
                    Toast.makeText(getContext(),"Select Debit",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(amount, amount_layout, getString(R.string.error_message_amount))) {
                    return;
                }else{
                    String dd = null;
                    try {
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = s.parse(date.getText().toString());
                        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
                        dd = ss.format(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (viewType.equals("Edit")){
                        String query = "UPDATE CashBook SET CBDate = '"+dd+"'," +
                                " DebitAccount = '"+dDebit+"'," +
                                " CreditAccount = '"+cCredit+"', " +
                                "CBRemarks = '"+description.getText().toString()+"', " +
                                "Amount = '"+amount.getText().toString()+"', " +
                                "UpdatedDate = '"+GenericConstants.NullFieldStandardText+"' WHERE CashBookID = "+cbID;
                        databaseHelper.updateCashBook(query);
                        CBUpdate cbUpdate = new CBUpdate(cbID,date.getText().toString(),dDebit,cCredit,description.getText().toString(),amount.getText().toString());
                        cbUpdate.save();
                        Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else {

                        // int seriolNo = databaseHelper.getMaxValue("SELECT MAX(SerialNo) AS MaxNo FROM CashBook") + 1;
                        int maxcashbookid = refdb.CashBookTableRef.getmaxCashBookID(databaseHelper, "" + prefrence.getClientIDSession());
                        String tablename="bk";
                        if(entrytype.equals(entrytypelist[0])){
                            tablename="Booking_Received";
                        }else if(entrytype.equals(entrytypelist[1])){
                            tablename="Booking_Expense";
                        }else{
                            tablename="Null";
                        }
                        databaseHelper.createCashBook(new CashBook(maxcashbookid+"",
                                date.getText().toString(),
                                dDebit,
                                cCredit,
                                description.getText().toString(),
                                amount.getText().toString(),
                                prefrence.getClientIDSession(),
                                prefrence.getClientUserIDSession(),
                                "0",
                                "0",
                                GenericConstants.NullFieldStandardText,
                                tableID,
                                String.valueOf(maxcashbookid),
                                tablename));
                        clearCashe();
                    }

                }
                break;
        }
    }


    public void clearCashe(){
        cCredit = null;
        dDebit = null;
        description.setText("");
        amount.setText("");
        debit_account.setText("Select Debit");
        credit_account.setText("Select Credit");
    }

    //Check Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
