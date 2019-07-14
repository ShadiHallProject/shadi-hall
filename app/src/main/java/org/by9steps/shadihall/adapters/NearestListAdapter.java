package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.DetailCalendarActivity;
import org.by9steps.shadihall.model.Client;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NearestListAdapter extends RecyclerView.Adapter{

    Context mCtx;
    List<Client> mList;
    String viewType;

    private OnItemClickListener listener;

    public NearestListAdapter(Context mCtx, List<Client> mList, String viewType) {
        this.mCtx = mCtx;
        this.mList = mList;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.nearest_list_item, null);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Client client = mList.get(position);

        if (viewType.equals("0")) {

            ((ListViewHolder) viewHolder).name.setText(client.getCompanyName());
            ((ListViewHolder) viewHolder).country.setText(client.getCountry());
            ((ListViewHolder) viewHolder).city.setText(client.getCity());
            ((ListViewHolder) viewHolder).subCity.setText(client.getSubCity());

            List<Client> images = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                images.add(new Client(client.getClientID()));
            }
            HorizontalImageViewAdapter adapter = new HorizontalImageViewAdapter(mCtx, images);
            ((ListViewHolder) viewHolder).recyclerView.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.HORIZONTAL, true));
            ((ListViewHolder) viewHolder).recyclerView.setHasFixedSize(true);
            ((ListViewHolder) viewHolder).recyclerView.setAdapter(adapter);

            ((ListViewHolder) viewHolder).call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "tel:" + client.getCompanyNumber();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    mCtx.startActivity(intent);
                }
            });

            ((ListViewHolder) viewHolder).booking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mCtx, DetailCalendarActivity.class);
                    i.putExtra("message", client.getCompanyName());
                    mCtx.startActivity(i);
                }
            });
            ((ListViewHolder) viewHolder).locate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null)
                        listener.replaceFragment(client.getLat(),client.getLng(), client.getCompanyName());
                }
            });


        }else {
            ((ListViewHolder) viewHolder).name.setText(client.getCompanyName());
            ((ListViewHolder) viewHolder).name.setBackgroundColor(Color.WHITE);
            ((ListViewHolder) viewHolder).name.setTextColor(Color.parseColor("#b73752"));

            ((ListViewHolder) viewHolder).country.setVisibility(View.GONE);
            ((ListViewHolder) viewHolder).city.setVisibility(View.GONE);
            ((ListViewHolder) viewHolder).subCity.setVisibility(View.GONE);
            ((ListViewHolder) viewHolder).recyclerView.setVisibility(View.GONE);
            ((ListViewHolder) viewHolder).call.setVisibility(View.GONE);
            ((ListViewHolder) viewHolder).locate.setVisibility(View.GONE);
            ((ListViewHolder) viewHolder).booking.setVisibility(View.GONE);

            ((ListViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(client.getClientID(),client.getCompanyName(),client.getCountry(),client.getCity(),
                                client.getSubCity(),client.getWebSite(),client.getEmail(),client.getCapacityOfPersons(),
                                client.getLat(), client.getLng(), client.getCompanyNumber());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView name, country, city, subCity;
        Button call, locate, booking;

        public ListViewHolder(View itemView) {
            super(itemView);

            recyclerView = itemView.findViewById(R.id.recycler);
            name = itemView.findViewById(R.id.name);
            country = itemView.findViewById(R.id.country);
            city = itemView.findViewById(R.id.city);
            subCity = itemView.findViewById(R.id.sub_city);
            call = itemView.findViewById(R.id.btn_call);
            locate = itemView.findViewById(R.id.btn_locate);
            booking = itemView.findViewById(R.id.btn_calendar);


        }
    }

    //load fragment on recyclerView OnClickListner
    public  void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String clientID, String name, String country, String city, String subCity
                ,String website, String email, String persons, String Lat, String Lng, String CompanyNumber);
        void replaceFragment(String Lat, String Lng, String title);
    }
}
