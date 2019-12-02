package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.PrefrenceResturentSeekBar;

public class DialogResturentTableItemLayoutSetting extends DialogFragment {

    public interface onClickItemInterface{
        void updateItemRecyclerView();
    }

    private Context context;
    private SeekBar seekbar;
    private TextView progressTextView;
    private PrefrenceResturentSeekBar prefrencebar;
    private  int value;
    private onClickItemInterface monClickItemInterface;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_resturent_seekbar, null);

        prefrencebar=new PrefrenceResturentSeekBar(getContext());
        value=Integer.parseInt(prefrencebar.getTABLE_ITEM_GRID_VIEW_COL());
        seekbar=(SeekBar)view.findViewById(R.id.myseekbar);
        monClickItemInterface=(onClickItemInterface)getContext();
        progressTextView=(TextView)view.findViewById(R.id.seekbar_label);

        seekbar.setMax(10);

        seekbar.setProgress(value);
        progressTextView.setText("Number of Column "+prefrencebar.getTABLE_ITEM_GRID_VIEW_COL());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if(progressChangedValue==0){
                    progressChangedValue=1;
                    prefrencebar.setTABLE_ITEM_GRID_VIEW_COL(String.valueOf(progressChangedValue));
                    monClickItemInterface.updateItemRecyclerView();
                }else {
                    prefrencebar.setTABLE_ITEM_GRID_VIEW_COL(String.valueOf(progressChangedValue));
                    monClickItemInterface.updateItemRecyclerView();
                }
                progressTextView.setText("Number of Column "+progressChangedValue);
            }
        });

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Layout Settings")
                .setOnDismissListener(this)
                .setCancelable(false)
                .create();

    }

}
