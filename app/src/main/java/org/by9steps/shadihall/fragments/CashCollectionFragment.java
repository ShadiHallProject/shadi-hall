package org.by9steps.shadihall.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.MNotificationClass;

public class CashCollectionFragment extends Fragment implements View.OnClickListener {
    Button btnRunningBills,btnPendingBills;
    CashCollectionFragmentSub newFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_cash_collection,container,false);
        btnRunningBills=view.findViewById(R.id.btnRunningBills);btnRunningBills.setOnClickListener(this);
        btnPendingBills=view.findViewById(R.id.btnPendingsBills);btnPendingBills.setOnClickListener(this);


        fillGridView("Running");
        return view;
    }


    public void fillGridView(String BillStatus){

        // Create new fragment and transaction
        newFragment = new CashCollectionFragmentSub(BillStatus);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.CashCollectionContainer, newFragment);
        transaction.commit();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btnRunningBills){
            fillGridView("Running");
            MNotificationClass.ShowToastTem(getContext(),"Running");
        }
        else if(v.getId()== R.id.btnPendingsBills){
            fillGridView("Pending");
            MNotificationClass.ShowToastTem(getContext(),"Pending");
        }


    }

    public void updateREcyclerView(){
        newFragment.updateRecyclerView();
    }
}
