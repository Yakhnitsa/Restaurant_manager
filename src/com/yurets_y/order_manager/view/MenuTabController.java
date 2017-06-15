package com.yurets_y.order_manager.view;

import com.yurets_y.order_manager.bin.Dish;
import com.yurets_y.order_manager.util.CollectionsUtil;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Admin on 26.05.2017.
 */
public class MenuTabController {

    private Stage primaryStage;
    @FXML
    private TableView<Dish> dishes;
    @FXML
    private TableColumn<Dish,String> dishName;
    @FXML
    private TableColumn<Dish,String> dishDay;
    @FXML
    private TableColumn<Dish,Double> dishPrise;

    private RootViewController rootController;



    public Dish getSelectedDish(){
        return dishes.getSelectionModel().getSelectedItem();
    }

    public void setContent(RootViewController rootController, List<Dish> dishList){

        this.rootController = rootController;
        this.primaryStage = rootController.getPrimaryStage();
        initialize(dishList);

    }

    /**
     * Метод содержит все основные настройки контроллера
     * @param dishList
     */
    private void initialize(List<Dish> dishList){
        dishName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        dishDay.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDays().toString()));
        dishPrise.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        FilteredList<Dish> dishFilteredList = CollectionsUtil.getFilteredList(dishList,rootController.dayFilterProperty());
        dishes.setItems(dishFilteredList);

        dishes.setRowFactory(tv -> {
            TableRow<Dish> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    rootController.addDishToOrder();
                }
            });
            return row;
        });
    }


}
