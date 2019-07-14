package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.ProjectsListAdapter;
import org.by9steps.shadihall.model.Projects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShadiHallHomeFragment extends Fragment implements View.OnClickListener{

    Button map, tree, nearest;

    public ShadiHallHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shadi_hall_home, container, false);

        map = view.findViewById(R.id.btn_map);
        tree = view.findViewById(R.id.btn_tree);
        nearest = view.findViewById(R.id.btn_nearest);

        map.setOnClickListener(this);
        tree.setOnClickListener(this);
        nearest.setOnClickListener(this);

        getChildFragmentManager().beginTransaction()
                .add(R.id.container, new MapFragment())
                .commit();

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_map:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new MapFragment())
                        .commit();
                break;
            case R.id.btn_tree:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new TreeFragment())
                        .commit();
                break;
            case R.id.btn_nearest:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container, new NearestFragment())
                        .commit();
                break;
        }

    }


}
