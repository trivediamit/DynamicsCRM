package com.dynamics.crm.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dynamics.crm.R;
import com.dynamics.crm.model.Attachment;
import com.dynamics.crm.model.Lead;

import java.util.ArrayList;

public class AttachmentListAdapter extends RecyclerView.Adapter<AttachmentListAdapter.AttachmentViewHolder> {

    private ArrayList<Attachment> mDataSet;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public AttachmentListAdapter(Context context, ArrayList<Attachment> myDataSet) {
        this.mDataSet = myDataSet;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AttachmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.attachment_card_view, viewGroup, false);
        return new AttachmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachmentViewHolder viewHolder, int position) {
        Attachment attachment = mDataSet.get(position);
        viewHolder.mAttachmentType.setText(attachment.getAttachmentType());

        Bitmap bmp = BitmapFactory.decodeFile(attachment.getAttachmentPath());
        viewHolder.mAttachmentImage.setImageBitmap(bmp);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class AttachmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mAttachmentType;
        ImageView mAttachmentImage;

        public AttachmentViewHolder(View itemView) {
            super(itemView);
            mAttachmentType = (TextView) itemView.findViewById(R.id.attachment_type);
            mAttachmentImage = (ImageView) itemView.findViewById(R.id.attachment_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.itemClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClick(View view, int position);
    }
}
