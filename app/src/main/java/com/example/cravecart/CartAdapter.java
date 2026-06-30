package com.example.cravecart;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cravecart.R;
import com.example.cravecart.CartItemEntity;
import java.util.*;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    public interface CartCallbacks {
        void onInc(CartItemEntity item);
        void onDec(CartItemEntity item);
        void onRemove(CartItemEntity item);
    }

    private final List<CartItemEntity> data = new ArrayList<>();
    private final CartCallbacks cb;

    public CartAdapter(CartCallbacks cb){ this.cb = cb; }

    public void setItems(List<CartItemEntity> items){
        data.clear(); data.addAll(items);
        notifyDataSetChanged();
    }

    public List<CartItemEntity> getItems(){ return data; }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_cart, p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos){
        CartItemEntity it = data.get(pos);
        h.tvName.setText(it.name);
        h.tvPrice.setText("₹" + it.unitPrice);
        h.tvQty.setText(String.valueOf(it.quantity));
        Glide.with(h.itemView.getContext()).load(it.imageUrl).into(h.img);

        h.btnPlus.setOnClickListener(v -> cb.onInc(it));
        h.btnMinus.setOnClickListener(v -> cb.onDec(it));
        h.btnRemove.setOnClickListener(v -> cb.onRemove(it));
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder{
        ImageView img; TextView tvName, tvPrice, tvQty; ImageButton btnPlus, btnMinus, btnRemove;
        VH(View v){
            super(v);
            img=v.findViewById(R.id.img); tvName=v.findViewById(R.id.tvName);
            tvPrice=v.findViewById(R.id.tvPrice); tvQty=v.findViewById(R.id.tvQty);
            btnPlus=v.findViewById(R.id.btnPlus); btnMinus=v.findViewById(R.id.btnMinus);
            btnRemove=v.findViewById(R.id.btnRemove);
        }
    }
}
