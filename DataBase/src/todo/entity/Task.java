package todo.entity;

import db.Entity;
import db.Trackable;

import javax.imageio.plugins.tiff.TIFFDirectory;
import java.io.ObjectInputFilter;
import java.util.Date;

public class Task extends Entity implements Trackable {


    public enum Status{NotStarted, InProgress, Completed}
    String title;
    String description;
    Date dueDate;
    private Date creationDate;
    private Date lastModificationDate;
    private Status status;

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

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }
    public  void setDueDate(java.sql.Date date) {
        this.dueDate = date;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        if (status != null) {
            this.status = status;
            this.lastModificationDate = new Date(); // Update modification date
        }
    }
    public Status getStatus() {
        return status;
    }


}
