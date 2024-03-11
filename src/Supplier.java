import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Supplier implements Runnable {
    private static AtomicInteger to_supply;
    private final Controller controller;
    private final int id;

    public static void set_max_supply(int to_supply) {
        Supplier.to_supply = new AtomicInteger(to_supply);
    }

    public Supplier(Controller controller, int id) {
        this.controller = controller;
        this.id = id;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (to_supply.getAndDecrement() > 0) {
            try {
                controller.full_sem.acquire();
                controller.occupied_sem.acquire();

                Random random = new Random();
                Item item = new Item(random.nextInt(0, 100));
                controller.items.add(item);
                System.out.printf("\u001B[33m" + "Supplier #%d adds new item %d\n" + "\u001B[0m", this.id, item.getId());

                controller.occupied_sem.release();
                controller.empty_sem.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("\u001B[35m" + "Supplier #" + this.id + " finished working" + "\u001B[0m");
    }
}

