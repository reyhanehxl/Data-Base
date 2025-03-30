package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Database {
    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static final HashMap<Integer, Validator> validators = new HashMap<Integer, Validator>();

    public static void registerValidator(int entityClass, Validator validator) {
        if (validators.containsKey(entityClass)) {
            throw new IllegalArgumentException("Validator already exists for entity: " + entityClass);
        }
        validators.put(entityClass, validator);
    }

    public static void add(Entity e) throws CloneNotSupportedException, InvalidEntityException {
        Validator validator = validators.get(e.getClass());
        if (validator != null) {
            validator.validate(e);
        }
        if(e instanceof Trackable) {
            Date currentDate = new Date();
            Trackable trackableEntity = (Trackable) e;

            if (trackableEntity.getCreationDate() == null)
                trackableEntity.setCreationDate(currentDate);

            trackableEntity.setLastModificationDate(currentDate);
        }
        e.id = entities.size() + 1;
        entities.add(e.clone());
    }

    public static Entity get(int id) throws CloneNotSupportedException {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity.clone();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) {
        for (Entity entity : entities) {
            if (entity.id == id) {
                entities.remove(entity);
                return;
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void update(Entity e) throws CloneNotSupportedException, InvalidEntityException {
        Validator validator = validators.get(e.getClass());
        if (validator != null) {
            validator.validate(e);
        }
        if (e instanceof Trackable) {
            Date currentDate = new Date();
            Trackable trackableEntity = (Trackable) e;
            trackableEntity.setLastModificationDate(currentDate);
        }

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).id == e.id) {
                entities.set(i, e.clone());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}
