package org.by9steps.shadihall.chartofaccountdialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MainActivity;
import org.by9steps.shadihall.adapters.ProjectsListAdapter;
import org.by9steps.shadihall.fragments.HomeFragment;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Projects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectMenuDialog extends DialogFragment {


    RecyclerView recyclerView;
    ProjectsListAdapter adapter;
    DatabaseHelper databaseHelper;
    List<Projects> projectsList = new ArrayList<>();
    public  ProjectsListAdapter.OnItemClickListener listenerproj;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=new DatabaseHelper(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout, null);

        // View view = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        recyclerView = view.findViewById(R.id.recycler);
        String query = "SELECT * FROM Project";
        projectsList = databaseHelper.getProjects(query);
        if (projectsList.size() == 0){
            getProject();
        }else {
            adapter = new ProjectsListAdapter(getContext(), projectsList);
            adapter.setOnItemClickListener(listenerproj);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
//        adapter = new ProjectsListAdapter(getContext(), projectsList);
//      //  adapter.setOnItemClickListener(HomeFragment.this);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Select Project")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }


    //    @Override
//    public void onItemClick(String id, String name) {
//
//        Prefrence prefrence=new Prefrence(getContext());
//        prefrence.setProjectIDSession(id+"");
//      //  dismiss();
//        try {
//            ((MainActivity)getActivity()).adatperlistenre=this;
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    public void getProject(){

        final ProgressDialog mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Getting Projects");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetProject.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetProject",response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                String query = "SELECT * FROM Project";
                                projectsList = databaseHelper.getProjects(query);
                                if (projectsList.size() > 0) {
                                    databaseHelper.deleteProject();
                                }
                                JSONArray jsonArray = jsonObj.getJSONArray("Project");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Project",jsonObject.toString());
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    String ProjectName = jsonObject.getString("ProjectName");
//                                    String ProjectDescription = jsonObject.getString("ProjectDescription");
                                    String ProjectType = jsonObject.getString("ProjectType");

                                    databaseHelper.createProjects(new Projects(ProjectID,ProjectName,ProjectType));
                                }

                                String que = "SELECT * FROM Project";
                                projectsList = databaseHelper.getProjects(que);
                                adapter = new ProjectsListAdapter(getContext(), projectsList);
                                // adapter.setOnItemClickListener(ProjectMenuDialog.this);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);

                                mProgress.dismiss();
                            }else {
                                mProgress.dismiss();
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
                mProgress.dismiss();
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 20000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }
}
