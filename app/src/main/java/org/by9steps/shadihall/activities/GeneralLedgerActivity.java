package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.GeneralLedgerAdapter;
import org.by9steps.shadihall.fragments.SelectDateFragment;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.GeneralLedger;
import org.by9steps.shadihall.model.Spinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class GeneralLedgerActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    List<GeneralLedger> mList = new ArrayList<>();
    GeneralLedgerAdapter adapter;
    Prefrence prefrence;

    DatabaseHelper databaseHelper;
    List<GeneralLedger> generalLedgerList;
    List<GeneralLedger> filterdList;
    String acID;

    ArrayList<String> listDebit = new ArrayList<>();
    ArrayList<String> mDebit = new ArrayList<>();
    SpinnerDialog debitDialog, creditDialog;


    int debit = 0, credit = 0, balance = 0;

    public static Button date_picker1, date_picker2;
    TextView debit_account;
    ImageView refresh;
    android.widget.Spinner sp_acgroup;
    private String spPosition;
    String query = "";

    //    EditText search;
    android.widget.Spinner spinner;
    SearchView searchView;
    int filter;

    String orderBy = "CBDate";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " ASC";

    //Print
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_ledger);
        SugarContext.init(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("General Ledger");
        }

        Intent intent = getIntent();
        if (intent != null){
            acID = intent.getStringExtra("AcID");
        }

        recyclerView = findViewById(R.id.recycler);
        date_picker1 = findViewById(R.id.date_picker1);
        date_picker2 = findViewById(R.id.date_picker2);
        refresh = findViewById(R.id.refresh);
        sp_acgroup = findViewById(R.id.sp_acgroup);
        searchView = findViewById(R.id.report_search);
        debit_account = findViewById(R.id.debit_account);
        spinner = findViewById(R.id.gl_spinner);

        debit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
        filterdList = new ArrayList<>();

        Date date = new Date();
        date_picker1.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        date_picker2.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));

        date_picker1.setOnClickListener(this);
        date_picker2.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(this);
        prefrence = new Prefrence(this);

        query = "SELECT * FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession();


        List<Spinner> li = databaseHelper.getSpinnerAcName(query);
        for (Spinner a : li){
//            listCredit.add(a.getName());
//            mCredit.add(a.getAcId());
            listDebit.add(a.getName());
            mDebit.add(a.getAcId());
        }

        debit_account.setOnClickListener(this);

        // Spinner Drop down elements
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("CB ID");
        spinner_list.add("Date");
        spinner_list.add("Account Name");
        spinner_list.add("Remarks");
        spinner_list.add("Debit");
        spinner_list.add("Credit");
        spinner_list.add("Balance");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_list);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        filter = 0;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 1:
                        filter = 1;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 2:
                        filter = 2;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 3:
                        filter = 3;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 4:
                        filter = 4;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 5:
                        filter = 5;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 6:
                        filter = 6;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 7:
                        filter = 7;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getGeneralLedger();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                acID = ((Account2Group) sp_acgroup.getSelectedItem()).getAcGroupID();
                getGeneralLedger();
                filter = 0;
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        });

    }

    public void getGeneralLedger(){
        mList.clear();
        debit = 0; credit = 0; balance = 0;
        Log.e("ACID",acID);

//            String query = "SELECT        ClientID, 0 AS EntryNo, MAX(CBDate) AS Date, 0 AS SelectedAc, 0 AS AgainstAc, '' AS AccountName, 'B/F' AS Particulars, SUM(Debit) AS Debit, SUM(Credit) AS Credit, '' AS EntryOf, SUM(Debit) - SUM(Credit) AS Balance\n" +
//                    "FROM            (SELECT        CashBook.ClientID, CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount AS SelectedAc, CashBook.CreditAccount, Account3Name.AcName, CashBook.CBRemarks, CashBook.Amount AS Debit, \n" +
//                    "                                                    0 AS Credit, 'CB' AS EntryOf\n" +
//                    "                          FROM            CashBook INNER JOIN\n" +
//                    "                                                    Account3Name ON CashBook.CreditAccount = Account3Name.AcNameID\n" +
//                    "                          WHERE        (CashBook.ClientID = "+prefrence.getClientIDSession()+") AND (CashBook.CBDate < '"+date_picker1.getText().toString()+"') AND (CashBook.DebitAccount = "+acID+")\n" +
//                    "                          UNION ALL\n" +
//                    "                          SELECT        CashBook_1.ClientID, CashBook_1.CashBookID, CashBook_1.CBDate, CashBook_1.CreditAccount AS SelectedAc, CashBook_1.DebitAccount, Account3Name_1.AcName, CashBook_1.CBRemarks, 0 AS Debit, \n" +
//                    "                                                   CashBook_1.Amount AS Credit, 'CB' AS EntryOf\n" +
//                    "                          FROM            CashBook AS CashBook_1 INNER JOIN\n" +
//                    "                                                   Account3Name AS Account3Name_1 ON CashBook_1.DebitAccount = Account3Name_1.AcNameID\n" +
//                    "                          WHERE        (CashBook_1.ClientID = "+prefrence.getClientIDSession()+") AND (CashBook_1.CBDate < '"+date_picker1.getText().toString()+"') AND (CashBook_1.CreditAccount = "+acID+")) AS derivedtbl_1\n" +
//                    "GROUP BY ClientID\n" +
//                    "UNION ALL\n" +
//                    "SELECT        CashBook_2.ClientID, CashBook_2.CashBookID, CashBook_2.CBDate, CashBook_2.DebitAccount AS SelectedAc, CashBook_2.CreditAccount AS AgainsAc, Account3Name_2.AcName, CashBook_2.CBRemarks, \n" +
//                    "                         CashBook_2.Amount AS Debit, 0 AS Credit, 'CB' AS EntryOf, CashBook_2.Amount AS Balance\n" +
//                    "FROM            CashBook AS CashBook_2 INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_2 ON CashBook_2.CreditAccount = Account3Name_2.AcNameID\n" +
//                    "WHERE        (CashBook_2.ClientID = "+prefrence.getClientIDSession()+") AND (CashBook_2.CBDate >= '"+date_picker1.getText().toString()+"') AND (CashBook_2.CBDate <= '"+date_picker2.getText().toString()+"') AND (CashBook_2.DebitAccount = "+acID+")\n" +
//                    "UNION ALL\n" +
//                    "SELECT        CashBook_3.ClientID, CashBook_3.CashBookID, CashBook_3.CBDate, CashBook_3.CreditAccount AS SelectedAc, CashBook_3.DebitAccount AS AgainstAc, Account3Name_3.AcName, CashBook_3.CBRemarks, 0 AS Debit, \n" +
//                    "                         CashBook_3.Amount AS Credit, 'CB' AS EntryOf, - CashBook_3.Amount AS Balance\n" +
//                    "FROM            CashBook AS CashBook_3 INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_3 ON CashBook_3.DebitAccount = Account3Name_3.AcNameID\n" +
//                    "WHERE        (CashBook_3.ClientID = "+prefrence.getClientIDSession()+") AND (CashBook_3.CBDate >= '"+date_picker1.getText().toString()+"') AND (CashBook_3.CBDate <= '"+date_picker2.getText().toString()+"') AND (CashBook_3.CreditAccount = "+acID+")"+orderby;
//
           String query="SELECT        ClientID, 0 AS EntryNo, MAX(CBDate) AS Date, 0 AS SelectedAc, 0 AS AgainstAc, '' AS AccountName, 'B/F' AS Particulars, SUM(Debit) AS Debit, SUM(Credit) AS Credit, '' AS EntryOf, SUM(Debit) - SUM(Credit) AS Balance, '' As TableName, '' As TableID, '' As User  \n" +
                   "                     FROM            (Select\n" +
                   "    CashBook.ClientID,\n" +
                   "    CashBook.CashBookID,\n" +
                   "    CashBook.CBDate,\n" +
                   "    CashBook.DebitAccount As SelectedAc,\n" +
                   "    CashBook.CreditAccount,\n" +
                   "    '' As AcName,\n" +
                   "    CashBook.CBRemarks,\n" +
                   "    CashBook.Amount As Debit,\n" +
                   "    0 As Credit,\n" +
                   "    'CB' As EntryOf,\n" +
                   "    '' As TableName,\n" +
                   "    '' As TableID,\n" +
                   "    '' As User\n" +
                   "From\n" +
                   "    CashBook\n" +
                   "Where\n" +
                   "    CashBook.ClientID = "+prefrence.getClientIDSession()+" And\n" +
                   "    CashBook.CBDate < '"+date_picker1.getText().toString()+"' And\n" +
                   "    CashBook.DebitAccount = "+acID+"\n" +
                   "                                               UNION ALL    \n" +
                   "                                              Select\n" +
                   "    CashBook_1.ClientID,\n" +
                   "    CashBook_1.CashBookID,\n" +
                   "    CashBook_1.CBDate,\n" +
                   "    CashBook_1.CreditAccount As SelectedAc,\n" +
                   "    CashBook_1.DebitAccount,\n" +
                   "    '' As AcName,\n" +
                   "    CashBook_1.CBRemarks,\n" +
                   "    0 As Debit,\n" +
                   "    CashBook_1.Amount As Credit,\n" +
                   "    'CB' As EntryOf,\n" +
                   "    '' As TableName,\n" +
                   "    CashBook_1.TableID As TableID,\n" +
                   "    '' As User\n" +
                   "From\n" +
                   "    CashBook As CashBook_1\n" +
                   "Where\n" +
                   "    CashBook_1.ClientID = "+prefrence.getClientIDSession()+" And\n" +
                   "    CashBook_1.CBDate < '"+date_picker1.getText().toString()+"' And\n" +
                   "    CashBook_1.CreditAccount = "+acID+") AS OpQuery\n" +
                   "                     GROUP BY ClientID \n" +
                   "\t\t\t\t\t \n" +
                   "\t\t\t\t\t Union ALL\n" +
                   "\t\t\t\t\t \n" +
                   "Select\n" +
                   "    CashBook_2.ClientID,\n" +
                   "    CashBook_2.CashBookID,\n" +
                   "    CashBook_2.CBDate,\n" +
                   "    CashBook_2.DebitAccount As SelectedAc,\n" +
                   "    CashBook_2.CreditAccount As AgainsAc,\n" +
                   "    Account3Name_2.AcName,\n" +
                   "    CashBook_2.CBRemarks,\n" +
                   "    CashBook_2.Amount As Debit,\n" +
                   "    0 As Credit,\n" +
                   "    'CB' As EntryOf,\n" +
                   "    CashBook_2.Amount As Balance,\n" +
                   "    CashBook_2.TableName,\n" +
                   "    CashBook_2.TableID,\n" +
                   "    Account3Name.AcName As User\n" +
                   "From\n" +
                   "    CashBook As CashBook_2 Left Join\n" +
                   "    Account3Name As Account3Name_2 On CashBook_2.CreditAccount = Account3Name_2.AcNameID\n" +
                   "            And Account3Name_2.ClientID = CashBook_2.ClientID Left Join\n" +
                   "    Account3Name On Account3Name.AcNameID = CashBook_2.ClientUserID\n" +
                   "            And Account3Name.ClientID = CashBook_2.ClientID\n" +
                   "Where\n" +
                   "    CashBook_2.ClientID = "+prefrence.getClientIDSession()+" And\n" +
                   "    CashBook_2.CBDate >= '"+date_picker1.getText().toString()+"' And\n" +
                   "    CashBook_2.CBDate <= '"+date_picker2.getText().toString()+"' And\n" +
                   "    CashBook_2.CreditAccount = "+acID+"\n" +
                   "    \n" +
                   "    UNION ALL    \n" +
                   "                    Select\n" +
                   "    CashBook_3.ClientID,\n" +
                   "    CashBook_3.CashBookID,\n" +
                   "    CashBook_3.CBDate,\n" +
                   "    CashBook_3.CreditAccount As SelectedAc,\n" +
                   "    CashBook_3.DebitAccount As AgainstAc,\n" +
                   "    Account3Name_3.AcName,\n" +
                   "    CashBook_3.CBRemarks,\n" +
                   "    0 As Debit,\n" +
                   "    CashBook_3.Amount As Credit,\n" +
                   "    'CB' As EntryOf,\n" +
                   "    -CashBook_3.Amount As Balance,\n" +
                   "    CashBook_3.TableName,\n" +
                   "    CashBook_3.TableID,\n" +
                   "    Account3Name.AcName As User\n" +
                   "From\n" +
                   "    CashBook As CashBook_3 Left Join\n" +
                   "    Account3Name As Account3Name_3 On CashBook_3.DebitAccount = Account3Name_3.AcNameID\n" +
                   "            And Account3Name_3.ClientUserID = CashBook_3.ClientID Left Join\n" +
                   "    Account3Name On Account3Name.AcName = CashBook_3.ClientUserID\n" +
                   "            And Account3Name.ClientID = CashBook_3.ClientID\n" +
                   "Where\n" +
                   "    CashBook_3.ClientID = "+prefrence.getClientIDSession()+" And\n" +
                   "    CashBook_3.CBDate >= '"+date_picker1.getText().toString()+"' And\n" +
                   "    CashBook_3.CBDate <= '"+date_picker2.getText().toString()+"' And\n" +
                   "    CashBook_3.CreditAccount = "+acID+" ";


            generalLedgerList = databaseHelper.getGeneralLedger(query);

        for (GeneralLedger g : generalLedgerList){
            balance = balance + Integer.valueOf(g.getBalance());
            debit = debit + Integer.valueOf(g.getDebit());
            credit = credit + Integer.valueOf(g.getCredit());
            mList.add(GeneralLedger.createRow(g.getClientID(),g.getEntryNo(),g.getDate(),g.getSelectedAc(), g.getAgainstAc(),g.getAccountName(),g.getParticulars(),g.getDebit(),g.getCredit(),g.getEntryOf(),String.valueOf(balance),g.getTablename(),g.getTableid()));
        }
        mList.add(GeneralLedger.createTotal("","Total","","", "","","",String.valueOf(debit),String.valueOf(credit),"",String.valueOf(debit - credit)));
        if (generalLedgerList.size() > 0 && generalLedgerList.get(0).getEntryNo().equals("0")){
            mList.add(GeneralLedger.createTotal("","Transaction Total","","", "","","",String.valueOf(debit - Integer.valueOf(generalLedgerList.get(0).getDebit())),String.valueOf(credit - Integer.valueOf(generalLedgerList.get(0).getCredit())),"",""));

        }else if (generalLedgerList.size() > 0 && !generalLedgerList.get(0).getEntryNo().equals("0")){
            mList.add(GeneralLedger.createTotal("","Transaction Total","","", "","","",String.valueOf(debit),String.valueOf(credit),"",""));
        }

        adapter = new GeneralLedgerAdapter(this,mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_picker1:
                AppController.date = "GL1";
                FragmentManager fm = getSupportFragmentManager();
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(fm, "DatePicker");
                break;
            case R.id.date_picker2:
                AppController.date = "GL2";
                FragmentManager fm1 = getSupportFragmentManager();
                DialogFragment newFragment1 = new SelectDateFragment();
                newFragment1.show(fm1, "DatePicker");
                break;
            case R.id.debit_account:
                debitDialog = new SpinnerDialog(GeneralLedgerActivity.this,listDebit,"Select Debit");
                debitDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        acID = mDebit.get(i);
                        debit_account.setText(s);
                    }
                });
                debitDialog.showSpinerDialog();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cb_menu, menu);
        MenuItem referesh = menu.findItem(R.id.action_refresh);
        referesh.setVisible(false);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_print:
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filter(String text) {
        filterdList = new ArrayList<>();

        //looping through existing elements
        if (!text.isEmpty()) {
            for (GeneralLedger s : mList) {
                if (s.isRow() == 1) {
                    switch (filter) {
                        case 1:
                            if (s.getEntryNo().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 2:
                            if (s.getDate().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 3:
                            if (s.getAccountName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 4:
                            if (s.getParticulars().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 5:
                            if (s.getDebit().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 6:
                            if (s.getCredit().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 7:
                            if (s.getBalance().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                    }
                }
            }
        } else {
            filterdList = mList;
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdList);

    }

    public void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfname = "GeneralLedger.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, output);
        writer.createXmpMetadata();
        writer.setTagged();
        writer.setPageEvent(new Footer());
        document.open();
        document.addLanguage("en-us");

        PdfDictionary parameters = new PdfDictionary();Log.e("PDFDocument","Created2");
        parameters.put(PdfName.MODDATE, new PdfDate());

        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Chunk chunk = new Chunk("Client Name", chapterFont);
        Paragraph name = new Paragraph("Address",paragraphFont);
        name.setIndentationLeft(0);
        Paragraph contact = new Paragraph("Contact",paragraphFont);
        contact.setIndentationLeft(0);

        PdfPTable title = new PdfPTable(new float[]{3, 5, 3});
        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        title.getDefaultCell().setMinimumHeight(25);
        title.setTotalWidth(PageSize.A4.getWidth());
        title.setWidthPercentage(100);
        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setSpacingBefore(5);
        title.setSpacingAfter(3);
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        PdfPCell cell = new PdfPCell(new Phrase("General Ledger",chapterFont));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(cell);
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));

        String query = "SELECT * FROM Account3Name WHERE AcNameID = "+acID;

        List<Account3Name> list = databaseHelper.getAccount3Name(query);

        for (Account3Name a : list){

            title.addCell(titleCell(a.getAcName(),PdfPCell.ALIGN_LEFT));
            title.addCell(titleCell("",PdfPCell.ALIGN_CENTER));
            title.addCell(titleCell(spinner.getSelectedItem()+": "+searchView.getQuery(),PdfPCell.ALIGN_CENTER));

            title.addCell(titleCell(a.getAcAddress(),PdfPCell.ALIGN_LEFT));
            title.addCell(titleCell("",PdfPCell.ALIGN_CENTER));
            title.addCell(titleCell("From"+" "+date_picker1.getText().toString(),PdfPCell.ALIGN_CENTER));

            title.addCell(titleCell(a.getAcContactNo(),PdfPCell.ALIGN_LEFT));
            title.addCell(titleCell("",PdfPCell.ALIGN_CENTER));
//            PdfPCell dCell2 = new PdfPCell(new Phrase());
//            dCell2.setBorder(PdfPCell.NO_BORDER);
//            dCell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//            dCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            title.addCell(titleCell("To"+"     "+date_picker2.getText().toString(),PdfPCell.ALIGN_CENTER));

        }

//        PdfPTable tab = new PdfPTable(new float[]{4, 6, 6, 6}); //one page contains 15 records
//        tab.setWidthPercentage(100);
//        tab.setSpacingBefore(10);
//        Font voucherFount = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD);
//        voucherFount.setColor(BaseColor.WHITE);
//        PdfPCell pre = new PdfPCell(new Phrase("Previous Balance",voucherFount));
//        pre.setHorizontalAlignment(Element.ALIGN_CENTER);
//        pre.setVerticalAlignment(Element.ALIGN_CENTER);
//        pre.setBackgroundColor(BaseColor.BLACK);
//        PdfPCell tra = new PdfPCell(new Phrase("Transaction",voucherFount));
//        tra.setHorizontalAlignment(Element.ALIGN_CENTER);
//        tra.setVerticalAlignment(Element.ALIGN_CENTER);
//        tra.setBackgroundColor(BaseColor.BLACK);
//        PdfPCell clo = new PdfPCell(new Phrase("Closing Balance",voucherFount));
//        clo.setHorizontalAlignment(Element.ALIGN_CENTER);
//        clo.setVerticalAlignment(Element.ALIGN_CENTER);
//        clo.setBackgroundColor(BaseColor.BLACK);
//        tab.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
//        tab.addCell(pre);
//        tab.addCell(tra);
//        tab.addCell(clo);

        PdfPTable table = new PdfPTable(new float[]{4, 3, 3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(40);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.setSpacingBefore(20);
        table.addCell("CB ID");
        table.addCell("Date");
        table.addCell("Ac Name");
        table.addCell("Remarks");
        table.addCell("Debit");
        table.addCell("Credit");
        table.addCell("Balance");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.PINK);
        }

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
        PdfPCell total = new PdfPCell(new Phrase("Total",totalFont));
        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
        total.setFixedHeight(35);

        debit = 0; credit = 0; balance = 0;

        if (filter > 0){
            for (GeneralLedger g : filterdList){
                balance = balance + Integer.valueOf(g.getBalance());
                debit = debit + Integer.valueOf(g.getDebit());
                credit = credit + Integer.valueOf(g.getCredit());

                table.addCell(getCell(g.getEntryNo(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getDate(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getAccountName(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getParticulars(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(String.valueOf(balance), PdfPCell.ALIGN_RIGHT));
            }
            table.addCell(total);
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(debit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(credit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(debit - credit), PdfPCell.ALIGN_RIGHT));

            PdfPCell traTotal = new PdfPCell(new Phrase("Transaction Total",totalFont));
            total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            total.setVerticalAlignment(Element.ALIGN_MIDDLE);
            total.setFixedHeight(35);

            table.addCell(traTotal);
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(debit - Integer.valueOf(filterdList.get(0).getDebit())), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(credit - Integer.valueOf(filterdList.get(0).getCredit())), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
        }else {
            for (GeneralLedger g : generalLedgerList){

                balance = balance + Integer.valueOf(g.getBalance());
                debit = debit + Integer.valueOf(g.getDebit());
                credit = credit + Integer.valueOf(g.getCredit());

                table.addCell(getCell(g.getEntryNo(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getDate(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getAccountName(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getParticulars(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(g.getCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(String.valueOf(balance), PdfPCell.ALIGN_RIGHT));
            }

            table.addCell(total);
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(debit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(credit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(debit - credit), PdfPCell.ALIGN_RIGHT));

            PdfPCell traTotal = new PdfPCell(new Phrase("Transaction Total",totalFont));
            total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            total.setVerticalAlignment(Element.ALIGN_MIDDLE);
            total.setFixedHeight(35);

            table.addCell(traTotal);
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(debit - Integer.valueOf(filterdList.get(0).getDebit())), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(credit - Integer.valueOf(filterdList.get(0).getCredit())), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
        }


        document.open();

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph("Cash Book \n\n", f);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(chunk);
        document.add(name);
        document.add(contact);
        document.add(title);
//        document.add(tab);
        document.add(table);

        document.close();
        customPDFView();
        Log.e("PDFDocument","Created");
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(35);
        cell.setPadding(3);
        return cell;
    }

    public PdfPCell footerCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(25);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    public PdfPCell titleCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(20);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public void customPDFView(){
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        } else {
            Toast.makeText(GeneralLedgerActivity.this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }

    class Footer extends PdfPageEventHelper {
        Font font;
        PdfTemplate t;
        Image total;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
                font =  new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            PdfPTable table = new PdfPTable(new float[]{3, 4, 2});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(20);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            Date dat = new Date();
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            table.addCell(footerCell(df.format(dat), PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("www.easysoft.com.pk",PdfPCell.ALIGN_LEFT));
            Log.e("PAGE NUMBER",String.valueOf(writer.getPageNumber()));
            table.addCell(footerCell(String.format("Page %d ", writer.getPageNumber() -1),PdfPCell.ALIGN_LEFT));
//            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
//            table.addCell(footerCell("www.easysoft.com.pk",PdfPCell.ALIGN_LEFT));
//            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));

            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            table.writeSelectedRows(0, -1, 36, 30, canvas);
            canvas.endMarkedContentSequence();

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), font),
                    2, 4, 0);
        }
    }
}
