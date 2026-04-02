import java.io.*;
import java.util.*;

// ---------------- Reservation ----------------
class Reservation implements Serializable {

    String reservationId;
    String guestName;
    String roomType;

    Reservation(String id, String guestName, String roomType) {
        this.reservationId = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// ---------------- Booking History ----------------
class BookingHistory implements Serializable {

    List<Reservation> history = new ArrayList<>();

    void addReservation(Reservation r) {
        history.add(r);
    }

    void display() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            r.display();
        }
    }
}

// ---------------- Room Inventory ----------------
class RoomInventory implements Serializable {

    Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    void display() {
        System.out.println("\n--- Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// ---------------- Wrapper State ----------------
class SystemState implements Serializable {

    BookingHistory history;
    RoomInventory inventory;

    SystemState(BookingHistory history, RoomInventory inventory) {
        this.history = history;
        this.inventory = inventory;
    }
}

// ---------------- Persistence Service ----------------
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // 💾 SAVE STATE
    void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("\nState saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // 🔄 LOAD STATE
    SystemState load() {

        File file = new File(FILE_NAME);

        // Handle missing file (first run)
        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("State loaded successfully.");
            return (SystemState) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state (corrupted file). Starting fresh.");
            return null;
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class PersistenceRecoveryApp {

    public static void main(String[] args) {

        System.out.println("UC12 - Data Persistence & Recovery\n");

        PersistenceService persistence = new PersistenceService();

        // 🔄 Attempt recovery
        SystemState state = persistence.load();

        BookingHistory history;
        RoomInventory inventory;

        if (state != null) {
            // ✅ Restore state
            history = state.history;
            inventory = state.inventory;
            System.out.println("System state restored.");
        } else {
            // ✅ Fresh start
            history = new BookingHistory();
            inventory = new RoomInventory();

            // Add sample data
            history.addReservation(new Reservation("R1", "Alice", "Single Room"));
            history.addReservation(new Reservation("R2", "Bob", "Double Room"));
        }

        // Display current state
        history.display();
        inventory.display();

        // 💾 Simulate shutdown → save state
        System.out.println("\nSimulating system shutdown...");
        persistence.save(new SystemState(history, inventory));

        System.out.println("\nRestart the program to see recovery in action.");
    }
}