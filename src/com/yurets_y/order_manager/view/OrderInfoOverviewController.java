package com.yurets_y.order_manager.view;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;
import com.yurets_y.order_manager.bin.Order;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 04.06.2017.
 */
public class OrderInfoOverviewController {
    private RootViewController rootViewController;
    private Order order;
    @FXML
    private BorderPane rootPane;

    @FXML
    private TableView<Dish> monday;
    @FXML
    private TableColumn<Dish,String> mondayColumn;
    @FXML
    private TableView<Dish> tuesday;
    @FXML
    private TableColumn<Dish,String> tuesdayColumn;
    @FXML
    private TableView<Dish> wednesday;
    @FXML
    private TableColumn<Dish,String> wednesdayColumn;
    @FXML
    private TableView<Dish> thrirsday;
    @FXML
    private TableColumn<Dish,String> thirsdayColumn;
    @FXML
    private TableView<Dish> friday;
    @FXML
    private TableColumn<Dish,String> fridayColumn;

    public static void showOrder(Order order, RootViewController rootViewController) throws IOException {
        FXMLLoader loader = new FXMLLoader(OrderInfoOverviewController.class.getResource("OrderInfoOverview.fxml"));
        BorderPane rootPane = loader.load();
        OrderInfoOverviewController controller = loader.getController();
        controller.rootPane = rootPane;
        controller.rootViewController = rootViewController;
        controller.order = order;

        controller.initializeData();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Birthday Statistics");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(rootViewController.getPrimaryStage());
        Scene scene = new Scene(rootPane);
        dialogStage.setScene(scene);
        dialogStage.show();
    }
    //TODO реализовать в следующей версии
    private static TableView getNewTable(String day, List<Dish> dishList){
        TableView tableView = new TableView();
        tableView.setItems(FXCollections.observableArrayList(dishList));
        TableColumn tableColumn = new TableColumn(day);
        return null;
    }

    @FXML
    private void handleSave(){
        rootViewController.saveOrderToExcel();
    }

    private void initializeData(){
        Map<Day,List<Dish>> orderMap = order.getOrderMap();
        setContentInTableView(monday,mondayColumn,Day.MONDAY.toString(),orderMap.get(Day.MONDAY));
        setContentInTableView(tuesday,tuesdayColumn,Day.TUESDAY.toString(),orderMap.get(Day.TUESDAY));
        setContentInTableView(wednesday,wednesdayColumn,Day.WEDNESDAY.toString(),orderMap.get(Day.WEDNESDAY));
        setContentInTableView(thrirsday,thirsdayColumn,Day.THURSDAY.toString(),orderMap.get(Day.THURSDAY));
        setContentInTableView(friday,fridayColumn,Day.FRIDAY.toString(),orderMap.get(Day.FRIDAY));
    }

    private void setContentInTableView(TableView<Dish> view, TableColumn<Dish,String> column, String day, List<Dish> dishes){
        view.setItems(FXCollections.observableArrayList(dishes));
        column.setText(day);
        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
    }

}
