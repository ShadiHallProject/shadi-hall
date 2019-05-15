package org.by9steps.shadihall.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.BalSheetActivity;
import org.by9steps.shadihall.activities.ProfitLossActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfitLossFragment extends Fragment implements View.OnClickListener {

    CardView profitEvent, profitDate, profitYear;


    public ProfitLossFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profit_loss, container, false);

        profitEvent = view.findViewById(R.id.profit_event);
        profitDate = view.findViewById(R.id.profit_date);
        profitYear = view.findViewById(R.id.profit_year);

        profitYear.setOnClickListener(this);
        profitDate.setOnClickListener(this);
        profitEvent.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.profit_event:
                intent = new Intent(getContext(), ProfitLossActivity.class);
                intent.putExtra("message","1");
                startActivity(intent);
                break;
            case R.id.profit_date:
                intent = new Intent(getContext(),ProfitLossActivity.class);
                intent.putExtra("message","2");
                startActivity(intent);
                break;
            case R.id.profit_year:
                intent = new Intent(getContext(),ProfitLossActivity.class);
                intent.putExtra("message","3");
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
