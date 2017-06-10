package tests.bin_tests;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;
import com.yurets_y.order_manager.bin.Order;
import com.yurets_y.order_manager.model.OrderSaverLoader;
import com.yurets_y.order_manager.util.OrderSerializer;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Admin on 07.06.2017.
 */
public class OrderSaverLoaderTest {
    public static void main(String[] args) throws IOException, JAXBException {
        serializeToXMLTest();

    }

    public static void saveToExcelTest() throws IOException {
        OrderSaverLoader orderManager = new OrderSaverLoader();
//        File file = new File("test_resources\\test.xlsx");
//        orderManager.saveOrderToExcel(file,getTestOrder());

        File tempFile = new File("test_resources\\OrderTemplate.xlsx");
        orderManager.saveOrderToExcel(tempFile, getTestOrder());
    }

    public static void serializationTest() throws IOException {
        File file = new File("serialized.ser");
//
//        Order testOrder = getTestOrder();
//
//        OrderSerializer.serilizeOrder(file,testOrder);

        Order deserializedOrder = OrderSerializer.deserializeOrder(file);
//        System.out.println(testOrder);
//        System.out.println(deserializedOrder);
//        //Проверка Enum:
//
//        Day oldDay = testOrder.getOrderMap().keySet().iterator().next();
        Day newDay = deserializedOrder.getOrderMap().keySet().iterator().next();

//        System.out.println(oldDay);
//        System.out.println(newDay);
        System.out.println(Day.FRIDAY == newDay);
    }

    public static void serializeToXMLTest() throws JAXBException {
        OrderSaverLoader manager = new OrderSaverLoader();
        Order order = getTestOrder();

        File file = new File("test_resources\\order.xml");
        manager.saveOrderToXML(file, order);

    }

    public static Order getTestOrder() {
        Order order = new Order();


        ArrayList<Dish> dishes1 = new ArrayList();
        List<Day> days = Arrays.asList(Day.FRIDAY, Day.MONDAY);
        dishes1.add(new Dish("супы", "Борщ", "", days, 19));
        dishes1.add(new Dish("салат", "Мимоза", "", days, 23));
        dishes1.add(new Dish("Мясо", "Котлета", "", days, 25));
        dishes1.add(new Dish("Мясо", "Котлета", "", days, 25));


        ArrayList<Dish> dishes2 = new ArrayList();
        dishes2.add(new Dish("супы", "Суп", "", days, 19));
        dishes2.add(new Dish("салат", "Греческий", "", days, 23));
        dishes2.add(new Dish("Мясо", "Бедро", "", days, 25));

        ArrayList<Dish> dishes3 = new ArrayList();
        dishes3.add(new Dish("супы", "Уха", "", days, 19));
        dishes3.add(new Dish("салат", "Оливье", "", days, 23));
        dishes3.add(new Dish("Мясо", "Ничего", "", days, 25));
        dishes3.add(new Dish("Мясо", "Ничего", "", days, 25));
        dishes3.add(new Dish("Мясо", "Ничего", "", days, 25));

        Map<Day, List<Dish>> orderMap = new LinkedHashMap<>();
        orderMap.put(Day.MONDAY, dishes1);
        orderMap.put(Day.TUESDAY, dishes2);
        orderMap.put(Day.WEDNESDAY, dishes3);

        order.setName("Яхница Юрий");
        order.setOrderMap(orderMap);

        return order;
    }
}
