package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.customGroup1Adapter;
import org.by9steps.shadihall.fragments.AccountCustomGroup;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Account5customGroup2;
import org.by9steps.shadihall.model.JoinQueryAccount3Name;

import java.util.List;

public class customGroupDialog2 extends DialogFragment implements View.OnClickListener{

    DatabaseHelper databaseHelper;
    Prefrence prefrence;
    customGroup1Adapter adapter;
    RecyclerView recyclerView;
    private List<JoinQueryAccount3Name> Newlist;
    AccountCustomGroup accountCustomGroup;

    Button buttonAdd;
    String CustomGroup1ID;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.customegroup_dialog2,null);

       CustomGroup1ID=getArguments().getString("CustomGroup1ID");

        databaseHelper=new DatabaseHelper(getContext());
        prefrence=new Prefrence(getContext());
        accountCustomGroup=new AccountCustomGroup();
        recyclerView =view.findViewById(R.id.recyclerviewCustomeGroupDialog2);
        buttonAdd=view.findViewById(R.id.btnAddMemberInGroup1Dialog2);buttonAdd.setOnClickListener(this);

        fillGridData();
        return new AlertDialog.Builder(getContext())
                .setView(view)
               // .setTitle("Add Vehicle")
                .setCancelable(false)
                .setOnDismissListener(this)
                .create();

    }


    public void fillGridData(){

        String query="Select\n" +
                "    Account3Name.AcNameID,\n" +
                "    Account3Name.AcName,\n" +
                "    Query1.AcNameID As AcNameID1,\n" +
                "    Account3Name.ClientID\n" +
                "From\n" +
                "    Account3Name Left Join\n" +
                "    (Select\n" +
                "         Account5CustomGroup2.CustomGroup1ID,\n" +
                "         Account5CustomGroup2.AcNameID,\n" +
                "         Account5CustomGroup2.ClientID\n" +
                "     From\n" +
                "         Account5CustomGroup2\n" +
                "     Where\n" +
                "         Account5CustomGroup2.CustomGroup1ID = '"+CustomGroup1ID+"') As Query1 On Query1.AcNameID = Account3Name.AcNameID And\n" +
                "            Query1.ClientID = Account3Name.ClientID\n" +
                "Where\n" +
                "    Query1.AcNameID Is Null And\n" +
                "    Account3Name.ClientID = '"+prefrence.getClientIDSession()+"'";


        Newlist=databaseHelper.GetDataFromJoinQueryAccount3Name(query);
        adapter=new customGroup1Adapter(getContext(),Newlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btnAddMemberInGroup1Dialog2)
        {

            List<JoinQueryAccount3Name> joinQueryAccount3Names;
            joinQueryAccount3Names=adapter.getdata();

            int size=joinQueryAccount3Names.size();
            MNotificationClass.ShowToast(getContext(),"list size "+size);
            for (int i = 0; i < size; i++) {

                int maxid=databaseHelper.getMaxValueOfAccount5customGroup2(prefrence.getClientIDSession());

                Account5customGroup2 account5customGroup2=new Account5customGroup2();
                account5customGroup2.setCustomGroup2ID(String.valueOf(maxid));
                account5customGroup2.setCustomGroup1ID(CustomGroup1ID);
                account5customGroup2.setAcNameID(joinQueryAccount3Names.get(i).getAcNameID());
                account5customGroup2.setClientID(prefrence.getClientIDSession());
                account5customGroup2.setClientUserID(prefrence.getClientUserIDSession());
                account5customGroup2.setSysCode("0");
                account5customGroup2.setNetCode("0");
                account5customGroup2.setUpdatedDate(GenericConstants.NullFieldStandardText);

              long id=  databaseHelper.createAccount5customGroup2(account5customGroup2);
                if(id!=-1){
                    MNotificationClass.ShowToast(getContext(),"Data Added");
                    dismiss();

                }else {
                    MNotificationClass.ShowToast(getContext(),"Sorry Error Found..!");
                }
            }

        }

    }


}
