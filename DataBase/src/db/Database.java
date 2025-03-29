package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static HashMap<Integer, Validator> validators;

    public static void registerValidator(int entityCode, Validator validator) {
        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator already exists for entity code: " + entityCode);
        }
        validators.put(entityCode, validator);
}

    public static void add(Entity e) throws CloneNotSupportedException, InvalidEntityException {
        Validator validator = validators.get(e.getClass());
        if(validator != null){
            validator.validate(e);
        }else{
            throw new IllegalArgumentException("No validator for entity code: "+ e.getClass().getName());
        }
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
    public static void update(Entity e) throws CloneNotSupportedException, InvalidEntityException {
        Validator validator = validators.get(e.getClass());
        if(validator != null){
            validator.validate(e);
        }else{
            throw new IllegalArgumentException("No validator for entity code: "+ e.getClass().getName());
        }
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