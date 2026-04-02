/**
 * BookMyStay Application
 * Hotel Booking Management System
 */

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
}