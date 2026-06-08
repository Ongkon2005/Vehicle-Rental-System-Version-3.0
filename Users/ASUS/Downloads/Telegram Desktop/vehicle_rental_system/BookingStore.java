import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookingStore {

    private static final ArrayList<Booking> BOOKINGS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    private BookingStore() {}

    public static String generateId() {
        return "DRV-" + String.format("%06d", RANDOM.nextInt(999999));
    }

    public static void add(Booking booking) {
        BOOKINGS.add(booking);
    }

    public static List<Booking> getByUser(String email) {
        ArrayList<Booking> result = new ArrayList<>();
        for (Booking b : BOOKINGS) {
            if (b.userEmail.equalsIgnoreCase(email)) {
                result.add(b);
            }
        }
        return result;
    }

    public static int countByUser(String email) {
        int count = 0;
        for (Booking b : BOOKINGS) {
            if (b.userEmail.equalsIgnoreCase(email)) count++;
        }
        return count;
    }
}
