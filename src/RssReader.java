import org.jsoup.Jsoup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class RssReader {

    private static final String DATA_FILE = "data.txt";
    private static final int MAX_ITEMS = 5;
    private static List<String> names = new ArrayList<>();
    private static List<String> urls = new ArrayList<>();
    private static List<String> rssUrls = new ArrayList<>();

    public static void main(String[] args) {
        loadWebsites();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("[1] Show Updates\n[2] Add URL\n[3] Remove URL\n[4] Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                    case 1:
                        showUpdates();
                        break;
                    case 2:
                        addUrl(scanner);
                        break;
                    case 3:
                        removeUrl(scanner);
                        break;
                    case 4:
                        running = false;
                        saveWebsites();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            scanner.close();
        }

    private static void loadWebsites() {
        try (Scanner fileScanner = new Scanner(new File(DATA_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    names.add(parts[0]);
                    urls.add(parts[1]);
                    rssUrls.add(parts[2]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading websites: " + e.getMessage());
        }
    }


    private static void showUpdates() {
        System.out.println("[0] All Websites");
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < urls.size(); i++) {
            String name = names.get(i);
            System.out.println("["+(i+1)+"] " + name);
        }
        System.out.println("Enter -1 to return");
        int choice = scanner.nextInt();
        while (choice>names.size() || choice<0){
            System.out.println("Enter a valid number!");
            choice=scanner.nextInt();
        }
        if (choice == -1)
            return;
        else if (choice == 0) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                String rssUrl = rssUrls.get(i);

                System.out.println("Updates from " + name + ":");
                List<String> recentItems = getRecentItemsFromRss(rssUrl);
                for (String item : recentItems) {
                    System.out.println("- " + item);
                }
                System.out.println();
            }
        }
        else{
            String name = names.get(choice-1);
            String rssUrl = rssUrls.get(choice-1);

            System.out.println("Updates from " + name + ":");
            List<String> recentItems = getRecentItemsFromRss(rssUrl);
            for (String item : recentItems) {
                System.out.println("- " + item);
            }
            System.out.println();
    }
    }

    private static List<String> getRecentItemsFromRss(String rssUrl) {
        List<String> recentItems = new ArrayList<>();
        try {
            URL url = new URL(rssUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(connection.getInputStream());
            doc.getDocumentElement().normalize();

            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < Math.min(itemList.getLength(), MAX_ITEMS); i++) {
                String title = itemList.item(i).getChildNodes().item(1).getTextContent();
                recentItems.add(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recentItems;
    }


    private static void addUrl(Scanner scanner) {
        System.out.print("Enter the website URL: ");
        String url = scanner.nextLine();
        if (urls.contains(url))
            System.out.println(url + " already exists");
        else {
            try {
                String name = Jsoup.connect(url).get().title();
                String rssUrl = Jsoup.connect(url).get().select("link[type=application/rss+xml]").attr("abs:href");

                if (!rssUrl.isEmpty()) {
                    names.add(name);
                    urls.add(url);
                    rssUrls.add(rssUrl);
                    System.out.println("Website added successfully!");
                } else {
                    System.out.println("RSS feed not found for the given URL.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while trying to retrieve the website information: " + e.getMessage());
            }
        }}



    private static void removeUrl(Scanner scanner) {
        System.out.print("Enter the website URL to remove: ");
        String nameToRemove = scanner.nextLine();

        int index = urls.indexOf(nameToRemove);
        if (index != -1) {
            names.remove(index);
            urls.remove(index);
            rssUrls.remove(index);
            System.out.println("Website removed successfully!");
        } else {
            System.out.println("Website not found!");
        }
    }


    private static void saveWebsites() {
        try (PrintWriter writer = new PrintWriter(new File(DATA_FILE))) {
            for (int i = 0; i < names.size(); i++) {
                writer.println(names.get(i) + ";" + urls.get(i) + ";" + rssUrls.get(i));
            }
            System.out.println("Websites saved successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Error saving websites: " + e.getMessage());
        }
    }

    }
