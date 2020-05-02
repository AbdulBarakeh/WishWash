package app.project.wishwash;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private boolean isAdmin = false;
    private String email, password;
    private String name;
    private String department;
    private List<Booking> bookingList;

    public User(){}

    public User(String email, String password) {
        this.userId = "";
        this.email = email;
        this.password = password;
        this.name = "";
        this.department = "";
        this.bookingList = new ArrayList<>();
    }

    public User(String userId, String email, String password, String name, String department, List<Booking> bookingList) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.department = department;

        if (!bookingList.isEmpty())
            this.bookingList = bookingList;
        else
            this.bookingList = new ArrayList<>();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public List<Booking> getBookingList() { return bookingList; }
    public void setBookingList(List<Booking> bookingList) { this.bookingList = bookingList; }
}
