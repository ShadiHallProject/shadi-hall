package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingDetailFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_BOOKING_ID = "event_date";

    private ProgressDialog mProgress;

    TextView booking_date, event_date, client_name, address, client_mobile_no, cnic_number, total_charges, event_name, total_persons,
             advance_fee, description;
    Button call, sms, edit, cash;
    String bookingID;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String phone = "phoneKey";
    String ph;


    // TODO: Rename and change types of parameters
    private String eventDate;

    public static BookingDetailFragment newInstance(String message) {
        BookingDetailFragment fragment = new BookingDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOOKING_ID, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventDate = getArguments().getString(ARG_BOOKING_ID);
        }
    }

    public BookingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_detail, container, false);

        booking_date = view.findViewById(R.id.booking_date);
        event_date = view.findViewById(R.id.event_date);
        client_name = view.findViewById(R.id.client_name);
        address = view.findViewById(R.id.address);
        client_mobile_no = view.findViewById(R.id.client_mobile_no);
        cnic_number = view.findViewById(R.id.cnic_number);
        total_charges = view.findViewById(R.id.total_charges);
        event_name = view.findViewById(R.id.event_name);
//        total_persons = view.findViewById(R.id.total_persons);
        advance_fee = view.findViewById(R.id.advance_fee);
        description = view.findViewById(R.id.description);
        call = view.findViewById(R.id.call);
        sms = view.findViewById(R.id.sms);
        edit = view.findViewById(R.id.edit);
        cash = view.findViewById(R.id.cash);

        call.setOnClickListener(this);
        sms.setOnClickListener(this);
        edit.setOnClickListener(this);
        cash.setOnClickListener(this);

        //shared prefrences
        sharedPreferences = getContext().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if(sharedPreferences.contains(phone)){
            ph = sharedPreferences.getString(phone,"");
        }


        getBooking();


        return view;
    }


    public void getBooking(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetBookings.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES",response);
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Bookings");
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("BOOKING",jsonObject.toString());
                                    String BookingID = jsonObject.getString("BookingID");
                                    String ClientName = jsonObject.getString("ClientName");
                                    String ClientMobile = jsonObject.getString("ClientMobile");
                                    String ClientAddress = jsonObject.getString("ClientAddress");
                                    String ClientNic = jsonObject.getString("ClientNic");
                                    String EventName = jsonObject.getString("EventName");
                                    String bd = jsonObject.getString("BookingDate");
                                    JSONObject jb = new JSONObject(bd);
                                    String BookingDate = jb.getString("date");
                                    String ed = jsonObject.getString("EventDate");
                                    JSONObject jbb = new JSONObject(ed);
                                    String EventDate = jbb.getString("date");
                                    String ChargesTotal = jsonObject.getString("ChargesTotal");
                                    String Description = jsonObject.getString("Description");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");

                                    String pattern="yyyy-MM-dd";
                                    DateFormat df = new SimpleDateFormat(pattern);
                                    Date date = df.parse(BookingDate);
                                    String bookingDate = df.format(date);
                                    date = df.parse(EventDate);
                                    String eventDate = df.format(date);

                                    booking_date.setText(bookingDate);
                                    event_date.setText(eventDate);
                                    client_name.setText(ClientName);
                                    address.setText(ClientAddress);
                                    client_mobile_no.setText(ClientMobile);
                                    cnic_number.setText(ClientNic);
                                    total_charges.setText(ChargesTotal);
                                    event_name.setText(EventName);
                                    advance_fee.setText("123456");
                                    description.setText(Description);
                                    bookingID = BookingID;

                                }

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
                    params.put("EventDate", eventDate);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call:
                String uri = "tel:" + ph;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.sms:
                Uri ur = Uri.parse("smsto:" +ph);
                Intent i = new Intent(Intent.ACTION_SENDTO, ur);
                i.putExtra("sms_body", "Welcome To Shaadi Hall Booking");
                startActivity(i);
                break;
            case R.id.edit:
                break;
            case R.id.cash:
                Intent inte = new Intent(getContext(), CashCollectionActivity.class);
                inte.putExtra("BookingID",bookingID);
                inte.putExtra("Spinner","Hide");
                startActivity(inte);
                break;
        }
    }
}
