package todo.service;
import db.Database;
import db.Entity;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class StepService {
    public static void saveStep(int taskRef, String title) throws InvalidEntityException, CloneNotSupportedException {
        Step step = new Step(title);
        step.setTaskRef(taskRef);
        Database.add(step);
    }
    public static List<Step> getStepsByTaskId(int taskId) throws InvalidEntityException {List<db.Entity> rawSteps = Database.getAll(taskId);
        List<Step> steps = new ArrayList<>();

        for (db.Entity e : rawSteps) {
            if (e instanceof Step) {
                Step step = (Step) e;
                if (step.getTaskRef() == taskId) {
                    steps.add(step);
                }
            }
        }
        return steps;
    }
}
