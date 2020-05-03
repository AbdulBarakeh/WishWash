package app.project.wishwash;

public class WashingMachine {

    String Id;
    String Name;

    public WashingMachine () {}

    public WashingMachine(String id , String name) {
        Id = id;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
