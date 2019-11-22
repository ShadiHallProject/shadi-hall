package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.GeneralLedgerActivity;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.ProjectMenu;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {


    private Context mCtx;
    List<ProjectMenu> mList;

    Prefrence prefrence;

    public RecyclerViewAdapter(Context mCtx, List<ProjectMenu> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
        prefrence = new Prefrence(mCtx);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fragment_section_list, null);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final ProjectMenu menu = mList.get(position);

        ((MenuViewHolder)viewHolder).name.setText(menu.getMenuName());

        Picasso.get()
                .load(ApiRefStrings.ServerAddress+"PhpApi/ProjectImages/MenuIcon/"+menu.getImageName()+".png")
                .placeholder(R.drawable.logo)
                .into(((MenuViewHolder)viewHolder).imageView);

//        ((MenuViewHolder)viewHolder).imageView.setImageResource(R.drawable.cash);

        ((MenuViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("key","title:"+menu.getMenuName()+" Page:"
                        +menu.getPageOpen()+" ValuePass:"+menu.getValuePass()+" ");
                if (menu.getPageOpen().equals("Ledger")){
                        Intent intent = new Intent(mCtx, GeneralLedgerActivity.class);
                        intent.putExtra("AcID","1");
                        mCtx.startActivity(intent);
                }else {
                    Intent intent = new Intent(mCtx,MenuClickActivity.class);
                    intent.putExtra("title",menu.getMenuName());
                    intent.putExtra("message",menu.getPageOpen());
                    intent.putExtra("position",menu.getValuePass());
                    mCtx.startActivity(intent);
                }
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
