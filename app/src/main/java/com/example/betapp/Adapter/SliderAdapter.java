package com.example.betapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.betapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.vh> {
    private final List<String> mSliderItems;

    public SliderAdapter(List<String > mSliderItems) {
        this.mSliderItems = mSliderItems;
    }

    @Override
    public vh onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new vh(inflate);
    }

    @Override
    public void onBindViewHolder(vh viewHolder, int position) {

        final String sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
                .load(sliderItem)
                .fitCenter()

                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public class vh extends ViewHolder {
        View itemView;
        ImageView imageViewBackground;

        public vh(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}
