package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.chartofaccountdialog.CustomDialogOnDismisListener;
import org.by9steps.shadihall.chartofaccountdialog.Dialog2ForAddNewItemEntry;
import org.by9steps.shadihall.chartofaccountdialog.DialogForAddNewItemEntry;
import org.by9steps.shadihall.chartofaccountdialog.DialogResturentTableLayoutSetting;
import org.by9steps.shadihall.chartofaccountdialog.VehicleBookingDialog;
import org.by9steps.shadihall.chartofaccountdialog.customGroupDialog1;
import org.by9steps.shadihall.fragments.AccountCustomGroup;
import org.by9steps.shadihall.fragments.BalSheetFragment;
import org.by9steps.shadihall.fragments.BookCalendarFragment;
import org.by9steps.shadihall.fragments.CashBookFragment;
import org.by9steps.shadihall.fragments.CashCollectionFragment;
import org.by9steps.shadihall.fragments.ChartOfAccFragment;
import org.by9steps.shadihall.fragments.DateBalSheetFragment;
import org.by9steps.shadihall.fragments.DateProfitLossFragment;
import org.by9steps.shadihall.fragments.FragmentResturent;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.MonthBalSheetFragment;
import org.by9steps.shadihall.fragments.MonthTrialBalance;
import org.by9steps.shadihall.fragments.ProfitLossFragment;
import org.by9steps.shadihall.fragments.RecoveryFragment;
import org.by9steps.shadihall.fragments.ReportsFragment;
import org.by9steps.shadihall.fragments.SalePur1Fragment;
import org.by9steps.shadihall.fragments.SummerizeTrailBalFragment;
import org.by9steps.shadihall.fragments.TrailBalanceFragment;
import org.by9steps.shadihall.fragments.VehicleBookingFragment;
import org.by9steps.shadihall.fragments.YearProfitLossFragment;
import org.by9steps.shadihall.fragments.fargmentTableAndPortation;
import org.by9steps.shadihall.fragments.fragmentAddItem;
import org.by9steps.shadihall.fragments.fragmentAddItemCategory;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuClickActivity extends AppCompatActivity implements CustomDialogOnDismisListener
        , DialogResturentTableLayoutSetting.onClickInterface{

    String currentDate;
    DatabaseHelper databaseHelper;
    Prefrence prefrence;
    //////////////////////listener for Change
    ReportsFragment reportsFragment;
    FragmentResturent ResturentFragObj;
    CashCollectionFragment cashCollectionFragment;
    fargmentTableAndPortation objTableAndPortation;
    fragmentAddItem fragmentAddItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_click);

        databaseHelper = new DatabaseHelper(this);
        prefrence=new Prefrence(this);

        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = curFormater.format(date);

        String title = "", message = "",ValuePass = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            message = extras.getString("message");
            ValuePass = extras.getString("position");
            Log.e("menusetup","Title:("+title+") message:("+message+") position:("+ValuePass+")");
        }

