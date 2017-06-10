package com.yurets_y.order_manager.view;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;
import com.yurets_y.order_manager.bin.Order;
import com.yurets_y.order_manager.model.MenuLoaderSaver;
import com.yurets_y.order_manager.model.OrderSaverLoader;
import com.yurets_y.order_manager.util.CollectionsUtil;
import com.yurets_y.order_manager.util.FileChooserDialog;
import com.yurets_y.order_manager.util.MessageManager;
import com.yurets_y.order_manager.util.PropertiesManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Admin on 25.05.2017.
 */
public class RootViewController {

    private Stage primaryStage;
    @FXML
    private TabPane menuTabs;
    @FXML
    private TabPane orderTabs;
    @FXML
    private CheckBox dayFilterCheckbox;

    private ObjectProperty<Day> dayFilter = new SimpleObjectProperty<>(Day.MONDAY);
    private List<Dish> dishMenu;

    private Map<Tab, MenuTabController> menuTabsMap = new HashMap<>();
    private Map<Tab, OrderTabController> orderTabsMap = new LinkedHashMap<>();

    private MenuLoaderSaver menuLoaderSaver = new MenuLoaderSaver();
    private OrderSaverLoader orderSaverLoader = new OrderSaverLoader();

    public Dish getSelectedDishFromMenu() {
        Tab selectedTab = menuTabs.getSelectionModel().getSelectedItem();
        MenuTabController currentTabController = menuTabsMap.get(selectedTab);

        return currentTabController != null ? currentTabController.getSelectedDish() : null;
    }

    private List<Dish> getSelectedDishFromOrder() {
        Tab selectedTab = orderTabs.getSelectionModel().getSelectedItem();
        OrderTabController currentTabController = orderTabsMap.get(selectedTab);
        return currentTabController.getSelectedDish();
    }

