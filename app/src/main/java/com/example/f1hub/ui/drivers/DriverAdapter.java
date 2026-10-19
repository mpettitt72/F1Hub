package com.example.f1hub.ui.drivers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.f1hub.R;
import com.example.f1hub.data.model.jolpica.Driver;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class DriverAdapter extends ListAdapter<Driver, DriverAdapter.ViewHolder> {

    interface OnClickListener {
        void onClick(Driver driver);
    }

    private final OnClickListener listener;
    private Map<String, String> headshotMap = Collections.emptyMap();
    private Set<String> favouriteIds = Collections.emptySet();

    public DriverAdapter(OnClickListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    public void setHeadshotMap(Map<String, String> map) {
        headshotMap = map != null ? map : Collections.emptyMap();
        notifyItemRangeChanged(0, getItemCount());
    }

    public void setFavouriteIds(Set<String> ids) {
        favouriteIds = ids != null ? ids : Collections.emptySet();
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driver, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Driver driver = getItem(position);
        h.name.setText(driver.givenName + " " + driver.familyName);
        h.nationality.setText(driver.nationality != null ? driver.nationality : "");
        h.code.setText(driver.code != null ? driver.code : "");

        String url = driver.permanentNumber != null
                ? headshotMap.get(driver.permanentNumber) : null;

        Glide.with(h.headshot.getContext())
                .load(url)
                .placeholder(R.drawable.bg_headshot_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(h.headshot);

        boolean fav = driver.driverId != null && favouriteIds.contains(driver.driverId);
        h.favIndicator.setVisibility(fav ? View.VISIBLE : View.GONE);

        h.itemView.setOnClickListener(v -> listener.onClick(driver));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headshot, favIndicator;
        TextView name, nationality, code;

        ViewHolder(View v) {
            super(v);
            headshot      = v.findViewById(R.id.iv_headshot);
            favIndicator  = v.findViewById(R.id.iv_fav_indicator);
            name          = v.findViewById(R.id.tv_driver_name);
            nationality   = v.findViewById(R.id.tv_nationality);
            code          = v.findViewById(R.id.tv_code);
        }
    }

    private static final DiffUtil.ItemCallback<Driver> DIFF = new DiffUtil.ItemCallback<Driver>() {
        @Override
        public boolean areItemsTheSame(@NonNull Driver a, @NonNull Driver b) {
            return a.driverId != null && a.driverId.equals(b.driverId);
        }
        @Override
        public boolean areContentsTheSame(@NonNull Driver a, @NonNull Driver b) {
            return a.driverId != null && a.driverId.equals(b.driverId);
        }
    };
}