//        if (AppController.internet.equals("Yes")){
//            getAccount3Name();
//        }


        if (savedInstanceState == null) {

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(title);
            }

            if (message.equals("SalPur1")) {
                Bundle bundle=new Bundle();
                bundle.putString("EntryType",title);
                SalePur1Fragment fragment=new SalePur1Fragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,fragment)
                        .commit();
            }
            else if (message.equals("Booking")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new BookCalendarFragment())
                        .commit();
            }
            else if (message.equals("Recovery")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new RecoveryFragment())
                        .commit();
            }
            else if (message.equals("ChartOfAcc")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ChartOfAccFragment())
                        .commit();
            }
            else if (message.equals("AddItem")){


                Bundle bundle=new Bundle();
                bundle.putString("AddItemTitle",title);
                bundle.putString("AddItemMessage",message);
                bundle.putString("AddItemPossition",ValuePass);

                fragmentAddItem=new fragmentAddItem();
                fragmentAddItem.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,fragmentAddItem)
                        .commit();

            }
            else if(message.equals("AddItemCategory")) {
               // MNotificationClass.ShowToastTem(this,"AddItemCategory");
                fragmentAddItemCategory AddItemCategory=new fragmentAddItemCategory();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,AddItemCategory)
                        .commit();
            }
            else if(message.equals("VehicleAdd")){
                VehicleBookingFragment vehicleBookingFragment=new VehicleBookingFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,vehicleBookingFragment)
                        .commit();
            }
            else if(message.equals("AccountCustomGroup")) {
                MNotificationClass.ShowToastTem(this,"CustomiseGroup");
                AccountCustomGroup accountCustomGroup=new AccountCustomGroup();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,accountCustomGroup)
                        .commit();
            }
            //Reports
            else if (message.equals("CashBook")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new CashBookFragment())
                        .commit();
            }
            else if (message.equals("AccountEntry")) {
                reportsFragment=ReportsFragment.newInstance(ValuePass);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, reportsFragment)
                        .commit();
            }
            else if (message.equals("TreeView")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ChartOfAccFragment())
                        .commit();
            }
            else if (message.equals("CompletTrailBalance")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ChartOfAccFragment())
                        .commit();
            }
            else if (message.equals("MonthWiseTrailBalance")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new SummerizeTrailBalFragment())
                        .commit();
            }
            else if (message.equals("YearlyTrailBalance")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new MonthTrialBalance())
                        .commit();
            }
            else if (message.equals("P/LStatmentBooking")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new RecoveryFragment())
                        .commit();
            }
            else if (message.equals("P/LStatmentDateWise")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new DateProfitLossFragment())
                        .commit();
            }
            else if (message.equals("P/LStatmentyearly")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new YearProfitLossFragment())
                        .commit();
            }
            else if (message.equals("BalanceSheetDate")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new DateBalSheetFragment())
                        .commit();
            }
            else if (message.equals("BalanceSheetMonth")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new MonthBalSheetFragment())
                        .commit();
            }
            else if(message.equals("TableService")){
                ResturentFragObj=new FragmentResturent();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,ResturentFragObj)
                        .commit();

            }else if(message.equals("CashCollection")) {
                cashCollectionFragment=new CashCollectionFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,cashCollectionFragment)
                        .commit();



            }else  if(message.equals("AddTables")){
                objTableAndPortation=new fargmentTableAndPortation();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container,objTableAndPortation)
                        .commit();
            }
            else if(message.equals("CounterSales")){

                Bundle bundle=new Bundle();
                bundle.putString("TableName","counterSale");
                bundle.putString("BillAmount","0");
                bundle.putString("SalePur1ID","0");
                bundle.putString("TableSatus","counterSale");
                bundle.putString("PortaionName","0");
                bundle.putString("ClientID",prefrence.getClientIDSession());
                bundle.putString("TableID","0");
                Intent intent=new Intent(this, ResturentAddItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }else if(message.equals("KitchenScreen")){
                MNotificationClass.ShowToastTem(this,"KitchenScreen");
            }




        }

        Log.e(this.getClass().getName(),"Client ID::"+new Prefrence(this).getClientIDSession());
        Log.e(this.getClass().getName(),"ClientUserID::"+new Prefrence(this).getClientUserIDSession());
        Log.e(this.getClass().getName(),"ProjectIDSerssion::"+new Prefrence(this).getProjectIDSession());
        Log.e(this.getClass().getName(),"UserRightSession::"+new Prefrence(this).getUserRighhtsSession());

    }


    @Override
    public void onDismissListener(String key) {
        reportsFragment.getReports();
        reportsFragment.filter = 0;
        reportsFragment.searchView.setQuery("", false);
        reportsFragment.searchView.clearFocus();
    }
    public void MenuItemClickListener(String  s1, String s2, String s3, int id){
        Bundle bundle=new Bundle();
        bundle.putInt("keyid",id);
        bundle.putString("type",s1);
        bundle.putString("item1typeid",s2);
        bundle.putString("item2groupName",s3);


        Dialog2ForAddNewItemEntry obj=new Dialog2ForAddNewItemEntry();
        obj.setArguments(bundle);
        obj.show(getSupportFragmentManager(),"tag");
    }
    public void myFunction(int id,String type,String name,String price,String code){
        Bundle bundle=new Bundle();
        bundle.putInt("keyid",id);
        bundle.putString("type",type);
        bundle.putString("name",name);
        bundle.putString("price",price);
        bundle.putString("code",code);
        DialogForAddNewItemEntry dialog=new DialogForAddNewItemEntry();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(),"TAG");
    }
    public void showVehicleDialog(String vehicleID, String vehicleName,
                                  String colour, String model,
                                  String registrationNo,
                                  String contactNo, String brand,
                                  String VehicleIDGroup,
                                  String driverName) {
        Bundle bundle=new Bundle();
        bundle.putString("type","Edit");
        bundle.putString("VehicleID",vehicleID);
        bundle.putString("VehicleName",vehicleName);
        bundle.putString("colour",colour);
        bundle.putString("model",model);
        bundle.putString("RegistrationNo",registrationNo);
        bundle.putString("ContactNo",contactNo);
        bundle.putString("brand",brand);
        bundle.putString("VehicleGroupID",VehicleIDGroup);
        bundle.putString("DriverName",driverName);

        VehicleBookingDialog dialog=new VehicleBookingDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(),"TAG");
    }
    public void showCustomeGroupDialog(String name,int id){
        Bundle bundle=new Bundle();
        bundle.putString("mode","Edit");
        bundle.putString("name",name);
        bundle.putInt("id",id);
        customGroupDialog1 dialog1=new customGroupDialog1();
        dialog1.setArguments(bundle);
        dialog1.show(getSupportFragmentManager(),"TAG");
    }

    public void tableLayoutSetting(){
        DialogResturentTableLayoutSetting dialog=new DialogResturentTableLayoutSetting();
        dialog.show(getSupportFragmentManager(),"TAG");
    }
    //seeekbar update Table column
    @Override
    public void updateRecyclerView() {
        ResturentFragObj.updateTableColumn();

    }

    //updatingCashcollectionRecyclerView;
    public void updateCashcollectionRecyclerView(){
        cashCollectionFragment.updateREcyclerView();
    }



    //Dialog update Table column
    public void updateTableRecyclerView(){
        ResturentFragObj.updateTableColumn();
    }

    //update Table column
    //updatating while retruning to ResturentAddItemActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            String returnString = data.getStringExtra("keyName");
            ResturentFragObj.updateTableColumn();
        }

    }

    public void updateTableGrid(){
        objTableAndPortation.updatefun();
    }

    public void updateItemDGrid(){
        fragmentAddItem.updateGridView();
    }
}
