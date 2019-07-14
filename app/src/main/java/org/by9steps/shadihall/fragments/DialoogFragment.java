package org.by9steps.shadihall.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.DetailCalendarActivity;
import org.by9steps.shadihall.adapters.HorizontalImageViewAdapter;
import org.by9steps.shadihall.adapters.NearestListAdapter;
import org.by9steps.shadihall.model.Client;

import java.util.List;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialoogFragment extends DialogFragment implements View.OnClickListener {

    private static final String ARG_CLIENT_ID = "clientid";
    private static final String ARG_NAME = "name";
    private static final String ARG_COUNTRY = "country";
    private static final String ARG_CITY = "city";
    private static final String ARG_SUB_CITY = "subcity";
    private static final String ARG_WEBSIITE = "website";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PERSONS = "persons";
    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lng";
    private static final String ARG_NUMBER = "number";

    RecyclerView recyclerView;
    TextView tv_name, tv_country, tv_city, tv_subCity, tv_website, tv_email, tv_persons;
    ImageView logo;
    Button call, locate, bookings;
    HorizontalImageViewAdapter adapter;

    DialogueClickListener listener;

    List<Client> mList = new ArrayList<>();

    String clientID, name, country, city, subcity, website, email, persons, lat, lng, number;

    public static DialoogFragment newInstance(String clientID, String name, String country, String city, String subCity
        ,String website, String email, String persons, String Lat, String Lng, String number) {

        DialoogFragment frag = new DialoogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CLIENT_ID, clientID);
        args.putString(ARG_NAME, name);
        args.putString(ARG_COUNTRY, country);
        args.putString(ARG_CITY, city);
        args.putString(ARG_SUB_CITY, subCity);
        args.putString(ARG_WEBSIITE, website);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_PERSONS, persons);
        args.putString(ARG_LAT, Lat);
        args.putString(ARG_LNG, Lng);
        args.putString(ARG_NUMBER, number);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientID = getArguments().getString(ARG_CLIENT_ID);
            name = getArguments().getString(ARG_NAME);
            country = getArguments().getString(ARG_COUNTRY);
            city = getArguments().getString(ARG_CITY);
            subcity = getArguments().getString(ARG_SUB_CITY);
            website = getArguments().getString(ARG_WEBSIITE);
            email = getArguments().getString(ARG_EMAIL);
            persons = getArguments().getString(ARG_PERSONS);
            lat = getArguments().getString(ARG_LAT);
            lng = getArguments().getString(ARG_LNG);
            number = getArguments().getString(ARG_NUMBER);

            Log.e("CLIENTID",clientID);
        }
    }

    public DialoogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialoog, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        tv_name = view.findViewById(R.id.name);
        tv_country = view.findViewById(R.id.country);
        tv_city = view.findViewById(R.id.city);
        tv_subCity = view.findViewById(R.id.sub_city);
        tv_website = view.findViewById(R.id.website);
        tv_email = view.findViewById(R.id.email);
        tv_persons = view.findViewById(R.id.persons);
        call = view.findViewById(R.id.btn_call);
        locate = view.findViewById(R.id.btn_locate);
        bookings = view.findViewById(R.id.btn_calendar);

        call.setOnClickListener(this);
        locate.setOnClickListener(this);
        bookings.setOnClickListener(this);

        for (int i = 0; i < 5; i++) {
            mList.add(new Client(clientID));
        }
        adapter = new HorizontalImageViewAdapter(getContext(), mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        tv_name.setText(name);
        tv_country.setText(country);
        tv_city.setText(city);
        tv_subCity.setText(subcity);
        tv_website.setText("Website : "+website);
        tv_email.setText("Email : "+email);
        tv_persons.setText("Capacity of Person : "+persons);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call:
                String uri = "tel:" + number;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.btn_locate:
                if (listener != null)
                    listener.updateMapLocation(lat,lng);
                dismiss();
                break;
            case R.id.btn_calendar:
                Intent i = new Intent(getContext(), DetailCalendarActivity.class);
                i.putExtra("message", name);
                getActivity().startActivity(i);
//                if (listener != null)
//                    listener.repllaceFragment(clientID);
                break;
        }
    }

    //load fragment on recyclerView OnClickListner
    public  void setOnDialogueClickListener(DialogueClickListener listener) {
        this.listener = listener;
    }

    public interface DialogueClickListener{
        void updateMapLocation(String lat, String lng);

        void repllaceFragment(String id);
    }
}
