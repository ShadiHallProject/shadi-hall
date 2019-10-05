package org.by9steps.shadihall.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.MenuItemsFragment;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.ActiveClients;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CBUpdate;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.Client;
import org.by9steps.shadihall.model.Item1Type;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.ProjectMenu;
import org.by9steps.shadihall.model.ShadiHallList;
import org.by9steps.shadihall.model.TableSession;
import org.by9steps.shadihall.model.UpdateDate;
import org.by9steps.shadihall.model.item3name.Item3Name;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.by9steps.shadihall.model.salepur1data.SalePur1Data;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;
import org.by9steps.shadihall.model.salepur2data.SalePur2Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.services.common.ApiKey;

public class ShadiHallListAdapter extends RecyclerView.Adapter {

     String TAG="NULL";
    Context mCtx;
    List<ActiveClients> mList;
    List<Client> clients;

    private ProgressDialog mProgress;
    Prefrence prefrence;
    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String log_in = "loginKey";
    public static final String phone = "phoneKey";

    DatabaseHelper databaseHelper;

    String cId, ph;

    String clientID, clientUserID, userRights, projectID;

    private OnItemClickListener listener;

    public ShadiHallListAdapter(Context mCtx, List<ActiveClients> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
        prefrence = new Prefrence(mCtx);
        mProgress = new ProgressDialog(mCtx);
        databaseHelper = new DatabaseHelper(mCtx);
        //shared prefrences
        sharedPreferences = mCtx.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.shadihall_list_item, null);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final ActiveClients shadiHallList = mList.get(position);

        ((ListViewHolder) viewHolder).name.setText(shadiHallList.getCompanyName());
        ((ListViewHolder) viewHolder).address.setText(shadiHallList.getCompanyAddress());
        ((ListViewHolder) viewHolder).rights.setText(shadiHallList.getUserRights());

        Picasso.get()
                .load(AppController.imageUrl + shadiHallList.getClientID() + "/logo.png")
                .placeholder(R.drawable.default_avatar)
                .into(((ListViewHolder) viewHolder).image);

        ((ListViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String query = "SELECT * FROM Client WHERE ClientID = " + shadiHallList.getClientID();
                clients = databaseHelper.getClient(query);

                if (clients.size() > 0) {
//                    User.deleteAll(User.class);
//                    for (Client c : clients) {
//                        User user = new User(c.getClientID(), "0", "0", "0", "0", c.getClientUserID(), "ss");
//                        user.save();
//                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(log_in, "Yes");
                    editor.apply();

                    if (isConnected()) {
                        clientID = shadiHallList.getClientID();
                        clientUserID = shadiHallList.getClientUserID();
                        userRights = shadiHallList.getUserRights();
                        projectID = shadiHallList.getProjectID();

                        mProgress.setTitle("Getting Details");
                        mProgress.setMessage("Please wait...");
                        mProgress.setCanceledOnTouchOutside(false);
                        mProgress.show();
                        getCashBook();
                    } else {
                        if (listener != null)
                            listener.onItemClick(shadiHallList.getClientID(), shadiHallList.getClientUserID(), shadiHallList.getProjectID(), shadiHallList.getUserRights());
                    }
                } else {

                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mCtx);
                    View mView = layoutInflaterAndroid.inflate(R.layout.password_input_dialog_box, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(mCtx);
                    alertDialogBuilderUserInput.setView(mView);

                    final EditText password = (EditText) mView.findViewById(R.id.userInputDialog);
                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    Log.e("Password", password.getText().toString());
                                    if (!password.getText().toString().equals("")) {
                                        clientID = shadiHallList.getClientID();
                                        clientUserID = shadiHallList.getClientUserID();
                                        userRights = shadiHallList.getUserRights();
                                        projectID = shadiHallList.getProjectID();

                                        login(shadiHallList.getClientID(), shadiHallList.getClientUserID(), password.getText().toString());
                                    } else {
                                        Toast.makeText(mCtx, "Enter Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })

                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            dialogBox.cancel();
                                        }
                                    });

                    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();

                }
            }

        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name, address, rights;

