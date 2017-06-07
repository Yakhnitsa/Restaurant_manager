package com.yurets_y.order_manager.model;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;
import com.yurets_y.order_manager.bin.Order;
import com.yurets_y.order_manager.bin.OrderMapWrapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 25.05.2017.
 */
public class OrderSaverLoader {

    public void saveOrder(File file, OrderMapWrapper order){

    }

    public void saveOrderToExcel(File file, Order order) throws IOException {
        Workbook workbook;
        if(file.exists()){
            workbook = getOvereatenWorkbookFromOrder(file,order);
        }else{
            workbook  = getNewWorkbookFromOrder(order);
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
    }

    private Workbook getOvereatenWorkbookFromOrder(File file, Order order) throws IOException {
        Workbook workbook;
        try(
                FileInputStream fileInn = new FileInputStream(file);
                ){
            workbook = new XSSFWorkbook(fileInn);
        }

        Sheet sheet = workbook.getSheetAt(0) != null ? workbook.getSheetAt(0) : workbook.createSheet();
        final int rowCount = order.getOrderMap()
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().size())
                .max(Comparator.comparing(size -> size.intValue())).get();
        //Заполнение строки заголовков
        Row titleRow = sheet.getRow(1) != null ? sheet.getRow(1) : sheet.createRow(1);
        int i = 0;
        for(Day day: order.getOrderMap().keySet()){
            if(titleRow.getCell(i) != null){
                titleRow.getCell(i).setCellValue(day.toString());
            }else{
                titleRow.createCell(i).setCellValue(day.toString());
            }
            i++;
        }
        int rowIndex = 0;
        while(rowIndex <= rowCount){
            int columnIndex = 0;
            Row row = sheet.getRow(rowIndex+2) != null ?
                    sheet.getRow(rowIndex+2) : sheet.createRow(rowIndex+2);
            for(Map.Entry<Day,List<Dish>> entry : order.getOrderMap().entrySet()){
                String value = entry.getValue().size() > rowIndex ?
                        entry.getValue().get(rowIndex).getName(): "";
                if(row.getCell(columnIndex) !=null){
                    row.getCell(columnIndex).setCellValue(value);
                }else{
                    row.createCell(columnIndex).setCellValue(value);
                }
                columnIndex++;
            }
            rowIndex++;
        }
        return workbook;
    }

    //Получение новой книги из заказа
    private Workbook getNewWorkbookFromOrder(Order order){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        final int rowCount = order.getOrderMap()
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().size())
                .max(Comparator.comparing(size -> size.intValue())).get();
        //Заполнение строки заголовков
        Row titleRow = sheet.createRow(1);
        int i = 0;
        for(Day day: order.getOrderMap().keySet()){
            titleRow.createCell(i++).setCellValue(day.toString());
        }
        int rowIndex = 0;
        while(rowIndex <= rowCount){
            int columnIndex = 0;
            Row row = sheet.createRow(rowIndex+2);
            for(Map.Entry<Day,List<Dish>> entry : order.getOrderMap().entrySet()){
                String value = entry.getValue().size() > rowIndex ?
                        entry.getValue().get(rowIndex).getName(): "";
                row.createCell(columnIndex++).setCellValue(value);
            }
            rowIndex++;
        }
        return workbook;
    }

    public void saveOrderToXML(File file, Order order) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Order.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Маршаллируем и сохраняем XML в файл.
        m.marshal(order, file);
    }

    public Order loadOrderFromXML(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Order.class);
        Unmarshaller m = context.createUnmarshaller();
        return (Order)m.unmarshal(file);
    }

}
