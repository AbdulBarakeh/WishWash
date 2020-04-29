package app.project.wishwash.chat.models;

import java.util.Date;

public class Booking {
    Date date;
    Date startTime;
    Date stopTime;
    User user;
    WachingMachine wachingMachine;

    public Booking() {
    }

    public Booking(Date date , Date startTime , Date stopTime , User user , WachingMachine wachingMachine) {
        this.date = date;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.user = user;
        this.wachingMachine = wachingMachine;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WachingMachine getWachingMachine() {
        return wachingMachine;
    }

    public void setWachingMachine(WachingMachine wachingMachine) {
        this.wachingMachine = wachingMachine;
    }
}
