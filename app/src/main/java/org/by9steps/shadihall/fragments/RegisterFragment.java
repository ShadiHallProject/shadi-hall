package org.by9steps.shadihall.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.by9steps.shadihall.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    TextInputLayout nameLayout;
    TextInputEditText name;
    TextInputLayout addressLayout;
    TextInputEditText address;
    //    TextInputLayout nicLayout;
//    TextInputEditText nic;
    TextInputLayout otherInfoLayout;
    TextInputEditText otherInfo;
    TextInputLayout monthluFeeLayout;
    TextInputEditText monthlyFee;
    //    TextInputLayout invoitLayout;
//    TextInputEditText invoit;
    TextInputLayout mobileLayout;
    TextInputEditText mobile;
    TextInputLayout contactLayout;
    TextInputEditText contact;
    TextInputLayout websiteLayout;
    TextInputEditText website;
    TextInputLayout emailLayout;
    TextInputEditText email;
    TextInputLayout locLayout;
    TextView location;
    ImageView loc_icon;
    ImageView contactList;
    CircleImageView image;
    Button register;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        name = view.findViewById(R.id.name);
        nameLayout = view.findViewById(R.id.name_layout);
        address = view.findViewById(R.id.address);
        addressLayout = view.findViewById(R.id.address_layout);
//        nic = findViewById(R.id.nic);
//        nicLayout = findViewById(R.id.nic_layout);
        otherInfo = view.findViewById(R.id.otherinfo);
        otherInfoLayout = view.findViewById(R.id.otherinfo_layout);
        monthlyFee = view.findViewById(R.id.monthlyfee);
        monthluFeeLayout = view.findViewById(R.id.monthlyfee_layout);
//        invoit = findViewById(R.id.invoit);
//        invoitLayout = findViewById(R.id.invoit_layout);
        mobile = view.findViewById(R.id.mobile);
        mobileLayout = view.findViewById(R.id.mobile_layout);
        contact = view.findViewById(R.id.contact);
        contactLayout = view.findViewById(R.id.contact_layout);
        website = view.findViewById(R.id.website);
        websiteLayout = view.findViewById(R.id.website_layout);
        email = view.findViewById(R.id.email);
        emailLayout = view.findViewById(R.id.email_layout);
        contactList = view.findViewById(R.id.contactList);
        image = view.findViewById(R.id.image);
        locLayout  = view.findViewById(R.id.location_layout);
        location = view.findViewById(R.id.locstion);
        loc_icon = view.findViewById(R.id.location_icon);
        register = view.findViewById(R.id.register);

        register.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
