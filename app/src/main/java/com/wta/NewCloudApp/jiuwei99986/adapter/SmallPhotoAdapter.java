package com.wta.NewCloudApp.jiuwei99986.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.listener.OnItemSelectListener;

import java.util.ArrayList;

/**
 * Created by donglua on 15/5/31.
 */
public class SmallPhotoAdapter extends RecyclerView.Adapter<SmallPhotoAdapter.PhotoViewHolder> {
    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context mContext;
    public final static int TYPE_ADD = 1;
    public final static int TYPE_PHOTO = 2;
    public final static int MAX = 9;
    private OnItemSelectListener onItemSelectListener;
    public SmallPhotoAdapter(Context mContext, ArrayList<String> photoPaths, OnItemSelectListener onItemSelectListener) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.onItemSelectListener = onItemSelectListener;
    }
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ADD:
                itemView = inflater.inflate(R.layout.item_small_add, parent, false);
                break;
            case TYPE_PHOTO:
                itemView = inflater.inflate(R.layout.item_photo, parent, false);
                break;
        }
        return new PhotoViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onPictureSelect(position);
                }
            }
        });
        if (getItemViewType(position) == TYPE_PHOTO) {
            //Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
            Glide.with(mContext)
                    .load(photoPaths.get(position))
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(holder.ivPhoto);
            holder.vSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemSelectListener != null) {
                        onItemSelectListener.onTabSelect(position);
                    }
                }
            });
        }else {

        }
    }
    @Override
    public int getItemCount() {
        int count = photoPaths.size() + 1;
        if (count > MAX) {
            count = MAX;
        }
        return count;
    }
    @Override
    public int getItemViewType(int position) {
        return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    }
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.photo_content);
            vSelected = itemView.findViewById(R.id.photo_delete);
        }
    }

}
