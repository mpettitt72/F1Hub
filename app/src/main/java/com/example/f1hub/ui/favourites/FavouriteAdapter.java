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
import com.example.f1hub.R;
import com.example.f1hub.data.db.FavouriteEntity;

public class FavouriteAdapter extends ListAdapter<FavouriteEntity, FavouriteAdapter.ViewHolder> {

    interface OnRemoveListener {
        void onRemove(FavouriteEntity entity);
    }

    private final OnRemoveListener listener;

    public FavouriteAdapter(OnRemoveListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourite, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        FavouriteEntity item = getItem(position);
        h.name.setText(item.name);
        h.subtitle.setText(item.subtitle != null ? item.subtitle : "");
        h.remove.setOnClickListener(v -> listener.onRemove(item));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, subtitle;
        ImageView remove;

        ViewHolder(View v) {
            super(v);
            name     = v.findViewById(R.id.tv_name);
            subtitle = v.findViewById(R.id.tv_subtitle);
            remove   = v.findViewById(R.id.btn_remove);
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
