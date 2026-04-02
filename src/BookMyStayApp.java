import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

// ---------------- Reservation ----------------
class Reservation {

    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// ---------------- Booking Queue ----------------
class BookingRequestQueue {

    Queue<Reservation> queue;

    BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Booking request added for " + r.guestName);
    }

    boolean hasRequests() {
        return !queue.isEmpty();
    }

    Reservation getNextRequest() {
        return queue.poll();
    }

    void showRequests() {
        System.out.println("\nBooking Requests in Queue:");
        for (Reservation r : queue) {
            r.display();
        }
    }
}

// ---------------- Room ----------------
abstract class Room {

    String type;
    int beds;
    double price;

    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 2000);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}

// ---------------- Inventory ----------------
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
        int current = getAvailability(roomType);
        inventory.put(roomType, current - 1);
    }

    void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    void displayInventory() {
        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }
    }

    class SearchService {
        void searchAvailableRooms(RoomInventory inventory) {

            SingleRoom single = new SingleRoom();
            DoubleRoom doubleRoom = new DoubleRoom();
            SuiteRoom suite = new SuiteRoom();

            System.out.println("Available Rooms\n");

            if (inventory.getAvailability("Single Room") > 0) {
                single.displayDetails();
                System.out.println("Available: " + inventory.getAvailability("Single Room"));
                System.out.println();
            }

            if (inventory.getAvailability("Double Room") > 0) {
                doubleRoom.displayDetails();
                System.out.println("Available: " + inventory.getAvailability("Double Room"));
                System.out.println();
            }

            if (inventory.getAvailability("Suite Room") > 0) {
                suite.displayDetails();
                System.out.println("Available: " + inventory.getAvailability("Suite Room"));
            }
        }
    }
}

// ---------------- Booking Service ----------------
class BookingService {

    HashMap<String, Set<String>> allocatedRooms = new HashMap<>();
    Set<String> usedRoomIds = new HashSet<>();

    String generateRoomId(String roomType, int number) {
        return roomType.replace(" ", "") + "-" + number;
    }

    void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        int counter = 1;

        while (queue.hasRequests()) {

            Reservation r = queue.getNextRequest();
            String roomType = r.roomType;

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                String roomId = generateRoomId(roomType, counter++);

                usedRoomIds.add(roomId);

                allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                allocatedRooms.get(roomType).add(roomId);

                inventory.decreaseAvailability(roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + r.guestName);
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println();

            } else {
                System.out.println("No rooms available for " + roomType);
            }
        }
    }
}

// ---------------- UC7 Add-On Service ----------------
class AddOnService {

    String serviceName;
    double price;

    AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    void display() {
        System.out.println(serviceName + " - ₹" + price);
    }
}

class AddOnServiceManager {

    HashMap<String, List<AddOnService>> serviceMap = new HashMap<>();

    void addService(String reservationId, AddOnService service) {

        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added " + service.serviceName + " to " + reservationId);
    }

    void showServices(String reservationId) {

        System.out.println("\nServices for " + reservationId);

        List<AddOnService> list = serviceMap.get(reservationId);

        if (list == null || list.isEmpty()) {
            System.out.println("No services added.");
            return;
        }

        for (AddOnService s : list) {
            s.display();
        }
    }

    double calculateTotalCost(String reservationId) {

        List<AddOnService> list = serviceMap.get(reservationId);

        if (list == null) return 0;

        double total = 0;

        for (AddOnService s : list) {
            total += s.price;
        }

        return total;
    }
}

// ---------------- Main ----------------
public class BookMyStayApp {

    public static void main(String[] args) {
        useCase7();
    }

    public static void useCase7() {

        System.out.println("\nBook My Stay - Hotel Booking System v7.0\n");

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));

        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory);

        String reservationId = "SingleRoom-1";

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(reservationId, new AddOnService("WiFi", 500));
        manager.addService(reservationId, new AddOnService("Breakfast", 800));
        manager.addService(reservationId, new AddOnService("Spa", 1500));

        manager.showServices(reservationId);

        double total = manager.calculateTotalCost(reservationId);

        System.out.println("\nTotal Add-On Cost: ₹" + total);

        System.out.println("\nInventory (unchanged):");
        inventory.displayInventory();
    }
}