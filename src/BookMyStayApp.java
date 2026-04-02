import java.util.*;

// ---------------- Custom Exceptions ----------------
class ReservationNotFoundException extends Exception {
    ReservationNotFoundException(String message) {
        super(message);
    }
}

class AlreadyCancelledException extends Exception {
    AlreadyCancelledException(String message) {
        super(message);
    }
}

// ---------------- Reservation ----------------
class Reservation {

    String reservationId;
    String guestName;
    String roomType;
    boolean isCancelled = false;

    Reservation(String id, String guestName, String roomType) {
        this.reservationId = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void cancel() {
        isCancelled = true;
    }

    void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType +
                " | Status: " + (isCancelled ? "CANCELLED" : "CONFIRMED"));
    }
}

// ---------------- Booking History ----------------
class BookingHistory {

    List<Reservation> history = new ArrayList<>();

    void addReservation(Reservation r) {
        history.add(r);
    }

    Reservation findById(String id) {
        for (Reservation r : history) {
            if (r.reservationId.equals(id)) {
                return r;
            }
        }
        return null;
    }

    void displayAll() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            r.display();
        }
    }
}

// ---------------- Room Inventory ----------------
class RoomInventory {

    Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
    }

    void increaseAvailability(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// ---------------- Cancellation Service ----------------
class CancellationService {

    // 🔁 Stack for rollback tracking (LIFO)
    Stack<String> rollbackStack = new Stack<>();

    void cancelBooking(String reservationId,
                       BookingHistory history,
                       RoomInventory inventory)
            throws ReservationNotFoundException, AlreadyCancelledException {

        // Step 1: Validate existence
        Reservation r = history.findById(reservationId);

        if (r == null) {
            throw new ReservationNotFoundException(
                    "Reservation not found: " + reservationId);
        }

        // Step 2: Validate not already cancelled
        if (r.isCancelled) {
            throw new AlreadyCancelledException(
                    "Reservation already cancelled: " + reservationId);
        }

        // Step 3: Record rollback (LIFO)
        rollbackStack.push(r.reservationId);

        // Step 4: Restore inventory
        inventory.increaseAvailability(r.roomType);

        // Step 5: Mark as cancelled
        r.cancel();

        System.out.println("Cancellation successful for ID: " + reservationId);
    }

    void showRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// ---------------- MAIN CLASS ----------------
public class CancellationUseCaseApp {

    public static void main(String[] args) {

        System.out.println("Use Case: Booking Cancellation with Rollback\n");

        // Setup
        BookingHistory history = new BookingHistory();
        RoomInventory inventory = new RoomInventory();
        CancellationService cancelService = new CancellationService();

        // Sample confirmed bookings
        Reservation r1 = new Reservation("SingleRoom-1", "Alice", "Single Room");
        Reservation r2 = new Reservation("DoubleRoom-1", "Bob", "Double Room");

        history.addReservation(r1);
        history.addReservation(r2);

        history.displayAll();
        inventory.displayInventory();

        // ---- Perform Cancellations ----
        try {
            cancelService.cancelBooking("SingleRoom-1", history, inventory);

            // Try duplicate cancellation (should fail)
            cancelService.cancelBooking("SingleRoom-1", history, inventory);

        } catch (ReservationNotFoundException |
                 AlreadyCancelledException e) {

            System.out.println("Cancellation Failed: " + e.getMessage());
        }

        // ---- Final State ----
        history.displayAll();
        inventory.displayInventory();
        cancelService.showRollbackStack();
    }
}