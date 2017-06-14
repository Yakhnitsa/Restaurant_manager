package com.yurets_y.order_manager.view;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

/**
 * Контроллер вкладки заказа
 *
 */
public class OrderTabController {

    private Stage primaryStage;

    private Day currentDay;

    private RootViewController rootController;
    @FXML
    private TableView<Dish> dishes;
    @FXML
    private TableColumn<Dish, String> dishType;
    @FXML
    private TableColumn<Dish, String> dishName;
    @FXML
    private TableColumn<Dish, Double> dishPrise;
    @FXML
    private Label totalPrise;

    private ObservableList<Dish> dishList;

    @FXML
    public void initialize() {
        dishList = FXCollections.observableArrayList();
        dishes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dishes.setItems(dishList);
        dishType.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getType()));
        dishName.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getName()));
        dishPrise.setCellValueFactory(celldata -> new SimpleDoubleProperty(celldata.getValue().getPrice()).asObject());

        DoubleProperty totalProperty = getDoubleProperty();

        totalPrise.textProperty().bind(totalProperty.asString());
    }

    /*
     * получение обновляемого объекта с общей ценой заказа
     */
    private DoubleProperty getDoubleProperty() {
        DoubleProperty totalProperty = new SimpleDoubleProperty(0);
        dishList.addListener((ListChangeListener.Change<? extends Dish> change) ->
        {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Dish p : change.getAddedSubList()) {
                        totalProperty.set(totalProperty.get() + p.getPrice());
                    }
                } else if (change.wasRemoved()) {
                    for (Dish p : change.getRemoved()) {
                        totalProperty.set(totalProperty.get() - p.getPrice());
                    }
                }
            }
        });
        return totalProperty;
    }

    List<Dish> getSelectedDish() {
        return dishes.getSelectionModel().getSelectedItems();
    }

    void addDish(Dish dish) {
        dishList.add(dish);
    }

    public void removeSelected() {
        if (getSelectedDish() != null) {
            dishList.removeAll(getSelectedDish());
        }

    }

    void setRootControllerAndDay(RootViewController controller,Day day) {

        this.rootController = controller;
        this.primaryStage = controller.getPrimaryStage();
        this.currentDay = day;
    }

    public void viewInfo() {
        System.out.println(getSelectedDish());
    }

    Day getCurrentDay() {
        return currentDay;
    }

    List<Dish> getDishList() {
        return dishList;
    }
    @FXML
    private void handleSaveButton(){
        rootController.saveOrderToExcel();
    }

}
