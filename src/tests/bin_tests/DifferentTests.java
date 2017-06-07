package tests.bin_tests;

import com.yurets_y.order_manager.util.PropertiesManager;

import java.io.File;
import java.util.Properties;

/**
 * Created by Admin on 29.05.2017.
 */
public class DifferentTests {
    public static void main(String[] args) {
        File file = PropertiesManager.getInstance().getDefaultMenuFile();
        System.out.println(file);
    }
}
