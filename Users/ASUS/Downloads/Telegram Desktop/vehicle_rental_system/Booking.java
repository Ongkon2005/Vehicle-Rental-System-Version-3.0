public class Booking {

    public final String bookingId;
    public final String vehicleName;
    public final double pricePerDay;
    public final int days;
    public final double total;
    public final String paymentMethod;
    public final String reference;
    public final String dateTime;
    public final String userEmail;

    public Booking(String bookingId, String vehicleName, double pricePerDay, int days,
                   double total, String paymentMethod, String reference,
                   String dateTime, String userEmail) {
        this.bookingId = bookingId;
        this.vehicleName = vehicleName;
        this.pricePerDay = pricePerDay;
        this.days = days;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.reference = reference;
        this.dateTime = dateTime;
        this.userEmail = userEmail;
    }
}
