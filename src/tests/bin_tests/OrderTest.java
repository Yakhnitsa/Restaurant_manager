package tests.bin_tests;

import com.yurets_y.order_manager.bin.Day;

import java.util.List;

/**
 * Created by Admin on 25.05.2017.
 */
public class OrderTest {
    public static void main(String[] args){
        Day day = Day.getDayFromString("срд");
        System.out.println(day);
        List<Day> days = Day.getDaysFromString("ср");
        System.out.println(days);
    }
}
