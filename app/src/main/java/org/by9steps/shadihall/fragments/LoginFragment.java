package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MainActivity;
import org.by9steps.shadihall.activities.RegisterActivity;
import org.by9steps.shadihall.helper.InputValidation;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    TextInputEditText owner_numb, user_number, password;
    TextInputLayout owner_layout, user_layout, password_layout;
    Button login, create_account;

    private ProgressDialog mProgress;
    private InputValidation inputValidation;

    String oNumber, uNumber, mPassword;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String log_in = "loginKey";


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        owner_layout = view.findViewById(R.id.owner_layout);
        owner_numb = view.findViewById(R.id.owner_mob);
        user_layout = view.findViewById(R.id.user_layout);
        user_number = view.findViewById(R.id.user_mob);
        password_layout = view.findViewById(R.id.password_layout);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login_btn);
        create_account = view.findViewById(R.id.create_account);

        inputValidation = new InputValidation(getContext());

        login.setOnClickListener(this);
        create_account.setOnClickListener(this);

        //shared prefrences
        sharedPreferences = getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:

                oNumber = owner_numb.getText().toString();
                uNumber = user_number.getText().toString();
                mPassword = password.getText().toString();

                if (!inputValidation.isInputEditTextFilled(owner_numb, owner_layout, getString(R.string.enter_owner_number))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(user_number, user_layout, getString(R.string.enter_user_number))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(password, password_layout, getString(R.string.enter_password))) {
                    return;
                }
                else {
                    mProgress = new ProgressDialog(getContext());
                    mProgress.setTitle("Checking credentials");
                    mProgress.setMessage("Please wait...");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    String tag_json_obj = "json_obj_req";
                    String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/Login.php";

                    StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("RES",response);
                                    mProgress.dismiss();
                                    JSONObject jsonObject = null;

                                    try {
                                        jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");
                                        String message = jsonObject.getString("message");
                                        if (success.equals("1")){
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(log_in, "Yes");
                                            editor.apply();

                                            FragmentManager manager = getFragmentManager();
                                            assert manager != null;
                                            FragmentTransaction transaction = manager.beginTransaction();
                                            transaction.replace(R.id.mContainer, new MenuItemsFragment());
                                            transaction.commit();
                                        }else {
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
                            Log.e("NUMBER",oNumber);
                            params.put("logMobNum", oNumber);
                            params.put("AcMobNum", uNumber);
                            params.put("password", mPassword);
                            return params;
                        }
                    };
                    int socketTimeout = 10000;//10 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsonObjectRequest.setRetryPolicy(policy);
                    AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
                }

                break;
            case R.id.create_account:
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                intent.putExtra("TYPE", "Register");
                startActivity(intent);
                break;
        }
    }
}
