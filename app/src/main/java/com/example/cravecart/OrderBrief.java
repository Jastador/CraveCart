package com.example.cravecart;

import com.google.firebase.Timestamp;
import java.util.List;
import java.util.Map;

public class OrderBrief {
    public String id;
    public String userId;
    public Timestamp createdAt;
    public int total;
    public String status;
    public List<Map<String,Object>> items;
    public OrderBrief() {}
}

