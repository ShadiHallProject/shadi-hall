package org.by9steps.shadihall.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.CashBookSettingActivity;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.adapters.CashBookAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CBSetting;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.CashEntry;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemSelectedListener;

import static android.content.Context.PRINT_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashBookFragment extends Fragment implements OnItemSelectedListener {

    ProgressDialog mProgress;
    RecyclerView recyclerView;
    EditText search;
    Spinner spinner;
//    ScrollView scrollView;
    HorizontalScrollView scrollView;
    LinearLayout header, pdfView;
    TextView tv_date,tv_id,tv_debit,tv_credit,tv_remarks,tv_amount;

    List<CashEntry> mList;
    DatabaseHelper databaseHelper;
    List<CashBook> cashBooksList;
    CashBookAdapter adapter;

    int m = 0, amount, gAmount , filter;
    Boolean listSorting = false;
    private Bitmap bitmap;
    public static String fDate1, fDate2;
    public static Button date1;
    public static Button date2;

    public CashBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_book, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        header = view.findViewById(R.id.header);
        scrollView = view.findViewById(R.id.scrollView);
        pdfView = view.findViewById(R.id.pdfView);
        search = view.findViewById(R.id.search);
        spinner = view.findViewById(R.id.spinner);
        tv_date = view.findViewById(R.id.tv_date);
        tv_id = view.findViewById(R.id.tv_id);
        tv_debit = view.findViewById(R.id.tv_debit);
        tv_credit = view.findViewById(R.id.tv_credit);
        tv_remarks = view.findViewById(R.id.tv_remarks);
        tv_amount = view.findViewById(R.id.tv_amount);

        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select");
        categories.add("Date");
        categories.add("CB ID");
        categories.add("Deb Account");
        categories.add("Cre Account");
        categories.add("Remarks");
        categories.add("Amount");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        databaseHelper = new DatabaseHelper(getContext());


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getContext(), CashCollectionActivity.class);
                intent.putExtra("BookingID","0");
                intent.putExtra("Spinner","View");
                startActivity(intent);
            }
        });

        mList = new ArrayList<>();

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSorting = !listSorting;
                getCashBook();
//                doPrint();
            }
        });

        List<CBSetting> list = CBSetting.listAll(CBSetting.class);
        if (list.size() == 0){
            CBSetting cbSetting = new CBSetting(true,true,true,true,true,true);
            cbSetting.save();
        }


