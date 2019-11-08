package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.by9steps.shadihall.model.item3name.UpdatedDate;

import java.util.List;

public class DialogForAddNewItemEntry extends DialogFragment implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {
    public static final String DIALOG_EDIT_TEXT_TITLE ="Edit";

    Button addsale,cancelsale;
    Spinner spinner;
    View customView;
    DatabaseHelper helper;
    int salepur1id = -99;
    String EntryType, Title,Message;
    EditText ItemSalePrice, ItemName, ItemCode;
    TextView totalamnt;
    int itemselectindex = -99, serialcount = -1;
    List<Item2Group> list;
    ImageView ImageBtnAdd, ItemImage;
    String [] spinnerdata;
    int keyid;
    String name,dialogtype,price,code;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        customView = LayoutInflater.from(getContext()).inflate(R.layout.itementrydialog, null);
        AssignIDToView(customView);



        //this will open the second dialogBox
        ImageBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("keyid",0);
                bundle.putString("type","new");
                bundle.putString("item1typeid","null");
                bundle.putString("item2groupName","null");

                Dialog2ForAddNewItemEntry AddItemEntry = new Dialog2ForAddNewItemEntry();
                AddItemEntry.setArguments(bundle);
                AddItemEntry.show(getFragmentManager(), "TAG");
            }
        });


        /**recive data***/

        keyid=getArguments().getInt("keyid");
        dialogtype=getArguments().getString("type");
        name=getArguments().getString("name");
        price=getArguments().getString("price");
        code=getArguments().getString("code");








         Prefrence prefrence=new Prefrence(getContext());
        int value =Integer.parseInt(prefrence.getClientIDSession());

        spinner.setOnItemSelectedListener(this);


        list= refdb.Table2Group.GetItem2GroupList(helper,"select * from Item2Group where  ClientID ='"+value+"'");
        spinnerdata=new String[list.size()];
        for (int i = 0; i <list.size() ; i++) {
            spinnerdata[i]=list.get(i).getItem2GroupName();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                spinnerdata);
        spinner.setAdapter(dataAdapter);


        if(dialogtype!=null&& dialogtype.equals(DIALOG_EDIT_TEXT_TITLE)){

            ItemName.setText(name);
            ItemSalePrice.setText(price);
            ItemCode.setText(code);
                return OpenEditTypeDialog();
        }
        else {
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
    public Dialog OpenEditTypeDialog(){
        return  new AlertDialog.Builder(getContext())
                .setView(customView)
                .setTitle("Edit "+name)
                .setCancelable(false)
                .create();
    }

    ////////////////////Real Time Text Watcher
    TextWatcher qtychange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

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



        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View view) {

        if(view.getId()== R.id.addsale && dialogtype.equals(DIALOG_EDIT_TEXT_TITLE))
        {
            updataData();

        } else if(view.getId()== R.id.addsale && dialogtype!=DIALOG_EDIT_TEXT_TITLE)
        {
            SaveSalePurToID();

        }else {
            this.dismiss();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        itemselectindex = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void AssignIDToView(View customView){

        spinner = customView.findViewById(R.id.itemnames);
        spinner.setOnItemSelectedListener(this);

        ItemName = customView.findViewById(R.id.edItemName);
        ItemName.addTextChangedListener(qtychange);

        ItemCode = customView.findViewById(R.id.edItemCode);
        ItemCode.addTextChangedListener(pricchange);

        ItemImage=customView.findViewById(R.id.itemImage);

        ItemSalePrice = customView.findViewById(R.id.edItemSalePrice);

        addsale=customView.findViewById(R.id.addsale);addsale.setOnClickListener(this);
        cancelsale=customView.findViewById(R.id.cancelsale);cancelsale.setOnClickListener(this);
        ImageBtnAdd=customView.findViewById(R.id.Imagebtn_add);

    }
    public void SaveSalePurToID() {

       //int maxid= getmaxid();

        if(ItemName.getText().toString().isEmpty() || ItemSalePrice.getText().toString().isEmpty() ) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }
        Prefrence prefrence=new Prefrence(getContext());
        Item3Name_ item3Name=new Item3Name_();



        int value=helper.getMaxValueOfItem3Name(prefrence.getClientIDSession());
        MNotificationClass.ShowToastTem(getContext(),""+value);

        item3Name.setItem3NameID(value);//server and local data ---/++
        item3Name.setItem2GroupID(Integer.parseInt(list.get(itemselectindex).getItem2GroupID()));// spinner   Item2GroupID)save
        item3Name.setItemName(ItemName.getText().toString());
        item3Name.setClientID(Integer.parseInt(prefrence.getClientIDSession()));
        item3Name.setClientUserID(Integer.parseInt(prefrence.getClientUserIDSession()));
        item3Name.setNetCode("0");
        item3Name.setSysCode("0");
        item3Name.setSalePrice(ItemSalePrice.getText().toString());
        item3Name.setItemCode(ItemCode.getText().toString());
        item3Name.setStock("0");

        UpdatedDate updatedDate=new UpdatedDate();
        updatedDate.setDate(GenericConstants.NullFieldStandardText);
        item3Name.setUpdatedDate(updatedDate);  //null
        Log.e("SendDataToDB",item3Name.toString());



        long status= refdb.Table3Name.AddItem3Name(helper,item3Name);
        if(status!=-1)
        {
            MNotificationClass.ShowToast(getContext(),"Item Added");
            serialcount--;
            setFieldsToEmpty();
        }else{
            MNotificationClass.ShowToast(getContext(),"Item Not Added");
        }

    }
    public void updataData(){

        if(ItemName.getText().toString().isEmpty() || ItemSalePrice.getText().toString().isEmpty()) {
            MNotificationClass.ShowToast(getContext(), "Some Fields Empty");
            return;
        }else {
            String query = "Update Item3Name SET UpdatedDate='Null', ItemName = '" + ItemName.getText().toString() + "', SalePrice = '" + ItemSalePrice.getText().toString() + "', ItemCode = '" + ItemCode.getText().toString() + "' WHERE ID =  "+keyid;
            helper.updateItem3Name(query);
            MNotificationClass.ShowToast(getContext(),"Updated Data");
            dismiss();
        }


    }
    public void setFieldsToEmpty(){
        ItemName.setText("");
        ItemSalePrice.setText("");
        ItemCode.setText("");
        itemselectindex=0;
    }


}
