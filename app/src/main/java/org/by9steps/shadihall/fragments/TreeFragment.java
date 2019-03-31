package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.orm.util.NamingHelper;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.bean.Dir;
import org.by9steps.shadihall.model.Tree;
import org.by9steps.shadihall.viewbinder.DirectoryNodeBinder;
import org.by9steps.shadihall.viewbinder.FileNodeBinder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TreeFragment extends Fragment {

    private RecyclerView rv;
    private TreeViewAdapter adapter;

    List<String> country = new ArrayList<>();
    List<String> city = new ArrayList<>();
    List<String> town = new ArrayList<>();
    List<String> community = new ArrayList<>();

    // Tag used to cancel the request
    String tag_json_obj = "json_obj_req";
    String url = "http://shadihall.easysoft.com.pk/PhpApi/ClientView.php";


    public TreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tree, container, false);

        rv = view.findViewById(R.id.rv);

        country.add("Pakistan");

        city.add( "Karachi");
        city.add("Lahore");
        city.add("Islamabad");
        city.add("Faisalabad");

        town.add("Saddar Town");
        town.add("Malir Town");
        town.add("New Karachi Town");

        community.add("Bussiness");
        community.add("Education");
        community.add("Sports");

        UpdateTree();

        return view;
    }


    private void initData() {
        List<TreeNode> nodes = new ArrayList<>();

        TreeNode<Dir> app;
        TreeNode<Dir> cit = null;
        TreeNode<Dir> ton = null;
        TreeNode<Dir> com = null;
        TreeNode<Dir> scom = null;

        List<Tree> da = Tree.find(Tree.class, NamingHelper.toSQLNameDefault("ClientParentID")+" = ?", "0");
        Log.e("DATA",da.toString());
        for (Tree d: da) {
            app = new TreeNode<>(new Dir(d.getClientID(), d.getCompanyName(),d.getEntryType()));
            nodes.add(app);
            List<Tree> da1 = Tree.find(Tree.class, NamingHelper.toSQLNameDefault("ClientParentID")+" = ?", d.getClientID());
            for (Tree dd: da1) {
                if (d.getClientID().equals(dd.getClientParentID())) {
                    cit = new TreeNode<>(new Dir(dd.getClientID(), dd.getCompanyName(),dd.getEntryType()));
                    app.addChild(cit);
                }
                List<Tree> da2 = Tree.find(Tree.class, NamingHelper.toSQLNameDefault("ClientParentID")+" = ?", dd.getClientID());
                for (Tree ddd: da2) {
                    if (dd.getClientID().equals(ddd.getClientParentID())) {
                        ton = new TreeNode<>(new Dir(ddd.getClientID(), ddd.getCompanyName(),ddd.getEntryType()));
                        cit.addChild(ton);
                    }
                    List<Tree> da3 = Tree.find(Tree.class, NamingHelper.toSQLNameDefault("ClientParentID")+" = ?", ddd.getClientID());
                    for (Tree dddd: da3) {
                        if (ddd.getClientID().equals(dddd.getClientParentID())) {
                            com = new TreeNode<>(new Dir(dddd.getClientID(), dddd.getCompanyName(),dddd.getEntryType()));
                            ton.addChild(com);
                        }
                        List<Tree> da4 = Tree.find(Tree.class, NamingHelper.toSQLNameDefault("ClientParentID")+" = ?", dddd.getClientID());
                        for (Tree ddddd: da4) {
                            if (dddd.getClientID().equals(ddddd.getClientParentID())) {
                                scom = new TreeNode<>(new Dir(ddddd.getClientID(), ddddd.getCompanyName(),ddddd.getEntryType()));
                                com.addChild(scom);
                            }
                        }
                    }
                }
            }
        }

//        for (String s : city){
//            cit = new TreeNode<>(new Dir("2",s));
//            app.addChild(cit);
//            for (String n : town){
//                ton = new TreeNode<>(new Dir("3",n));
//                cit.addChild(ton);
//                for (String t : community){
//                    com = new TreeNode<>(new Dir("4",t));
//                    ton.addChild(com);
//                }
//            }
//        }

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
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


    public void UpdateTree(){
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
            pDialog.show();
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            String text = "", ClientID = "", ClientParentID = "", EntryType = "", LoginMobile = "", CompanyName = "", CompanyAddress = "", CompanyNumber = "", NameofPerson = "", Email = "";
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray json2 = json.getJSONArray("Client");
                                Log.e("Response",json2.toString());
                                Tree.deleteAll(Tree.class);
                                for (int k = 0; k < json2.length(); k++) {
                                    text = json2.get(k).toString();
                                    JSONObject obj = new JSONObject(text);
                                    ClientID = obj.getString("ClientID");
                                    ClientParentID = obj.getString("ClientParentID");
                                    EntryType = obj.getString("EntryType");
                                    LoginMobile = obj.getString("LoginMobile");
                                    CompanyName = obj.getString("CompanyName");
                                    CompanyAddress = obj.getString("CompanyAddress");
                                    CompanyNumber = obj.getString("CompanyNumber");
                                    NameofPerson = obj.getString("NameofPerson");
                                    Email = obj.getString("Email");

                                    Log.e("DAta",CompanyNumber+" "+NameofPerson+" "+Email);
                                    Tree tree = new Tree(ClientID, ClientParentID, EntryType, LoginMobile, CompanyName,CompanyAddress, CompanyNumber, NameofPerson, Email);
                                    tree.save();

                                }

                                initData();
                                pDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.toString());
                    pDialog.dismiss();
//                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            });

            int socketTimeout = 10000;//10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

}
