import java.util.*;

// ---------------- Custom Exceptions ----------------
class InvalidRoomTypeException extends Exception {
    InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InvalidBookingDataException extends Exception {
    InvalidBookingDataException(String message) {
        super(message);
    }
}

class NoAvailabilityException extends Exception {
    NoAvailabilityException(String message) {
        super(message);
    }
}

// ---------------- Reservation ----------------
class Reservation {

    String guestName;
    String roomType;
    String reservationId;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void setReservationId(String id) {
        this.reservationId = id;
    }

    void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// ---------------- Booking History ----------------
class BookingHistory {

    List<Reservation> history = new ArrayList<>();

    void addReservation(Reservation r) {
        history.add(r);
    }

    List<Reservation> getAllReservations() {
        return history;
    }
}

// ---------------- Report Service ----------------
class BookingReportService {

    void showAllBookings(BookingHistory history) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history.getAllReservations()) {
            r.display();
        }
    }

    void showSummary(BookingHistory history) {
        System.out.println("\n--- Booking Summary ---");
        Map<String, Integer> countMap = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            countMap.put(r.roomType,
                    countMap.getOrDefault(r.roomType, 0) + 1);
        }

        for (String type : countMap.keySet()) {
            System.out.println(type + " Bookings: " + countMap.get(type));
        }
    }
}

// ---------------- Room Inventory ----------------
class RoomInventory {

    HashMap<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    void decreaseAvailability(String type) throws NoAvailabilityException {

        int current = getAvailability(type);

        // Guard system state
        if (current <= 0) {
            throw new NoAvailabilityException(
                    "Cannot reduce inventory below zero for " + type);
        }

        inventory.put(type, current - 1);
    }

    boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}

// ---------------- Booking Validator ----------------
class BookingValidator {

    void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingDataException,
            InvalidRoomTypeException,
            NoAvailabilityException {

        // Validate guest name
        if (r.guestName == null || r.guestName.trim().isEmpty()) {
            throw new InvalidBookingDataException(
                    "Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(r.roomType)) {
            throw new InvalidRoomTypeException(
                    "Invalid room type: " + r.roomType);
        }

        // Validate availability
        if (inventory.getAvailability(r.roomType) <= 0) {
            throw new NoAvailabilityException(
                    "No rooms available for " + r.roomType);
        }
    }
}

// ---------------- Booking Service ----------------
class BookingService {

    int counter = 1;
    BookingValidator validator = new BookingValidator();

    void processBookings(Queue<Reservation> queue,
                         RoomInventory inventory,
                         BookingHistory history) {

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();

            try {
                // FAIL-FAST VALIDATION
                validator.validate(r, inventory);

                // Generate ID
                String id = r.roomType.replace(" ", "") + "-" + counter++;
                r.setReservationId(id);

                // Safe inventory update
                inventory.decreaseAvailability(r.roomType);

                // Store booking
                history.addReservation(r);

                System.out.println("Booking Confirmed!");
                r.display();
                System.out.println();

            } catch (InvalidBookingDataException |
                     InvalidRoomTypeException |
                     NoAvailabilityException e) {

                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
                System.out.println("System remains stable.\n");
            }
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Use Case 8 (Validated System)\n");

        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        Queue<Reservation> queue = new LinkedList<>();

        // Valid bookings
        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Double Room"));

        // Invalid scenarios
        queue.add(new Reservation("", "Single Room"));        // Invalid name
        queue.add(new Reservation("Eve", "Penthouse"));       // Invalid room
        queue.add(new Reservation("Charlie", "Suite Room"));
        queue.add(new Reservation("David", "Suite Room"));
        queue.add(new Reservation("Frank", "Suite Room"));    // Exceeds availability

        // Process bookings
        BookingService service = new BookingService();
        service.processBookings(queue, inventory, history);

        // Reporting
        BookingReportService report = new BookingReportService();
        report.showAllBookings(history);
        report.showSummary(history);

        // Remaining inventory
        inventory.displayInventory();
    }
}