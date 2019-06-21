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
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Bookings;
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

    DatabaseHelper databaseHelper;
    List<Bookings> bookingList;

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

        databaseHelper = new DatabaseHelper(getContext());

        String query = "";
        List<User> list = User.listAll(User.class);
        for (User u: list) {
//            query = "SELECT * FROM Booking WHERE ClientID =" + u.getClientID();
            query = "SELECT * FROM Booking WHERE ClientID =" + u.getClientID()+" AND EventDate = '"+eventDate+" 00:00:00.000000'";
        }
        bookingList = databaseHelper.getBookings(query);
        Log.e("Booking",String.valueOf(bookingList.size()));

        //shared prefrences
        sharedPreferences = getContext().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if(sharedPreferences.contains(phone)){
            ph = sharedPreferences.getString(phone,"");
        }


        for (Bookings b : bookingList){
            booking_date.setText(b.getBookingDate());
            event_date.setText(b.getEventDate());
            client_name.setText(b.getClientName());
            address.setText(b.getClientAddress());
            client_mobile_no.setText(b.getClientMobile());
            cnic_number.setText(b.getClientNic());
            total_charges.setText(b.getChargesTotal());
            event_name.setText(b.getEventName());
            advance_fee.setText("123456");
            description.setText(b.getDescription());
            bookingID = b.getBookingID();
        }

        return view;
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
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.menu_container, BookingFormFragment.newInstance(event_date.getText().toString(), bookingID))
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.cash:
                Intent inte = new Intent(getContext(), CashCollectionActivity.class);
                inte.putExtra("BookingID",bookingID);
                inte.putExtra("Spinner","Hide");
                inte.putExtra("Type","Add");
                inte.putExtra("CashBookID","");
                startActivity(inte);
                break;
        }
    }
}
