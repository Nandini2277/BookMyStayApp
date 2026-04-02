import java.util.*;

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
            countMap.put(r.roomType, countMap.getOrDefault(r.roomType, 0) + 1);
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

    void decreaseAvailability(String type) {
        inventory.put(type, getAvailability(type) - 1);
    }

    void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}

// ---------------- Booking Service ----------------
class BookingService {

    int counter = 1;

    void processBookings(Queue<Reservation> queue,
                         RoomInventory inventory,
                         BookingHistory history) {

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();

            if (inventory.getAvailability(r.roomType) > 0) {

                String id = r.roomType.replace(" ", "") + "-" + counter++;
                r.setReservationId(id);

                inventory.decreaseAvailability(r.roomType);

                history.addReservation(r); // store in history

                System.out.println("Booking Confirmed!");
                r.display();
                System.out.println();

            } else {
                System.out.println("No rooms available for " + r.roomType);
            }
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Use Case 8 (Booking History & Reporting)\n");

        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        Queue<Reservation> queue = new LinkedList<>();

        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Double Room"));
        queue.add(new Reservation("Charlie", "Suite Room"));
        queue.add(new Reservation("David", "Single Room"));

        // Process bookings
        BookingService service = new BookingService();
        service.processBookings(queue, inventory, history);

        // Reporting
        BookingReportService report = new BookingReportService();

        report.showAllBookings(history);
        report.showSummary(history);

        // Show remaining inventory
        inventory.displayInventory();
    }
}