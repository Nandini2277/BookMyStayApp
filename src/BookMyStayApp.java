/**
 * BookMyStay Application
 * Hotel Booking Management System
 */
import java.util.HashMap;

// Room Inventory class
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor initializes inventory
    RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");

        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType));
        }
    }
}

abstract class Room {

    String roomType;
    int beds;
    int size;
    double price;

    Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: $" + price);
    }
}


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


public class BookMyStayApp {

    public static void main(String[] args) {

        useCase1();
        useCase2();
        useCase3();

    }


    // USE CASE 1
    public static void useCase1() {

        System.out.println("=================================");
        System.out.println("Welcome to BookMyStay");
        System.out.println("Hotel Booking System v1.0");
        System.out.println("=================================");

    }


    // USE CASE 2
    public static void useCase2() {

        System.out.println("\n--- Available Room Types ---");

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display room details and availability
        single.displayRoomDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println();

        dbl.displayRoomDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println();

        suite.displayRoomDetails();
        System.out.println("Available: " + suiteAvailable);
        System.out.println();
    }
    public static void useCase3() {

        System.out.println("BookMyStay - Version 3.0");
        System.out.println("Centralized Room Inventory\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display inventory
        inventory.displayInventory();

        System.out.println("\nChecking availability for Double Room:");
        System.out.println("Available: " + inventory.getAvailability("Double Room"));

        // Update inventory
        inventory.updateAvailability("Double Room", 2);

        System.out.println("\nInventory after update:");
        inventory.displayInventory();
    }
}