//        getCashBook();
//        getRecoveries();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cb_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }else if (item.getItemId() == R.id.action_print){
            exportPdf();
            return true;
        }else if (item.getItemId() == R.id.action_settings){
            startActivity(new Intent(getContext(), CashBookSettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCashBook();

        List<CBSetting> list = CBSetting.listAll(CBSetting.class);
        for (CBSetting c : list){
            if (!c.getDste()){
                tv_date.setVisibility(View.GONE);
            }else {
                tv_date.setVisibility(View.VISIBLE);
            }

            if (!c.getCbId()){
                tv_id.setVisibility(View.GONE);
            }else {
                tv_id.setVisibility(View.VISIBLE);
            }

            if (!c.getDebit()){
                tv_debit.setVisibility(View.GONE);
            }else {
                tv_debit.setVisibility(View.VISIBLE);
            }

            if (!c.getCredit()){
                tv_credit.setVisibility(View.GONE);
            }else {
                tv_credit.setVisibility(View.VISIBLE);
            }

            if (!c.getRemarks()){
                tv_remarks.setVisibility(View.GONE);
            }else {
                tv_remarks.setVisibility(View.VISIBLE);
            }

            if (!c.getAmount()){
                tv_amount.setVisibility(View.GONE);
            }else {
                tv_amount.setVisibility(View.VISIBLE);
            }
        }
        Log.e("Resume","Resume");
    }

    private void getCashBook(){
        mList.clear();
        m = 0;
        amount = 0;
        gAmount = 0;
        String query = "";

        List<User> list = User.listAll(User.class);
        for (User u : list){
//            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
//                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
//                    "FROM            CashBook INNER JOIN\n" +
//                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID\n" +
//                    "WHERE        (CashBook.ClientID = "+u.getClientID()+")";
            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
                    "FROM            CashBook LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID\n" +
                    "WHERE        (CashBook.ClientID = "+u.getClientID()+")";
            cashBooksList = databaseHelper.getCashBookEntry(query);
        }

//        String s = "SELECT * FROM CashBook WHERE ClientID = 69";
//        databaseHelper.getCashBook(s);

        if (listSorting){
            for (int i = cashBooksList.size()-1; i >= 0; i--){
                String[] separated = cashBooksList.get(i).getCBDate().split("-");
                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);

                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[1])){
                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);
                }
            }
        }else {
            for (CashBook c : cashBooksList){

                String[] separated = c.getCBDate().split("-");

                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);

                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[1])){
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);
                }
            }
        }

        mList.add(CashEntry.createTotal(String.valueOf(amount)));
        mList.add(CashEntry.createSection("Grand Total"));
        mList.add(CashEntry.createTotal(String.valueOf(gAmount)));
        adapter = new CashBookAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void getRecoveries(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetCashbookEntry.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES",response);
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                mList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String CashBookID = jsonObject.getString("CashBookID");
                                    String CB_Date = jsonObject.getString("CBDate");
                                    JSONObject jbb = new JSONObject(CB_Date);
                                    String CBDate = jbb.getString("date");
                                    String DebitAccount = jsonObject.getString("DebitAccount");
                                    String CreditAccount = jsonObject.getString("CreditAccount");
//                                    String CBRemarks = jsonObject.getString("CBRemarks");
                                    String Amount = jsonObject.getString("Amount");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String BookingID = jsonObject.getString("BookingID");
                                    String DebitAccountName = jsonObject.getString("DebitAccountName");
                                    String CreditAccountName = jsonObject.getString("CreditAccountName");
                                    String UserName = jsonObject.getString("UserName");
//                                    String Updated_Date = jsonObject.getString("UpdatedDate");
//                                    JSONObject jbbb = new JSONObject(Updated_Date);
//                                    String UpdatedDate = jbbb.getString("date");

                                    DateFormat old = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    String pattern="dd-MM-yyyy";
                                    DateFormat df = new SimpleDateFormat(pattern);
                                    Date date = old.parse(CBDate);
                                    String CBDate1 = df.format(date);
                                    String[] separated = CBDate1.split("-");

                                    if (m == 0) {
                                        mList.add(CashEntry.createSection(separated[1]+"/"+separated[2]));
                                        mList.add(CashEntry.createRow(CashBookID,CBDate1,DebitAccount,CreditAccount,"ss",Amount,ClientID,ClientUserID,BookingID,DebitAccountName,CreditAccountName,UserName, "s"));
                                        m = Integer.valueOf(separated[1]);

                                        amount = Integer.valueOf(Amount) + amount;
                                        gAmount = Integer.valueOf(Amount) + gAmount;
                                    }else if (m == Integer.valueOf(separated[1])){
                                        amount = Integer.valueOf(Amount) + amount;
                                        gAmount = Integer.valueOf(Amount) + gAmount;
                                        mList.add(CashEntry.createRow(CashBookID,CBDate1,DebitAccount,CreditAccount,"ss",Amount,ClientID,ClientUserID,BookingID,DebitAccountName,CreditAccountName,UserName, "s"));
                                    }else {
                                        mList.add(CashEntry.createTotal(String.valueOf(amount)));
                                        amount = 0;
                                        amount = Integer.valueOf(Amount) + amount;
                                        gAmount = Integer.valueOf(Amount) + gAmount;

                                        mList.add(CashEntry.createSection(separated[1]+"/"+separated[2]));
                                        mList.add(CashEntry.createRow(CashBookID,CBDate1,DebitAccount,CreditAccount,"ss",Amount,ClientID,ClientUserID,BookingID,DebitAccountName,CreditAccountName,UserName, "s"));
                                        m = Integer.valueOf(separated[1]);
                                    }
                                }
                                mList.add(CashEntry.createTotal(String.valueOf(amount)));
                                mList.add(CashEntry.createSection("Grand Total"));
                                mList.add(CashEntry.createTotal(String.valueOf(gAmount)));
                                CashBookAdapter adapter = new CashBookAdapter(getContext(),mList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(adapter);

                            }else {
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
                Log.e("Error",error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                List<User> list = User.listAll(User.class);
                for (User u: list) {
                    params.put("ClientID", u.getClientID());
                }
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void exportPdf(){
        Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
        bitmap = loadBitmapFromView(scrollView, scrollView.getChildAt(0).getWidth(), scrollView.getChildAt(0).getHeight());
        createPdf();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        v.draw(c);

        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) scrollView.getChildAt(0).getHeight(), convertWidth = (int) scrollView.getChildAt(0).getWidth();

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);
        doPhotoPrint(bitmap);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
//        String targetPdf = "/sdcard/shadihall.pdf";
//        File filePath;
//        filePath = new File(targetPdf);
//        try {
//            document.writeTo(new FileOutputStream(filePath));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//
//        // close the document
//        document.close();
//        Toast.makeText(getContext(), "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

//        openGeneratedPDF();

    }
    private void openGeneratedPDF(){
        File file = new File("/sdcard/shadihall.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(getContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doPhotoPrint(Bitmap bitmap) {

        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        photoPrinter.printBitmap("cashbook", bitmap);
    }

    private void filter(String text) {Log.e("Search","SEARCH");
        //new array list that will hold the filtered data
        List<CashEntry> filterd = new ArrayList<>();

        //looping through existing elements
        if (!text.isEmpty()) {
            for (CashEntry s : mList) {
                Log.e("Search", String.valueOf(s.isRow()));
                //if the existing elements contains the search input
                if (s.isRow() == 1) {
                    switch (filter){
                        case 2:
                            if (s.getCashBookID().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                        case 3:
                            if (s.getDebitAccountName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                        case 4:
                            if (s.getCreditAccountName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                        case 5:
                            if (s.getCBRemarks().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                        case 6:
                            if (s.getAmount().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                    }
                }
//                else if (s.isRow() == 0){
//                    filterd.add(s);
//                }else if (s.isRow() == 2){
//                    filterd.add(s);
//                }
            }
        }else {
            filterd = mList;
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterd);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position!=1){
            getCashBook();
        }
        switch (position){
            case 1:
                // custom dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.date_filter_dialog);
                dialog.setTitle("Select Date");

                date1 = dialog.findViewById(R.id.date1);
                date2 = dialog.findViewById(R.id.date2);
                Button ok = dialog.findViewById(R.id.ok);
                Button cancel = dialog.findViewById(R.id.cancel);

                date1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppController.fDate1 = "Date1";
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                        fDate1 = date1.getText().toString();
                    }
                });

                date2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppController.fDate2 = "Date2";
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                        fDate2 = date2.getText().toString();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fDate1 = date1.getText().toString();
                        fDate2 = date2.getText().toString();
                        dateFilter();
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        spinner.setSelection(0);
                    }
                });

                dialog.show();

                break;
            case 2:
                filter = 2;
                break;
            case 3:
                filter = 3;
                break;
            case 4:
                filter = 4;
                break;
            case 5:
                filter = 5;
                break;
            case 6:
                filter = 6;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void dateFilter(){
        mList.clear();
        m = 0;
        amount = 0;
        gAmount = 0;
        String query = "";

        List<User> list = User.listAll(User.class);
        for (User u : list){
//            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
//                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
//                    "FROM            CashBook INNER JOIN\n" +
//                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID\n" +
//                    "WHERE        (CashBook.ClientID = "+u.getClientID()+")";
            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
                    "FROM            CashBook LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID\n" +
                    "WHERE        (CashBook.ClientID = "+u.getClientID()+" AND CashBook.CBDate >= Datetime('"+fDate1+"') AND CashBook.CBDate <= Datetime('"+fDate2+"'))";
            cashBooksList = databaseHelper.getCashBookEntry(query);
        }

//        String s = "SELECT * FROM CashBook WHERE ClientID = 69";
//        databaseHelper.getCashBook(s);

        if (listSorting){
            for (int i = cashBooksList.size()-1; i >= 0; i--){
                String[] separated = cashBooksList.get(i).getCBDate().split("-");
                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);

                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[1])){
                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);
                }
            }
        }else {
            for (CashBook c : cashBooksList){

                String[] separated = c.getCBDate().split("-");

                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);

                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[1])){
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);
                }
            }
        }

        mList.add(CashEntry.createTotal(String.valueOf(amount)));
        mList.add(CashEntry.createSection("Grand Total"));
        mList.add(CashEntry.createTotal(String.valueOf(gAmount)));
        adapter = new CashBookAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}

