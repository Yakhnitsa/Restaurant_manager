package tests.bin_tests.model_tests;

import com.yurets_y.order_manager.bin.Dish;
import com.yurets_y.order_manager.model.MenuSaverLoader;
import com.yurets_y.order_manager.util.CollectionsUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Admin on 30.05.2017.
 */
public class LoaderTest {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        MenuSaverLoader menuSaverLoader = new MenuSaverLoader();
        File excelFile = new File("res/files/Menu2.xlsx");
        List<Dish> dishes = menuSaverLoader.loadMenuFromExcel(excelFile);
        Set<String> dishTypes = CollectionsUtil.getMenusSet(dishes);
        System.out.println(dishTypes);
        List<Dish> salats = CollectionsUtil.getMenuByType(dishes,dishTypes.iterator().next());
        salats.forEach(salat -> System.out.println(salat.getDays()));

    }
    public static Row getTestedRow(){
        Workbook myExcelBook = new XSSFWorkbook();
        Sheet myExcelSheet = myExcelBook.createSheet();
        Row row = myExcelSheet.createRow(0);
        row.createCell(0).setCellValue("Первые блюда");
        row.createCell(1).setCellValue("Борщ");
        row.createCell(2).setCellValue("все");
        row.createCell(3).setCellValue("150");
        row.createCell(4).setCellValue(20.50);

        return row;
    }
}
