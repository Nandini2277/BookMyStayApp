import java.util.*;

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
    SingleRoom() { super("Single Room", 1, 200, 100); }
}

class DoubleRoom extends Room {
    DoubleRoom() { super("Double Room", 2, 350, 180); }
}

class SuiteRoom extends Room {
    SuiteRoom() { super("Suite Room", 3, 500, 300); }
}


/* Room Inventory */

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

    void decreaseAvailability(String roomType) {

        int count = inventory.get(roomType);
        inventory.put(roomType, count - 1);
    }

    void displayInventory() {

        System.out.println("Current Inventory:");

        for (String room : inventory.keySet()) {
            System.out.println(room + " : " + inventory.get(room));
        }
    }
}


/* Reservation */

class Reservation {

    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
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
        useCase6();
    }


    /* Use Case 1 */

    public static void useCase1() {

        System.out.println("=================================");
        System.out.println("Welcome to BookMyStay");
        System.out.println("Hotel Booking System v1.0");
        System.out.println("=================================");
    }


    /* Use Case 2 */

    public static void useCase2() {

        System.out.println("\n--- Use Case 2: Room Types ---");

        Room s = new SingleRoom();
        Room d = new DoubleRoom();
        Room su = new SuiteRoom();

        s.displayRoomDetails();
        d.displayRoomDetails();
        su.displayRoomDetails();
    }


    /* Use Case 3 */

    public static void useCase3() {

        System.out.println("\n--- Use Case 3: Centralized Inventory ---");

        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();
    }


    /* Use Case 4 */

    public static void useCase4() {

        System.out.println("\n--- Use Case 4: Room Search ---");

        RoomInventory inventory = new RoomInventory();

        Room[] rooms = {new SingleRoom(), new DoubleRoom(), new SuiteRoom()};

        for (Room r : rooms) {

            int available = inventory.getAvailability(r.type);

            if (available > 0) {

                r.displayRoomDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }


    /* Use Case 5 - Booking Queue */

    public static void useCase5() {

        System.out.println("\n--- Use Case 5: Booking Request Queue ---");

        Queue<Reservation> queue = new LinkedList<>();

        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Double Room"));
        queue.add(new Reservation("Charlie", "Suite Room"));

        System.out.println("Requests in Queue:");

        for (Reservation r : queue) {
            System.out.println(r.guestName + " requested " + r.roomType);
        }
    }


    /* Use Case 6 - Safe Room Allocation */

    public static void useCase6() {

        System.out.println("\n--- Use Case 6: Booking Confirmation ---");

        RoomInventory inventory = new RoomInventory();

        Queue<Reservation> queue = new LinkedList<>();

        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Double Room"));
        queue.add(new Reservation("Charlie", "Suite Room"));
        queue.add(new Reservation("David", "Double Room"));


        // Track allocated room IDs
        Set<String> allocatedRoomIDs = new HashSet<>();

        // Map room type → assigned room IDs
        HashMap<String, Set<String>> roomAssignments = new HashMap<>();


        while (!queue.isEmpty()) {

            Reservation req = queue.poll();

            int available = inventory.getAvailability(req.roomType);

            if (available > 0) {

                String roomID = req.roomType.substring(0,2).toUpperCase() + (available);

                if (!allocatedRoomIDs.contains(roomID)) {

                    allocatedRoomIDs.add(roomID);

                    roomAssignments
                            .computeIfAbsent(req.roomType, k -> new HashSet<>())
                            .add(roomID);

                    inventory.decreaseAvailability(req.roomType);

                    System.out.println("Booking Confirmed:");
                    System.out.println(req.guestName + " assigned Room ID: " + roomID);
                }

            } else {

                System.out.println("Booking Failed for " + req.guestName +
                        " (No rooms available)");
            }
        }

        System.out.println("\nAllocated Rooms:");
        System.out.println(roomAssignments);
    }
}