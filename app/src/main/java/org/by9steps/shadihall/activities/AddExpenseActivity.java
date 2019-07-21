package org.by9steps.shadihall.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.CashBook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout date_layout;
    TextView date;
    TextInputLayout description_layout;
    TextInputEditText description;
    TextInputLayout amount_layout;
    TextInputEditText amount;
    TextInputLayout debit_account_layout, credit_account_layout;
    TextView debit_account, credit_account, tv_cr, tv_db;
    Button add;

    InputValidation inputValidation;
    Prefrence prefrence;

    String tableID, spinnerType;
    ProgressDialog pDialog;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Expense Collection");
        }

        Intent intent = getIntent();
        if (intent != null) {
            tableID = intent.getStringExtra("BookingID");
            spinnerType = intent.getStringExtra("Spinner");
        }

        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);
        prefrence = new Prefrence(this);

        date_layout = findViewById(R.id.date_layout);
        date = findViewById(R.id.date);
        description_layout = findViewById(R.id.description_layout);
        description = findViewById(R.id.description);
        amount_layout = findViewById(R.id.enter_amount_layout);
        amount = findViewById(R.id.enter_amount);
        debit_account = findViewById(R.id.debit_account);
        debit_account_layout = findViewById(R.id.debit_account_layout);
        credit_account = findViewById(R.id.credit_account);
        credit_account_layout = findViewById(R.id.credit_account_layout);
        tv_cr = findViewById(R.id.tv_cr);
        tv_db = findViewById(R.id.tv_db);
        add = findViewById(R.id.add);

        Date date1 = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        String d = curFormater.format(date1);
        date.setText(d);

        if (!spinnerType.equals("Hide")){
            tv_cr.setVisibility(View.GONE);
            tv_db.setVisibility(View.GONE);
            debit_account.setOnClickListener(this);
            credit_account.setOnClickListener(this);
            debit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
            credit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
        }else {
//            credit_account_layout.setVisibility(View.GONE);
//            debit_account_layout.setVisibility(View.GONE);
            credit_account.setText("Expense");
            debit_account.setText("Cash");
        }

        add.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add:

                if (!inputValidation.isInputEditTextFilled(amount, amount_layout, getString(R.string.error_message_amount))) {
                    return;
                }else{

                    int seriolNo = databaseHelper.getMaxValue("SELECT max(SerialNo) FROM CashBook") + 1;
                    String cashID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Cash'");
                    String expenseID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Booking Expense'");

                    Log.e("IDSS EXPEN", String.valueOf(seriolNo)+" / "+cashID+" / "+expenseID);

                    databaseHelper.createCashBook(new CashBook("0", date.getText().toString(), expenseID, cashID, description.getText().toString(), amount.getText().toString(), prefrence.getClientIDSession(),prefrence.getClientUserIDSession(),"0","0","0",tableID,String.valueOf(seriolNo),"Booking"));
                    cleraCashe();

                }
                break;
        }

    }

    public void cleraCashe(){
        description.setText("");
        amount.setText("");
    }
}
