package cpen391.resty.resty.Objects;

import java.util.ArrayList;
import java.util.List;

import cpen391.resty.resty.menu.RestaurantMenuItem;

public class Order extends GsonSerializable {
    private final String customer_id;
    private final String restaurant_id;
    private final String table_id;
    private final List<ItemOrder> items;

    private Order(String customer_id, String restaurant_id, String table_id) {
        this.customer_id = customer_id;
        this.restaurant_id = restaurant_id;
        this.table_id = table_id;
        this.items = new ArrayList<>();
    }

    private void addItems(List<RestaurantMenuItem> items) {
        for(RestaurantMenuItem item : items) {
            this.items.add(new ItemOrder(item.getId(), item.getName()));
        }
    }

    public static Order create(List<RestaurantMenuItem> items, String userId, Table table) {
        Order order = new Order(userId, table.getRestaurantId(), table.getTableId());
        order.addItems(items);

        return order;
    }

    private class ItemOrder {
        private final String menu_id;
        private final String customer_name;

        ItemOrder(String id, String name) {
            this.customer_name = name;
            this.menu_id = id;
        }
    }
}