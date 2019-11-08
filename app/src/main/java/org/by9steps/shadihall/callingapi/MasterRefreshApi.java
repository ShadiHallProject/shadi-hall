package org.by9steps.shadihall.callingapi;

import android.app.ProgressDialog;
import android.content.Context;

import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.CashBook;

public class MasterRefreshApi {

    public interface finalMasterCallBackListner{
        public void allCallBackReport(StringBuilder builder);
    }
private Context context;
private ProgressDialog progressDialog;
private DatabaseHelper databaseHelper;
private Prefrence prefrence;
//////////////////////////////
private Item2GroupApis item2GroupApis;
private Item3NameApis item3NameApis;
private SalePur1And2Apis salePur1And2Apis;
private BookingApis bookingApis;
private CashBookApis cashBookApis;
//////////////////////////////
  public StringBuilder finalReportofAllmethod;

    public MasterRefreshApi(Context context, ProgressDialog progressDialog, DatabaseHelper databaseHelper, Prefrence prefrence) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.databaseHelper = databaseHelper;
        this.prefrence=prefrence;
        finalReportofAllmethod=new StringBuilder();
        item2GroupApis=new Item2GroupApis(context,progressDialog,databaseHelper);
        item3NameApis=new Item3NameApis(context,progressDialog,databaseHelper);
        salePur1And2Apis=new SalePur1And2Apis(context,progressDialog,databaseHelper,prefrence);
        bookingApis=new BookingApis(context,progressDialog,databaseHelper,prefrence);
        cashBookApis=new CashBookApis(context,progressDialog,databaseHelper,prefrence);
    }


    public  void RefreshAllApis(final finalMasterCallBackListner listner){

        item2GroupApis.trigerAllMethodinSequence(prefrence.getClientIDSession(), new Item2GroupApis.listentodatafinish() {
            @Override
            public void method(String name) {
                finalReportofAllmethod.append("\n Item2Group:"+name);
                item3NameApis.trigerAllMethod(prefrence.getClientIDSession(), new Item3NameApis.item3namefunListener() {
                    @Override
                    public void method(String success, String funType) {
                        finalReportofAllmethod.append("\n Item3Name: Sucess:"+success+" FunType"+funType);
                        salePur1And2Apis.trigerAllSalpur1and2Method();
                        salePur1And2Apis.finallistnerForAlldone=new SalePur1And2Apis.SalePur1funListener() {
                            @Override
                            public void FinishCallBackmethod(String success, String funType) {
                                finalReportofAllmethod.append("\n Salepur1And2: Sucess:"+success+" FunType"+funType);
                                bookingApis.trigerAllMethodInRow(new BookingApis.BookingApisListener(){
                                    @Override
                                    public void FinishBookingCallBackMethod(String success, String funType) {
                                        finalReportofAllmethod.append("\n Booking: Sucess:"+success+" FunType"+funType);
                                        cashBookApis.trigerAllMethodInRow(new CashBookApis.CashBookApiListener() {
                                            @Override
                                            public void FinishCashBookCallBackMethod(String success, String funType) {
                                                finalReportofAllmethod.append("\n CAshBook: Sucess:"+success+" FunType"+funType);
                                            listner.allCallBackReport(finalReportofAllmethod);
                                            }
                                        });
                                    }
                                });
                            }
                        };
                    }
                });
            }
        });




    }
}
