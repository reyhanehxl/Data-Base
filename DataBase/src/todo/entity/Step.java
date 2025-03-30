package todo.entity;

import db.Entity;

public class Step extends Entity {
    enum Status{NotStarted, Completed}
    String title;
    Status status;
    int taskRef;

    public Step(String title){
        this.title = title;
        this.status = Status.NotStarted;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public int getTaskRef(){
        return taskRef;
    }
    public void setTaskRef(int taskRef){
        this.taskRef = taskRef;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    @Override
    public int getEntityCode() {
        return title.hashCode();
    }
}
