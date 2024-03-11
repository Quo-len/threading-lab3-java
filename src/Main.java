import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int num_receivers = 2;
        int num_suppliers = 3;
        int vault_size = 3;
        int total_items = 10;

        Controller controller = new Controller(vault_size);
        Receiver.set_max_receive(total_items);
        Supplier.set_max_supply(total_items);

        for (int i = 0; i < num_suppliers; i++) {
            new Supplier(controller, i + 1);
        }

        for (int i = 0; i < num_receivers; i++) {
            new Receiver(controller, i + 1);
        }

    }
}