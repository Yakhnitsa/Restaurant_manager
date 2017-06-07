package com.yurets_y.order_manager.model;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;
import com.yurets_y.order_manager.bin.OrderMapWrapper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by Admin on 25.05.2017.
 */
public class MenuLoaderSaver {

    public List<Dish> loadMenuFromExcel(File file) throws IOException, InvalidFormatException {
        List<Dish> dishes = new ArrayList<>();
        XSSFWorkbook myExcelBook = new XSSFWorkbook(file);
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        int rowIndex = 0;
        XSSFRow row;
        Dish dish;
        do {
            row = myExcelSheet.getRow(rowIndex++);
            if (row == null) {
                System.out.println("null reference " + rowIndex);
                break;
            }
            dish = getDishFromRow(row);
            if (dish != null) {
                dishes.add(dish);
            }

        } while (row != null);

        myExcelBook.close();

        return dishes;
    }

    public void serializeMenu(List<Dish> menu, File file) throws IOException {
        try (
                OutputStream outSream = new FileOutputStream(file);
                ObjectOutputStream objOut = new ObjectOutputStream(outSream)) {
            objOut.writeObject(menu);
        }

    }

    public List<Dish> deserializeMenu(File file) throws IOException, ClassNotFoundException {
        try (
                InputStream innStream = new FileInputStream(file);
                ObjectInputStream objInn = new ObjectInputStream(innStream);
        ) {
            ArrayList<Dish> dishMenu = (ArrayList<Dish>) objInn.readObject();
            return dishMenu;
        }
    }

    public List<Dish> loadMenuFromXML(File file) {
        throw new RuntimeException("Метод недоделан");
    }

    public OrderMapWrapper loadOrderFromXML(File file) {
        throw new RuntimeException("Метод недоделан");
    }

    public OrderMapWrapper loadOrderFromExcel(File file) {
        throw new RuntimeException("Метод недоделан");
    }

    //Разбор строки XSSFRow и получение блюда из строки
    //TODO изменить на private
    public Dish getDishFromRow(Row row) {
        String type = "";
        String name = "";
        String days = "";
        String weight = "";
        double price = 0;
        if (row.getCell(0) == null) {
            return null;
        }
        if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING)
            type = row.getCell(0).getStringCellValue();
        if (row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
            name = row.getCell(1).getStringCellValue();
        if (row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING)
            days = row.getCell(2).getStringCellValue();
        if (row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING) {
            weight = row.getCell(3).getStringCellValue();
        } else {
            weight = String.valueOf(row.getCell(4).getNumericCellValue());
        }

        if (row.getCell(4) == null) {
            return null;
        }
        if (row.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC) {
            price = row.getCell(4).getNumericCellValue();
        } else {
            return null;
        }
        List<Day> daysList = Day.getDaysFromString(days);
        String description = String.format("масса %s дни: %s", weight, days);
        Dish dish = new Dish(type, name, description, daysList, price);
        return dish;
    }

}
