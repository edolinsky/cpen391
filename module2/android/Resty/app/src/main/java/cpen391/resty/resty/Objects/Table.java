package cpen391.resty.resty.Objects;


public class Table extends GsonSerializable {
    private final String restaurantId;
    private final String tableId;

    public Table(String restaurantId, String tableId) {
        this.restaurantId = restaurantId;
        this.tableId = tableId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getTableId() {
        return tableId;
    }
}
