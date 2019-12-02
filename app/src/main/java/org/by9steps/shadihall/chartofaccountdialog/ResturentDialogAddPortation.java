package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.by9steps.shadihall.R;

public class ResturentDialogAddPortation extends DialogFragment implements View.OnClickListener{
    TextInputEditText portationName;
    Button buttonAdd,buttonCancel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.resturent_dialog_add_portaion,null);
        portationName=view.findViewById(R.id.tietPortaionName);
        buttonAdd=view.findViewById(R.id.btnResturentPortationAdd);


        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add Vehicle")
                .setCancelable(false)
                .setOnDismissListener(this)
                .create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnResturentPortationAdd){

        }else if(v.getId()==R.id.btnResturentPortationCancel){
            this.dismiss();
        }
    }
}
