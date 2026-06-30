package com.example.cravecart;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cravecart.R;
import com.example.cravecart.CartRepository;
import com.example.cravecart.FoodItem;
import java.util.*;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.VH> {
    private final List<FoodItem> data = new ArrayList<>();
    private final Map<String,Integer> qty = new HashMap<>();
    private final CartRepository repo;

    public FoodAdapter(CartRepository repo){ this.repo = repo; }

    public void setItems(List<FoodItem> items){
        data.clear(); data.addAll(items); notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_food, p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos){
        FoodItem it = data.get(pos);
        h.tvName.setText(it.name);
        h.tvDesc.setText(it.description);
        h.tvPrice.setText("₹" + it.price);
        Glide.with(h.itemView.getContext()).load(it.imageUrl).into(h.img);

        int q = qty.getOrDefault(it.id, 0);
        h.tvQty.setText(String.valueOf(q));

        h.btnPlus.setOnClickListener(v -> {
            int newQ = q + 1; qty.put(it.id, newQ);
            notifyItemChanged(h.getBindingAdapterPosition());
            repo.addOrInc(it.id, it.name, it.price, it.imageUrl, +1);
        });

        h.btnMinus.setOnClickListener(v -> {
            int newQ = Math.max(0, q - 1); qty.put(it.id, newQ);
            notifyItemChanged(h.getBindingAdapterPosition());
            repo.addOrInc(it.id, it.name, it.price, it.imageUrl, -1);
        });
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder{
        TextView tvName, tvDesc, tvPrice, tvQty; ImageView img; ImageButton btnPlus, btnMinus;
        VH(View v){
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvDesc = v.findViewById(R.id.tvDesc);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvQty   = v.findViewById(R.id.tvQty);
            img     = v.findViewById(R.id.img);
            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus= v.findViewById(R.id.btnMinus);
        }
    }
}
