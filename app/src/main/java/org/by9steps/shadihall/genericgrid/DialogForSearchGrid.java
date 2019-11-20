package org.by9steps.shadihall.genericgrid;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.chartofaccountdialog.CustomDialogOnDismisListener;
import org.by9steps.shadihall.helper.MNotificationClass;

import java.util.ArrayList;

public class DialogForSearchGrid extends DialogFragment implements View.OnClickListener {
    private View customView;
    private Spinner spinner;
    private Button filtergrid;
    private EditText editText;
    ArrayList<String> arrayListbackup;
    public DialogClickListener listener;
public interface DialogClickListener{
    void HandleClickLisnterOfDialog(String colName,String SearchText);
}
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        customView = LayoutInflater.from(getContext()).inflate(R.layout.generic_grid_search, null);
        spinner = customView.findViewById(R.id.spinnerforcol);
        filtergrid = customView.findViewById(R.id.ggridsearchbtn);filtergrid.setOnClickListener(this);
        editText=customView.findViewById(R.id.grideditext);
        Bundle bundle = getArguments();
        ArrayList<String> arrayList = bundle.getStringArrayList("columnarray");
        arrayListbackup=new ArrayList<>(arrayList);
        for (int i = 0; i <arrayList.size() ; i++) {
            if(arrayList.get(i).contains("_")){
                String aray[]=arrayList.get(i).split("_");
                arrayList.remove(i);
                arrayList.add(i,aray[0]);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return new AlertDialog.Builder(getActivity())
                .setView(customView)
                .setTitle(getTag())
                .setCancelable(false)
                .create();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null) {
            long index=spinner.getSelectedItemId();

            listener.HandleClickLisnterOfDialog(arrayListbackup.get((int) index),
                    editText.getText().toString());
        }
      //  MNotificationClass.ShowToastTem(getContext(),"item:"+spinner.getSelectedItem()+" Txt:"+editText.getText());
    }
}
