import db.Database;
import db.exception.EntityNotFoundException;
import example.Human;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException{
        Human ali = new Human("Ali");
        Database.add(ali);

        ali.name = "Ali Hosseini";

        int aliId = ali.id;
        Human aliFromTheDatabase = (Human) Database.get(aliId);

        System.out.println("ali's name in the database: " + aliFromTheDatabase.name);
    }
}