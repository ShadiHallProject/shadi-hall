package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.Projects;
import org.by9steps.shadihall.model.ShadiHallList;

import java.util.List;

public class ProjectsListAdapter extends RecyclerView.Adapter{

    Context mCtx;
    List<Projects> mList;

    String url;

    private OnItemClickListener listener;

    public ProjectsListAdapter(Context mCtx, List<Projects> mList) {
        this.mCtx = mCtx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fragment_section_list, null);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final Projects projects = mList.get(position);

        ((ListViewHolder)viewHolder).title.setText(projects.getProjectName());
        int i = position + 1;
        url = "http://shadihall.easysoft.com.pk/ProjectImages/ProjectsLogo/"+projects.getProjectID()+".png";
        Log.e("URL",url);
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.default_avatar)
                .into(((ListViewHolder)viewHolder).image);
        ((ListViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(projects.getProjectID(), projects.getProjectName());
            }
        });
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
            title = itemView.findViewById(R.id.title);

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
