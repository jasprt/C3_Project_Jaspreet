import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Restaurant {
    public LocalTime openingTime;
    public LocalTime closingTime;
    private String name;
    private String location;
    private List<Item> menu = new ArrayList<>();

    public Restaurant(String name, String location, LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.location = location;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public boolean isRestaurantOpen() {
        LocalTime timeNow = getCurrentTime();
        return timeNow.isAfter(openingTime) && timeNow.isBefore(closingTime);

    }

    public LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    public List<Item> getMenu() {
        if (isRestaurantOpen()) return this.menu;
        else {
            System.out.println("Restaurant is closed! Visit between: " + openingTime + " & " + closingTime);
            return Collections.emptyList();
        }
    }

    private Item findItemByName(String itemName) {
        for (Item item : menu) {
            if (item.getName().equals(itemName))
                return item;
        }
        return null;
    }

    public void addToMenu(String name, int price) {
        Item newItem = new Item(name, price);
        menu.add(newItem);
    }

    public void removeFromMenu(String itemName) throws itemNotFoundException {

        Item itemToBeRemoved = findItemByName(itemName);
        if (itemToBeRemoved == null)
            throw new itemNotFoundException(itemName);

        menu.remove(itemToBeRemoved);
    }

    public void displayDetails() {
        System.out.println("Restaurant:" + name + "\n"
                + "Location:" + location + "\n"
                + "Opening time:" + openingTime + "\n"
                + "Closing time:" + closingTime + "\n"
                + "Menu:" + "\n" + getMenu());

    }

    public String getName() {
        return name;
    }

    //>>>>>>>>>>>>>>>>TDD Feature Method<<<<<<<<<<<<<<<<<<<<<<<
    /**
     * TDD approached method call to get total Order Amount for
     * selected items from the restaurant menu
     *
     * @param itemList which contains items selected by user
     * @return sum of the price of each item in the itemList
     */

    protected int getOrderAmount(List<Item> itemList) {
        return itemList.stream().mapToInt(Item::getPrice).sum();
    }

    /**
     * TDD approached method call to get total Order Amount for
     * selected item names from the restaurant menu
     *
     * @param itemNames which contains string item names seperated by "," selected by user
     * @return sum of the price of total selected items
     */

    public int getOrderAmount(String itemNames) {

        //replace uneven spacing between item names separated by ,(comma)
        String processedItemNames = itemNames.replaceAll(", | , | ,", ",");

        List<String> itemNameList = Arrays.asList(processedItemNames.split(","));
        List<Item> itemList = itemNameList.stream().map(this::findItemByName).collect(Collectors.toList());

        return getOrderAmount(itemList);
    }
    //<<<<<<<<<<<<<<<<<<<<<<<TDD Feature Method>>>>>>>>>>>>>>>>

}
