package org.by9steps.shadihall.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.BookingAdapter;
import org.by9steps.shadihall.callback.BookFormClickListener;
import org.by9steps.shadihall.dbhelper.BookingManager;
import org.by9steps.shadihall.model.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFormFragment extends Fragment implements View.OnClickListener, BookFormClickListener {

    public static final int EDIT = 0;
    public static final int DELETE = 1;
    public Button btnAdd, btnEdit;
    public RecyclerView mRecyclerView;
    public List<Booking> itemList;
    public BookingAdapter mAdapter;
    public BookingManager mDatabase;
    EditText ed_client_id, ed_net_code, ed_sys_code, ed_updated_date;
    EditText ed_ClientName, ed_clientAddress, ed_clientMobile, ed_clientCNIC, ed_charges,ed_eventname,ed_eventDat,ed_persons,ed_bookDate,ed_description;
    int edit_id = -1;


    public BookingFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_form, container, false);

        ed_client_id = (EditText) view.findViewById(R.id.ed_client_id1);
        ed_net_code = (EditText) view.findViewById(R.id.ed_net_code1);
        ed_sys_code = (EditText) view.findViewById(R.id.ed_sys_code1);
        ed_updated_date = (EditText) view.findViewById(R.id.ed_updated_date1);

        ed_ClientName = (EditText) view.findViewById(R.id.ed_AcName1);
        ed_clientAddress = (EditText) view.findViewById(R.id.ed_AcAddress1);
        ed_clientMobile = (EditText) view.findViewById(R.id.ed_ClientMobileNo1);
        ed_clientCNIC = (EditText) view.findViewById(R.id.ed_NICnumber);
        ed_charges = (EditText) view.findViewById(R.id.ed_charges);
        ed_eventname = (EditText) view.findViewById(R.id.ed_eventName);
        ed_persons = (EditText) view.findViewById(R.id.ed_persons);
        //    ed_bookDate = (EditText) findViewById(R.id.ed_bookDate);
        ed_description = (EditText) view.findViewById(R.id.ed_description);


        btnAdd = (Button) view.findViewById(R.id.btnSubmit);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);

        itemList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mDatabase = new BookingManager(getContext());
        mDatabase.open();
        itemList = mDatabase.getBooking();
        if (itemList.size() > 0) {
            mAdapter = new BookingAdapter(getContext(), itemList, this);
            mRecyclerView.setAdapter(mAdapter);
        }
        mDatabase.close();

        btnAdd.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        if (btnAdd == view) {

            String client_id = ed_client_id.getText().toString().trim();
            String net_code = ed_net_code.getText().toString().trim();
            String sys_code = ed_sys_code.getText().toString().trim();
            String updated_date = ed_updated_date.getText().toString().trim();

            String ac_name = ed_ClientName.getText().toString().trim();
            String ac_address = ed_clientAddress.getText().toString().trim();
            String ac_mobile_no = ed_clientMobile.getText().toString().trim();
            String ac_email_address = ed_clientCNIC.getText().toString().trim();
            String ac_salary = ed_charges.getText().toString().trim();
            String eventName = ed_eventname.getText().toString().trim();
            String ev_date = ed_eventDat.getText().toString().trim();
            String person = ed_persons.getText().toString().trim();
            String bookdate = ed_bookDate.getText().toString().trim();
            String description = ed_description.getText().toString().trim();


            if ( !client_id.isEmpty() || !net_code.isEmpty() || !sys_code.isEmpty() ||
                    !updated_date.isEmpty() || !ac_name.isEmpty() || !ac_address.isEmpty() || !ac_mobile_no.isEmpty() ||
                    !ac_email_address.isEmpty() || !ac_salary.isEmpty() || !description.isEmpty() || !eventName.isEmpty()
                    || !ev_date.isEmpty()|| !person.isEmpty()|| !bookdate.isEmpty()) {


                Booking item = new Booking();

                item.ClientID = client_id;
                item.NetCode = net_code;
                item.SysCode = sys_code;
                item.setUpdateDate( updated_date);

                item.setClientName( ac_name);
                item.setClientAddress(ac_address);
                item.setClientMobile( ac_mobile_no);
                item.setClientCNIC( ac_email_address);
                item.setCharges(ac_salary);

                mDatabase.open();
                mDatabase.insertBooking(item);
                mDatabase.close();

                mDatabase.open();
                itemList.clear();
                itemList = mDatabase.getBooking();
                mDatabase.close();

                if (itemList.size() > 0) {
                    mAdapter = new BookingAdapter(getContext(), itemList, this);
                    mRecyclerView.setAdapter(mAdapter);
                }

                ed_client_id.setText("");
                ed_net_code.setText("");
                ed_sys_code.setText("");
                ed_updated_date.setText("");

                ed_ClientName.setText("");
                ed_clientAddress.setText("");
                ed_clientMobile.setText("");
                ed_clientCNIC.setText("");
                ed_charges.setText("");
                ed_eventname.setText("");
                ed_eventDat.setText("");
                ed_persons.setText("");
                ed_bookDate.setText("");
                ed_description.setText("");


            } else {
                Toast.makeText(getContext(), "Please write somthing", Toast.LENGTH_LONG).show();
            }
        } else if (view == btnEdit) {


            btnAdd.setEnabled(true);
            btnAdd.setBackgroundResource(R.color.colorPrimaryDark);
            Toast.makeText(getContext(), "Enable Save Button", Toast.LENGTH_SHORT).show();

            String client_id = ed_client_id.getText().toString().trim();
            String net_code = ed_net_code.getText().toString().trim();
            String sys_code = ed_sys_code.getText().toString().trim();
            String updated_date = ed_updated_date.getText().toString().trim();

            String ac_name = ed_ClientName.getText().toString().trim();
            String ac_address = ed_clientAddress.getText().toString().trim();
            String ac_mobile_no = ed_clientMobile.getText().toString().trim();
            String ac_email_address = ed_clientCNIC.getText().toString().trim();
            String ac_salary = ed_charges.getText().toString().trim();
            String eventName = ed_eventname.getText().toString().trim();
            String ev_date = ed_eventDat.getText().toString().trim();
            String bookdate = ed_bookDate.getText().toString().trim();
            String person = ed_persons.getText().toString().trim();
            String description = ed_description.getText().toString().trim();


            if ( !client_id.isEmpty() || !net_code.isEmpty() || !sys_code.isEmpty() ||
                    !updated_date.isEmpty() || !ac_name.isEmpty() || !ac_address.isEmpty() || !ac_mobile_no.isEmpty() ||
                    !ac_email_address.isEmpty() || !ac_salary.isEmpty() || !description.isEmpty() || !eventName.isEmpty()
                    || !ev_date.isEmpty()|| !person.isEmpty()|| !bookdate.isEmpty()) {



                Booking item = new Booking();

                item.ClientID = client_id;
                item.NetCode = net_code;
                item.SysCode = sys_code;
                item.setUpdateDate(updated_date);

                item.setClientName(ac_name);
                item.setClientAddress( ac_address);
                item.setClientMobile( ac_mobile_no);
                item.setClientCNIC(ac_email_address);
                item.setCharges(ac_salary);


                mDatabase.open();
                mDatabase.updateBooking(item, edit_id);
                itemList.clear();
                itemList = mDatabase.getBooking();
                mDatabase.close();


                if (itemList.size() > 0) {
                    mAdapter = new BookingAdapter(getContext(), itemList, this);
                    mRecyclerView.setAdapter(mAdapter);
                }
                mAdapter.notifyDataSetChanged();

                ed_client_id.setText("");
                ed_net_code.setText("");
                ed_sys_code.setText("");
                ed_updated_date.setText("");

                ed_ClientName.setText("");
                ed_clientAddress.setText("");
                ed_clientMobile.setText("");
                ed_clientCNIC.setText("");
                ed_charges.setText("");
                ed_eventname.setText("");
                ed_eventDat.setText("");
                ed_persons.setText("");
                ed_bookDate.setText("");
                ed_description.setText("");


            } else {
                Toast.makeText(getContext(), "Please write somthing", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClickBooking(int id, int TAG, String client_id, String net_code, String sys_code, String updated_date, String ac_name, String ac_address, String ac_mobile_no, String ac_email_address, String ac_salary) {

    }
}
