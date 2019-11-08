package org.by9steps.shadihall.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.adapters.BookingAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Bookings;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedDateBookingsFragment extends Fragment {

    private static final String ARG_EVENT_DATE = "event_date";
    private static final String ARG_EVENT_SHIFT = "event_shift";

    RecyclerView recyclerView;
    BookingAdapter adapter;
    Prefrence prefrence;
    DatabaseHelper databaseHelper;
    List<Bookings> bookingsList;


    String query ;

    // TODO: Rename and change types of parameters
    private String eventDate, eventShift;

    public static SelectedDateBookingsFragment newInstance(String eventDate, String eventShift) {
        SelectedDateBookingsFragment fragment = new SelectedDateBookingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_DATE, eventDate);
        args.putString(ARG_EVENT_SHIFT, eventShift);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventDate = getArguments().getString(ARG_EVENT_DATE);
            eventShift = getArguments().getString(ARG_EVENT_SHIFT);
        }
    }

    public SelectedDateBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_date_bookings, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler);

        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());


        query = "SELECT * FROM Booking WHERE ClientID =" + prefrence.getClientIDSession()+" AND EventDate = '"+eventDate+"' AND Shift = '"+eventShift+"'";

        bookingsList = databaseHelper.getBookings(query);

        adapter = new BookingAdapter(getContext(),bookingsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.menu_container, BookingFormFragment.newInstance(eventDate,"id",eventShift))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
