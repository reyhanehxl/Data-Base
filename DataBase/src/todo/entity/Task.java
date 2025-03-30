package todo.entity;

import db.Entity;
import db.Trackable;

import java.io.ObjectInputFilter;
import java.util.Date;

public class Task extends Entity implements Trackable {

    enum Status{NotStarted, InProgress, Completed}
    String title;
    String description;
    Date dueDate;
    private Date creationDate;
    private Date lastModificationDate;


    @Override
    public int getEntityCode() {
        return (title + dueDate).hashCode();
    }

    @Override
    public void setCreationDate(Date date) {
       this.creationDate = date;
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public void setLastModificationDate(Date date) {
        this.lastModificationDate = date;
    }

    @Override
    public Date getLastModificationDate() {
        return this.lastModificationDate;
    }
}
