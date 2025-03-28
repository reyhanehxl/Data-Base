package db;

import db.exception.EntityNotFoundException;

import java.util.ArrayList;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    public static void add(Entity e) throws CloneNotSupportedException{
        e.id = entities.size() + 1;
        entities.add(e.clone());
    }
    public static Entity get(int id) throws CloneNotSupportedException{
        for(Entity entity : entities){
            if(entity.id == id)
                return entity.clone();
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
    public static void update(Entity e) throws CloneNotSupportedException{
        try{
            for(int i = 0; i < entities.size(); i++){
                if(entities.get(i).id == e.id){
                    entities.set(i, e.clone());
                    return;
                }
            }

        } catch (CloneNotSupportedException ex){
            throw new RuntimeException("Cloning failed", ex);
        }
        throw new EntityNotFoundException(e.id);
    }
}