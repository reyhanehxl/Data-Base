package todo.service;
import db.Database;
import db.exception.InvalidEntityException;
import todo.entity.Step;

public class StepService {
    public static void saveStep(int taskRef, String title) throws InvalidEntityException, CloneNotSupportedException {
        Step step = new Step(title);
        step.setTaskRef(taskRef);
        Database.add(step);
    }
}
