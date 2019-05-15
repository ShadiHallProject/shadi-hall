package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.BalSheetDateAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DateBalSheetFragment extends Fragment implements View.OnClickListener {

    ProgressDialog mProgress;
    String orderBy = "CBDate";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " DESC";

    TextView dbs_date, dbs_capital, dbs_profitloss, dbs_liabilities, dbs_cpl, dbs_assets;

    RecyclerView recyclerView;
    List<BalSheet> mList = new ArrayList<>();
    int m = 0, capital, profitLoss, liabilities, cpl, assets;
    int gCapital, gProfitLoss, gLiabilities, gCpl, gAssets;

    DatabaseHelper databaseHelper;
    List<BalSheet> balSheetList;


    List<BalSheet> filterdList;

//    EditText search;
    Spinner spinner;
    SearchView searchView;
    int filter;
    BalSheetDateAdapter adapter;

    //Print
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;
    String d = "0";

    public DateBalSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_bal_sheet, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler);

        searchView = view.findViewById(R.id.dbs_search);
        spinner = view.findViewById(R.id.dbs_spinner);

        dbs_date = view.findViewById(R.id.dbs_date);
        dbs_capital = view.findViewById(R.id.dbs_capital);
        dbs_profitloss = view.findViewById(R.id.dbs_profitloss);
        dbs_liabilities = view.findViewById(R.id.dbs_liabilities);
        dbs_cpl = view.findViewById(R.id.dbs_cpl);
        dbs_assets = view.findViewById(R.id.dbs_assets);

        dbs_date.setOnClickListener(this);
        dbs_capital.setOnClickListener(this);
        dbs_profitloss.setOnClickListener(this);
        dbs_liabilities.setOnClickListener(this);
        dbs_cpl.setOnClickListener(this);
        dbs_assets.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getContext());

        getBalsheet();

