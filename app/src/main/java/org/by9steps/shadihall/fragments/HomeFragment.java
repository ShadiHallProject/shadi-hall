package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Projects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ProjectsListAdapter.OnItemClickListener{

    RecyclerView recyclerView;

    ProgressDialog mProgress;
    ProjectsListAdapter adapter;
    BottomSheetDialog dialog;
    DatabaseHelper databaseHelper;
    Prefrence prefrence;

    List<Projects> projectsList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
     //   ((MainActivity)getActivity()).nv.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);
        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());

        getChildFragmentManager().beginTransaction()
                .add(R.id.container, new ShadiHallHomeFragment())
                .commit();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        item.setVisible(false);

        dialog = new BottomSheetDialog(getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_list_item_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position == 0) {
//                    getChildFragmentManager().beginTransaction()
//                            .replace(R.id.container, new TreeFragment())
//                            .commit();
//                } else if (position == 1) {
//                    getChildFragmentManager().beginTransaction()
//                            .replace(R.id.container, new ListFragment())
//                            .commit();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setAdapter(adapter);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        Fragment fragment = ((MainActivity.ViewPagerAdapter)viewPager.getAdapter()).getFragment(0);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_projects) {
//            startActivity(new Intent(getContext(), MapsActivity.class));
            View view = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
            recyclerView = view.findViewById(R.id.recycler);
            dialog.setContentView(view);
            dialog.show();

            String query = "SELECT * FROM Project";
            projectsList = databaseHelper.getProjects(query);
            if (projectsList.size() == 0){
                getProject();
            }else {

                adapter = new ProjectsListAdapter(getContext(), projectsList);
                adapter.setOnItemClickListener(HomeFragment.this);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            }
//            getProjects();
        }

        return super.onOptionsItemSelected(item);
    }

//    public void getProjects(){
//
//        mProgress = new ProgressDialog(getContext());
//        mProgress.setTitle("Getting Projects");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();
//
//        String tag_json_obj = "json_obj_req";
//        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetProjects.php";
//
//        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("RES",response);
//                        JSONObject jsonObj = null;
//
//                        try {
//                            jsonObj= new JSONObject(response);
//                            JSONArray jsonArray = jsonObj.getJSONArray("Project");
//                            String success = jsonObj.getString("success");
//                            Log.e("Success",success);
//                            if (success.equals("1")){
//                                projectsList.clear();
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    Log.e("Project",jsonObject.toString());
//                                    String ProjectID = jsonObject.getString("ProjectID");
//                                    String ProjectName = jsonObject.getString("ProjectName");
//                                    String ProjectType = jsonObject.getString("ProjectType");
//                                    projectsList.add(new Projects(ProjectID, ProjectName, ProjectType));
//                                }
//                                adapter = new ProjectsListAdapter(getContext(), projectsList);
//                                adapter.setOnItemClickListener(HomeFragment.this);
//                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
////                                recyclerView.setHasFixedSize(true);
//                                recyclerView.setAdapter(adapter);
//                                mProgress.dismiss();
//
//                            }else {
//                                mProgress.dismiss();
//                                String message = jsonObj.getString("message");
////                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
//                Log.e("Error",error.toString());
////                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        int socketTimeout = 10000;//10 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//    }

    @Override
    public void onItemClick(String id, String name) {

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(name);
        }
        Log.e("PROJECT ID",id);
        prefrence.setProjectIDSession(id);
        prefrence.setUserRighhtsSession("0");
        prefrence.setClientUserIDSession("0");
        prefrence.setMYClientUserIDSession("0");
        prefrence.setClientIDSession("0");

        dialog.dismiss();
        if (id.equals("2")){
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, new ShadiHallHomeFragment())
                    .commit();
        }else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, new ListFragment())
                    .commit();
        }

    }

    public void getProject(){

        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Getting Projects");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u= ApiRefStrings.GetShadiHallList;

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
                                adapter.setOnItemClickListener(HomeFragment.this);
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
