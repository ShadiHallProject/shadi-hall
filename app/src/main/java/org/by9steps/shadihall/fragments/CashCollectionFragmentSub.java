package org.by9steps.shadihall.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.cashCollectionAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.joinQueryCashCollection;

import java.util.List;

@SuppressLint("ValidFragment")
public class CashCollectionFragmentSub extends Fragment {

    String BillStatus;

    @SuppressLint("ValidFragment")
    public CashCollectionFragmentSub(String billStatus) {
        BillStatus = billStatus;
    }

    RecyclerView recyclerView;
    Prefrence prefrence;
    DatabaseHelper databaseHelper;
    List<joinQueryCashCollection> mlist;
    cashCollectionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_cash_collection_sub,container,false);
        prefrence=new Prefrence(getContext());
        databaseHelper=new DatabaseHelper(getContext());
        recyclerView=view.findViewById(R.id.CashcollectionrecyclerView);

        String query="Select\n" +
                "    SalePur1.ClientID,\n" +
                "    SalePur1.SalePur1ID As BillNo,\n" +
                "    SalePur1.EntryType,\n" +
                "    SalePur1.BillAmount,\n" +
                "    Query1.BillReceivedAmt As Received,\n" +
                "    IfNull(SalePur1.BillAmount, 0) - IfNull(Query1.BillReceivedAmt, 0) As BillBalance,\n" +
                "    SalePur1.BillStatus,\n" +
                "    SalePur1.Remarks,\n" +
                "    SalePur1.SPDate,\n" +
                "    Account3Name.AcName As User\n" +
                "From\n" +
                "    SalePur1 Left Join\n" +
                "    (Select\n" +
                "         CashBook.ClientID,\n" +
                "         CashBook.TableID,\n" +
                "         CashBook.TableName,\n" +
                "         Sum(CashBook.Amount) As BillReceivedAmt\n" +
                "     From\n" +
                "         CashBook\n" +
                "     Where\n" +
                "         CashBook.ClientID = '"+prefrence.getClientIDSession()+"' And\n" +
                "         CashBook.TableName = 'SalPur1_Sales'\n" +
                "     Group By\n" +
                "         CashBook.ClientID,\n" +
                "         CashBook.TableID,\n" +
                "         CashBook.TableName) As Query1 On Query1.TableID = SalePur1.SalePur1ID\n" +
                "            And Query1.ClientID = SalePur1.ClientID\n" +
                "            And Query1.TableName = SalePur1.EntryType Left Join\n" +
                "    Account3Name On Account3Name.AcNameID = SalePur1.ClientUserID\n" +
                "            And Account3Name.ClientID = SalePur1.ClientID\n" +
                "Where\n" +
                "    SalePur1.ClientID = '"+prefrence.getClientIDSession()+"' And\n" +
                "    SalePur1.EntryType = 'SalPur1_Sales' And\n" +
                "    IfNull(SalePur1.BillAmount, 0) - IfNull(Query1.BillReceivedAmt, 0) <> 0 And\n" +
                "    SalePur1.BillStatus = '"+BillStatus+"'";



        mlist=databaseHelper.getDataFromJoinQueryCashCollection(query);



//        for (int i = 0; i < mlist.size(); i++) {
//            Log.d("result", "ClientID index "+i+ " "+mlist.get(i).getClientID());
//            Log.d("result", "BillNo: index "+i+ " "+mlist.get(i).getBillNo());
//            Log.d("result", "EntryType: index "+i+ " "+mlist.get(i).getEntryType());
//            Log.d("result", "BillAmount: index "+i+ " "+mlist.get(i).getBillAmount());
//            Log.d("result", "Received: index "+i+ " "+mlist.get(i).getReceived());
//            Log.d("result", "BillBalance: index "+i+ " "+mlist.get(i).getBillBalance());
//            Log.d("result", "BillStatus: index "+i+ " "+mlist.get(i).getBillStatus());
//            Log.d("result", "Remarks: index "+i+ " "+mlist.get(i).getRemarks());
//            Log.d("result", "SPDate: index "+i+ " "+mlist.get(i).getSPDate());
//            Log.d("result", "User: index "+i+ " "+mlist.get(i).getUser());
//
//        }


        MNotificationClass.ShowToastTem(getContext(),"list size "+mlist.size());
        adapter=new cashCollectionAdapter(getContext(),mlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        return view;
    }

    public void updateRecyclerView()
    {
        adapter.notifyDataSetChanged();
    }


}
