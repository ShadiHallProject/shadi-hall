package org.by9steps.shadihall.viewbinder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.SugarContext;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.ChaartOfAccAddActivity;
import org.by9steps.shadihall.activities.DetailCalendarActivity;
import org.by9steps.shadihall.activities.RegisterActivity;
import org.by9steps.shadihall.bean.Dir;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class DirectoryNodeBinder extends TreeViewBinder<DirectoryNodeBinder.ViewHolder> {

    Context mCtx;

    public DirectoryNodeBinder(Context context){
        mCtx = context;
    }

    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(final ViewHolder holder, int position, TreeNode node) {
        holder.ivArrow.setRotation(0);
        holder.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp);
        int rotateDegree = node.isExpand() ? 90 : 0;
        holder.ivArrow.setRotation(rotateDegree);
        final Dir dirNode = (Dir) node.getContent();
        holder.tvName.setText(dirNode.dirName);

//            Picasso.get()
//                    .load("http://aapkawakeel.easysoft.com.pk/ClientImages/1/"+dirNode.dirID+".jpg")
//                    .placeholder(R.drawable.default_avatar)
//                    .into(holder.imageDir);
        holder.imageDir.setVisibility(View.GONE);

        if (dirNode.type.equals("1")){
            holder.imageDir.setVisibility(View.VISIBLE);
            holder.detail.setVisibility(View.VISIBLE);
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedDate = dirNode.dirName;
                    Intent intent = new Intent(mCtx, DetailCalendarActivity.class);
                    intent.putExtra("message", selectedDate);
                    mCtx.startActivity(intent);
                }
            });
        }else if (dirNode.type.equals("Add")){
            holder.detail.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
            holder.add.setVisibility(View.VISIBLE);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, ChaartOfAccAddActivity.class);
                    intent.putExtra("GroupID", dirNode.dirID);
                    mCtx.startActivity(intent);
                }
            });

        }else if (dirNode.type.equals("Edit")){
            holder.detail.setVisibility(View.GONE);
            holder.add.setVisibility(View.GONE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, ChaartOfAccAddActivity.class);
                    mCtx.startActivity(intent);
                }
            });

        }else if(dirNode.type.equals("CA")){
            holder.imageDir.setVisibility(View.GONE);
            holder.detail.setVisibility(View.GONE);
        }else {
            holder.imageDir.setVisibility(View.VISIBLE);
            holder.detail.setVisibility(View.GONE);
            holder.add.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
        }


        if (node.isLeaf())
            holder.ivArrow.setVisibility(View.INVISIBLE);
        else holder.ivArrow.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }

    public static class ViewHolder extends TreeViewBinder.ViewHolder {
        private ImageView ivArrow;
        private CircleImageView imageDir;
        private ImageButton msgIcon;
        private TextView tvName;
        public Button detail, add, edit;

        public ViewHolder(View rootView) {
            super(rootView);
            this.ivArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.detail = rootView.findViewById(R.id.detail);
            this.add = rootView.findViewById(R.id.add);
            this.edit = rootView.findViewById(R.id.edit);
            this.imageDir = rootView.findViewById(R.id.image_dir);
            SugarContext.init(itemView.getContext());
        }

        public ImageView getIvArrow() {
            return ivArrow;
        }

        public TextView getTvName() {
            return tvName;
        }
    }

}
