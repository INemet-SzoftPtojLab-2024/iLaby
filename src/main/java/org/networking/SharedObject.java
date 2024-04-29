package main.java.org.networking;

public class SharedObject {
    boolean isReady = false;

    synchronized public void waitForNotification() throws InterruptedException {
        while (!isReady) {
            wait(); // Releases lock and waits for notification
        }
        System.out.println("Notification received!");
    }

    synchronized public void sendNotification() {
        isReady = true;
        notify(); // Wakes up one waiting thread
    }
}
