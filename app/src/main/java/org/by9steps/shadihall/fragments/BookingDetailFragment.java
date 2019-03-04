package org.by9steps.shadihall.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.by9steps.shadihall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingDetailFragment extends Fragment {

    private static final String ARG_MESSAGE = "message";

    TextView view_call, view_sms, view_edit, view_collection;
//    Booking book = new Booking();
//    ArrayList<Booking> bookings = new ArrayList<>();
    String pattern = "yyyy-MM-dd";
    private static final int REQUEST_PHONE_CALL = 1, REQUEST_SMS_SEND = 2;
    TextView ed_client_id1, ed_net_code1, ed_sys_code1, ed_updated_date1, ed_clientName1, ed_clientAddress1, ed_clientMobileNo1, ed_client_user_id, ed_charges;


    // TODO: Rename and change types of parameters
    private String message;

    public static BookingDetailFragment newInstance(String message) {
        BookingDetailFragment fragment = new BookingDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            message = getArguments().getString(ARG_MESSAGE);
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

        view_call = view.findViewById(R.id.view_call);
        view_sms = view.findViewById(R.id.view_sms);
        view_edit = view.findViewById(R.id.view_edit);
        view_collection = view.findViewById(R.id.view_collection);
        ed_charges = view.findViewById(R.id.ed_charges);
        ed_client_user_id = view.findViewById(R.id.ed_client_user_id);
        ed_clientMobileNo1 = view.findViewById(R.id.ed_clientMobileNo1);
        ed_clientAddress1 = view.findViewById(R.id.ed_clientAddress1);
        ed_clientName1 = view.findViewById(R.id.ed_clientName1);
        ed_updated_date1 = view.findViewById(R.id.ed_updated_date1);
        ed_sys_code1 = view.findViewById(R.id.ed_sys_code1);
        ed_net_code1 = view.findViewById(R.id.ed_net_code1);
        ed_client_id1 = view.findViewById(R.id.ed_client_id1);


        return view;
    }

}
