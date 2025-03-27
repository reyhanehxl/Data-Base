package db;

import db.exception.EntityNotFoundException;

import java.util.ArrayList;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    public static void add(Entity e){
        e.id = entities.size() + 1;
        entities.add(e);
    }
}