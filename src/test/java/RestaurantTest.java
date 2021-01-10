import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class RestaurantTest {
    Restaurant restaurant;
    List<Item> itemList;

    //REFACTOR ALL THE REPEATED LINES OF CODE
    private void createDummyRestaurantWithItems() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = new Restaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
        restaurant.displayDetails();
        restaurant.getMenu().forEach(Item::toString);
        itemList = restaurant.getMenu();
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        createDummyRestaurantWithItems();
        //Change restaurant timing to be open all the time
        restaurant.openingTime = LocalTime.MIN;
        restaurant.closingTime = LocalTime.MAX;

        boolean isOpen = restaurant.isRestaurantOpen();

        assertTrue(isOpen);
        assertEquals(LocalTime.MIN, restaurant.openingTime);
        assertEquals(LocalTime.MAX, restaurant.closingTime);
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {

        Restaurant restaurant = Mockito.mock(Restaurant.class);
        Mockito.doReturn(LocalTime.MAX).when(restaurant).getCurrentTime();

        boolean isOpen = restaurant.isRestaurantOpen();

        assertFalse(isOpen);
        Mockito.verify(restaurant, Mockito.times(1)).isRestaurantOpen();

    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1() {
        createDummyRestaurantWithItems();
        int initialMenuSize = restaurant.getMenu().size();

        restaurant.addToMenu("Sizzling brownie", 319);

        assertEquals(initialMenuSize + 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        createDummyRestaurantWithItems();
        int initialMenuSize = restaurant.getMenu().size();

        restaurant.removeFromMenu("Vegetable lasagne");

        assertEquals(initialMenuSize - 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        createDummyRestaurantWithItems();

        assertThrows(itemNotFoundException.class,
                () -> restaurant.removeFromMenu("French fries"));
    }

    @Test
    public void menu_returns_empty_list_if_restaurant_is_not_open() {

        Restaurant restaurant = new Restaurant("","",LocalTime.MIN,LocalTime.MIN);
        restaurant = Mockito.spy(restaurant);

        int listSize = restaurant.getMenu().size();

        assertEquals(0, listSize);
        Mockito.verify(restaurant, Mockito.times(1)).getMenu();
    }

    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>ORDER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /*This module needs to be implemented the TDD way
    i.e.
            1. Write failing test cases for the requirement.
            2. Start actual coding on the failing test case.
            3. Check if test case passes after coding the module method.
     */

    @Test
    public void return_expected_order_amount_for_all_items_from_a_restaurant_menu() {
        createDummyRestaurantWithItems();
        List<Item> itemList = restaurant.getMenu();

        int orderAmount = restaurant.getOrderAmount(itemList);

        assertEquals(388, orderAmount);
    }

    @Test
    public void return_expected_order_amount_for_selected_items_from_a_restaurant_menu() {
        createDummyRestaurantWithItems();

        int itemPrice = 500;
        String itemName = "Pizza";
        Item item = new Item(itemName, itemPrice);
        itemList = restaurant.getMenu();
        itemList.add(item);

        //Get a sub list of selected items from the existing item list in the menu
        //@fromindex(inclusive) 2 to @toIndex(exclusive) itemList.size() to fetch the last added item
        List<Item> lastSelectedItemList = restaurant.getMenu().subList(2, itemList.size());
        int orderAmount = restaurant.getOrderAmount(lastSelectedItemList);

        assertEquals(500, orderAmount);

    }

    @Test
    public void return_expected_order_amount_for_last_entered_item_name_in_the_restaurant_menu() {
        createDummyRestaurantWithItems();

        int itemPrice = 500;
        String itemName = "Pizza";
        Item item = new Item(itemName, itemPrice);
        itemList.add(item);

        int orderAmount = restaurant.getOrderAmount(itemName);

        assertEquals(itemPrice, orderAmount);

    }

    @Test
    public void return_expected_order_amount_for_all_item_name_in_the_restaurant_menu() {
        createDummyRestaurantWithItems();

        String orderedItemNames = "Sweet corn soup , Vegetable lasagne";
        int orderAmount = restaurant.getOrderAmount(orderedItemNames);

        assertEquals(388, orderAmount);

    }

    //<<<<<<<<<<<<<<<<<<<<<<<ORDER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}