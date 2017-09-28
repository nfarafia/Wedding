package com.vergiliy.wedding.coasts;

import static android.R.attr.id;

class Coast {
    private	int	id, id_section;
    private	String name;
    private	int	quantity;

    Coast(int id_section, String name, int quantity) {
        this.id_section = id_section;
        this.name = name;
        this.quantity = quantity;
    }

    Coast(int id, int id_section, String name, int quantity) {
        this.id = id;
        this.id_section = id_section;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int getIdSection() {
        return id_section;
    }

    public void setIdSection(int id) {
        this.id = id_section;
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
