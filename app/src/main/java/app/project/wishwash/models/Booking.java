package app.project.wishwash.models;

public class Booking {
    String bookingID;
    int dateYear;
    int dateMonth;
    int dateDayOfMonth;
    String dateHour;
    User user;
    WashingMachine washingMachine;

    public Booking() {
    }

    public Booking(String bookingID, int dateYear, int dateMonth, int dateDayOfMonth, String dateHour, User user, WashingMachine washingMachine) {
        this.bookingID = bookingID;
        this.dateYear = dateYear;
        this.dateMonth = dateMonth;
        this.dateDayOfMonth = dateDayOfMonth;
        this.dateHour = dateHour;
        this.user = user;
        this.washingMachine = washingMachine;
    }

    public int getDateYear() { return dateYear; }
    public void setDateYear(int dateYear) { this.dateYear = dateYear; }

    public int getDateMonth() { return dateMonth; }
    public void setDateMonth(int dateMonth) { this.dateMonth = dateMonth; }

    public int getDateDayOfMonth() { return dateDayOfMonth; }
    public void setDateDayOfMonth(int dateDayOfMonth) { this.dateDayOfMonth = dateDayOfMonth; }

    public String getDateHour() { return dateHour; }
    public void setDateHour(String dateHour) { this.dateHour = dateHour; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public WashingMachine getWashingMachine() { return washingMachine; }
    public void setWashingMachine(WashingMachine washingMachine) { this.washingMachine = washingMachine; }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }
}