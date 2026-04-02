import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

/* Abstract Room Class */
abstract class Room {

    String type;
    int beds;
    int size;
    double price;

    Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    void displayRoomDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: $" + price);
    }
}

/* Concrete Room Types */

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 200, 100);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 350, 180);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 500, 300);
    }
}


/* Room Inventory using HashMap */

class RoomInventory {

    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    void displayInventory() {

        System.out.println("Current Room Inventory:");

        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType));
        }
    }
}


/* Reservation Class */

class Reservation {

    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void displayReservation() {
        System.out.println("Guest: " + guestName + " requested " + roomType);
    }
}


/* Main Application */

public class BookMyStayApp {

    public static void main(String[] args) {

        useCase1();
        useCase2();
        useCase3();
        useCase4();
        useCase5();

    }


    /* Use Case 1 - Application Entry */
    public static void useCase1() {

        System.out.println("=================================");
        System.out.println("Welcome to BookMyStay");
        System.out.println("Hotel Booking System v1.0");
        System.out.println("=================================");
    }


    /* Use Case 2 - Basic Room Types */

    public static void useCase2() {

        System.out.println("\n--- Use Case 2: Room Types ---\n");

        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        single.displayRoomDetails();
        System.out.println("Available: " + singleAvailable + "\n");

        dbl.displayRoomDetails();
        System.out.println("Available: " + doubleAvailable + "\n");

        suite.displayRoomDetails();
        System.out.println("Available: " + suiteAvailable + "\n");
    }


    /* Use Case 3 - Centralized Inventory */

    public static void useCase3() {

        System.out.println("\n--- Use Case 3: Centralized Inventory ---\n");

        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();
    }


    /* Use Case 4 - Room Search */

    public static void useCase4() {

        System.out.println("\n--- Use Case 4: Room Search ---\n");

        RoomInventory inventory = new RoomInventory();

        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = {single, dbl, suite};

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.type);

            if (available > 0) {

                room.displayRoomDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }


    /* Use Case 5 - Booking Request Queue */

    public static void useCase5() {

        System.out.println("\n--- Use Case 5: Booking Request Queue ---\n");

        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Guest booking requests
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Suite Room"));
        bookingQueue.add(new Reservation("Diana", "Double Room"));

        System.out.println("Booking Requests in Arrival Order:\n");

        for (Reservation r : bookingQueue) {
            r.displayReservation();
        }

        System.out.println("\nTotal Requests in Queue: " + bookingQueue.size());

        System.out.println("\nRequests are waiting for allocation (FIFO order preserved).");
    }
}