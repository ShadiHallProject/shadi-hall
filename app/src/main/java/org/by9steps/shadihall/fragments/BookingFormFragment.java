package org.by9steps.shadihall.fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orm.SugarContext;
import com.squareup.timessquare.CalendarPickerView;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.RegisterActivity;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFormFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_BOOKING_DATE = "event_date";

    InputValidation inputValidation;

    TextInputLayout date_layout;
    TextView date;
    TextInputLayout event_date_layout;
    TextView event_date;
    TextInputLayout person_name_layout;
    TextInputEditText person_name;
    TextInputLayout address_layout;
    TextInputEditText address;
    TextInputLayout client_mobile_no_layout;
    TextInputEditText client_mobile_no;
    TextInputLayout cnic_number_layout;
    TextInputEditText cnic_number;
    TextInputLayout total_charges_layout;
    TextInputEditText total_charges;
    TextInputLayout event_name_layout;
    TextInputEditText event_name;
    TextInputLayout total_persons_layout;
    TextInputEditText total_persons;
    TextInputLayout description_layout;
    TextInputEditText description;
    TextInputLayout advance_fee_layout;
    TextInputEditText advance_fee;
    Button add_event;

    ProgressDialog pDialog;

    private String eventDate;

    public static BookingFormFragment newInstance(String message) {
        BookingFormFragment fragment = new BookingFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOOKING_DATE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventDate = getArguments().getString(ARG_BOOKING_DATE);
        }
    }

    public BookingFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_form, container, false);

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add New Event");
        }

        SugarContext.init(getContext());

        inputValidation = new InputValidation(getContext());

        date = view.findViewById(R.id.date);
        date_layout = view.findViewById(R.id.date_layout);
        event_date = view.findViewById(R.id.event_date);
        event_date_layout = view.findViewById(R.id.event_date_layout);
        person_name = view.findViewById(R.id.name_of_person);
        person_name_layout = view.findViewById(R.id.name_of_person_layout);
        address = view.findViewById(R.id.address);
        address_layout = view.findViewById(R.id.address_layout);
        client_mobile_no = view.findViewById(R.id.client_number);
        client_mobile_no_layout = view.findViewById(R.id.client_number_layout);
        cnic_number = view.findViewById(R.id.cnic_number);
        cnic_number_layout = view.findViewById(R.id.cnic_number_layout);
        total_charges = view.findViewById(R.id.total_charges);
        total_charges_layout = view.findViewById(R.id.total_charges_layout);
        event_name = view.findViewById(R.id.event_name);
        event_name_layout = view.findViewById(R.id.event_name_layout);
        total_persons = view.findViewById(R.id.total_persons);
        total_persons_layout = view.findViewById(R.id.total_persons_layout);
        description = view.findViewById(R.id.description);
        description_layout = view.findViewById(R.id.description_layout);
        advance_fee = view.findViewById(R.id.advance_fee);
        advance_fee_layout = view.findViewById(R.id.advance_fee_layout);
        add_event = view.findViewById(R.id.add);

        add_event.setOnClickListener(this);

        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(formatter.format(d));

        event_date.setText(eventDate);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
//                String pattern="yyyy-MM-dd";
//                DateFormat df = new SimpleDateFormat(pattern);
//                Date d1 = null, d2 = null;
//                try {
//                    d1 = df.parse(event_date.getText().toString());
//                    d2 = df.parse(date.getText().toString());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                final String eventDate = df.format(d1);
//                final String bookingDate = df.format(d2);
//                Log.e("DATE",d1+"   "+d2);
//                if (!inputValidation.isInputEditTextFilled(event_date, event_date_layout, getString(R.string.error_message_c_name))) {
//                    return;
//                }
                if (!inputValidation.isInputEditTextFilled(person_name, person_name_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(address, address_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(client_mobile_no, client_mobile_no_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(cnic_number, cnic_number_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(total_charges, total_charges_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(event_name, event_name_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(total_persons, total_persons_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(advance_fee, advance_fee_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(description, description_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                else {
                    String tag_json_obj = "json_obj_req";
                    String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddEvent.php";

                    pDialog = new ProgressDialog(getContext());
                    pDialog.setMessage("Searching...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pDialog.dismiss();
                                    Log.e("Response",response);
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            Log.e("Error",error.toString());
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() {
                            List<User> list = User.listAll(User.class);
                            Map<String, String> params = new HashMap<String, String>();
                            for (User u : list) {
                                params.put("ClientName", person_name.getText().toString());
                                params.put("ClientMobile", client_mobile_no.getText().toString());
                                params.put("ClientAddress", address.getText().toString());
                                params.put("ClientNic", cnic_number.getText().toString());
                                params.put("EventName", event_name.getText().toString());
                                params.put("BookingDate", date.getText().toString());
                                params.put("EventDate", event_date.getText().toString());
                                params.put("ArrangePersons", total_persons.getText().toString());
                                params.put("ChargesTotal", total_charges.getText().toString());
                                params.put("Description", description.getText().toString());
                                params.put("ClientID", u.getClientID());
                                params.put("ClientUserID", u.getClientUserID());
                                params.put("NetCode", "0");
                                params.put("SysCode", "0");
                                params.put("DebitAccount", u.getCashID());
                                params.put("CreditAccount", u.getBookingIncomeID());
                                params.put("Amount", advance_fee.getText().toString());
                            }
                            return params;
                        }
                    };
                    int socketTimeout = 30000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsonObjectRequest.setRetryPolicy(policy);
                    AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
                }
                break;
        }

    }

}
