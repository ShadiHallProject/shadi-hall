package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Bookings;


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
    private static final String ARG_BOOKING_ID = "booking_id";
    private static final String ARG_EVENT_SHIFT = "event_shift";

    InputValidation inputValidation;
    DatabaseHelper databaseHelper;
    Prefrence prefrence;

    TextInputLayout date_layout;
    TextView date;
    TextInputLayout event_date_layout;
    static Button event_date;
    TextInputLayout event_shift_layout;
    TextView event_shift;
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
    List<Bookings> bookingList;
    String id;

    private String eventDate, bookingID, eventShift;

    public static BookingFormFragment newInstance(String message, String bookingID, String eventShift) {
        BookingFormFragment fragment = new BookingFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOOKING_DATE, message);
        args.putString(ARG_BOOKING_ID, bookingID);
        args.putString(ARG_EVENT_SHIFT, eventShift);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventDate = getArguments().getString(ARG_BOOKING_DATE);
            bookingID = getArguments().getString(ARG_BOOKING_ID);
            eventShift = getArguments().getString(ARG_EVENT_SHIFT);
            Log.e("argumentpassed",eventDate+"--"+bookingID+"--"+eventShift+"--");
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
        setHasOptionsMenu(true);
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add New Event");
        }

        SugarContext.init(getContext());

        inputValidation = new InputValidation(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());

        date = view.findViewById(R.id.date);
        date_layout = view.findViewById(R.id.date_layout);
        event_date = view.findViewById(R.id.event_date);
        event_date_layout = view.findViewById(R.id.event_date_layout);
        event_shift = view.findViewById(R.id.event_shift);
        event_shift_layout = view.findViewById(R.id.event_shift_layout);
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

        event_shift.setText(eventShift);
        event_date.setText(eventDate);

        if (!bookingID.equals("id")){
            String query = "";

            query = "SELECT * FROM Booking WHERE BookingID = '" + bookingID+"'";

            bookingList = databaseHelper.getBookings(query);
            for (Bookings b : bookingList){
                date.setText(b.getBookingDate());
                event_date.setText(b.getEventDate());
                person_name.setText(b.getClientName());
                address.setText(b.getClientAddress());
                client_mobile_no.setText(b.getClientMobile());
                cnic_number.setText(b.getClientNic());
                total_charges.setText(b.getChargesTotal());
                event_name.setText(b.getEventName());
                total_persons.setText(b.getArrangePersons());
                description.setText(b.getDescription());
                advance_fee.setText(b.getAmount());
                advance_fee.setVisibility(View.GONE);
                add_event.setText(getString(R.string.update));
                event_shift.setText(b.getShift());
                id = b.getId();
            }
        }

        add_event.setOnClickListener(this);
        event_date.setOnClickListener(this);

        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        date.setText(formatter.format(d));

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
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
                if (bookingID.equals("id") || bookingID.equals("o")) {
                    if (!inputValidation.isInputEditTextFilled(advance_fee, advance_fee_layout, getString(R.string.error_message_c_name))) {
                        return;
                    }
                }
                if (!inputValidation.isInputEditTextFilled(description, description_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                else {

                        if (bookingID.equals("id")) {

                            int seriolNo = refdb.BookingTable.GetMaximumBookingIDFromDb(databaseHelper,prefrence.getClientIDSession());


                            databaseHelper.createBooking(new Bookings(""+seriolNo, person_name.getText().toString(), client_mobile_no.getText().toString(), address.getText().toString(),
                                    cnic_number.getText().toString(),
                                    event_name.getText().toString(),
                                    date.getText().toString(),
                                    event_date.getText().toString(),
                                    total_persons.getText().toString(),
                                    total_charges.getText().toString(),
                                    description.getText().toString(),
                                    prefrence.getClientIDSession(),
                                    prefrence.getClientUserIDSession(),
                                    "0", "0",
                                    GenericConstants.NullFieldStandardText, advance_fee.getText().toString(), event_shift.getText().toString(),String.valueOf(seriolNo)));
                            clearCashe();
                        }else {
                            String query = "UPDATE Booking SET ClientName = '"+person_name.getText().toString()+"', ClientMobile = '"+client_mobile_no.getText().toString()
                                    +"', ClientAddress = '"+address.getText().toString()+"', ClientNic = '"+cnic_number.getText().toString()+"', EventName = '"+event_name.getText().toString()
                                    +"', ArrangePersons = '"+total_persons.getText().toString()+"', ChargesTotal = '"+total_charges.getText().toString()+"', Description = '"+description.getText().toString()
                                    +"', UpdatedDate = '"+GenericConstants.NullFieldStandardText+"', Advance = '"+advance_fee.getText().toString()+"' WHERE ID = "+id;
                            databaseHelper.updateBooking(query);
                            Toast.makeText(getContext(), "Booking Update", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }

                }
                break;
            case R.id.event_date:
                AppController.date = "Booking";
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                break;
        }

    }
    public void clearCashe(){
        person_name.setText("");
        client_mobile_no.setText("");
        address.setText("");
        cnic_number.setText("");
        event_name.setText("");
        total_persons.setText("");
        total_charges.setText("");
        description.setText("");
        advance_fee.setText("");
    }

}
