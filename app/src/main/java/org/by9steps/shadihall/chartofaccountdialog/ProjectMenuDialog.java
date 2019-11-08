package org.by9steps.shadihall.chartofaccountdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;

public class ProjectMenuDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View vv= inflater.inflate(R.layout.dialog_for_accounting, container,false);


        return vv;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

     //   Dialog dialog=new Dialog(getContext());

       // View vv= LayoutInflater.from(getContext()).inflate(R.layout.dialog_for_accounting, null);
//this.setCancelable(false);
//        AlertDialog.Builder dialog1=new AlertDialog.Builder(getContext());
//        dialog1.setView(vv);
//        dialog1.setCancelable(false);
//        return dialog1.create();

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


}
