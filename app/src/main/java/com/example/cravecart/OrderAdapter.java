package com.example.cravecart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.VH> {
    private final List<OrderBrief> data = new ArrayList<>();
    private final SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

    public void setItems(List<OrderBrief> items){ data.clear(); data.addAll(items); notifyDataSetChanged(); }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_order, p, false));
    }
    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        OrderBrief o = data.get(pos);
        h.tvOrderId.setText("Order: " + o.id);
        String date = (o.createdAt != null) ? fmt.format(o.createdAt.toDate()) : "—";
        h.tvDate.setText(date);
        h.tvTotal.setText("Total: ₹" + o.total);
        h.tvStatus.setText("Status: " + (o.status==null?"—":o.status));
    }
    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvTotal, tvStatus;
        VH(View v){ super(v);
            tvOrderId=v.findViewById(R.id.tvOrderId);
            tvDate=v.findViewById(R.id.tvDate);
            tvTotal=v.findViewById(R.id.tvTotal);
            tvStatus=v.findViewById(R.id.tvStatus);
        }
    }
}

