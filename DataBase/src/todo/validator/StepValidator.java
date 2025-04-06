package todo.validator;

import db.Database;
import db.Entity;
import db.Validator;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

public class StepValidator implements Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if(!(entity instanceof Step)){
            throw new IllegalArgumentException("Invalid entity type: Expected Step.");
        }
        Step step = (Step) entity;
        if (step.getTitle() == null || step.getTitle().trim().isEmpty()) {
            throw new InvalidEntityException("Step title cannot be null or empty.");
        }
        try {
            Entity taskEntity = Database.get(step.getTaskRef());  // دریافت Task از دیتابیس
            if (!(taskEntity instanceof Task)) {
                throw new InvalidEntityException("Invalid task reference: The referenced entity is not a Task.");
            }
        } catch (EntityNotFoundException | CloneNotSupportedException e) {
            throw new InvalidEntityException("Invalid task reference: No Task found with the given ID.");
        }
    }
}