    public void addMenu(String title, List<Dish> dishList) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("MenuTab.fxml"));
            AnchorPane table = loader.load();
            MenuTabController controller = loader.getController();
            controller.setContent(this, dishList);
            Tab tab = new Tab(title);
            tab.setContent(table);

            menuTabsMap.put(tab, controller);
            menuTabs.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addOrderTab(Day day) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("OrderTab.fxml"));
            AnchorPane table = loader.load();
            OrderTabController controller = loader.getController();
            controller.setRootControllerAndDay(this, day);
            Tab tab = new Tab(day.toString());

            tab.setOnSelectionChanged(event -> {
                dayFilter.setValue(dayFilterCheckbox.isSelected() ? controller.getCurrentDay() : Day.ANY_DAY);
            });
            tab.setContent(table);

            orderTabsMap.put(tab, controller);
            orderTabs.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDishToOrder() {
        OrderTabController orderController = orderTabsMap.get(orderTabs.getSelectionModel().getSelectedItem());
        Dish selectedDish = getSelectedDishFromMenu();
        if (selectedDish != null) {
            orderController.addDish(selectedDish);

        } else {
            System.out.println("No selected item");
        }
    }

    public void removeDishFromOrder() {
        OrderTabController orderController = orderTabsMap.get(orderTabs.getSelectionModel().getSelectedItem());
        orderController.removeSelected();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void testMethod() {
        Tab selectedTab = menuTabs.getSelectionModel().getSelectedItem();
        MenuTabController currentTabController = menuTabsMap.get(selectedTab);
        if (currentTabController != null) {
            System.out.println(currentTabController.getSelectedDish());
        }
        dayFilter.setValue(Day.THURSDAY);
    }

    @FXML
    private void showOrder() {
        try {
            OrderInfoOverviewController.showOrder(getOrder(),this);
        } catch (IOException e) {
            e.printStackTrace();
            String message = "Ошибка отображения окна заказа";
            String contentText = "";
            MessageManager.getInstance().showErrorMessage(primaryStage,message,contentText);
        }
    }

    @FXML
    private void saveOrderToXML() {
        File defaultFile = PropertiesManager.getInstance().getDefaultOrderFile();
        File file = FileChooserDialog.getInstance().getXMLFileToSave(primaryStage, defaultFile);
        if (file == null) {
            return;
        }
        try {
            orderSaverLoader.saveOrderToXML(file, getOrder());
            String message = "Заказ успешно сохранен";
            String content = "Файл заказа находится по пути:\n" + file.getAbsolutePath();
            MessageManager.getInstance().showInfoMessage(primaryStage, message, content);
        }catch (JAXBException e) {
            String message = "Невозможно сохранить файл!";
            String contentText = "Возможно файл открыт в другом приложении или защищен от записи";
            MessageManager.getInstance().showErrorMessage(primaryStage, message, contentText);
            e.printStackTrace();
        }
    }

    @FXML
    void saveOrderToExcel() {
        File defaultFile = PropertiesManager.getInstance().getDefaultOrderFile();
        File file = FileChooserDialog.getInstance().getExelFileToSave(primaryStage, defaultFile);
        if (file == null) {
            return;
        }
        try {
            orderSaverLoader.saveOrderToExcel(file, getOrder());
            String message = "Заказ успешно сохранен";
            String content = "Файл заказа находится по пути:\n" + file.getAbsolutePath();
            MessageManager.getInstance().showInfoMessage(primaryStage, message, content);
        } catch (IOException e) {
            String message = "Невозможно сохранить файл!";
            String contentText = "Возможно файл открыт в другом приложении или защищен от записи";
            MessageManager.getInstance().showErrorMessage(primaryStage, message, contentText);
            e.printStackTrace();
        }
    }

    @FXML
    private void loadOrderFromXML() {
        File defaultFile = PropertiesManager.getInstance().getDefaultAddFolder();
        File file = FileChooserDialog.getInstance().getXMLFileToLoad(primaryStage,defaultFile);
        if((file != null)&&(file.exists())){
            try {
                orderSaverLoader.saveOrderToXML(file,getOrder());
                String message = "Заказ успешно сохранен в файл";
                String contentText = "Путь сохранения :\n" + file.getAbsolutePath();
                MessageManager.getInstance().showInfoMessage(primaryStage,message,contentText);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void loadOrderFromExcel() {

    }

    @FXML
    private void loadMenuFromExcel() {
        File defaultFile = PropertiesManager.getInstance().getDefaultMenuFile();
        File file = FileChooserDialog.getInstance().getExelFileToLoad(primaryStage, defaultFile);
        try {
            dishMenu = menuLoaderSaver.loadMenuFromExcel(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        loadMenu(dishMenu);

    }

    @FXML
    public void handleFilterCheckBoxClicked() {
        Tab currentTab = orderTabs.getSelectionModel().getSelectedItem();
        OrderTabController tabController = orderTabsMap.get(currentTab);
        if (dayFilterCheckbox.isSelected()) {
            dayFilter.setValue(tabController.getCurrentDay());
        } else {
            dayFilter.setValue(Day.ANY_DAY);
        }
    }

    @FXML
    public void handleExit() {
        String tytle = "Выход из приложения";
        String message = "Сохранить заказ в файле по умолчанию?";
        String contentText = "Нажмите \"ОК\" чтобы сохранить заказ в файле по умолчанию";
        ButtonType answer = MessageManager.getInstance().showConfirmMessage(primaryStage, message, tytle, contentText);
        if (answer == ButtonType.OK) {
            serializeMenuOnExit();
            serializeOrderOnExit();
            System.out.println("Сохранено");
        }
        //TODO Дописать код по сохранению заказа в файле по умолчанию
        System.exit(0);
    }

    public Day getDayFilter() {
        return dayFilter.get();
    }

    public ObjectProperty<Day> dayFilterProperty() {
        return dayFilter;
    }

    public void setDayFilter(Day dayFilter) {
        this.dayFilter.setValue(dayFilter);
    }

    private void loadMenu(List<Dish> menu) {
        Set<String> menuTypes = CollectionsUtil.getMenusSet(menu);
        for (String menuType : menuTypes) {
            List<Dish> dishes = CollectionsUtil.getMenuByType(menu, menuType);
            addMenu(menuType, dishes);
        }
    }



    public void loadBackupFiles(){
        deserializeMenuOnStartup();
        deserializeOrderOnStartup();
    }
    /**
     * Десериализация меню при запуске
     */
    private void deserializeMenuOnStartup() {
        File file = PropertiesManager.getInstance().getDefaultMenuFile();
        if ((file != null) && (file.exists())) {
            try {
                dishMenu = menuLoaderSaver.deserializeMenu(file);
                loadMenu(dishMenu);
            } catch (ClassNotFoundException | IOException e) {
                MessageManager.getInstance().showExceptionMessage(e, null);
            }
        }
    }
    /**
     * Сериализация меню при закрытии приложения
     */
    private void serializeMenuOnExit() {
        File file = PropertiesManager.getInstance().getDefaultMenuFile();
        if (dishMenu != null) {
            try {
                menuLoaderSaver.serializeMenu(dishMenu, file);
                System.out.println("сериализация прошла успешно");
            } catch (IOException e) {
                MessageManager.getInstance().showExceptionMessage(e, primaryStage);
            }
        }
    }

    /**
     *
     * @return
     */
    private void deserializeOrderOnStartup(){
        File file = PropertiesManager.getInstance().getDefaultOrderFile();
        if ((file != null) && (file.exists())) {
            try {
                Order order = orderSaverLoader.loadOrderFromXML(file);
                loadOrderToScene(order);
            } catch (JAXBException e) {
                MessageManager.getInstance().showExceptionMessage(e, null);
            }
        }
    }

    /**
     * Сериализация заказа при закрытии
     */
    private void serializeOrderOnExit(){
        File file = PropertiesManager.getInstance().getDefaultOrderFile();
        System.out.println(file);
        if(file != null){
            try {
                orderSaverLoader.saveOrderToXML(file, getOrder());
                System.out.println("Сериализация заказа прошла успешно");
            } catch (JAXBException e) {
                MessageManager.getInstance().showExceptionMessage(e, primaryStage);
            }
        }
    }
    /*
     * Загрузка заказа в окно заказа
     */
    private void loadOrderToScene(Order order){
        orderTabsMap.entrySet().forEach(entry ->{
            OrderTabController tabController = entry.getValue();
            Day day = entry.getValue().getCurrentDay();
            tabController.getDishList().addAll(order.getOrderMap().get(day));
                }
        );
    }

    Order getOrder() {
        Order order = new Order();
        //TODO Прописать присвоение имени заказу

        Map<Day, List<Dish>> orderMap = new LinkedHashMap<>();
        orderTabsMap.forEach((key, entry) -> {
            orderMap.put(entry.getCurrentDay(), entry.getDishList());
        });
        order.setOrderMap(orderMap);
        return order;
    }
}
