package org.by9steps.shadihall.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.CBSetting;
import java.util.List;

public class CashBookSettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Switch date, id, debit, credit, remarks, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_book_setting);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cashbook Setting");
        }

        date = findViewById(R.id.sw_date);
        id = findViewById(R.id.sw_id);
        debit = findViewById(R.id.sw_debit);
        credit = findViewById(R.id.sw_credit);
        remarks = findViewById(R.id.sw_remarks);
        amount = findViewById(R.id.sw_amount);

        date.setOnCheckedChangeListener(this);
        id.setOnCheckedChangeListener(this);
        debit.setOnCheckedChangeListener(this);
        credit.setOnCheckedChangeListener(this);
        remarks.setOnCheckedChangeListener(this);
        amount.setOnCheckedChangeListener(this);

        List<CBSetting> list = CBSetting.listAll(CBSetting.class);
        for (CBSetting c : list){
            date.setChecked(c.getDste());
            id.setChecked(c.getCbId());
            debit.setChecked(c.getDebit());
            credit.setChecked(c.getCredit());
            remarks.setChecked(c.getRemarks());
            amount.setChecked(c.getAmount());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CBSetting cbSetting = CBSetting.findById(CBSetting.class,1);
        String message;
        switch (buttonView.getId()){
            case R.id.sw_date:
                if (isChecked){
                    cbSetting.setDste(true);
                    cbSetting.save();
                }else {
                    cbSetting.setDste(false);
                    cbSetting.save();
                }
                break;
            case R.id.sw_id:
                if (isChecked){
                    cbSetting.setCbId(true);
                    cbSetting.save();
                }else {
                    cbSetting.setCbId(false);
                    cbSetting.save();
                }
                break;
            case R.id.sw_debit:
                if (isChecked){
                    cbSetting.setDebit(true);
                    cbSetting.save();
                }else {
                    cbSetting.setDebit(false);
                    cbSetting.save();
                }
                break;
            case R.id.sw_credit:
                if (isChecked){
                    cbSetting.setCredit(true);
                    cbSetting.save();
                }else {
                    cbSetting.setCredit(false);
                    cbSetting.save();
                }
                break;
            case R.id.sw_remarks:
                if (isChecked){
                    cbSetting.setRemarks(true);
                    cbSetting.save();
                }else {
                    cbSetting.setRemarks(false);
                    cbSetting.save();
                }
                break;
            case R.id.sw_amount:
                if (isChecked){
                    cbSetting.setAmount(true);
                    cbSetting.save();
                }else {
                    cbSetting.setAmount(false);
                    cbSetting.save();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
