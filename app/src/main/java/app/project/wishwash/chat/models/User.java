package app.project.wishwash.chat.models;

public class User {
    String id;
    String name;
//    Boolean currentUser;
//    Message message;

    public User(){}
    public User(String id , String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
//    public Message getMessage() {
//        return message;
//    }
//    public void setMessage(Message message) {
//        this.message = message;
//    }
}
