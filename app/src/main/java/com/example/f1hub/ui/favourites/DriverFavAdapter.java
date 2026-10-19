package com.example.f1hub.ui.favourites;

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
import com.example.f1hub.data.db.FavouriteEntity;
import java.util.Collections;
import java.util.Map;

public class DriverFavAdapter extends ListAdapter<FavouriteEntity, DriverFavAdapter.ViewHolder> {

    interface OnRemoveListener {
        void onRemove(FavouriteEntity entity);
    }

    private final OnRemoveListener listener;
    private Map<String, String> headshotMap = Collections.emptyMap();

    public DriverFavAdapter(OnRemoveListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    public void setHeadshotMap(Map<String, String> map) {
        headshotMap = map != null ? map : Collections.emptyMap();
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driver_favourite, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        FavouriteEntity item = getItem(position);

        h.name.setText(item.name);
        h.subtitle.setText(item.subtitle != null ? item.subtitle : "");
        h.code.setText(item.code != null ? item.code : "");

        String url = item.permanentNumber != null
                ? headshotMap.get(item.permanentNumber) : null;

        Glide.with(h.headshot.getContext())
                .load(url)
                .placeholder(R.drawable.bg_headshot_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(h.headshot);

        h.remove.setOnClickListener(v -> listener.onRemove(item));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headshot, remove;
        TextView name, subtitle, code;

        ViewHolder(View v) {
            super(v);
            headshot = v.findViewById(R.id.iv_headshot);
            remove   = v.findViewById(R.id.btn_remove);
            name     = v.findViewById(R.id.tv_driver_name);
            subtitle = v.findViewById(R.id.tv_subtitle);
            code     = v.findViewById(R.id.tv_code);
        }
    }

    private static final DiffUtil.ItemCallback<FavouriteEntity> DIFF = new DiffUtil.ItemCallback<FavouriteEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull FavouriteEntity a, @NonNull FavouriteEntity b) {
            return a.id.equals(b.id);
        }
        @Override
        public boolean areContentsTheSame(@NonNull FavouriteEntity a, @NonNull FavouriteEntity b) {
            return a.id.equals(b.id) && a.name.equals(b.name);
        }
    };
}