//        getBalSheet();

        // Spinner Drop down elements
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("CB Date");
        spinner_list.add("Capital");
        spinner_list.add("ProfitLoss");
        spinner_list.add("Liabilities");
        spinner_list.add("C+P+L");
        spinner_list.add("Assets");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinner_list);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        filter = 0;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 1:
                        filter = 1;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 2:
                        filter = 2;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 3:
                        filter = 3;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 4:
                        filter = 4;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 5:
                        filter = 5;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 6:
                        filter = 6;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        return view;
    }

    public void getBalsheet(){

        List<User> list = User.listAll(User.class);
        for (User u : list) {
            String query = "SELECT        CBDate, SUM(Capital) AS Capital, SUM(Expense) + SUM(Revenue) AS ProfitLoss, SUM(Liabilities) AS Liabilities, SUM(Expense) + SUM(Revenue) + SUM(Capital) + SUM(Liabilities) AS [C + P + L], SUM(Assets) AS Assets,\n" +
                    "                         ClientID\n" +
                    "FROM            (SELECT        derivedtbl_1.ClientID, derivedtbl_1.CBDate, Account1Type.AcTypeName, CAST(CASE WHEN AcTypeName = 'Expense' THEN (SUM(derivedtbl_1.Debit) - SUM(derivedtbl_1.Credit)) ELSE 0 END AS INT) AS Expense,\n" +
                    "                                                    CAST(CASE WHEN AcTypeName = 'Revenue' THEN (SUM(derivedtbl_1.Debit) - SUM(derivedtbl_1.Credit)) ELSE 0 END AS INT) AS Revenue, CAST(CASE WHEN AcTypeName = 'Capital' THEN (SUM(derivedtbl_1.Debit)\n" +
                    "                                                    - SUM(derivedtbl_1.Credit)) ELSE 0 END AS INT) AS Capital, CAST(CASE WHEN AcTypeName = 'Assets And Liability' THEN (CASE WHEN (SUM(Debit) - SUM(Credit)) > 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END)\n" +
                    "                                                    ELSE 0 END AS INT) AS Assets, CAST(CASE WHEN AcTypeName = 'Assets And Liability' THEN (CASE WHEN (SUM(Debit) - SUM(Credit)) < 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END) ELSE 0 END AS INT)\n" +
                    "                                                    AS Liabilities\n" +
                    "                          FROM            (SELECT        CreditAccount AS AccountID, 0 AS Debit, Amount AS Credit, ClientID, CBDate\n" +
                    "                                                    FROM            CashBook AS CashBook\n" +
                    "                                                    WHERE        (ClientID = " + u.getClientID() + ")\n" +
                    "                                                    UNION ALL\n" +
                    "                                                    SELECT        DebitAccount AS AccountID, Amount AS Debit, 0 AS Credit, ClientID, CBDate\n" +
                    "                                                    FROM            CashBook AS CashBook_1\n" +
                    "                                                    WHERE        (ClientID = " + u.getClientID() + ")) AS derivedtbl_1 INNER JOIN\n" +
                    "                                                    Account3Name ON derivedtbl_1.AccountID = Account3Name.AcNameID INNER JOIN\n" +
                    "                                                    Account2Group ON Account3Name.AcGroupID = Account2Group.AcGroupID INNER JOIN\n" +
                    "                                                    Account1Type ON Account2Group.AcTypeID = Account1Type.AcTypeID\n" +
                    "                          GROUP BY derivedtbl_1.ClientID, Account1Type.AcTypeName, derivedtbl_1.CBDate) AS derivedtbl_2\n" +
                    "GROUP BY ClientID, CBDate"+
                    orderby;
            balSheetList = databaseHelper.getBalSheet(query);
        }

        for (BalSheet b : balSheetList) {
            String[] separated = b.getCBDate().split("-");
            if (m == 0) {
                mList.add(BalSheet.createSection(separated[1] + "/" + separated[2]));
                mList.add(BalSheet.createRow(b.getCBDate(), b.getCapital(), b.getProfitLoss(), b.getLiabilities(), b.getC_P_L(), b.getAssets(), b.getClientID()));
                m = Integer.valueOf(separated[1]);

                capital = Integer.valueOf(b.getCapital()) + capital;
                profitLoss = Integer.valueOf(b.getProfitLoss()) + profitLoss;
                liabilities = Integer.valueOf(b.getLiabilities()) + liabilities;
                cpl = Integer.valueOf(b.getC_P_L()) + cpl;
                assets = Integer.valueOf(b.getAssets()) + assets;
                gCapital = Integer.valueOf(b.getCapital()) + gCapital;
                gProfitLoss = Integer.valueOf(b.getProfitLoss()) + gProfitLoss;
                gLiabilities = Integer.valueOf(b.getLiabilities()) + gLiabilities;
                gCpl = Integer.valueOf(b.getC_P_L()) + gCpl;
                gAssets = Integer.valueOf(b.getAssets()) + gAssets;
            } else if (m == Integer.valueOf(separated[1])) {
                capital = Integer.valueOf(b.getCapital()) + capital;
                profitLoss = Integer.valueOf(b.getProfitLoss()) + profitLoss;
                liabilities = Integer.valueOf(b.getLiabilities()) + liabilities;
                cpl = Integer.valueOf(b.getC_P_L()) + cpl;
                assets = Integer.valueOf(b.getAssets()) + assets;
                gCapital = Integer.valueOf(b.getCapital()) + gCapital;
                gProfitLoss = Integer.valueOf(b.getProfitLoss()) + gProfitLoss;
                gLiabilities = Integer.valueOf(b.getLiabilities()) + gLiabilities;
                gCpl = Integer.valueOf(b.getC_P_L()) + gCpl;
                gAssets = Integer.valueOf(b.getAssets()) + gAssets;
                mList.add(BalSheet.createRow(b.getCBDate(), b.getCapital(), b.getProfitLoss(), b.getLiabilities(), b.getC_P_L(), b.getAssets(), b.getClientID()));
            } else {
                mList.add(BalSheet.createTotal(String.valueOf(capital), String.valueOf(profitLoss), String.valueOf(liabilities), String.valueOf(cpl), String.valueOf(assets)));
                capital = 0;
                profitLoss = 0;
                liabilities = 0;
                cpl = 0;
                assets = 0;
                capital = Integer.valueOf(b.getCapital()) + capital;
                profitLoss = Integer.valueOf(b.getProfitLoss()) + profitLoss;
                liabilities = Integer.valueOf(b.getLiabilities()) + liabilities;
                cpl = Integer.valueOf(b.getC_P_L()) + cpl;
                assets = Integer.valueOf(b.getAssets()) + assets;
                gCapital = Integer.valueOf(b.getCapital()) + gCapital;
                gProfitLoss = Integer.valueOf(b.getProfitLoss()) + gProfitLoss;
                gLiabilities = Integer.valueOf(b.getLiabilities()) + gLiabilities;
                gCpl = Integer.valueOf(b.getC_P_L()) + gCpl;
                gAssets = Integer.valueOf(b.getAssets()) + gAssets;
                mList.add(BalSheet.createSection(separated[1] + "/" + separated[2]));
                mList.add(BalSheet.createRow(b.getCBDate(), b.getCapital(), b.getProfitLoss(), b.getLiabilities(), b.getC_P_L(), b.getAssets(), b.getClientID()));
                m = Integer.valueOf(separated[1]);
            }
        }
        mList.add(BalSheet.createTotal(String.valueOf(capital), String.valueOf(profitLoss), String.valueOf(liabilities), String.valueOf(cpl), String.valueOf(assets)));
        mList.add(BalSheet.createSection("Grand Total"));
        mList.add(BalSheet.createTotal(String.valueOf(gCapital), String.valueOf(gProfitLoss), String.valueOf(gLiabilities), String.valueOf(gCpl), String.valueOf(gAssets)));
        adapter = new BalSheetDateAdapter(getContext(), mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void getBalSheet() {
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/BalSheet.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("BalSheet");
                            Log.e("SSSSS", jsonArray.toString());
                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {
//                                Account3Name.deleteAll(Account3Name.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery", jsonObject.toString());
                                    String CB_Date = jsonObject.getString("CBDate");
                                    JSONObject jbb = new JSONObject(CB_Date);
                                    String CBDate = jbb.getString("date");
                                    String Capital = jsonObject.getString("Capital");
                                    String ProfitLoss = jsonObject.getString("ProfitLoss");
                                    String Liabilities = jsonObject.getString("Liabilities");
                                    String C_P_L = jsonObject.getString("C + P + L");
                                    String Assets = jsonObject.getString("Assets");
                                    String ClientID = jsonObject.getString("ClientID");

                                    String pattern = "yyyy-MM-dd";
                                    DateFormat df = new SimpleDateFormat(pattern);
                                    Date date = df.parse(CBDate);
                                    DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
                                    String cbDate = dff.format(date);
                                    String[] separated = cbDate.split("-");

                                    if (m == 0) {
                                        mList.add(BalSheet.createSection(separated[1] + "/" + separated[0]));
                                        mList.add(BalSheet.createRow(cbDate, Capital, ProfitLoss, Liabilities, C_P_L, Assets, ClientID));
                                        m = Integer.valueOf(separated[1]);

                                        capital = Integer.valueOf(Capital) + capital;
                                        profitLoss = Integer.valueOf(ProfitLoss) + profitLoss;
                                        liabilities = Integer.valueOf(Liabilities) + liabilities;
                                        cpl = Integer.valueOf(C_P_L) + cpl;
                                        assets = Integer.valueOf(Assets) + assets;
                                        gCapital = Integer.valueOf(Capital) + gCapital;
                                        gProfitLoss = Integer.valueOf(ProfitLoss) + gProfitLoss;
                                        gLiabilities = Integer.valueOf(Liabilities) + gLiabilities;
                                        gCpl = Integer.valueOf(C_P_L) + gCpl;
                                        gAssets = Integer.valueOf(Assets) + gAssets;
                                    } else if (m == Integer.valueOf(separated[1])) {
                                        capital = Integer.valueOf(Capital) + capital;
                                        profitLoss = Integer.valueOf(ProfitLoss) + profitLoss;
                                        liabilities = Integer.valueOf(Liabilities) + liabilities;
                                        cpl = Integer.valueOf(C_P_L) + cpl;
                                        assets = Integer.valueOf(Assets) + assets;
                                        gCapital = Integer.valueOf(Capital) + gCapital;
                                        gProfitLoss = Integer.valueOf(ProfitLoss) + gProfitLoss;
                                        gLiabilities = Integer.valueOf(Liabilities) + gLiabilities;
                                        gCpl = Integer.valueOf(C_P_L) + gCpl;
                                        gAssets = Integer.valueOf(Assets) + gAssets;
                                        mList.add(BalSheet.createRow(cbDate, Capital, ProfitLoss, Liabilities, C_P_L, Assets, ClientID));
                                    } else {
                                        mList.add(BalSheet.createTotal(String.valueOf(capital), String.valueOf(profitLoss), String.valueOf(liabilities), String.valueOf(cpl), String.valueOf(assets)));
                                        capital = 0;
                                        profitLoss = 0;
                                        liabilities = 0;
                                        cpl = 0;
                                        assets = 0;
                                        capital = Integer.valueOf(Capital) + capital;
                                        profitLoss = Integer.valueOf(ProfitLoss) + profitLoss;
                                        liabilities = Integer.valueOf(Liabilities) + liabilities;
                                        cpl = Integer.valueOf(C_P_L) + cpl;
                                        assets = Integer.valueOf(Assets) + assets;
                                        gCapital = Integer.valueOf(Capital) + gCapital;
                                        gProfitLoss = Integer.valueOf(ProfitLoss) + gProfitLoss;
                                        gLiabilities = Integer.valueOf(Liabilities) + gLiabilities;
                                        gCpl = Integer.valueOf(C_P_L) + gCpl;
                                        gAssets = Integer.valueOf(Assets) + gAssets;
                                        mList.add(BalSheet.createSection(separated[1] + "/" + separated[0]));
                                        mList.add(BalSheet.createRow(cbDate, Capital, ProfitLoss, Liabilities, C_P_L, Assets, ClientID));
                                        m = Integer.valueOf(separated[1]);
                                    }
                                }
                                mList.add(BalSheet.createTotal(String.valueOf(capital), String.valueOf(profitLoss), String.valueOf(liabilities), String.valueOf(cpl), String.valueOf(assets)));
                                mList.add(BalSheet.createSection("Grand Total"));
                                mList.add(BalSheet.createTotal(String.valueOf(gCapital), String.valueOf(gProfitLoss), String.valueOf(gLiabilities), String.valueOf(gCpl), String.valueOf(gAssets)));
                                adapter = new BalSheetDateAdapter(getContext(), mList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(adapter);

                                mProgress.dismiss();

                            } else {
                                String message = jsonObj.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                Log.e("Error", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                List<User> list = User.listAll(User.class);
                for (User u : list) {
                    params.put("ClientID", u.getClientID());
                    params.put("Date", "1");
                }
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void filter(String text) {
        filterdList = new ArrayList<>();

        //looping through existing elements
        if (!text.isEmpty()) {
            for (BalSheet s : mList) {
                if (s.isRow() == 1)
                    switch (filter) {
                        case 1:
                            if (s.getCBDate().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 2:
                            if (s.getCapital().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 3:
                            if (s.getProfitLoss().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 4:
                            if (s.getLiabilities().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 5:
                            if (s.getC_P_L().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 6:
                            if (s.getAssets().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                    }
            }
        } else {
            filterdList = mList;
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdList);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dbs_date:
                orderBy = "CBDate";
                orderBy(orderBy);
                break;
            case R.id.dbs_capital:
                orderBy = "Capital";
                orderBy(orderBy);
                break;
            case R.id.dbs_profitloss:
                orderBy = "ProfitLoss";
                orderBy(orderBy);
                break;
            case R.id.dbs_liabilities:
                orderBy = "Liabilities";
                orderBy(orderBy);
                break;
            case R.id.dbs_cpl:
                orderBy = "C_P_L";
//                getBalsheet();
                break;
            case R.id.dbs_assets:
                orderBy = "Assets";
                orderBy(orderBy);
                break;
        }
    }
    public void orderBy(String order_by){
        if (status == 0) {
            status = 1;
            orderby = " ORDER BY " + order_by + " DESC";
        } else {
            status = 0;
            orderby = " ORDER BY " + order_by + " ASC";
        }
        getBalsheet();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cb_menu,menu);
        MenuItem referesh = menu.findItem(R.id.action_refresh);
        referesh.setVisible(false);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }else if (item.getItemId() == R.id.action_print){
            try {
                createPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfname = "BalanceSheet.pdf";
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

        PdfPTable title = new PdfPTable(new float[]{3, 4, 3});
        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        title.getDefaultCell().setFixedHeight(30);
        title.setTotalWidth(PageSize.A4.getWidth());
        title.setWidthPercentage(100);
        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setSpacingBefore(5);
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        PdfPCell cell = new PdfPCell(new Phrase("Balance Sheet By Date",chapterFont));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(cell);
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));

        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        PdfPCell pCell = new PdfPCell(new Phrase(spinner.getSelectedItem()+": "+searchView.getQuery()));
        pCell.setBorder(PdfPCell.NO_BORDER);
        pCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(pCell);

        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(40);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.setSpacingBefore(20);
        table.addCell("");
        table.addCell("Capital");
        table.addCell("ProfitLoss");
        table.addCell("Liabilities");
        table.addCell("C + P + L");
        table.addCell("Assets");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.PINK);
        }

        capital = 0; profitLoss = 0; liabilities = 0; cpl = 0; assets = 0; gCapital = 0;
        gProfitLoss = 0; gLiabilities = 0; gCpl = 0; gAssets = 0; d = "0";

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
        PdfPCell total = new PdfPCell(new Phrase("Total",totalFont));
        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
        total.setFixedHeight(35);
        String[] separated = null;
        if (filter > 0){
            for (BalSheet c : filterdList){
                if (!c.getCBDate().equals(""))
                    separated = c.getCBDate().split("-");
                if (d.equals("0")){
                    d = separated[1];

                    PdfPCell section = new PdfPCell(new Phrase(separated[1]+"-"+separated[0],totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));

                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCapital(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfitLoss(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getLiabilities(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getC_P_L(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getAssets(), PdfPCell.ALIGN_RIGHT));

                    capital = Integer.valueOf(c.getCapital()) + capital;
                    profitLoss = Integer.valueOf(c.getProfitLoss()) + profitLoss;
                    liabilities = Integer.valueOf(c.getLiabilities()) + liabilities;
                    cpl = Integer.valueOf(c.getC_P_L()) + cpl;
                    assets = Integer.valueOf(c.getAssets()) + assets;
                    gCapital = Integer.valueOf(c.getCapital()) + gCapital;
                    gProfitLoss = Integer.valueOf(c.getProfitLoss()) + gProfitLoss;
                    gLiabilities = Integer.valueOf(c.getLiabilities()) + gLiabilities;
                    gCpl = Integer.valueOf(c.getC_P_L()) + gCpl;
                    gAssets = Integer.valueOf(c.getAssets()) + gAssets;
                }else if (d.equals(separated[1])){
                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCapital(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfitLoss(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getLiabilities(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getC_P_L(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getAssets(), PdfPCell.ALIGN_RIGHT));

                    capital = Integer.valueOf(c.getCapital()) + capital;
                    profitLoss = Integer.valueOf(c.getProfitLoss()) + profitLoss;
                    liabilities = Integer.valueOf(c.getLiabilities()) + liabilities;
                    cpl = Integer.valueOf(c.getC_P_L()) + cpl;
                    assets = Integer.valueOf(c.getAssets()) + assets;
                    gCapital = Integer.valueOf(c.getCapital()) + gCapital;
                    gProfitLoss = Integer.valueOf(c.getProfitLoss()) + gProfitLoss;
                    gLiabilities = Integer.valueOf(c.getLiabilities()) + gLiabilities;
                    gCpl = Integer.valueOf(c.getC_P_L()) + gCpl;
                    gAssets = Integer.valueOf(c.getAssets()) + gAssets;
                }else if (!d.equals("0") && !d.equals(separated[1])){
                    d = separated[1];
                    table.addCell(total);
                    table.addCell(getCell(String.valueOf(capital), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(profitLoss), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(liabilities), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(cpl), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(assets), PdfPCell.ALIGN_RIGHT));
                    capital = 0; profitLoss = 0; liabilities = 0; cpl = 0; assets = 0;

                    PdfPCell section = new PdfPCell(new Phrase(separated[1]+"-"+separated[0],totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));

                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCapital(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfitLoss(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getLiabilities(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getC_P_L(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getAssets(), PdfPCell.ALIGN_RIGHT));

                    capital = Integer.valueOf(c.getCapital()) + capital;
                    profitLoss = Integer.valueOf(c.getProfitLoss()) + profitLoss;
                    liabilities = Integer.valueOf(c.getLiabilities()) + liabilities;
                    cpl = Integer.valueOf(c.getC_P_L()) + cpl;
                    assets = Integer.valueOf(c.getAssets()) + assets;
                    gCapital = Integer.valueOf(c.getCapital()) + gCapital;
                    gProfitLoss = Integer.valueOf(c.getProfitLoss()) + gProfitLoss;
                    gLiabilities = Integer.valueOf(c.getLiabilities()) + gLiabilities;
                    gCpl = Integer.valueOf(c.getC_P_L()) + gCpl;
                    gAssets = Integer.valueOf(c.getAssets()) + gAssets;
                }
            }
        }else {
            for (BalSheet c : balSheetList){
                separated = c.getCBDate().split("-");
                if (d.equals("0")){
                    d = separated[1];

                    PdfPCell section = new PdfPCell(new Phrase(separated[1]+"-"+separated[0],totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));

                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCapital(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfitLoss(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getLiabilities(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getC_P_L(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getAssets(), PdfPCell.ALIGN_RIGHT));

                    capital = Integer.valueOf(c.getCapital()) + capital;
                    profitLoss = Integer.valueOf(c.getProfitLoss()) + profitLoss;
                    liabilities = Integer.valueOf(c.getLiabilities()) + liabilities;
                    cpl = Integer.valueOf(c.getC_P_L()) + cpl;
                    assets = Integer.valueOf(c.getAssets()) + assets;
                    gCapital = Integer.valueOf(c.getCapital()) + gCapital;
                    gProfitLoss = Integer.valueOf(c.getProfitLoss()) + gProfitLoss;
                    gLiabilities = Integer.valueOf(c.getLiabilities()) + gLiabilities;
                    gCpl = Integer.valueOf(c.getC_P_L()) + gCpl;
                    gAssets = Integer.valueOf(c.getAssets()) + gAssets;
                }else if (d.equals(separated[1])){
                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCapital(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfitLoss(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getLiabilities(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getC_P_L(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getAssets(), PdfPCell.ALIGN_RIGHT));

                    capital = Integer.valueOf(c.getCapital()) + capital;
                    profitLoss = Integer.valueOf(c.getProfitLoss()) + profitLoss;
                    liabilities = Integer.valueOf(c.getLiabilities()) + liabilities;
                    cpl = Integer.valueOf(c.getC_P_L()) + cpl;
                    assets = Integer.valueOf(c.getAssets()) + assets;
                    gCapital = Integer.valueOf(c.getCapital()) + gCapital;
                    gProfitLoss = Integer.valueOf(c.getProfitLoss()) + gProfitLoss;
                    gLiabilities = Integer.valueOf(c.getLiabilities()) + gLiabilities;
                    gCpl = Integer.valueOf(c.getC_P_L()) + gCpl;
                    gAssets = Integer.valueOf(c.getAssets()) + gAssets;
                }else if (!d.equals("0") && !d.equals(separated[1])){
                    d = separated[1];
                    table.addCell(total);
                    table.addCell(getCell(String.valueOf(capital), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(profitLoss), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(liabilities), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(cpl), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(assets), PdfPCell.ALIGN_RIGHT));
                    capital = 0; profitLoss = 0; liabilities = 0; cpl = 0; assets = 0;

                    PdfPCell section = new PdfPCell(new Phrase(separated[1]+"-"+separated[0],totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));

                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCapital(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfitLoss(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getLiabilities(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getC_P_L(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getAssets(), PdfPCell.ALIGN_RIGHT));

                    capital = Integer.valueOf(c.getCapital()) + capital;
                    profitLoss = Integer.valueOf(c.getProfitLoss()) + profitLoss;
                    liabilities = Integer.valueOf(c.getLiabilities()) + liabilities;
                    cpl = Integer.valueOf(c.getC_P_L()) + cpl;
                    assets = Integer.valueOf(c.getAssets()) + assets;
                    gCapital = Integer.valueOf(c.getCapital()) + gCapital;
                    gProfitLoss = Integer.valueOf(c.getProfitLoss()) + gProfitLoss;
                    gLiabilities = Integer.valueOf(c.getLiabilities()) + gLiabilities;
                    gCpl = Integer.valueOf(c.getC_P_L()) + gCpl;
                    gAssets = Integer.valueOf(c.getAssets()) + gAssets;
                }
            }
        }

        table.addCell(total);
        table.addCell(getCell(String.valueOf(capital), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(profitLoss), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(liabilities), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(cpl), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(assets), PdfPCell.ALIGN_RIGHT));

        table.addCell(getCell("Grand Total", PdfPCell.ALIGN_LEFT));
        table.addCell(getCell(String.valueOf(gCapital), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gProfitLoss), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gLiabilities), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCpl), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gAssets), PdfPCell.ALIGN_RIGHT));



//        Footer footer = new Footer();

        document.open();

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph("Cash Book \n\n", f);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(chunk);
        document.add(name);
        document.add(contact);
        document.add(title);
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
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public void customPDFView(){
        PackageManager packageManager = getContext().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            getContext().startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
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
            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
            Log.e("PAGE NUMBER",String.valueOf(writer.getPageNumber()));
            table.addCell(footerCell(String.format("Page %d ", writer.getPageNumber() -1),PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("www.easysoft.com.pk",PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));

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
