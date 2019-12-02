package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.ItemRecyclerViewAdapter;
import org.by9steps.shadihall.adapters.OrderSelectedItems;
import org.by9steps.shadihall.chartofaccountdialog.DialogResturentGetQuantity;
import org.by9steps.shadihall.chartofaccountdialog.DialogResturentTableItemLayoutSetting;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.PrefrenceResturentSeekBar;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.DifferentRowAdapter;
import org.by9steps.shadihall.model.ResturentSectionModel3;
import org.by9steps.shadihall.model.SelectedItems;
import org.by9steps.shadihall.model.item3name.UpdatedDate;
import org.by9steps.shadihall.model.joinQueryForResturentAddOrder;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResturentAddItemActivity extends AppCompatActivity implements
        View.OnClickListener, DifferentRowAdapter.onClickBillAmountInterface,
        DialogResturentTableItemLayoutSetting.onClickItemInterface, DialogResturentGetQuantity.sendCustomQtyPrice  {

    private PrefrenceResturentSeekBar mPrefrenceseekbar;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private Prefrence prefrence;
    int NUMBER_OF_COLS=1;
    Button btnCancelOrder,btnAddOrder;
   // SectionRecyclerViewAdapterAddOrder adapterAddOrder;
    int myViewType;
    DifferentRowAdapter myAdapter;
    int maxid;
    //for getting bundle data
    String tableName,billAmount,SalePur1ID,TableSatus,PortaionName,ClientID,TableID;

    private int total,thisBillAmount;
    TextView tvNetBillAmount,tvTotal, tvBillAmount,tvBillsalpur1ID;

    private RecyclerView recyclerViewUserorder;
    private List<SelectedItems> listSelectedItems;
    private ImageView imageViewTrolley;
    private ScrollView containerSelectedItems;

    private int serialcount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_resturent_add_item);


        tableName=getIntent().getExtras().getString("TableName");
        billAmount=getIntent().getExtras().getString("BillAmount");
        SalePur1ID=getIntent().getExtras().getString("SalePur1ID");

        TableSatus=getIntent().getExtras().getString("TableSatus");
        PortaionName=getIntent().getExtras().getString("PortaionName");
        ClientID=getIntent().getExtras().getString("ClientID");
        TableID=getIntent().getExtras().getString("TableID");


        if(billAmount == null)
            billAmount=String.valueOf(0);

        tvNetBillAmount=findViewById(R.id.label_netBillAmount);
        tvBillAmount =findViewById(R.id.labe_thisAmount);

        tvTotal=findViewById(R.id.label_total1);
        tvNetBillAmount.setText(billAmount);
        tvTotal.setText(billAmount);
        tvBillsalpur1ID=findViewById(R.id.Billsalpur1ID);
        tvBillsalpur1ID.setText(SalePur1ID);



        databaseHelper=new DatabaseHelper(getApplicationContext());
        prefrence=new Prefrence(getApplicationContext());
        mPrefrenceseekbar=new PrefrenceResturentSeekBar(getApplicationContext());


        btnCancelOrder=findViewById(R.id.btnCancelOrder);btnCancelOrder.setOnClickListener(this);
        btnAddOrder=findViewById(R.id.btnAddOrder);btnAddOrder.setOnClickListener(this);



        setUpRecyclerView();
        myPopulateRecyclerView();


       imageViewTrolley=findViewById(R.id.imgRecyclerView);imageViewTrolley.setOnClickListener(this);
        containerSelectedItems=findViewById(R.id.containerSelectedItems);
        recyclerViewUserorder=findViewById(R.id.userSelectedItems);
        listSelectedItems=new ArrayList<SelectedItems>();



       popupOrderRecyclerView();


    }
    public void popupOrderRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
      //  layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        OrderSelectedItems items=new OrderSelectedItems(this,listSelectedItems);
        recyclerViewUserorder.setLayoutManager(layoutManager);
        recyclerViewUserorder.setAdapter(items);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sb_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView1 = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView1.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String text = s;
                myAdapter.filter(text);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
           onBackPressed();
        if(item.getItemId()== R.id.action_seekbar){
            DialogResturentTableItemLayoutSetting obj=new DialogResturentTableItemLayoutSetting();
            obj.show(getSupportFragmentManager(),"TAG");
        }

        return super.onOptionsItemSelected(item);
    }

    //setup recycler view
    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.sectioned_recycler_view_addOrder);
        recyclerView.setHasFixedSize(true);
    }

    //populate recycler view
    public void myPopulateRecyclerView(){

        int NUMCOL=Integer.parseInt(mPrefrenceseekbar.getTABLE_ITEM_GRID_VIEW_COL());

        String query2="select * from Item2Group where ClientID = '"+prefrence.getClientIDSession()+"'";
        List<String> Item2GroupName=databaseHelper.getItem2GroupName(query2);
        String query4="Select\n" +
                "    Item3Name.Item2GroupID,\n" +
                "    Item2Group.Item2GroupName,\n" +
                "    Item3Name.Item3NameID,\n" +
                "    Item3Name.ItemName,\n" +
                "    Item3Name.SalePrice,\n" +
                "    Item3Name.Stock,\n" +
                "    Item3Name.ItemStatus,\n" +
                "    Item1Type.ItemType,\n" +
                "    Item3Name.ClientID\n" +
                "From\n" +
                "    Item3Name Left Join\n" +
                "    Item2Group On Item2Group.Item2GroupID = Item3Name.Item2GroupID\n" +
                "            And Item2Group.ClientID = Item3Name.ClientID Left Join\n" +
                "    Item1Type On Item1Type.Item1TypeID = Item2Group.Item1TypeID\n" +
                "Where\n" +
                "    Item1Type.ItemType <> 'Raw Material' And\n" +
                "    Item3Name.ClientID = '"+prefrence.getClientIDSession()+"'";

        List<joinQueryForResturentAddOrder> addOrderList;
        addOrderList=databaseHelper.GetDataFromjoinQueryForResturentAddOrder(query4);
        List<ResturentSectionModel3> resturentSectionModel3list=new ArrayList<>();
        for (int i = 0; i < Item2GroupName.size(); i++) {
            myViewType=1;
            for (int j = 0; j < addOrderList.size(); j++) {
                if(Item2GroupName.get(i).equals(addOrderList.get(j).getItem2GroupName()))
                {
                    resturentSectionModel3list.add(new ResturentSectionModel3(Item2GroupName.get(i),myViewType,addOrderList.get(j)));
                    if(myViewType==1) {
                        resturentSectionModel3list.add(new ResturentSectionModel3(Item2GroupName.get(i),0,addOrderList.get(j)));
                    }
                    myViewType=0;
                }
            }
            myViewType=1;
           }

        myAdapter=new DifferentRowAdapter(this,resturentSectionModel3list,billAmount);

        //for header satting
        final GridLayoutManager manager=new GridLayoutManager(this,NUMCOL);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return myAdapter.isHeader(i) ? manager.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {

        final MediaPlayer sound = MediaPlayer.create(this, R.raw.click);
        if(v.getId()== R.id.imgRecyclerView)
        {
            sound.start();


            if(containerSelectedItems.getVisibility()==View.GONE){
                containerSelectedItems.setVisibility(View.VISIBLE);
                imageViewTrolley.setImageResource(R.drawable.ic_arrow_drop_up);
            }else{
                containerSelectedItems.setVisibility(View.GONE);
                imageViewTrolley.setImageResource(R.drawable.ic_arrow_drop_down);
            }
        }
         if(v.getId()== R.id.btnCancelOrder){
            sound.start();
             Intent intent = new Intent();
             intent.putExtra("keyName","sendback description from 2nd activity");
             setResult(RESULT_OK, intent);
             finish();
             MNotificationClass.ShowToastTem(this,"Order Cancel");

        } else if(v.getId()== R.id.btnAddOrder) {
             int count=0;
             for (int i = 0; i < myAdapter.myfun().size(); i++) {
                 if(myAdapter.myfun().get(i).getItemArrayListJoinQuery().getIsselected() && myAdapter.myfun().get(i).getType()!=ResturentSectionModel3.HEADER_TYPE)
                 {
                     count++;
                 }
             }
             if(count>0) {
                 count=0;

                 sound.start();
                 thisBillAmount = 0;

                 boolean flag = true;
                 for (int i = 0; i < myAdapter.myfun().size(); i++) {
                     if (myAdapter.myfun().get(i).getItemArrayListJoinQuery().getIsselected() && myAdapter.myfun().get(i).getType() != ResturentSectionModel3.HEADER_TYPE) {
                         //this is only for setting maxid and TableStauts To Running
                         //insetting empty record for getting the salePur1ID
                         if (flag && TableSatus.equals(ItemRecyclerViewAdapter.TABLE_STATUS_RUNNING)) {

                             //  MNotificationClass.ShowToastTem(this,"if Flag "+flag+" TableStatus "+TableSatus);

                             flag = false;
                             maxid = Integer.parseInt(SalePur1ID);
                         } else if (flag) {
                             //  MNotificationClass.ShowToastTem(this,"elseif() Flag "+flag+" TableStatus "+TableSatus);

                             flag = false;
                             SaveSalePur1ToDB(); //setting maxid
                         }

                         Log.d("myviewtype", "onClick: " + i + "section name " + myAdapter.myfun().get(i).getSectionLabel());
                         Log.d("myviewtype", "onClick: " + i + "item type " + myAdapter.myfun().get(i).getType());
                         Log.d("myviewtype", "onClick: " + i + "Name  " + myAdapter.myfun().get(i).getItemArrayListJoinQuery().getItemName());
                         Log.d("myviewtype", "onClick: " + i + "Quantitye " + myAdapter.myfun().get(i).getQuantity());
                         Log.d("myviewtype", "onClick: " + i + "*******end *****");
                         thisBillAmount = thisBillAmount + SaveSalePur2ToDB(myAdapter.myfun().get(i).getQuantity(), myAdapter.myfun().get(i).getItemArrayListJoinQuery());
                         count++;


                     }
                 }

                 total = thisBillAmount + Integer.parseInt(billAmount);
                 updateBillAmountSalePur1(total);

                 if(TableSatus.equals("counterSale")){
                    // MNotificationClass.ShowToastTem(this, "counterSale " + count);
                     insertIntoCashBook();
                     updateSalepur1BillStatus();

                 }else {
                     updateResturent2TableSalePur1IDandTableStatus();//updatating Salepur1Id
                     //   MNotificationClass.ShowToastTem(this, "Total Item Order " + count);
                 }
                 //updating recycler view table
                 Intent intent = new Intent();
                 intent.putExtra("keyName", "amir");
                 setResult(RESULT_OK, intent);
                 finish();
             }else {
                 MNotificationClass.ShowToast(this,"Select Items First");
             }




        }

    }

   // createSalePur1Data

    private void SaveSalePur1ToDB() {

        String remarks=null;
        if(TableSatus.equals("counterSale")){
            remarks="counterSale";
        }else{
            remarks=tableName;
        }

        //int   maxid = refdb.SlePur1.GetMaximumID(databaseHelper, ClientID, EntryType);;
        String entryType="SalPur1_Sales";

        maxid = refdb.SlePur1.GetMaximumID(databaseHelper, prefrence.getClientIDSession(), entryType);;

        Log.d("maxid", "SaveDataToSalePur1: "+maxid);

        Salepur1 salepur1 = new Salepur1();
        salepur1.setSalePur1ID((maxid));
        salepur1.setEntryType("SalPur1_Sales");
        ////////////////////Setting Today Date
        // SPDate spDate = new SPDate();
        //spDate.setDate(datePicker.getText().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date());

        salepur1.setData(date);
        /////////////////////////////////////////
        salepur1.setAcNameID(1312);
        salepur1.setRemarks(remarks);
        salepur1.setClientID(Integer.parseInt(prefrence.getClientIDSession()));
        salepur1.setClientUserID(Integer.parseInt(prefrence.getClientUserIDSession()));
        salepur1.setNetCode(GenericConstants.NullFieldStandardText);
        salepur1.setSysCode(GenericConstants.NullFieldStandardText);
        ////////////////////////////Set Updated Date
        UpdatedDate updatedDate = new UpdatedDate();
        updatedDate.setDate(GenericConstants.NullFieldStandardText);
        salepur1.setUpdatedDate(updatedDate);
        /////////////////
        salepur1.setNameOfPerson(null);

        salepur1.setPayAfterDay(0);

        salepur1.setBillSatus("Running");


        long iid = refdb.SlePur1.AddItemSalePur1(databaseHelper, salepur1);
        Log.d("iid", "SaveDataToSalePur1: "+iid);
        if (iid != -1) {

           // MNotificationClass.ShowToast(this, "Receipt Created " + iid);
            MNotificationClass.ShowToast(this, "SaveDataToSalePur1 " +maxid);

        } else {
            MNotificationClass.ShowToast(this, "Receipt Not Created ");
        }

    }



    public int SaveSalePur2ToDB(int qty, joinQueryForResturentAddOrder mylistitem ) {

        String entryType="SalPur1_Sales";

        int totalBill=0;
        SalePur2 salePur2 = new SalePur2();
        salePur2.setItem3NameID(Integer.parseInt(mylistitem.getItem3NameID()));
        salePur2.setSalePur1ID(maxid);
        salePur2.setEntryType(entryType);
        salePur2.setItemSerial(serialcount);
        salePur2.setItemDescription(tableName);
        salePur2.setQtyAdd("0");
        salePur2.setQtyLess(String.valueOf(qty));

        salePur2.setQty(String.valueOf(qty));
        salePur2.setPrice(mylistitem.getSalePrice());

        totalBill=qty* Integer.parseInt(mylistitem.getSalePrice());
        salePur2.setTotal(String.valueOf(totalBill));
        salePur2.setLocation("1");
        salePur2.setClientID(Integer.parseInt(prefrence.getClientIDSession()));
        salePur2.setClientUserID(Integer.parseInt(prefrence.getClientUserIDSession()));
        salePur2.setNetCode(GenericConstants.NullFieldStandardText);
        salePur2.setSysCode(GenericConstants.NullFieldStandardText);
        ///////////////////////Setting Updated Date
        UpdatedDate updatedDate = new UpdatedDate();
        updatedDate.setDate(GenericConstants.NullFieldStandardText);
        salePur2.setUpdatedDate(updatedDate);
        long status = refdb.SalePur2.AddItemSalePur2(databaseHelper, salePur2);
        if (status != -1) {
            serialcount--;
            MNotificationClass.ShowToast(getApplicationContext(), "Item Added SalePur2");
        } else {
            MNotificationClass.ShowToast(getApplicationContext(), "Item Not Added");
            return -1;
        }
        return totalBill;

    }

    public void updateBillAmountSalePur1(int billAmount){
        int statusid=databaseHelper.updateSalePur1BillAmount(maxid,billAmount);
        if (statusid == -1){
             MNotificationClass.ShowToast(this, "Data Not Updated BillAmount");
        }
        else{
            MNotificationClass.ShowToast(this, "Data Updated BillAmount");
        }

    }


//show dialog
    public void showQuantityDialog(String itemName,String itemPrice,int qty,int index){

        Bundle bundle=new Bundle();
        bundle.putString("itemName",itemName);
        bundle.putString("itemPrice",itemPrice);
        bundle.putInt("qty",qty);
        bundle.putInt("indexID",index);

        DialogResturentGetQuantity obj=new DialogResturentGetQuantity();
        obj.setArguments(bundle);
        obj.show(getSupportFragmentManager(),"TAG");
    }

//this is for getting value of thisBillAmount
    @Override
    public void updateBillAmount(List<ResturentSectionModel3> list) {
      //  thisBillAmountInt=quantity*Integer.parseInt(salePrice);

        thisBillAmount=0;
        int count=0;
        listSelectedItems.clear();

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getItemArrayListJoinQuery().getIsselected() && list.get(i).getType()!=ResturentSectionModel3.HEADER_TYPE)
            {

                Log.d("myviewtype", "onClick: "+i+"section name "+list.get(i).getSectionLabel());
                Log.d("myviewtype", "onClick: "+i+"item type "+list.get(i).getType());
                Log.d("myviewtype", "onClick: "+i+"Name  "+list.get(i).getItemArrayListJoinQuery().getItemName());
                Log.d("myviewtype", "onClick: "+i+"Quantitye "+list.get(i).getQuantity());
                Log.d("myviewtype", "onClick: "+i+"*******end *****");

                thisBillAmount= thisBillAmount + list.get(i).getQuantity() * Integer.parseInt(list.get(i).getItemArrayListJoinQuery().getSalePrice());
                count++;

                listSelectedItems.add(new SelectedItems(list.get(i).getItemArrayListJoinQuery().getItemName(),list.get(i).getItemArrayListJoinQuery().getSalePrice(),list.get(i).getQuantity(),list.get(i).getQuantity() * Integer.parseInt(list.get(i).getItemArrayListJoinQuery().getSalePrice())));

            }

        }
        tvBillAmount.setText(""+thisBillAmount);
        total=thisBillAmount + Integer.parseInt(billAmount);
        tvTotal.setText(""+total);

        popupOrderRecyclerView();

        MNotificationClass.ShowToastTem(this,"Total Item Order "+ count);

    }

    private void updateResturent2TableSalePur1IDandTableStatus(){

        String TableStatus="Running";
        int statusid=databaseHelper.updateResturent2TableSalePur1ID(tableName,maxid,TableStatus,TableID);
        if (statusid == -1)
            MNotificationClass.ShowToast(this, "Data Not Updated SalePur1ID Resturent2");
        else {
            MNotificationClass.ShowToast(this, "Data Updated SalePur1ID Resturent2");
        }
    }



    //for seekbar update itemGridView
    @Override
    public void updateItemRecyclerView() {
        myPopulateRecyclerView();
       // MNotificationClass.ShowToastTem(getApplicationContext(),"notifydatachange");
    }

    @Override
    public void getmyCustomeData(int index, String Quantity, String Price) {

        myAdapter.getdatefromDialogQuantity(index,Quantity,Price);
     //   MNotificationClass.ShowToastTem(this,"click pass");

    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("keyName","sendback description from 2nd activity");
        setResult(RESULT_OK, intent);

        finish();

    }

    //inserting data into cashBook
    private void insertIntoCashBook(){


        int maxcashbookid = refdb.CashBookTableRef.getmaxCashBookID(databaseHelper, "" + prefrence.getClientIDSession());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

       // MNotificationClass.ShowToastTem(getContext(),"insertIntoCashBookid "+maxcashbookid +" date "+date);

        databaseHelper.createCashBook(new CashBook(maxcashbookid+"",
                date,  //CBDAte
                prefrence.getClientIDSession(), //DebitAccount
                "8",
                tableName,   //remarks
                String.valueOf(total), //Amount
                prefrence.getClientIDSession(),//ClientID
                prefrence.getClientUserIDSession(),//ClientUSerID
                "0",
                "0",
                GenericConstants.NullFieldStandardText,
                String.valueOf(maxid),//TableID
                String.valueOf(maxcashbookid), //serialNo
                "SalPur1_Sales")); //
    }

    private void updateSalepur1BillStatus(){

        SalePur1ID=String.valueOf(maxid);

        String BillStatus="Clear";
        int statusid=databaseHelper.updateSalePur1BillSatus(SalePur1ID,BillStatus,prefrence.getClientIDSession());
        if (statusid == -1)
            MNotificationClass.ShowToast(this, "Data Not Updated updateSalePur1BillSatus");
        else
            MNotificationClass.ShowToast(this, "Data Updated BillStatus = Clear");



    }

}
