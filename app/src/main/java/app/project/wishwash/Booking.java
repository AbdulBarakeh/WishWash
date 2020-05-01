package app.project.wishwash;

import java.util.Calendar;

public class Booking {
    Calendar date;
    User user;
    WashingMachine washingMachine;

    public Booking() {
    }

    public Booking(Calendar date, User user, WashingMachine washingMachine) {
        this.date = date;
        this.user = user;
        this.washingMachine = washingMachine;
    }

    public Calendar getDate() { return date; }
    public void setDate(Calendar date) { this.date = date; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public WashingMachine getWashingMachine() { return washingMachine; }
    public void setWashingMachine(WashingMachine washingMachine) { this.washingMachine = washingMachine; }
}