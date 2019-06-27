package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.BookingActivity;
import org.by9steps.shadihall.model.Bookings;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter{

    private Context mCtx;
    List<Bookings> mList;

    public BookingAdapter(Context mCtx, List<Bookings> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.selected_date_booking_detail, null);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Bookings bookings = mList.get(position);

        ((MenuViewHolder)viewHolder).eventName.setText("Event Name : "+bookings.getEventName());
        ((MenuViewHolder)viewHolder).totalPersons.setText("Arrangement of Person :"+bookings.getArrangePersons());
        ((MenuViewHolder)viewHolder).clientName.setText("Name or Person :"+bookings.getClientName());
        ((MenuViewHolder)viewHolder).totalCharges.setText("Booking Charges :"+bookings.getChargesTotal());
//        ((MenuViewHolder)viewHolder).recieved.setText(bookings.getEventName());
//        ((MenuViewHolder)viewHolder).balance.setText(bookings.getEventName());

        ((MenuViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, BookingActivity.class);
                intent.putExtra("BookingID",bookings.getBookingID());
                intent.putExtra("Type","Detail");
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, totalPersons, clientName, totalCharges, recieved, balance;

        public MenuViewHolder(View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.booking_name);
            totalPersons = itemView.findViewById(R.id.total_persons);
            clientName = itemView.findViewById(R.id.client_name);
            totalCharges = itemView.findViewById(R.id.total_charges);
            recieved = itemView.findViewById(R.id.recieved);
            balance = itemView.findViewById(R.id.balance);


        }
    }
}
