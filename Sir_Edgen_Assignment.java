import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Product> list = new ArrayList<>();
    static final String FILE = "products.json";

    static class Product {
        int id;
        String name;
        double price;

        Product(int pamagat, String pangalan, double presyo) {
            id = pamagat;
            name = pangalan;
            price = presyo;
        }
    }

    public static void main(String[] args) {
        load();

        while (true) {
            System.out.println("1.Add Product");
            System.out.println("2.View Product");
            System.out.println("3.Update Product");
            System.out.println("4.Delete Product");
            System.out.println("5.Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            System.out.println();

            if (choice == 1) add();
            else if (choice == 2) view();
            else if (choice == 3) update();
            else if (choice == 4) delete();
            else if (choice == 5) break;
        }
    }

    static void add() {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (idExists(id)) {
            System.out.println("ID already exists!\n");
            return;
        }

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Price: ");
        double price = sc.nextDouble();

        list.add(new Product(id, name, price));
        save();
        System.out.println("Added!");
        System.out.println();
    }

    // nagtanong din po sa AI pano maayos table hehe
    static void view() {
        System.out.printf("%-5s | %-20s | %-10s\n", "ID", "NAME", "PRICE");
        System.out.println("--------------------------------------------------");
        for (Product p : list) {
            System.out.printf("%-5d | %-20s | %-10.2f\n", p.id, p.name, p.price);
        }
        System.out.println();
    }

    static void update() {
        System.out.print("Enter ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Product p : list) {
            if (p.id == id) {
                System.out.print("New name: ");
                p.name = sc.nextLine();

                System.out.print("New price: ");
                p.price = sc.nextDouble();

                save();
                System.out.println("Updated!");
                System.out.println();
                return;
            }
        }

        System.out.println("Not found!");
        System.out.println();
    }

    static void delete() {
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();

        boolean found = false;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) {
                list.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            save();
            System.out.println("Deleted!");
            System.out.println();
        } else {
            System.out.println("Not found!");
            System.out.println();
        }
    }

    // nalito nagtanong din kay AI
    static boolean idExists(int id) {
        for (Product p : list) {
            if (p.id == id) return true;
        }
        return false;
    }

    // AI  po sir d po alam JSON
    static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            writer.write("[\n");
            for (int i = 0; i < list.size(); i++) {
                Product p = list.get(i);
                writer.write("  {\n");
                writer.write("    \"id\": " + p.id + ",\n");
                writer.write("    \"name\": \"" + p.name + "\",\n");
                writer.write("    \"price\": " + p.price + "\n");
                writer.write("  }");
                if (i < list.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Error saving file");
        }
    }

    // AI din po sir Android lng nagpatulong kay AI part din po sya ng JSON
    static void load() {
        list.clear();
        File file = new File(FILE);
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(FILE)) {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) sb.append((char) c);

            String content = sb.toString().trim();
            if (content.isEmpty()) return;

            String[] products = content.replace("[", "").replace("]", "").split("\\},");
            for (String prod : products) {
                prod = prod.replace("{", "").replace("}", "").trim();
                String[] lines = prod.split(",");
                int id = 0;
                String name = "";
                double price = 0;
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("\"id\"")) id = Integer.parseInt(line.split(":")[1].trim());
                    else if (line.startsWith("\"name\"")) name = line.split(":")[1].trim().replace("\"", "");
                    else if (line.startsWith("\"price\"")) price = Double.parseDouble(line.split(":")[1].trim());
                }
                list.add(new Product(id, name, price));
            }

        } catch (IOException e) {
            System.out.println("Error loading file");
        }
    }
}