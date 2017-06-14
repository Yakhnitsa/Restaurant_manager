package com.yurets_y.order_manager.view;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;
import com.yurets_y.order_manager.bin.Order;
import com.yurets_y.order_manager.model.MenuSaverLoader;
import com.yurets_y.order_manager.model.OrderSaverLoader;
import com.yurets_y.order_manager.util.CollectionsUtil;
import com.yurets_y.order_manager.util.FileChooserDialog;
import com.yurets_y.order_manager.util.MessageManager;
import com.yurets_y.order_manager.util.PropertiesManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Главный контроллер приложения
 */
public class RootViewController {

    private Stage primaryStage;
    @FXML
    private TabPane menuTabs;
    @FXML
    private TabPane orderTabs;
    @FXML
    private CheckBox dayFilterCheckbox;
    @FXML
    private Button changeTitleButton;
    @FXML
    private TextField titleTextfield;

    private ObjectProperty<Day> dayFilter = new SimpleObjectProperty<>(Day.MONDAY);
    private List<Dish> dishMenu;

    private Map<Tab, MenuTabController> menuTabsMap = new HashMap<>();
    private Map<Tab, OrderTabController> orderTabsMap = new LinkedHashMap<>();

    private MenuSaverLoader menuSaverLoader = new MenuSaverLoader();
    private OrderSaverLoader orderSaverLoader = new OrderSaverLoader();

    @FXML
    private void initialize(){
        changeTitleButton.setOnAction(event -> {
            titleTextfield.setDisable(false);
        });
        titleTextfield.setDisable(true);
        titleTextfield.setOnAction(event ->{
            titleTextfield.setDisable(true);
                }
        );
    }

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
            String title = "Coхранение заказа";
            String content = "Вы желаете открыть файл заказа:\n" + file.getAbsolutePath();
            ButtonType answer = MessageManager.getInstance().showConfirmMessage(primaryStage,title, message, content);
            if(answer == ButtonType.OK){
                openFileInDesktop(file);
            }
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
                Order order = orderSaverLoader.loadOrderFromXML(file);
                boolean sucess = loadOrderToScene(order);
                if(sucess){
                    String message = "Заказ успешно загружен";
                    MessageManager.getInstance().showInfoMessage(primaryStage,message,"");
                }

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
            dishMenu = menuSaverLoader.loadMenuFromExcel(file);
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
                dishMenu = menuSaverLoader.loadMenuFromXML(file);
                loadMenu(dishMenu);
            } catch (JAXBException e) {
                dishMenu = new ArrayList<>();
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
                menuSaverLoader.saveMenuToXML(dishMenu, file);
                System.out.println("сериализация меню прошла успешно");
            } catch (JAXBException e) {
                MessageManager.getInstance().showExceptionMessage(e, primaryStage);
            }
        }
    }

    /**
     *
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
     * Загрузка заказа в окно заказа и проверка на наличие блюд в меню
     * return false если загрузка прошла не совсем успешно
     */
    private boolean loadOrderToScene(Order order){
        StringBuilder sb = new StringBuilder();
        titleTextfield.setText(order.getName());
        orderTabsMap.entrySet().forEach(entry ->{
            OrderTabController tabController = entry.getValue();
            Day day = entry.getValue().getCurrentDay();
            tabController.getDishList().clear();
            List<Dish> dishes = order.getOrderMap().get(day);
            //проверка каждого блюда на соответствие меню:
                dishes.forEach(dish ->{
                    if(dishMenu.contains(dish)){
                        tabController.getDishList().add(dish);
                    }
                    else{
                        sb.append(dish.getName()).append(System.lineSeparator());
                    }
                });
            });
        String dishScroll = sb.toString();
        if(!dishScroll.equals("")){
            String message = "Несоответствие меню и текущего заказа";
            String title = "Ошибка при загрузке заказа";
            MessageManager.getInstance().showTextAreaMessage(primaryStage,title,message,dishScroll);
            return false;
        }
        return true;
    }

    private void openFileInDesktop(File file){
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            desktop.open(file);
        } catch (IOException ioe) {
            MessageManager.getInstance().showExceptionMessage(ioe,null);
            ioe.printStackTrace();
        }
    }

    Order getOrder() {
        Order order = new Order();
        order.setName(titleTextfield.getText());

        Map<Day, List<Dish>> orderMap = new LinkedHashMap<>();
        orderTabsMap.forEach((key, entry) -> {
            orderMap.put(entry.getCurrentDay(), entry.getDishList());
        });
        order.setOrderMap(orderMap);
        return order;
    }
}
