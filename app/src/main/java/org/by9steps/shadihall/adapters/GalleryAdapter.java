package org.by9steps.shadihall.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.by9steps.shadihall.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.RecyclerEachViewHolder> {

    private Context ctx;
    private int pos;
    ArrayList<String> mArrayUri;

   public listeneachclicklistner listeneachclicklistner;

    public interface listeneachclicklistner {
         void ImageClicked(int pos, String obj, ImageView imageView);
         void onImageDeleteClick(int pos, String obj, ImageView imageView);
    }

    public GalleryAdapter(Context ctx, ArrayList<String> mArrayUri) {
        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
    }


    @NonNull
    @Override
    public RecyclerEachViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(ctx).inflate(R.layout.gv_item, viewGroup, false);


        return new RecyclerEachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerEachViewHolder recyclerEachViewHolder, int i) {

        File f = new File(mArrayUri.get(i));
        Log.e("resusltfilter", i + ")" + mArrayUri.get(i));
        Bitmap d = new BitmapDrawable(ctx.getResources(), f.getAbsolutePath()).getBitmap();

        recyclerEachViewHolder.imageView.setImageBitmap(d);
        recyclerEachViewHolder.imageView.setId(i);
        recyclerEachViewHolder.pos = i;
        recyclerEachViewHolder.obj = mArrayUri.get(i);
    }

    @Override
    public int getItemCount() {
        return mArrayUri.size();
    }

    class RecyclerEachViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        int pos;
        String obj;

        public RecyclerEachViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivGallery);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listeneachclicklistner != null)
                        listeneachclicklistner.ImageClicked(pos,obj,imageView);
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listeneachclicklistner != null)
                        listeneachclicklistner.onImageDeleteClick(pos,obj,imageView);

                    return true;
                }
            });
        }
    }


}
