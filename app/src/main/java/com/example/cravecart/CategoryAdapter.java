package com.example.cravecart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH>
{
    public interface OnCategoryClick { void onClick(String key); }
    private final List<Category> data; private final OnCategoryClick cb;
    private int selected = 0;

    public CategoryAdapter(List<Category> data, OnCategoryClick cb)
    {
        this.data = data; this.cb = cb;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v)
    {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_category, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos)
    {
        Category c = data.get(pos);
        h.tv.setText(c.label);
        h.itemView.setAlpha(selected==pos? 1f : 0.6f);
        h.itemView.setOnClickListener(v -> {
            int adapterPos = h.getBindingAdapterPosition();
            if (adapterPos == RecyclerView.NO_POSITION) return;
            int old = selected;
            selected = adapterPos;
            notifyItemChanged(old);
            notifyItemChanged(selected);
            cb.onClick(data.get(adapterPos).key);
        });
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(View v){ super(v); tv = v.findViewById(R.id.tvCategory); }
    }
}
