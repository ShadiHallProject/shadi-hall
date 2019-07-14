package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.Client;
import org.by9steps.shadihall.model.Projects;

import java.util.List;

public class HorizontalImageViewAdapter extends RecyclerView.Adapter{

    Context mCtx;
    List<Client> mList;

    String url;

    private OnItemClickListener listener;

    public HorizontalImageViewAdapter(Context mCtx, List<Client> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.horizontal_adapter_item, null);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final Client client = mList.get(position);

        int i = position + 1;

        Picasso.get()
                .load(AppController.imageUrl+client.getClientID()+"/SliderImages/image"+i+".png")
                .placeholder(R.drawable.logo)
                .into(((ListViewHolder)viewHolder).image);

        Log.e("URL",AppController.imageUrl+client.getClientID()+"/SliderImages/image"+i+".png");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        public ListViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);

        }
    }

    //load fragment on recyclerView OnClickListner
    public  void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String id, String name);
    }
}
