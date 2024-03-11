import java.util.concurrent.atomic.AtomicInteger;

public class Receiver implements Runnable {
    private static AtomicInteger to_receive;
    private final Controller controller;
    private final int id;

    public static void set_max_receive(int to_receive) {
        Receiver.to_receive = new AtomicInteger(to_receive);
    }

    public Receiver(Controller controller, int id) {
        this.controller = controller;
        this.id = id;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (to_receive.getAndDecrement() > 0) {
            try {
                controller.empty_sem.acquire();
                controller.occupied_sem.acquire();

                Item item = controller.items.removeFirst();
                System.out.printf("\u001B[36m" + "Receiver #%d takes item %d\n" + "\u001B[0m", this.id, item.getId());

                controller.occupied_sem.release();
                controller.full_sem.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("\u001B[31m" + "Receiver #" + this.id + " finished working" + "\u001B[0m");
    }
}
