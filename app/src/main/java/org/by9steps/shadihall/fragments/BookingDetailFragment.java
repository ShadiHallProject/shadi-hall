package org.by9steps.shadihall.fragments;


import android.Manifest;
import android.app.PendingIntent;
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
import org.by9steps.shadihall.dbhelper.BookingManager;
import org.by9steps.shadihall.model.Booking;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingDetailFragment extends Fragment {

    private static final String ARG_MESSAGE = "message";

    TextView view_call, view_sms, view_edit, view_collection;
    BookingManager mDatabase;
    Booking book = new Booking();
    ArrayList<Booking> bookings = new ArrayList<>();
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

        if (message != null) {
            mDatabase = new BookingManager(getContext());
            mDatabase.open();
            mDatabase.getBooking();
            for (Booking item : mDatabase.getBooking()) {
                if (item.getEventDate().substring(0, item.getEventDate().indexOf(" ")).equals(message)) {
                    book = item;
                    bookings.add(book);
                    Log.d("Found", item.getEventDate());
                }
            }
            mDatabase.close();
            //The key argument here must match that used in the other activity
        } else {
//            finish();
            Toast.makeText(getContext(),"Data Not Found",Toast.LENGTH_LONG).show();
        }

        ed_client_id1.setText(book.getClientID());
        ed_net_code1.setText(book.getNetCode());
        ed_sys_code1.setText(book.getSysCode());
        ed_updated_date1.setText(book.getUpdateDate());
        ed_clientName1.setText(book.getClientName());
        ed_clientAddress1.setText(book.getClientAddress());
        ed_clientMobileNo1.setText(book.getClientMobile());
        ed_client_user_id.setText(book.getClientUserID());
        ed_charges.setText(book.getCharges());
        view_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String posted_by = book.getClientMobile();

                String uri = "tel:" + posted_by.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            }
        });
        view_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), BookingDetails.class);
//                PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_SEND);
                    } else {
                   /*     SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage("03153867994", null, "Hello ShadiHall Project", pi, null);
finish();
                   */
                        Uri uri = Uri.parse("smsto:" + book.getClientMobile());
                        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                        i.putExtra("sms_body", "Welcome To Shaadi Hall Booking");
                        startActivity(i);
                    }
                } else {
                    Uri uri = Uri.parse("smsto:" + book.getClientMobile());
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.putExtra("sms_body", "Welcome To Shaadi Hall Booking");
                    startActivity(i);
/*
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage("03153867994", null, "Hello ShadiHall Project", pi, null);
finish();
*/
                }
//Get the SmsManager instance and call the sendTextMessage method to send message
            }
        });
        view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(getContext(), BookingForm.class);
//                startActivity(in);
            }
        });
        view_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(getContext(), CashBookActivity.class);
//                startActivity(in);
            }
        });
        return view;
    }

}
