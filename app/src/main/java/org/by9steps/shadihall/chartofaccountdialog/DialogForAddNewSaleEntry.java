package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.PrecomputedText;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.Salepur1AddNewActivity;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.ModelForSalePur1page2;
import org.by9steps.shadihall.model.item3name.UpdatedDate;
import org.by9steps.shadihall.model.salepur2data.SalePur2;

import java.util.ArrayList;
import java.util.List;

public class DialogForAddNewSaleEntry extends DialogFragment implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    Button addsale,cancelsale;
    Spinner spinner;
    View customView;
    DatabaseHelper helper;
    int salepur1id = -99;
    String EntryType, ClientID;
    EditText itemdesc, qty, price;
    TextView totalamnt;
    int itemselectindex = -99, serialcount = -1;
    List<ModelForSalePur1page2> salepur2data;

    String Entrytypes[]={"Sale","Purchase","Sale Return","Purchase Return"};

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        customView = LayoutInflater.from(getContext()).inflate(R.layout.saleentrydialog, null);
        spinner = customView.findViewById(R.id.itemnames);
        spinner.setOnItemSelectedListener(this);
        qty = customView.findViewById(R.id.txtqty);
        qty.addTextChangedListener(qtychange);
        price = customView.findViewById(R.id.txtprice);
        price.addTextChangedListener(pricchange);
        totalamnt = customView.findViewById(R.id.txttotal);
        itemdesc = customView.findViewById(R.id.itemdesc);
        addsale=customView.findViewById(R.id.addsale);addsale.setOnClickListener(this);
        cancelsale=customView.findViewById(R.id.cancelsale);cancelsale.setOnClickListener(this);

        ClientID = getArguments().getString("ClientID");
        salepur1id = getArguments().getInt("SalePur1ID");
        EntryType = getArguments().getString("EntryType");

        helper = new DatabaseHelper(getContext());
        //////Item3NameID,ItemName,Item2GroupName,ItemCode,Stock,ClientID
        String spinerdialogquery = " SELECT        Item3Name.Item3NameID, Item3Name.ItemName, Item2Group.Item2GroupName, Item3Name.ItemCode, Item3Name.Stock, Item3Name.ClientID\n" +
                "FROM            Item3Name INNER JOIN\n" +
                "                         Item2Group ON Item3Name.Item2GroupID = Item2Group.Item2GroupID AND Item3Name.ClientID = Item2Group.ClientID\n" +
                "WHERE        (Item3Name.ClientID = " + ClientID + ") AND (Item3Name.Item3NameID != 0) ";

        salepur2data = helper.GetDataFroJoinQuerySalerpurpage2(spinerdialogquery);
        List<String> spinner_list = new ArrayList<>();
        for (ModelForSalePur1page2 data : salepur2data) {
            spinner_list.add(data.Columns[1] + "," + data.Columns[2]);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinner_list);
        spinner.setAdapter(dataAdapter);
        return new AlertDialog.Builder(getContext())
                .setView(customView)
                .setTitle("Sale Entry")
                .setOnDismissListener(this)
                .setCancelable(false)
                .create();
    }

    public void SaveSalePurToID() {
        if(qty.getText().toString().isEmpty() || price.getText().toString().isEmpty())
        {
            MNotificationClass.ShowToast(getContext(),"Some Fields Empty");
            return;
        }
        String ClientUserID = new Prefrence(getContext()).getClientUserIDSession();
        SalePur2 salePur2 = new SalePur2();
        salePur2.setItem3NameID(Integer.parseInt(salepur2data.get(itemselectindex).Columns[0]));
        salePur2.setSalePur1ID(salepur1id);
        salePur2.setEntryType(EntryType);
        salePur2.setItemSerial(serialcount);
        salePur2.setItemDescription(itemdesc.getText().toString());
        if(EntryType.equals(Entrytypes[0]))
        {
            salePur2.setQtyAdd("0");
            salePur2.setQtyLess(qty.getText().toString());
        }else if(EntryType.equals(Entrytypes[1]))
        {
            salePur2.setQtyAdd(qty.getText().toString());
            salePur2.setQtyLess("0");
        }else if(EntryType.equals(Entrytypes[2]))
        {
            salePur2.setQtyAdd(qty.getText().toString());
            salePur2.setQtyLess("0");
        }else if(EntryType.equals(Entrytypes[3]))
        {
            salePur2.setQtyAdd("0");
            salePur2.setQtyLess(qty.getText().toString());
        }

        salePur2.setQty(qty.getText().toString());
        salePur2.setPrice(price.getText().toString());
        salePur2.setTotal(totalamnt.getText().toString());
        salePur2.setLocation(GenericConstants.NullFieldStandardText);
        salePur2.setClientID(Integer.parseInt(ClientID));
        salePur2.setClientUserID(Integer.parseInt(ClientUserID));
        salePur2.setNetCode(GenericConstants.NullFieldStandardText);
        salePur2.setSysCode(GenericConstants.NullFieldStandardText);
        ///////////////////////Setting Updated Date
        UpdatedDate updatedDate = new UpdatedDate();
        updatedDate.setDate(GenericConstants.NullFieldStandardText);
        salePur2.setUpdatedDate(updatedDate);
       long status= refdb.SalePur2.AddItemSalePur2(helper,salePur2);
       if(status!=-1)
       {
           MNotificationClass.ShowToast(getContext(),"Item Added");
           serialcount--;
       }else{
           MNotificationClass.ShowToast(getContext(),"Item Not Added");
       }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemselectindex = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    ////////////////////Real Time Text Watcher
    TextWatcher qtychange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("changeqty", "" + s);
            String strqty = qty.getText().toString();
            String strprice = price.getText().toString();
            if (!strqty.isEmpty()&&!strprice.isEmpty()) {
                if (TextUtils.isDigitsOnly(strqty) && TextUtils.isDigitsOnly(strprice)) {
                    int quan = Integer.parseInt(strqty);
                    int pric = Integer.parseInt(strprice);
                    totalamnt.setText("" + (quan * pric));

                }
            }else{
                totalamnt.setText("0");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    ////////////////////Real Time Text Watcher
    TextWatcher pricchange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Log.e("changeprice", "" + s);
            String strqty = qty.getText().toString();
            String strprice = price.getText().toString();
            if (!strqty.isEmpty()&&!strprice.isEmpty()) {
                if (TextUtils.isDigitsOnly(strqty) && TextUtils.isDigitsOnly(strprice)) {
                    int quan = Integer.parseInt(strqty);
                    int pric = Integer.parseInt(strprice);
                    totalamnt.setText("" + (quan * pric));

                }
            }else{
                totalamnt.setText("0");
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        Salepur1AddNewActivity  activity=(Salepur1AddNewActivity)getActivity();
        activity.SetDataOnGridView();
        switch (v.getId())
        {
            case R.id.addsale:
                SaveSalePurToID();
                break;
            case R.id.cancelsale:
                this.dismiss();
                break;
        }
    }
}
