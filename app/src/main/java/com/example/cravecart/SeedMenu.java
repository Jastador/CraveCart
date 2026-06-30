package com.example.cravecart;

import com.google.firebase.firestore.*;
import java.util.*;

public class SeedMenu {

    static class SI {
        String cat, name, desc, url; int price;
        SI(String c, String n, String d, int p, String u){ cat=c; name=n; desc=d; price=p; url=u; }
    }

    public static void run(FirebaseFirestore db) {
        List<SI> items = new ArrayList<>();

        // --- Snacks & Starters
        items.add(new SI("Snacks & Starters","Crispy French Fries","Golden fried potato sticks, perfectly salted and served hot.",99,"https://cdn.imgchest.com/files/7pjcqdqbao7.png"));
        items.add(new SI("Snacks & Starters","Paneer Tikka (5 pcs)","Succulent cottage cheese cubes marinated in spicy yogurt and grilled to perfection.",189,"https://cdn.imgchest.com/files/7w6c2z26ewy.png"));
        items.add(new SI("Snacks & Starters","Veg Samosa (2 pcs)","Classic crispy pastry filled with spiced potatoes and peas. Served with chutney.",79,"https://cdn.imgchest.com/files/yrgcnanpkd4.png"));
        items.add(new SI("Snacks & Starters","Chicken Chilli Dry","Wok-tossed chicken pieces with bell peppers, onions, and a spicy oriental sauce.",249,"https://cdn.imgchest.com/files/739cxgx2bz7.png"));
        items.add(new SI("Snacks & Starters","Garlic Bread with Cheese","Toasted bread generously topped with garlic butter and melted mozzarella.",129,"https://cdn.imgchest.com/files/7w6c2z2a62y.png"));

        // --- Pizza & Pasta  (normalized to match your left panel)
        items.add(new SI("Pizza & Pasta","Margherita Pizza (8 inch)","Classic pizza with a rich tomato sauce, mozzarella, and fresh basil.",299,"https://cdn.imgchest.com/files/yq9c323wn34.png"));
        items.add(new SI("Pizza & Pasta","Veggie Supreme Pizza (8 inch)","Loaded with bell peppers, onions, olives, mushrooms, and sweet corn on a cheesy base.",349,"https://cdn.imgchest.com/files/7mmc939d8v7.png"));
        items.add(new SI("Pizza & Pasta","Chicken Pepperoni Pizza (8 inch)","Savory chicken pepperoni slices layered over melted cheese and tangy tomato sauce.",379,"https://cdn.imgchest.com/files/4apc5w5eoe4.png"));
        items.add(new SI("Pizza & Pasta","Creamy White Sauce Pasta (Chicken)","Penne pasta tossed in a rich, velvety white sauce with exotic vegetables.",269,"https://cdn.imgchest.com/files/45xcvqvrw27.png"));
        items.add(new SI("Pizza & Pasta","Spicy Arrabbiata Pasta (Chicken)","Penne pasta with tender chicken in a fiery tomato sauce with garlic and chili flakes.",299,"https://cdn.imgchest.com/files/y8xcn2nj824.png"));

        // --- Main Course
        items.add(new SI("Main Course","Butter Chicken","Creamy and rich North Indian curry with tender chicken pieces in a tomato-based gravy.",329,"https://cdn.imgchest.com/files/yvdcwgw5zvy.png"));
        items.add(new SI("Main Course","Dal Makhani","Slow-cooked black lentils simmered in butter, cream, and aromatic spices.",249,"https://cdn.imgchest.com/files/7bwckekz3d7.png"));
        items.add(new SI("Main Course","Veg Biryani","Aromatic basmati rice cooked with mixed vegetables, fragrant spices, and fresh herbs. Served with raita.",289,"https://cdn.imgchest.com/files/7pjcqdq35l7.png"));
        items.add(new SI("Main Course","Hakka Noodles (Veg)","Stir-fried noodles with crisp vegetables in a savory Indo-Chinese sauce.",199,"https://cdn.imgchest.com/files/yvdcwgw5eny.png"));
        items.add(new SI("Main Course","Classic Chicken Burger","Juicy chicken patty, fresh lettuce, tomato, onion, and our special sauce in a toasted bun.",219,"https://cdn.imgchest.com/files/49zc292qx3y.png"));

        // --- Desserts
        items.add(new SI("Desserts","Chocolate Lava Cake","A decadent chocolate cake with a molten, gooey chocolate center.",149,"https://cdn.imgchest.com/files/4gdcxox6rm4.png"));
        items.add(new SI("Desserts","Gulab Jamun (2 pcs)","Soft, deep-fried milk solids soaked in a fragrant sugar syrup.",99,"https://cdn.imgchest.com/files/4z9cvkv6xw7.png"));
        items.add(new SI("Desserts","Vanilla Ice Cream Scoop","Classic creamy vanilla ice cream, a perfect cool-down treat.",79,"https://cdn.imgchest.com/files/y8xcn2njp24.png"));
        items.add(new SI("Desserts","Brownie with Ice Cream","Warm, fudgy chocolate brownie served with a scoop of vanilla ice cream.",189,"https://cdn.imgchest.com/files/4gdcxox6nm4.png"));
        items.add(new SI("Desserts","Mango Kulfi","Traditional Indian frozen dessert with the rich flavor of ripe mangoes.",109,"https://cdn.imgchest.com/files/ye3c2829bb4.png"));

        // --- Hot Beverages
        items.add(new SI("Hot Beverages","Masala Chai","Aromatic Indian tea brewed with ginger, cardamom, and other spices.",69,"https://cdn.imgchest.com/files/7lxcprpqd37.png"));
        items.add(new SI("Hot Beverages","Cappuccino","Espresso with steamed milk and a generous layer of foamed milk.",139,"https://cdn.imgchest.com/files/y2pckdkgop7.png"));
        items.add(new SI("Hot Beverages","Hot Chocolate","Rich, comforting chocolate drink, perfect for a cozy moment.",149,"https://cdn.imgchest.com/files/7kzcawajbd7.png"));
        items.add(new SI("Hot Beverages","Black Coffee","Strong, invigorating black coffee, pure and unsweetened.",99,"https://cdn.imgchest.com/files/7ogcbwbmkby.png"));
        items.add(new SI("Hot Beverages","Green Tea","A healthy and refreshing brew, light and calming.",79,"https://cdn.imgchest.com/files/7pjcqdq3wn7.png"));

        // --- Cold Beverages
        items.add(new SI("Cold Beverages","Coca-Cola (300ml Can)","The classic refreshing fizzy drink.",40,"https://cdn.imgchest.com/files/7w6c2z2aroy.png"));
        items.add(new SI("Cold Beverages","Fresh Lime Soda (Sweet/Salted)","Zesty fresh lime juice mixed with soda, perfectly balanced with sweet or salty notes.",89,"https://cdn.imgchest.com/files/739cxgx23w7.png"));
        items.add(new SI("Cold Beverages","Chocolate Milkshake","Creamy blend of milk and rich chocolate syrup, topped with a swirl of whipped cream.",159,"https://cdn.imgchest.com/files/7mmc939d5v7.png"));
        items.add(new SI("Cold Beverages","Watermelon Juice","Freshly squeezed, hydrating watermelon juice, no added sugar.",119,"https://cdn.imgchest.com/files/yq9c323web4.png"));
        items.add(new SI("Cold Beverages","Mineral Water (500ml)","Pure, refreshing bottled drinking water.",20,"https://cdn.imgchest.com/files/4jdcv3vljw4.png"));

        WriteBatch batch = db.batch();
        CollectionReference col = db.collection("menu");

        for (SI s : items) {
            DocumentReference doc = col.document(); // auto-id
            Map<String,Object> m = new HashMap<>();
            m.put("id", doc.getId());
            m.put("category", s.cat);
            m.put("name", s.name);
            m.put("description", s.desc);
            m.put("price", s.price);
            m.put("imageUrl", s.url);
            m.put("ordersCount", 0);
            batch.set(doc, m);
        }

        batch.commit()
                .addOnSuccessListener(v -> System.out.println("Seeded menu ✅"))
                .addOnFailureListener(e -> System.err.println("Seeding failed: " + e.getMessage()));
    }
}