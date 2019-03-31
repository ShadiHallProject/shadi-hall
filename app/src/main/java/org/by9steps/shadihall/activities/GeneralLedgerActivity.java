package org.by9steps.shadihall.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.orm.SugarContext;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.GeneralLedgerAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.GeneralLedger;
import org.by9steps.shadihall.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneralLedgerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<GeneralLedger> mList = new ArrayList<>();

    DatabaseHelper databaseHelper;
    List<GeneralLedger> generalLedgerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_ledger);
        SugarContext.init(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("General Ledger");
        }

        recyclerView = findViewById(R.id.recycler);

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String d = sf.format(date);

        databaseHelper = new DatabaseHelper(this);

        List<User> list = User.listAll(User.class);
        for (User u : list){
            String query = "SELECT        ClientID, 0 AS EntryNo, MAX(CBDate) AS Date, 0 AS SelectedAc, 0 AS AgainstAc, '' AS AccountName, 'B/F' AS Particulars, SUM(Debit) AS Debit, SUM(Credit) AS Credit, '' AS EntryOf, SUM(Debit) - SUM(Credit) AS Balance\n" +
                    "FROM            (SELECT        CashBook.ClientID, CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount AS SelectedAc, CashBook.CreditAccount, Account3Name.AcName, CashBook.CBRemarks, CashBook.Amount AS Debit, \n" +
                    "                                                    0 AS Credit, 'CB' AS EntryOf\n" +
                    "                          FROM            CashBook INNER JOIN\n" +
                    "                                                    Account3Name ON CashBook.CreditAccount = Account3Name.AcNameID\n" +
                    "                          WHERE        (CashBook.ClientID = "+u.getClientID()+") AND (CashBook.CBDate < '"+d+"') AND (CashBook.DebitAccount = 2)\n" +
                    "                          UNION ALL\n" +
                    "                          SELECT        CashBook_1.ClientID, CashBook_1.CashBookID, CashBook_1.CBDate, CashBook_1.CreditAccount AS SelectedAc, CashBook_1.DebitAccount, Account3Name_1.AcName, CashBook_1.CBRemarks, 0 AS Debit, \n" +
                    "                                                   CashBook_1.Amount AS Credit, 'CB' AS EntryOf\n" +
                    "                          FROM            CashBook AS CashBook_1 INNER JOIN\n" +
                    "                                                   Account3Name AS Account3Name_1 ON CashBook_1.DebitAccount = Account3Name_1.AcNameID\n" +
                    "                          WHERE        (CashBook_1.ClientID = "+u.getClientID()+") AND (CashBook_1.CBDate < '"+d+"') AND (CashBook_1.CreditAccount = 2)) AS derivedtbl_1\n" +
                    "GROUP BY ClientID\n" +
                    "UNION ALL\n" +
                    "SELECT        CashBook_2.ClientID, CashBook_2.CashBookID, CashBook_2.CBDate, CashBook_2.DebitAccount AS SelectedAc, CashBook_2.CreditAccount AS AgainsAc, Account3Name_2.AcName, CashBook_2.CBRemarks, \n" +
                    "                         CashBook_2.Amount AS Debit, 0 AS Credit, 'CB' AS EntryOf, CashBook_2.Amount AS Balance\n" +
                    "FROM            CashBook AS CashBook_2 INNER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook_2.CreditAccount = Account3Name_2.AcNameID\n" +
                    "WHERE        (CashBook_2.ClientID = "+u.getClientID()+") AND (CashBook_2.CBDate >= '"+d+"') AND (CashBook_2.CBDate <= '"+d+"') AND (CashBook_2.DebitAccount = 2)\n" +
                    "UNION ALL\n" +
                    "SELECT        CashBook_3.ClientID, CashBook_3.CashBookID, CashBook_3.CBDate, CashBook_3.CreditAccount AS SelectedAc, CashBook_3.DebitAccount AS AgainstAc, Account3Name_3.AcName, CashBook_3.CBRemarks, 0 AS Debit, \n" +
                    "                         CashBook_3.Amount AS Credit, 'CB' AS EntryOf, - CashBook_3.Amount AS Balance\n" +
                    "FROM            CashBook AS CashBook_3 INNER JOIN\n" +
                    "                         Account3Name AS Account3Name_3 ON CashBook_3.DebitAccount = Account3Name_3.AcNameID\n" +
                    "WHERE        (CashBook_3.ClientID = "+u.getClientID()+") AND (CashBook_3.CBDate >= '"+d+"') AND (CashBook_3.CBDate <= '"+d+"') AND (CashBook_3.CreditAccount = 2)";
            generalLedgerList = databaseHelper.getGeneralLedger(query);
        }
        for (GeneralLedger g : generalLedgerList){
            Log.e("NAME",g.getAccountName());
            mList.add(GeneralLedger.createRow(g.getClientID(),g.getEntryNo(),g.getDate(),g.getSelectedAc(), g.getAgainstAc(),g.getAccountName(),g.getParticulars(),g.getDebit(),g.getCredit(),g.getEntryOf(),g.getBalance()));
        }

        GeneralLedgerAdapter adapter = new GeneralLedgerAdapter(this,mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
}
