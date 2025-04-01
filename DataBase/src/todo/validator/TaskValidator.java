package todo.validator;

import db.Entity;
import db.Trackable;
import db.Validator;
import db.exception.InvalidEntityException;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import todo.entity.Task;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.IOException;

public class TaskValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException{
        if(!(entity instanceof Task)){
            throw new IllegalArgumentException("Invalid entity type: Expected Task.");
        }
        Task task = (Task) entity;
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new InvalidEntityException("Task title cannot be null or empty.");
        }
    }
}
