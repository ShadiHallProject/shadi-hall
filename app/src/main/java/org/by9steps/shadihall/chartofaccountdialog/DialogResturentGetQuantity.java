package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.by9steps.shadihall.R;


public class DialogResturentGetQuantity extends DialogFragment implements View.OnClickListener {

    private Activity context;
    TextView itemName,itemTotalPrice;
    EditText itemQuantity,itemPrice;
    Button btnAddOrder,btnCancleOrder;
    private sendCustomQtyPrice msendCustomQtyPrice;

    private int price;
    private int qty;
    private int totalPrice;
    private  int index;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.quantity_entry_dialog,null);

        itemName=view.findViewById(R.id.label_itemName);
        itemQuantity=view.findViewById(R.id.label_itemQuantity);
        itemPrice=view.findViewById(R.id.label_itemPrice);
        itemTotalPrice=view.findViewById(R.id.label_totalPrice);
        btnAddOrder=view.findViewById(R.id.btnAddOrder);btnAddOrder.setOnClickListener(this);
        btnCancleOrder=view.findViewById(R.id.btnCancleOrder1);btnCancleOrder.setOnClickListener(this);


        //
        msendCustomQtyPrice=(sendCustomQtyPrice)getContext();

        itemName.setText(getArguments().getString("itemName"));

        if(getArguments().getString("itemPrice")!=null)
            price=Integer.parseInt(getArguments().getString("itemPrice"));
        else
            price=0;
        itemPrice.setText(getArguments().getString("itemPrice"));


        if(String.valueOf(getArguments().getInt("qty"))!=null)
             qty=getArguments().getInt("qty");
        else
            qty=0;
        itemQuantity.setText(String.valueOf(qty));

        index=getArguments().getInt("indexID");


        totalPrice=qty*price;
        itemTotalPrice.setText(String.valueOf(totalPrice));

        itemQuantity.addTextChangedListener(new TextWatcher() {
            int myOrderText=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() != 0) {

                    myOrderText=  Integer.parseInt(itemQuantity.getText().toString());
                    totalPrice =myOrderText*price;
                    itemTotalPrice.setText(String.valueOf(totalPrice));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });




        itemPrice.addTextChangedListener(new TextWatcher() {
            int myOrderTextPrice=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() != 0) {

                    myOrderTextPrice=  Integer.parseInt(itemPrice.getText().toString());
                    totalPrice =qty*myOrderTextPrice;
                    itemTotalPrice.setText(String.valueOf(totalPrice));
                   // itemPrice.setText(myOrderTextPrice);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Quantity Entry")
                .setOnDismissListener(this)
                .setCancelable(false)
                .create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAddOrder:
                if(itemQuantity.getText().toString().isEmpty() && itemPrice.getText().toString().isEmpty())
                    return;
                else {
                    msendCustomQtyPrice.getmyCustomeData(index, itemQuantity.getText().toString(), itemPrice.getText().toString());
                    this.dismiss();
                }
                break;
            case R.id.btnCancleOrder1:
                this.dismiss();
                break;
        }
    }

    //sending data in DifferRowAdapter class;
    public interface sendCustomQtyPrice{
        void getmyCustomeData(int index, String Quantity, String Price);

    }
}
