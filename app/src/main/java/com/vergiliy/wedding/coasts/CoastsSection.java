package com.vergiliy.wedding.coasts;

class CoastsSection {
    private	int	id;
    private	String name;

    CoastsSection(String name) {
        this.name = name;
    }

    CoastsSection(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
