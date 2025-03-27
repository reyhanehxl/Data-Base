package db;

import db.exception.EntityNotFoundException;

import java.util.ArrayList;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    public static void add(Entity e){
        e.id = entities.size() + 1;
        entities.add(e);
    }
    public static Entity get(int id){
        for(Entity entity : entities){
            if(entity.id == id)
                return entity;
        }
        throw new EntityNotFoundException(id);
    }
    public static void delete(int id){
        for(Entity entity : entities){
            if(entity.id == id){
                entities.remove(entity);
                return;
            }
        }
        throw new EntityNotFoundException(id);
    }
    public static void update(Entity e){
        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i).id == e.id){
                entities.set(i, e);
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}