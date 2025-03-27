package db;

public abstract class Entity {
    public int id;
    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }
}
