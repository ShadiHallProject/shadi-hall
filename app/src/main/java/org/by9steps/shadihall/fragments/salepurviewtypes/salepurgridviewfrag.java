package org.by9steps.shadihall.fragments.salepurviewtypes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.SalePur1RecyclerAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.JoinQueryDaliyEntryPage1;

import java.util.List;

public class salepurgridviewfrag extends Fragment {

    private SalePur1RecyclerAdapter recyclerAdapter;
    private DatabaseHelper databaseHelper;
    String EntryType=null;
    private RecyclerView mrecyclerview;
    public static List<JoinQueryDaliyEntryPage1> page1List;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.salepur1fragment_part1gridview, container, false);
        AssignIdsToViewWidget(view);
        /////////////////////////////Sample TEsting
        EntryType=getArguments().getString("EntryType");


        ///////////////////////////////////////////////
        FillRecyclerViewAdapter(1);

        return view;

    }

    private void AssignIdsToViewWidget(View vv) {

        databaseHelper = new DatabaseHelper(getContext());
        mrecyclerview = vv.findViewById(R.id.salepurrecyclerview);
    }

    private void FillRecyclerViewAdapter(int sortbyfiels) {

        String ClientId = new Prefrence(getContext()).getClientIDSession();

        String query = "  SELECT         SalePur1.SalePur1ID, SalePur1.EntryType, SalePur1.SPDate, Account3Name.AcName, SalePur1.Remarks, SUM(SalePur2.Total) AS BillAmt, SalePur1.ClientID, SalePur1.NameOfPerson, SalePur1.PayAfterDay,SalePur1.ID\n" +
                "        FROM            SalePur1 INNER JOIN\n" +
                "        Account3Name ON SalePur1.AcNameID = Account3Name.AcNameID INNER JOIN\n" +
                "        SalePur2 ON SalePur1.SalePur1ID = SalePur2.SalePur1ID\n" +
                "        GROUP BY SalePur1.SalePur1ID, SalePur1.EntryType, SalePur1.SPDate, Account3Name.AcName, SalePur1.ClientID, SalePur1.NameOfPerson, SalePur1.PayAfterDay, SalePur1.Remarks\n" +
                "        HAVING        (SalePur1.EntryType = '" + EntryType + "') AND (SalePur1.ClientID = " + ClientId + ")\n" +
                "        ORDER BY "+sortbyfiels+" DESC  ";

        page1List=databaseHelper.GetDataFroJoinQuery(query);

        recyclerAdapter = new SalePur1RecyclerAdapter(getContext(), page1List);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mrecyclerview.setAdapter(recyclerAdapter);

    }
}
