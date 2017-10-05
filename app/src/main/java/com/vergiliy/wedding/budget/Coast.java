package com.vergiliy.wedding.budget;

class Coast {
    private	int	id, id_category;
    private	String name;
    private	int	quantity;

    Coast(int id_category, String name, int quantity) {
        this.id_category = id_category;
        this.name = name;
        this.quantity = quantity;
    }

    Coast(int id, int id_category, String name, int quantity) {
        this.id = id;
        this.id_category = id_category;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int getIdCategory() {
        return id_category;
    }

    public void setIdCategory(int id) {
        this.id = id_category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
