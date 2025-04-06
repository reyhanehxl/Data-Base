import db.*;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import todo.entity.Task;
import todo.entity.Step;
import todo.service.StepService;
import todo.service.TaskService;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InvalidEntityException, CloneNotSupportedException {
        Scanner scn = new Scanner(System.in);
        int command;

        while (true) {
            System.out.println("1. add task\n2. add step\n3. delete\n4. update task\n5. update step\n6. get task by id\n7. get all tasks\n8. get incomplete tasks\n9. exit");
            command = scn.nextInt();
            scn.nextLine();

            switch (command) {
                case 1:
                    System.out.println("Enter task details:");
                    System.out.print("Title: ");
                    String title = scn.nextLine().trim();

                    System.out.print("Description: ");
                    String description = scn.nextLine().trim();

                    System.out.print("Due date (yyyy-MM-dd): ");
                    String dueDateStr = scn.nextLine().trim();

                    if (title.isEmpty()) {
                        System.out.println("Cannot save task.");
                        System.out.println("Error: Task title cannot be empty.");
                        break;
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dueDate;

                    try {
                        dueDate = sdf.parse(dueDateStr);
                    } catch (Exception e) {
                        System.out.println("Cannot save task.");
                        System.out.println("Error: Invalid date format.");
                        break;
                    }

                    try {
                        Task savedTask = TaskService.createTask(title, description, dueDate);
                        System.out.println("Task saved successfully.");
                        System.out.println("ID: " + savedTask.id);
                    } catch (InvalidEntityException | CloneNotSupportedException | IllegalArgumentException e) {
                        System.out.println("Cannot save task.");
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;


                case 2:
                    try {
                        List<Task> allTasks = TaskService.getAllTasks();

                        if (allTasks.isEmpty()) {
                            System.out.println("No tasks found.");
                            break;
                        }

                        for (Task task : allTasks) {
                            System.out.println("ID: " + task.id);
                            System.out.println("Title: " + task.getTitle());
                            System.out.println("Due Date: " + new SimpleDateFormat("yyyy-MM-dd").format(task.getDueDate()));
                            System.out.println("Status: " + task.getStatus());

                            List<db.Entity> relatedEntities = Database.getAll(new Task().getEntityCode()); // stepها با task.id ذخیره شدن
                            List<todo.entity.Step> steps = new ArrayList<>();
                            for (db.Entity e : relatedEntities) {
                                if (e instanceof todo.entity.Step) {
                                    steps.add((todo.entity.Step) e);
                                }
                            }

                            if (!steps.isEmpty()) {
                                System.out.println("Steps:");
                                for (todo.entity.Step s : steps) {
                                    System.out.println("    + " + s.getTitle() + ":");
                                    System.out.println("        ID: " + s.id);
                                    System.out.println("        Status: " + s.getStatus());
                                }
                            }

                            System.out.println(); // خط جداکننده بین تسک‌ها
                        }

                    } catch (InvalidEntityException | CloneNotSupportedException e) {
                        System.out.println("Error while retrieving tasks.");
                        System.out.println("Reason: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("ID: ");
                    int entityId = scn.nextInt();
                    scn.nextLine();

                    try {
                        Task task = (Task) Database.get(entityId);
                        if (task != null) {
                            System.out.println("Deleting steps associated with Task ID=" + entityId);
                            List<db.Entity> rawSteps = Database.getAll(new Task().getEntityCode());
                            for (db.Entity entity : rawSteps) {
                                if (entity instanceof Step)
                                    Database.delete(entity.id);
                            }
                            Database.delete(entityId);
                            System.out.println("Entity with ID=" + entityId + " successfully deleted.");
                        }
                    } catch (EntityNotFoundException e) {
                        System.out.println("Cannot delete entity with ID=" + entityId);
                        System.out.println("Error: Entity not found.");
                    }
                    break;

                case 4:
                    System.out.print("ID: ");
                    int taskIdToUpdate = scn.nextInt();
                    scn.nextLine();
                    System.out.print("Field: ");
                    String field = scn.nextLine();

                    System.out.print("New Value: ");
                    String newValue = scn.nextLine();

                    try {
                        Task task = (Task) Database.get(taskIdToUpdate);
                        if (task == null) {
                            throw new EntityNotFoundException(taskIdToUpdate);
                        }
                        String oldValue = "";

                        if (field.equalsIgnoreCase("title")) {
                            oldValue = task.getTitle();
                            task.setTitle(newValue);
                        } else if (field.equalsIgnoreCase("description")) {
                            oldValue = task.getDescription();
                            task.setDescription(newValue);
                        } else if (field.equalsIgnoreCase("dueDate")) {
                            oldValue = task.getDueDate().toString();
                            task.setDueDate(java.sql.Date.valueOf(newValue));
                        } else if (field.equalsIgnoreCase("status")) {
                            oldValue = task.getStatus().toString();
                            Task.Status newStatus = Task.Status.valueOf(newValue);
                            task.setStatus(newStatus);

                            if (newStatus == Task.Status.Completed) {
                                List<db.Entity> rawSteps = Database.getAll(new Task().getEntityCode());
                                for (db.Entity entity : rawSteps) {
                                    if (entity instanceof Step) {
                                        Step step = (Step) entity;
                                        step.setStatus(Step.Status.Completed);
                                        Database.update(step);
                                    }
                                }
                            }
                        }

                        Database.update(task);
                        System.out.println("Successfully updated the task.");
                        System.out.println("Field: " + field);
                        System.out.println("Old Value: " + oldValue);
                        System.out.println("New Value: " + newValue);
                        System.out.println("Modification Date: " + task.getLastModificationDate());

                    } catch (EntityNotFoundException e) {
                        System.out.println("Cannot update task with ID=" + taskIdToUpdate);
                        System.out.println("Error: Cannot find entity with id=" + taskIdToUpdate);
                    }
                    break;

                case 5:
                    System.out.println("Enter step update details:");
                    System.out.print("ID: ");
                    int stepId = scn.nextInt();
                    scn.nextLine();
                    System.out.print("Field: ");
                    String field1 = scn.nextLine();

                    System.out.print("New Value: ");
                    String newValue1 = scn.nextLine();

                    try {
                        Step step = (Step) Database.get(stepId);
                        if (step == null) {
                            throw new EntityNotFoundException("Step with ID=" + stepId + " not found.");
                        }

                        String oldValue = "";

                        if (field1.equalsIgnoreCase("status")) {
                            oldValue = step.getStatus().toString();
                            Step.Status newStatus = Step.Status.valueOf(newValue1);
                            step.setStatus(newStatus);

                            Task task = (Task) Database.get(step.getTaskRef());
                            if (task != null) {
                                boolean allStepsCompleted = true;
                                boolean hasNotStartedStep = false;

                                List<db.Entity> rawSteps = Database.getAll(new Task().getEntityCode());
                                for (db.Entity entity : rawSteps) {
                                    if (entity instanceof Step) {
                                        Step s = (Step) entity;
                                        if (s.getStatus() != Step.Status.Completed) {
                                            allStepsCompleted = false;
                                        }
                                        if (s.getStatus() == Step.Status.NotStarted) {
                                            hasNotStartedStep = true;
                                        }
                                    }
                                }

                                if (allStepsCompleted) {
                                    task.setStatus(Task.Status.Completed);
                                } else if (hasNotStartedStep && task.getStatus() == Task.Status.NotStarted) {
                                    task.setStatus(Task.Status.InProgress);
                                }

                                Database.update(task);
                            }
                        }

                        Database.update(step);

                        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                        System.out.println("Successfully updated the step.");
                        System.out.println("Field: " + field1);
                        System.out.println("Old Value: " + oldValue);
                        System.out.println("New Value: " + newValue1);
                        System.out.println("Modification Date: " + sdf2.format(new Date()));

                    } catch (EntityNotFoundException e) {
                        System.out.println("Cannot update step with ID=" + stepId);
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;

                case 6:
                    System.out.print("Enter Task ID: ");
                    int taskIdToView = scn.nextInt();
                    scn.nextLine();

                    try {
                        Task task = (Task) Database.get(taskIdToView);
                        if (task == null) {
                            System.out.println("Cannot find task with ID=" + taskIdToView);
                            break;
                        }

                        System.out.println("ID: " + task.id);
                        System.out.println("Title: " + task.getTitle());
                        System.out.println("Due Date: " + task.getDueDate());
                        System.out.println("Status: " + task.getStatus());

                        List<db.Entity> rawSteps = Database.getAll(new Task().getEntityCode());
                        List<Step> steps = new ArrayList<>();
                        for (db.Entity e : rawSteps) {
                            if (e instanceof Step) {
                                steps.add((Step) e);
                            }
                        }

                        if (steps.isEmpty()) {
                            System.out.println("Steps: None");
                        } else {
                            System.out.println("Steps:");
                            for (Step step : steps) {
                                System.out.println("    + " + step.getTitle() + ":");
                                System.out.println("        ID: " + step.id);
                                System.out.println("        Status: " + step.getStatus());
                                System.out.println("        Creation Date: " + task.getCreationDate());
                            }
                        }

                    } catch (EntityNotFoundException e) {
                        System.out.println("Cannot find task with ID=" + taskIdToView);
                        System.out.println("Error: Entity not found.");
                    }
                    break;

                case 7:
                    System.out.println("Getting all tasks...");

                    try {
                        List<db.Entity> rawTasks = Database.getAll(new Task().getEntityCode());
                        List<Task> tasks = new ArrayList<>();

                        for (db.Entity e : rawTasks) {
                            if (e instanceof Task) {
                                tasks.add((Task) e);
                            }
                        }

                        if (tasks.isEmpty()) {
                            System.out.println("No tasks found.");
                        } else {
                            for (Task task : tasks) {
                                System.out.println("ID: " + task.id);
                                System.out.println("Title: " + task.getTitle());
                                System.out.println("Due Date: " + task.getDueDate());
                                System.out.println("Status: " + task.getStatus());

                                List<Step> steps = StepService.getStepsByTaskId(task.id);

                                if (steps.isEmpty()) {
                                    System.out.println("    Steps: None");
                                } else {
                                    System.out.println("    Steps:");
                                    for (Step step : steps) {
                                        System.out.println("        + " + step.getTitle() + ":");
                                        System.out.println("            ID: " + step.id);
                                        System.out.println("            Status: " + step.getStatus());
                                        System.out.println("            Creation Date: " + task.getCreationDate());
                                    }
                                }
                            }
                        }

                    } catch (EntityNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;


                case 8:
                    System.out.println("Getting incomplete tasks...");

                    try {
                        List<db.Entity> rawTasks = Database.getAll(new Task().getEntityCode());
                        List<Task> tasks = new ArrayList<>();
                        for (db.Entity e : rawTasks) {
                            if (e instanceof Task) {
                                tasks.add((Task) e);
                            }
                        }

                        for (Task task : tasks) {
                            if (!task.getStatus().equals(Task.Status.Completed)) {
                                System.out.println("ID: " + task.id);
                                System.out.println("Title: " + task.getTitle());
                                System.out.println("Due Date: " + task.getDueDate());
                                System.out.println("Status: " + task.getStatus());
                            }
                        }
                    } catch (EntityNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 9:
                    System.out.println("Exiting...");
                    scn.close();
                    return;

                default:
                    System.out.println("Invalid command.");
            }
        }
    }
}