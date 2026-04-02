import java.util.*;

// ---------------- Reservation ----------------
class Reservation {

    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------------- Room Inventory ----------------
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    // 🔒 Critical Section (Thread-Safe)
    synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " allocating " + roomType);

            inventory.put(roomType, available - 1);

            return true;
        } else {
            return false;
        }
    }

    void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }
    }
}

// ---------------- Booking Queue ----------------
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    // 🔒 Synchronized enqueue
    synchronized void addReservation(Reservation r) {
        queue.add(r);
    }

    // 🔒 Synchronized dequeue
    synchronized Reservation getNextReservation() {
        return queue.poll();
    }
}

// ---------------- Booking Processor (Thread) ----------------
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation r;

            // 🔒 Critical section for queue access
            synchronized (queue) {
                r = queue.getNextReservation();
            }

            if (r == null) {
                break; // No more requests
            }

            // 🔒 Critical section for inventory update
            boolean success = inventory.allocateRoom(r.roomType);

            if (success) {
                System.out.println(getName() + " → Booking CONFIRMED for " + r.guestName);
            } else {
                System.out.println(getName() + " → Booking FAILED (No availability) for " + r.guestName);
            }

            // Simulate processing delay (to expose race conditions if unsynchronized)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class ConcurrentBookingApp {

    public static void main(String[] args) {

        System.out.println("UC11 - Concurrent Booking Simulation\n");

        // Shared resources
        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple guest requests
        queue.addReservation(new Reservation("Alice", "Single Room"));
        queue.addReservation(new Reservation("Bob", "Single Room"));
        queue.addReservation(new Reservation("Charlie", "Single Room")); // should fail
        queue.addReservation(new Reservation("David", "Double Room"));
        queue.addReservation(new Reservation("Eve", "Double Room"));     // should fail

        // Multiple threads (guests processing concurrently)
        Thread t1 = new BookingProcessor("Thread-1", queue, inventory);
        Thread t2 = new BookingProcessor("Thread-2", queue, inventory);
        Thread t3 = new BookingProcessor("Thread-3", queue, inventory);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final system state
        inventory.displayInventory();
    }
}