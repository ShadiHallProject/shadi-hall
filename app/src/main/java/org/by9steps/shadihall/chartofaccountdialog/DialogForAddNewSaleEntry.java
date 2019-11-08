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
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.ModelForSalePur1page2;
import org.by9steps.shadihall.model.item3name.UpdatedDate;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;

import java.util.ArrayList;
import java.util.List;

public class DialogForAddNewSaleEntry extends DialogFragment implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    Button addsale, cancelsale;
    Spinner spinner;
    View customView;
    DatabaseHelper helper;
    int salepur1id = -99;
    String EntryType, ClientID;
    /////////////////////Ref ID For CAshBokk
    String cashbokrefid;
    EditText itemdesc, qty, price;
    TextView totalamnt;
    int itemselectindex = -99, serialcount = -1;
    List<ModelForSalePur1page2> salepur2data;
    ///////////For Edit Type Dialog Variable
    String ID;
    ///////////////Object for Update SalePur2 Data
    SalePur2 updatedobjectforslaepur2;

    public static String Entrytypes[] = {"Sale", "Purchase", "Sale Return", "Purchase Return"};

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
        addsale = customView.findViewById(R.id.addsale);
        addsale.setOnClickListener(this);
        cancelsale = customView.findViewById(R.id.cancelsale);
        cancelsale.setOnClickListener(this);

        ClientID = getArguments().getString("ClientID");
        salepur1id = getArguments().getInt("SalePur1ID");
        EntryType = getArguments().getString("EntryType");
        Log.e("defaultparam"," cid:"+ClientID+" salepur1id:"+salepur1id);
        ///////////////////////////ONly for new Salepur1 cashbook refID
        cashbokrefid = getArguments().getString("cashbookrefid");
        ID = getArguments().getString("ID");
        helper = new DatabaseHelper(getContext());
        /////////////For New Edition
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
        if (ID != null) {
            ///////////////For Edit The existing Record
            //////Item3NameID,ItemName,Item2GroupName,ItemCode,Stock,ClientID
            setValueForRecordEditing(customView);
            return new AlertDialog.Builder(getContext())
                    .setView(customView)
                    .setTitle("Edit Mode " + ID)
                    .setOnDismissListener(this)
                    .setCancelable(false)
                    .create();
        } else {
            ///////////////For New Create New Item Dialog
            return new AlertDialog.Builder(getContext())
                    .setView(customView)
                    .setTitle("Sale Entry_" + cashbokrefid)
                    .setOnDismissListener(this)
                    .setCancelable(false)
                    .create();
        }


    }

    private void setValueForRecordEditing(View customView) {
        addsale.setText("Update");
        List<SalePur2> listsalepur2;
        SalePur2 curentSelcItem = null;
        listsalepur2 = refdb.SalePur2.GetSalePurItemList(helper, "Select * from SalePur2 where ID=" + ID);
        for (int i = 0; i < listsalepur2.size(); i++) {
            curentSelcItem = listsalepur2.get(i);
            Log.e("dataSalePur2data", listsalepur2.get(i).toString());
            itemdesc.setText(listsalepur2.get(i).getItemDescription());
            qty.setText(listsalepur2.get(i).getQty());
            price.setText(listsalepur2.get(i).getPrice());
            totalamnt.setText(listsalepur2.get(i).getTotal());
            updatedobjectforslaepur2 = listsalepur2.get(i);
        }
        if (salepur2data != null) {
            for (int i = 0; i < salepur2data.size(); i++) {
                boolean spinr = salepur2data.get(i).Columns[0].equals(curentSelcItem.getItem3NameID() + "");
                Log.e("spinr", spinr + " Spinner Data:" + salepur2data.get(i).Columns[0] +
                        ": acName ID::" + curentSelcItem.getItem3NameID() + ":");
                if (spinr) {
                    spinner.setSelection(i, true);
                }
            }
        }


//        itemdesc
//        qty;
//        price;
//
    }


    public void SaveSalePurToDB() {
        if (qty.getText().toString().isEmpty() || price.getText().toString().isEmpty()) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }
        String ClientUserID = new Prefrence(getContext()).getClientUserIDSession();
        SalePur2 salePur2 = new SalePur2();
        salePur2.setItem3NameID(Integer.parseInt(salepur2data.get(itemselectindex).Columns[0]));
        salePur2.setSalePur1ID(salepur1id);
        salePur2.setEntryType(EntryType);
        salePur2.setItemSerial(serialcount);
        salePur2.setItemDescription(itemdesc.getText().toString());
        if (EntryType.equals(Entrytypes[0])) {
            salePur2.setQtyAdd("0");
            salePur2.setQtyLess(qty.getText().toString());
        } else if (EntryType.equals(Entrytypes[1])) {
            salePur2.setQtyAdd(qty.getText().toString());
            salePur2.setQtyLess("0");
        } else if (EntryType.equals(Entrytypes[2])) {
            salePur2.setQtyAdd(qty.getText().toString());
            salePur2.setQtyLess("0");
        } else if (EntryType.equals(Entrytypes[3])) {
            salePur2.setQtyAdd("0");
            salePur2.setQtyLess(qty.getText().toString());
        }
        String quantity = qty.getText().toString();
        //////////////////////Setting Quantity
        if (quantity.isEmpty())
            salePur2.setQty("0");
        else
            salePur2.setQty(quantity);
        String pri = price.getText().toString();
        ///////////////Setting Price
        if (pri.isEmpty())
            salePur2.setPrice("0");
        else
            salePur2.setPrice(pri);
        //////
        salePur2.setTotal(totalamnt.getText().toString());
        salePur2.setLocation("1");
        salePur2.setClientID(Integer.parseInt(ClientID));
        salePur2.setClientUserID(Integer.parseInt(ClientUserID));
        salePur2.setNetCode(GenericConstants.NullFieldStandardText);
        salePur2.setSysCode(GenericConstants.NullFieldStandardText);
        ///////////////////////Setting Updated Date
        UpdatedDate updatedDate = new UpdatedDate();
        updatedDate.setDate(GenericConstants.NullFieldStandardText);
        salePur2.setUpdatedDate(updatedDate);
        long status = refdb.SalePur2.AddItemSalePur2(helper, salePur2);
        if (status != -1) {
            price.setText("");
            qty.setText("");
            itemdesc.setText("");
            serialcount--;
            String sum = refdb.CashBookTableRef.calcGrandTotal(helper,
                    salePur2.getSalePur1ID() + "",
                    salePur2.getClientID() + ""
                    , salePur2.getEntryType());
            Log.e("sumcalc", sum);
            long upiddref = refdb.CashBookTableRef.updateCashBook(helper, cashbokrefid, sum);


//            List<CashBook> list = helper.getCashBook("Select * from CashBook where ID=" + cashbokrefid);
//            Log.e("statuscashBefore", list.get(0).toString());
//
//            long upiddref = -1;
//            try {
//                int newamnt = Integer.parseInt(salePur2.getTotal());
//                int preamnt = Integer.parseInt(list.get(0).getAmount());
//                int newtotalamnt = newamnt + preamnt;
//                Log.e("amnt", "preamnt:" + preamnt + " newamnt:" + newamnt + " totalnew:" + newtotalamnt);
//                upiddref = refdb.CashBookTableRef.updateCashBook(helper, cashbokrefid, "" + newtotalamnt);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//           List<CashBook> list2= helper.getCashBook("Select * from CashBook where ID="+cashbokrefid);
//           Log.e("statuscashAfter","Total amnu:"+totalamnt.getText().toString()+"--"+list2.get(0).toString());

            MNotificationClass.ShowToast(getContext(), "Item Added "+upiddref);
        } else {
            MNotificationClass.ShowToast(getContext(), "Item Not Added");
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
            if (!strqty.isEmpty() && !strprice.isEmpty()) {
                if (TextUtils.isDigitsOnly(strqty) && TextUtils.isDigitsOnly(strprice)) {
                    int quan = Integer.parseInt(strqty);
                    int pric = Integer.parseInt(strprice);
                    totalamnt.setText("" + (quan * pric));

                }
            } else {
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
            if (!strqty.isEmpty() && !strprice.isEmpty()) {
                if (TextUtils.isDigitsOnly(strqty) && TextUtils.isDigitsOnly(strprice)) {
                    int quan = Integer.parseInt(strqty);
                    int pric = Integer.parseInt(strprice);
                    totalamnt.setText("" + (quan * pric));

                }
            } else {
                totalamnt.setText("0");
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        Salepur1AddNewActivity activity = (Salepur1AddNewActivity) getActivity();
        activity.SetDataOnGridView();
        switch (v.getId()) {
            case R.id.addsale:
                if (ID != null) {
                    MNotificationClass.ShowToastTem(getContext(), "Update Click");
                    UpdateDataFromDialogToSqlite();
                } else {
                    SaveSalePurToDB();
                }
                break;
            case R.id.cancelsale:
                this.dismiss();
                break;
        }
    }

    private void UpdateDataFromDialogToSqlite() {
        if (updatedobjectforslaepur2 != null) {
            updatedobjectforslaepur2.setItem3NameID(Integer.parseInt(salepur2data.get(itemselectindex).Columns[0]));

            UpdatedDate updatedDate = new UpdatedDate();
            updatedDate.setDate(GenericConstants.NullFieldStandardText);
            updatedobjectforslaepur2.setUpdatedDate(updatedDate);
            updatedobjectforslaepur2.setItemDescription(itemdesc.getText().toString());
            updatedobjectforslaepur2.setQty(qty.getText().toString());
            String pri = price.getText().toString();
            ///////////////Setting Price
            if (pri.isEmpty())
                updatedobjectforslaepur2.setPrice("0");
            else
                updatedobjectforslaepur2.setPrice(pri);
            updatedobjectforslaepur2.setTotal(totalamnt.getText().toString());
            long ii = helper.UpdateDataForSalePur2(ID, updatedobjectforslaepur2, false);
            if (ii != -1) {
                String quer="Select * from SalePur1 where SalePur1ID=" + salepur1id+
                        " AND ClientID = "+ClientID+
                        " AND EntryType ='"+EntryType+"'";
                List<Salepur1> llist = refdb.SlePur1.GetSalePur1Data(helper,
                        quer);
                Log.e("kkss",quer+"->Size:"+llist.size());
                if (llist.size() > 0) {
                        Salepur1AddNewActivity salepur1AddNewActivity= (Salepur1AddNewActivity) getContext();
                        String iidd = salepur1AddNewActivity.updateThisSalePur1EntryToCAshBook(llist.get(0));

                        MNotificationClass.ShowToast(getContext(), "Data Updated cashbkid=" + iidd);

                }else{
                    MNotificationClass.ShowToast(getContext(), "Item Updated");

                }
              // refdb.CashBookTableRef.calcGrandTotal(helper,)
//                String sum = refdb.CashBookTableRef.calcGrandTotal(helper,
//                        updatedobjectforslaepur2.getSalePur1ID() + "",
//                        updatedobjectforslaepur2.getClientID() + ""
//                        , updatedobjectforslaepur2.getEntryType());
//                Log.e("sumcalc", sum);
//                long upiddref = refdb.CashBookTableRef.updateCashBook(helper, cashbokrefid, sum);
            } else {
                MNotificationClass.ShowToast(getContext(), "Item Not Updated");
            }
        }
    }
}
