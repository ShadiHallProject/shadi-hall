package org.by9steps.shadihall.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.BalSheetActivity;
import org.by9steps.shadihall.activities.TrailBalanceActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalSheetFragment extends Fragment implements View.OnClickListener {

    CardView dateBalSheet, monthBalSheet;

    public BalSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bal_sheet, container, false);

        dateBalSheet = view.findViewById(R.id.bl_date);
        monthBalSheet = view.findViewById(R.id.bl_month);

        dateBalSheet.setOnClickListener(this);
        monthBalSheet.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bl_date:
                intent = new Intent(getContext(), BalSheetActivity.class);
                intent.putExtra("message","1");
                startActivity(intent);
                break;
            case R.id.bl_month:
                intent = new Intent(getContext(),BalSheetActivity.class);
                intent.putExtra("message","2");
                startActivity(intent);
                break;
        }
    }
}
