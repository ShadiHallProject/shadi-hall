package org.by9steps.shadihall.viewbinder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.bean.File;

import de.hdodenhof.circleimageview.CircleImageView;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;

;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class FileNodeBinder extends TreeViewBinder<FileNodeBinder.ViewHolder> {

    Context mCtx;

    public FileNodeBinder(Context context){
        mCtx = context;
    }

    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, final TreeNode node) {
        final File fileNode = (File) node.getContent();
        holder.tvName.setText(fileNode.fileName);

//            Picasso.get()
//                    .load(AppController.imageUrl+fileNode.fileID+".JPG")
//                    .placeholder(R.drawable.default_avatar)
//                    .into(holder.fileImage);

        holder.invite.setVisibility(View.GONE);
        holder.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mCtx.startActivity(new Intent(mCtx, RegisterActivity.class));
//                AppController.comunityID = fileNode.fileID;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_file;
    }

    public class ViewHolder extends TreeViewBinder.ViewHolder {
        public TextView tvName;
        public Button invite;
        public CircleImageView fileImage;

        public ViewHolder(View rootView) {
            super(rootView);
            this.tvName = rootView.findViewById(R.id.tv_name);
            this.invite = rootView.findViewById(R.id.invite);
            this.fileImage = rootView.findViewById(R.id.fileImage);
        }

    }
}
