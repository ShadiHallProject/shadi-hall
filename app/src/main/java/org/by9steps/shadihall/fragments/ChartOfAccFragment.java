package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orm.util.NamingHelper;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.RecoveryAdapter;
import org.by9steps.shadihall.adapters.ReportsAdapter;
import org.by9steps.shadihall.bean.Dir;
import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.Recovery;
import org.by9steps.shadihall.model.User;
import org.by9steps.shadihall.viewbinder.DirectoryNodeBinder;
import org.by9steps.shadihall.viewbinder.FileNodeBinder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartOfAccFragment extends Fragment {

    private RecyclerView rv;
    private TreeViewAdapter adapter;
    ProgressDialog mProgress;
    String currentDate;

    List<String> country = new ArrayList<>();
    List<String> city = new ArrayList<>();
    List<String> town = new ArrayList<>();


    public ChartOfAccFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart_of_acc, container, false);

        rv = view.findViewById(R.id.rv);
        mProgress = new ProgressDialog(getContext());
        getAccount3Name();


        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = curFormater.format(date);

//        country.add("Pakistan");
//
//        city.add( "Karachi");
//        city.add("Lahore");
//        city.add("Islamabad");
//        city.add("Faisalabad");
//
//        town.add("Saddar Town");
//        town.add("Malir Town");
//        town.add("New Karachi Town");

        return view;
    }

    private void initData() {
        List<TreeNode> nodes = new ArrayList<>();
        TreeNode<Dir> app = null;
        TreeNode<Dir> cit = null;
        TreeNode<Dir> ton;
        TreeNode<Dir> com;

//        List<Account1Type> list = Account1Type.listAll(Account1Type.class);
//        for (Account1Type a: list){
//            app = new TreeNode<>(new Dir(a.getAcTypeID(),a.getAcTypeName(),"CA"));
//            nodes.add(app);
//            List<Account3Name> list1 = Account3Name.find(Account3Name.class, NamingHelper.toSQLNameDefault("AcTypeID")+" = ?", a.getAcTypeID());
//            for (Account3Name aa: list1){
//                cit = new TreeNode<>(new Dir(aa.getAcGroupID(),aa.getAcGruopName(),"Add"));
//                app.addChild(cit);
//                List<Account3Name> list2 = Account3Name.find(Account3Name.class, NamingHelper.toSQLNameDefault("AcGroupID")+" = ?", aa.getAcGroupID());
//                for (Account3Name aaa: list2){
//                    ton = new TreeNode<>(new Dir(aaa.getAccountID(),aaa.getAcName(),"Edit"));
//                    cit.addChild(ton);
//                }
//            }
//        }

        List<Account3Name> list = Account3Name.findWithQuery(Account3Name.class, "SELECT * FROM Account3_Name GROUP BY Ac_Type_Name");
        for (Account3Name a: list){
            app = new TreeNode<>(new Dir(a.getAcTypeID(),a.getAcTypeName(),"CA"));
            nodes.add(app);
                List<Account3Name> list1 = Account3Name.findWithQuery(Account3Name.class, "SELECT * FROM Account3_Name GROUP BY Ac_Gruop_Name");
                Log.e("LOOP",a.getAcTypeID());
            for (Account3Name aa: list1){
                Log.e("TYPEID",a.getAcTypeID()+"  "+ aa.getAcGroupID());
                if (a.getAcTypeID().equals(aa.getAcTypeID())) {
                    cit = new TreeNode<>(new Dir(aa.getAcGroupID(), aa.getAcGruopName(), "Add"));
                    app.addChild(cit);
                }
                List<Account3Name> list2 = Account3Name.find(Account3Name.class, NamingHelper.toSQLNameDefault("AcGroupID")+" = ?", aa.getAcGroupID());
                for (Account3Name aaa: list2){
                    if (aa.getAcTypeID().equals(aaa.getClientID())) {
                        ton = new TreeNode<>(new Dir(aaa.getAccountID(), aaa.getAcName(), "Edit"));
                        cit.addChild(ton);
                    }
                }
            }
        }

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TreeViewAdapter(nodes, Arrays.asList(new FileNodeBinder(getContext()), new DirectoryNodeBinder(getContext())));
        // whether collapse child nodes when their parent node was close.
//        adapter.ifCollapseChildWhileCollapseParent(true);
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                if (!node.isLeaf()) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand(), holder);
//                    if (!node.isExpand())
//                        adapter.collapseBrotherNode(node);
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
            }
        });
        rv.setAdapter(adapter);
    }

    public void getAccount3Name(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccountDetails.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
                                Account3Name.deleteAll(Account3Name.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcTypeName = jsonObject.getString("AcTypeName");
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");
                                    String AccountID = jsonObject.getString("AccountID");
                                    String AcName = jsonObject.getString("AcName");
                                    String Debit = jsonObject.getString("Debit");
                                    String Credit = jsonObject.getString("Credit");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String Bal = jsonObject.getString("Bal");
                                    String DebitBL = jsonObject.getString("DebitBL");
                                    String CreditBL = jsonObject.getString("CreditBL");
                                    String ed = jsonObject.getString("MaxDate");
                                    JSONObject jbb = new JSONObject(ed);
                                    String MaxDate = jbb.getString("date");

                                    Account3Name account3Name = new Account3Name(AcTypeID,AcTypeName,AcGroupID,AcGruopName,AccountID,AcName,Debit,Credit,ClientID,MaxDate,Bal,DebitBL,CreditBL);
                                    account3Name.save();


                                }
                                initData();
//                                getAccountTypes();

                            }else {
                                String message = jsonObj.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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
                    params.put("CBDate", currentDate);
                }
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


    public void getAccountTypes(){
//        mProgress.setTitle("Loading");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccountType.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES",response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account1Type");
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                Account1Type.deleteAll(Account1Type.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcTypeName = jsonObject.getString("AcTypeName");

                                    Account1Type account1Type = new Account1Type(AcTypeID, AcTypeName);
                                    account1Type.save();

                                }
                                getAccountGroups();

                            }else {
                                String message = jsonObj.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountGroups(){
//        mProgress = new ProgressDialog(getContext());
//        mProgress.setTitle("Loading");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccountGroup.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES",response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account2Group");
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                Account2Group.deleteAll(Account2Group.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");

                                    Account2Group account2Group = new Account2Group(AcGroupID,AcTypeID,AcGruopName);
                                    account2Group.save();

                                }
                                initData();
                                mProgress.dismiss();

                            }else {
                                String message = jsonObj.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

}
