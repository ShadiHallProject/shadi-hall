package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.model.Menu;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {


    private Context mCtx;
    List<Menu> mList;

    public RecyclerViewAdapter(Context mCtx, List<Menu> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fragment_menu_item, null);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final Menu menu = mList.get(i);

        ((MenuViewHolder)viewHolder).name.setText(menu.getTitle());

        ((MenuViewHolder)viewHolder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx,MenuClickActivity.class);
                intent.putExtra("message",menu.getTitle());
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        TextView name;
        ImageView imageView;


        public MenuViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            name = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);

        }
    }
}
