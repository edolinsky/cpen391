package cpen391.resty.resty.menu;


public class MenuItem {
    private String id;
    private String name;
    private String description;
    private String price;
    private String type;
    private int amount;

    public MenuItem(String id, String name, String description, String price, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.amount = 0;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}