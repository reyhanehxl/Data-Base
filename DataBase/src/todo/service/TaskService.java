package todo.service;

import db.Database;
import db.Entity;
import db.exception.InvalidEntityException;
import todo.entity.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TaskService {
    public static void setAsCompleted(int taskId) throws CloneNotSupportedException, InvalidEntityException {
        updateTaskStatus(taskId, Task.Status.Completed);
    }
    public static void setAsInProgress(int taskId) throws CloneNotSupportedException, InvalidEntityException {
        updateTaskStatus(taskId, Task.Status.InProgress);
    }
    public static void setAsNotStarted(int taskId) throws CloneNotSupportedException, InvalidEntityException {
        updateTaskStatus(taskId, Task.Status.NotStarted);
    }
    public static Task createTask(String title, String description, Date dueDate) throws InvalidEntityException, CloneNotSupportedException {
        if (title == null || title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty.");
        if (dueDate == null || dueDate.before(new Date())) throw new IllegalArgumentException("Due date must be in the future.");

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCreationDate(new Date());
        task.setLastModificationDate(new Date());
        task.setStatus(Task.Status.NotStarted);

        Database.add(task);
        return task;
    }
    public static Task getTask(int taskId) throws InvalidEntityException, CloneNotSupportedException {
        return (Task) Database.get(taskId);
    }
    public static void updateTask(int taskId, String title, String description, Date dueDate) throws InvalidEntityException, CloneNotSupportedException {
        Task task = (Task) Database.get(taskId);

        if (title != null && !title.isEmpty()) task.setTitle(title);
        if (description != null) task.setDescription(description);

        task.setLastModificationDate(new Date());
        Database.update(task);
    }
    public static void deleteTask(int taskId) throws InvalidEntityException {
        Database.delete(taskId);
    }
    private static void updateTaskStatus(int taskId, Task.Status status) throws InvalidEntityException, CloneNotSupportedException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(status);
        task.setLastModificationDate(new Date());
        Database.update(task);
    }
    public static List<Task> getAllTasks() throws InvalidEntityException, CloneNotSupportedException {
        List<Entity> all = Database.getAll(new Task().getEntityCode());
        List<Task> tasks = new ArrayList<>();

        for (db.Entity e : all) {
            if (e instanceof Task) {
                tasks.add((Task) e);
            }
        }

        tasks.sort(Comparator.comparing(Task::getDueDate));
        return tasks;
    }

}