        public ListViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            rights = itemView.findViewById(R.id.user_rights);

        }
    }

    //load fragment on recyclerView OnClickListner
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String clientID, String clientUserId, String projectID, String userRights);
    }

    //Send Request To Server
    private void login(final String mClientID, final String mClientUserID, final String mPassword) {
        mProgress.setMessage("Please wait...login");
        if (sharedPreferences.contains(phone)) {
            ph = sharedPreferences.getString(phone, "");
        }

        mProgress = new ProgressDialog(mCtx);
        mProgress.setTitle("Checking credentials");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

//        CBUpdate.deleteAll(CBUpdate.class);

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/Login.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES", response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            String message = jsonObj.getString("message");
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("UserInfo");
                                Log.e("UserInfo", jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String cashID = jsonObject.getString("CashID");
                                    String bookingIncomeID = jsonObject.getString("BookingIncomeID");
                                    String bookingExpenseID = jsonObject.getString("BookingExpenseID");
                                    String acNameID = "0";
                                    String acName = "ss";
                                    cId = jsonObject.getString("ClientID");
                                    String ClientParentID = jsonObject.getString("ClientParentID");
                                    String EntryType = jsonObject.getString("EntryType");
                                    String LoginMobileNo = jsonObject.getString("LoginMobileNo");
                                    String CompanyName = jsonObject.getString("CompanyName");
                                    String CompanyAddress = jsonObject.getString("CompanyAddress");
                                    String CompanyNumber = jsonObject.getString("CompanyNumber");
                                    String NameOfPerson = jsonObject.getString("NameOfPerson");
                                    String Email = jsonObject.getString("Email");
                                    String WebSite = jsonObject.getString("WebSite");
                                    String Password = jsonObject.getString("Password");
                                    String ActiveClient = jsonObject.getString("ActiveClient");
                                    String Country = jsonObject.getString("Country");
                                    String City = jsonObject.getString("City");
                                    String SubCity = jsonObject.getString("SubCity");
                                    String CapacityOfPersons = jsonObject.getString("CapacityOfPersons");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jb = new JSONObject(up);
                                    String UpdatedDate = jb.getString("date");
                                    String Lat = jsonObject.getString("Lat");
                                    String Lng = jsonObject.getString("Lng");
                                    String ProjectID = jsonObject.getString("ProjectID");

                                    databaseHelper.createClient(new Client(cId, ClientParentID, EntryType, LoginMobileNo, CompanyName,
                                            CompanyAddress, CompanyNumber, NameOfPerson, Email, WebSite, Password, ActiveClient,
                                            Country, City, SubCity, CapacityOfPersons, ClientUserID, SysCode, NetCode, UpdatedDate,
                                            Lat, Lng, ProjectID));

//                                    User user = new User(cId,cashID,bookingIncomeID,bookingExpenseID,acNameID,ClientUserID,acName);
//                                    user.save();
                                }

                                if (isConnected()) {
                                    getCashBook();
                                } else {
                                    Toast.makeText(mCtx, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                mProgress.dismiss();
                                Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                Log.e("Error", error.toString());
                Toast.makeText(mCtx, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", mClientID);
                params.put("ClientUserID", mClientUserID);
                params.put("Password", mPassword);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getCashBook() {
        mProgress.setMessage("Please wait...getCashBook");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/Test2.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {
                                databaseHelper.deleteCashBook();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("CashBook", jsonObject.toString());
                                    String CashBookID = jsonObject.getString("CashBookID");
                                    String cb = jsonObject.getString("CBDate");
                                    JSONObject jbb = new JSONObject(cb);
                                    String CBDate = jbb.getString("date");
                                    SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = ss.parse(CBDate);
                                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                    String CBDate1 = sf.format(date);
                                    String DebitAccount = jsonObject.getString("DebitAccount");
                                    String CreditAccount = jsonObject.getString("CreditAccount");
                                    String CBRemark = jsonObject.getString("CBRemarks");
                                    String Amount = jsonObject.getString("Amount");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
//                                    JSONObject jb = new JSONObject(ed);
//                                    String UpdatedDate = jb.getString("date");
                                    String TableID = jsonObject.getString("TableID");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String SerialNo = jsonObject.getString("SerialNo");
                                    String TableName = jsonObject.getString("TableName");

                                    databaseHelper.createCashBook(new CashBook(CashBookID, CBDate1, DebitAccount, CreditAccount, CBRemark, Amount, ClientID, ClientUserID, NetCode, SysCode, UpdatedDate, TableID, SerialNo, TableName));

                                }
//                                mProgress.dismiss();

                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            getAccount3Name();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccount3Name() {
        mProgress.setMessage("Please wait...getAccount3Name");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/test.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        Log.e("Account3Name ", response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {
                                databaseHelper.deleteAccount3Name();
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Account3Name", jsonObject.toString());
                                    String AcNameID = jsonObject.getString("AcNameID");
                                    String AcName = jsonObject.getString("AcName");
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcAddress = jsonObject.getString("AcAddress");
                                    String AcMobileNo = jsonObject.getString("AcMobileNo");
                                    String AcContactNo = jsonObject.getString("AcContactNo");
                                    String AcEmailAddress = jsonObject.getString("AcEmailAddress");
                                    String AcDebitBal = jsonObject.getString("AcDebitBal");
                                    String AcCreditBal = jsonObject.getString("AcCreditBal");
                                    String AcPassward = jsonObject.getString("AcPassward");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String ed = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(ed);
                                    String UpdatedDate = jbb.getString("date");
                                    String SerialNo = jsonObject.getString("SerialNo");
                                    String UserRights = jsonObject.getString("UserRights");
                                    String SecurityRights = jsonObject.getString("SecurityRights");
                                    String Salary = jsonObject.getString("Salary");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    databaseHelper.createAccount3Name(new Account3Name(AcNameID, AcName, AcGroupID, AcAddress, AcMobileNo, AcContactNo, AcEmailAddress, AcDebitBal, AcCreditBal, AcPassward, ClientID, ClientUserID, SysCode, NetCode, UpdatedDate, SerialNo, UserRights, SecurityRights, Salary));

//                                    if (i == jsonArray.length() - 1) {
//                                        TableSession.deleteAll(TableSession.class);
//                                        TableSession session = new TableSession("Account3Name", AcNameID, SessionDate, SessionDate);
//                                        session.save();
//                                    }
                                }
                                getAccountGroups();

                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountGroups() {
        mProgress.setMessage("Please wait...getAccountGroups");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccountGroup.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES", response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account2Group");
                            String success = jsonObj.getString("success");
                            Log.e("Success", success);
                            if (success.equals("1")) {
                                databaseHelper.deleteAccount2Group();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery", jsonObject.toString());
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");

                                    databaseHelper.createAccount2Group(new Account2Group(AcGroupID, AcTypeID, AcGruopName));
                                }

                                getAccountTypes();
//
//                                mProgress.dismiss();
//                                getCashBook();

                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountTypes() {
        mProgress.setMessage("Please wait...getAccountTypes");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccountType.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Account1Type", response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account1Type");
                            String success = jsonObj.getString("success");
                            Log.e("Success", success);
                            if (success.equals("1")) {
                                databaseHelper.deleteAccount1Type();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery", jsonObject.toString());
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcTypeName = jsonObject.getString("AcTypeName");

                                    databaseHelper.createAccount1Type(new Account1Type(AcTypeID, AcTypeName));
                                }
                                getItem1Type();
                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getItem1Type() {
        mProgress.setMessage("Please wait...getItem1Type");

        Log.e("GetItem1Type", "OK");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetItem1TypeLoc;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetItem1Type", response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Success", success);
                            if (success.equals("1")) {
                                databaseHelper.deleteAllItem1Type();
                                JSONArray jsonArray = jsonObj.getJSONArray("Item1Type");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("GetItem1Type", jsonObject.toString());
                                    Item1Type item1Type = new Item1Type();
                                    item1Type.setItem1TypeID(jsonObject.getString("Item1TypeID"));
                                    item1Type.setItemType(jsonObject.getString("ItemType"));
                                    long idd = refdb.TableItem1.AddItem1Type(databaseHelper, item1Type);
                                    Log.e("GetItem1Type", "On Add ITem " + idd);
                                }
                                getItem2Group();

                            } else {
                                String message = jsonObj.getString("message");
                                GenericConstants.ShowDebugModeDialog(mCtx, "Error",
                                        message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            GenericConstants.ShowDebugModeDialog(mCtx, "Error",
                                    e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getItem2Group() {
         final String TAG="getItem2Group";
        mProgress.setMessage("Please wait...getItem2Group");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetItem2GorupLoc;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Item2Group");
                            Log.e(TAG, response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {
                                databaseHelper.deleteAllItem2Group();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e(TAG, jsonObject.toString());
                                    Item2Group item2Group = new Item2Group();
                                    item2Group.setClientID(jsonObject.getString("ID"));
                                    item2Group.setItem2GroupID(jsonObject.getString("Item2GroupID"));
                                    item2Group.setItem1TypeID(jsonObject.getString("Item1TypeID"));
                                    item2Group.setItem2GroupName(jsonObject.getString("Item2GroupName"));
                                    item2Group.setClientUserID(jsonObject.getString("ClientUserID"));
                                    item2Group.setClientID(jsonObject.getString("ClientID"));
                                    item2Group.setSysCode(jsonObject.getString("SysCode"));
                                    item2Group.setNetCode(jsonObject.getString("NetCode"));
                                    item2Group.setUpdatedDate(jsonObject.getString("UpdatedDate"));
                     long idd=refdb.Table2Group.AddItem2Group(databaseHelper,item2Group);
                     Log.e(TAG,"Item2Group InsertID "+idd);
                                }
                                getProjectMenu();

                            } else {
                                getProjectMenu();
                                String message = jsonObj.getString("message");
                                Log.e(TAG, message);

//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            GenericConstants.ShowDebugModeDialog(mCtx,
                                    "Error", e.getMessage());
                            Log.e("Item2Group", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        getItem3Name();
    }

    public void getItem3Name() {
        TAG="getitem3Name";
        mProgress.setMessage("Please wait...getItem3Name");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetItem3NameLoc;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            Item3Name item3 = gson.fromJson(response, Item3Name.class);
                            if (item3.getSuccess() != 0) {
                                databaseHelper.deleteAllItem3Name();
                                for (Item3Name_ name : item3.getItem3Name()) {
                                    Log.e(TAG, " " + name.getClientID() + " --" + name.getUpdatedDate().getDate());
                                    long idd=refdb.Table3Name.AddItem3Name(databaseHelper,name);
                                    Log.e(TAG,"inser id"+idd);
                                }
                                Log.e(TAG, "onResponse: " + item3.getItem3Name().toString());


                            } else {
                                MNotificationClass.ShowToast(mCtx, "No Data Found in Item3Name");
                            }
                        }

                        Log.e(TAG, "onResponse: " + response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GenericConstants.ShowDebugModeDialog(mCtx, "Error", error.getMessage());
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        getSalePur1Data();
    }

    public void getSalePur1Data() {
        final String TAG="getSalePur1Data";
        mProgress.setMessage("Please wait...getSalePur1DAta");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetSalePur1Data;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            SalePur1Data salePur1Data = gson.fromJson(response, SalePur1Data.class);
                            if (salePur1Data.getSuccess() != 0) {
                                databaseHelper.deleteAllSalePur1();
                                for (Salepur1 salepur1 : salePur1Data.getSalepur1()) {
                                    Log.e("getSalePur1Data", " " + salepur1.getClientID() + " --" + salepur1.getUpdatedDate().getDate());
                                    long idd=refdb.SlePur1.AddItemSalePur1(databaseHelper,salepur1);
                                    Log.e("getSalePur1Data","inser id"+idd);
                                }
                                Log.e("getSalePur1Data", "onResponse: " + salePur1Data.getSalepur1());


                            } else {
                                MNotificationClass.ShowToast(mCtx, "No Data Found in SalePur1Table");
                            }
                        }

                        Log.e("getSalePur1Data", "onResponse: " + response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GenericConstants.ShowDebugModeDialog(mCtx, "Error", error.getMessage());
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                //params.put("EntryType","Sales");
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        getSalePur2Data();
    }


    public void getSalePur2Data() {
        final String TAG="getSalePur2Data";
        mProgress.setMessage("Please wait...getSalePur2Data");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetSalePur2Data;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            SalePur2Data salePur2Data = gson.fromJson(response, SalePur2Data.class);
                            if (salePur2Data.getSuccess() != 0) {
                                databaseHelper.deleteAllSalePur2();
                                for (SalePur2 salePur2 : salePur2Data.getSalePur2()) {
                                    Log.e("getSalePur2Data", " " + salePur2.getClientID() + " --" + salePur2.getUpdatedDate().getDate());
                                    long idd=refdb.SalePur2.AddItemSalePur2(databaseHelper,salePur2);
                                    Log.e("getSalePur2Data","inser id"+idd);
                                }
                                Log.e("getSalePur2Data", "onResponse: " + salePur2Data.getSalePur2());


                            } else {
                                MNotificationClass.ShowToast(mCtx, "No Data Found in SalePur1Table");
                            }
                        }

                        Log.e("getSalePur2Data", "onResponse: " + response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GenericConstants.ShowDebugModeDialog(mCtx, "Error", error.getMessage());
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    public void getProjectMenu() {
        mProgress.setMessage("Please wait...getProjectMenu");

        Log.e("GetProjectMenu", "OK");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetProjectMenu.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetProjectMenu", response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("ProjectMenu", success);
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("ProjectMenu");
                                databaseHelper.deleteProjectMenu();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("ProjectMenu", jsonObject.toString());
                                    String MenuID = jsonObject.getString("MenuID");
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    String MenuGroup = jsonObject.getString("MenuGroup");
                                    String MenuName = jsonObject.getString("MenuName");
                                    String PageOpen = jsonObject.getString("PageOpen");
                                    String ValuePass = jsonObject.getString("ValuePass");
                                    String ImageName = jsonObject.getString("ImageName");
                                    String GroupSortBy = jsonObject.getString("GroupSortBy");
                                    String SortBy = jsonObject.getString("SortBy");

                                    databaseHelper.createProjectMenu(new ProjectMenu(MenuID, ProjectID, MenuGroup, MenuName, PageOpen, ValuePass, ImageName, GroupSortBy, SortBy));
                                }

                                getBookings();

                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getBookings() {
        mProgress.setMessage("Please wait...getBookings");

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetBookings.php";


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons = "", ChargesTotal = "", Description = "", ClientID = "", ClientUserID = "", NetCode = "", SysCode = "", UpdatedDate = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Response", success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Bookings");
                                Log.e("SSSS", jsonArray.toString());
                                databaseHelper.deleteBooking();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    BookingID = jsonObject.getString("BookingID");
                                    ClientName = jsonObject.getString("ClientName");
                                    ClientMobile = jsonObject.getString("ClientMobile");
                                    ClientAddress = jsonObject.getString("ClientAddress");
                                    ClientNic = jsonObject.getString("ClientNic");
                                    EventName = jsonObject.getString("EventName");
                                    String bd = jsonObject.getString("BookingDate");
                                    JSONObject jbbb = new JSONObject(bd);
                                    BookingDate = jbbb.getString("date");
                                    String ed = jsonObject.getString("EventDate");
                                    JSONObject jb = new JSONObject(ed);
                                    EventDate = jb.getString("date");
                                    Log.e("TEST", EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    Log.e("TEST", "TEST");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String Shift = jsonObject.getString("Shift");
                                    String SerialNo = jsonObject.getString("SerialNo");

                                    Log.e("TEST", "SSSS");

                                    databaseHelper.createBooking(new Bookings(BookingID, ClientName, ClientMobile, ClientAddress, ClientNic, EventName, BookingDate, EventDate, ArrangePersons, ChargesTotal, Description, ClientID, ClientUserID, NetCode, SysCode, UpdatedDate, Shift, SerialNo));

//                                    if (i == jsonArray.length() - 1) {
//                                        TableSession session = new TableSession("Bookings", BookingID, SessionDate, SessionDate);
//                                        session.save();
//                                    }
                                }
                                getAccount4UserRights();

//                                FetchFromDb();
//                                pDialog.dismiss();
                            } else {
                                Log.e(GenericConstants.ByPASSForGetBooking, "ByPASSing fun");
                                getAccount4UserRights();
//                                pDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccount4UserRights() {
        mProgress.setMessage("Please wait...getAccount4UserRights");

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccount4UserRights.php";


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Success", success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Account4UserRights");
                                Log.e("Account4UserRights", jsonArray.toString());
//                                databaseHelper.deleteBooking();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String AcRightsID = jsonObject.getString("AcRightsID");
                                    String Account3ID = jsonObject.getString("Account3ID");
                                    String MenuName = jsonObject.getString("MenuName");
                                    String PageOpen = jsonObject.getString("PageOpen");
                                    String ValuePass = jsonObject.getString("ValuePass");
                                    String ImageName = jsonObject.getString("ImageName");
                                    String Inserting = jsonObject.getString("Inserting");
                                    String Edting = jsonObject.getString("Edting");
                                    String Deleting = jsonObject.getString("Deleting");
                                    String Reporting = jsonObject.getString("Reporting");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String UpdateDate = jsonObject.getString("UpdateDate");
                                    String SortBy = jsonObject.getString("SortBy");

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(log_in, "Yes");
                                    editor.apply();

                                    ///////////////////////////////////////////////
                                    Log.e(GenericConstants.MYEdittion, "Editing");
                                    Log.e(this.getClass().getName(), "Client ID::" + clientID);
                                    Log.e(this.getClass().getName(), "ClientUserID::::" + clientUserID);
                                    Log.e(this.getClass().getName(), "ProjectIDSerssion::" + projectID);
                                    Log.e(this.getClass().getName(), "UserRightSession::" + userRights);
                                    new Prefrence(mCtx).setMYClientUserIDSession(clientUserID);

                                    ////////////////////////////////
                                    if (listener != null)
                                        listener.onItemClick(clientID, clientUserID, projectID, userRights);

                                    mProgress.dismiss();

                                }
                            } else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(log_in, "Yes");
                                editor.apply();
///////////////////////////////////////////////
                                Log.e(GenericConstants.MYEdittion, "Editing");
                                Log.e(this.getClass().getName(), "Client ID::" + clientID);
                                Log.e(this.getClass().getName(), "ClientUserID::" + clientUserID);
                                Log.e(this.getClass().getName(), "ProjectIDSerssion::" + projectID);
                                Log.e(this.getClass().getName(), "UserRightSession::" + userRights);
                                new Prefrence(mCtx).setMYClientUserIDSession(clientUserID);
                                ////////////////////////////////
                                if (listener != null)
                                    listener.onItemClick(clientID, clientUserID, projectID, userRights);

                                mProgress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Account3ID", "1");
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


    //Check Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
