package com.dynamics.crm.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dynamics.crm.R;
import com.dynamics.crm.model.Attachment;
import com.dynamics.crm.model.LeadLocation;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryAdapter.LocationViewHolder> {

    private ArrayList<LeadLocation> mDataSet;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private Context context;

    public LocationHistoryAdapter(Context context, ArrayList<LeadLocation> myDataSet) {
        this.mDataSet = myDataSet;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.lead_location_card_view, viewGroup, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder viewHolder, int position) {
        LeadLocation leadLocation = mDataSet.get(position);

        Address address = getLocation(leadLocation.getLatitude(), leadLocation.getLongitude());

        viewHolder.mLocationTitle.setText(address.getCountryName());
        viewHolder.mLocationTitle.setTypeface(Typeface.DEFAULT_BOLD);

        viewHolder.mLocationSubTitle.setText(address.getAddressLine(1) + "\n" + address.getAddressLine(3));

        viewHolder.mLocationDate.setText(leadLocation.getDate());
    }

    private Address getLocation(final String latitude, final String longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Address result = null;
        try {

            BigDecimal bd;
            bd = new BigDecimal(latitude);
            double longLatitude = bd.doubleValue();

            bd = new BigDecimal(longitude);
            double longLongitude = bd.doubleValue();

            List<Address> addressList = geocoder.getFromLocation(
                    longLatitude, longLongitude, 1);

            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                result = address;
            }
        } catch (IOException e) {
            Log.e("", "Unable connect to Geocoder", e);
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mLocationTitle;
        TextView mLocationSubTitle;
        TextView mLocationDate;

        public LocationViewHolder(View itemView) {
            super(itemView);
            mLocationTitle = (TextView) itemView.findViewById(R.id.location_title);
            mLocationSubTitle = (TextView) itemView.findViewById(R.id.location_sub_title);
            mLocationDate = (TextView) itemView.findViewById(R.id.location_date);
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
