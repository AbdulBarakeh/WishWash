package app.project.wishwash.booking;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class BookingContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<BookingItem> ITEMS = new ArrayList<BookingItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, BookingItem> ITEM_MAP = new HashMap<String, BookingItem>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createBookingItem(i));
        }
    }

    private static void addItem(BookingItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name , item);
    }

    private static BookingItem createBookingItem(int position) {
        return new BookingItem("name" ,new Date().toString(),"StartTime",new Date().toString(),"Machine 2");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class BookingItem {
        public final String name;
        public final String date;
        public final String startTime;
        public final String stopTime;
        public final String washingMachine;

        public BookingItem(String name , String date , String startTime , String stopTime , String washingMachine) {
            this.name = name;
            this.date = date;
            this.startTime = startTime;
            this.stopTime = stopTime;
            this.washingMachine = washingMachine;
        }

        @Override
        public String toString() {
            return startTime;
        }
    }
}
