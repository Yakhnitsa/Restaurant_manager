package com.yurets_y.order_manager;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.DishMenuFabric;
import com.yurets_y.order_manager.util.MessageManager;
import com.yurets_y.order_manager.view.RootViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by Admin on 25.05.2017.
 */
public class MainApp extends Application {
    private RootViewController rootViewController;
    private Stage primaryStage;
    private AnchorPane rootLayout;

    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler(MessageManager.getInstance());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Менеджер заказов");

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/RootView.fxml"));
        rootLayout = loader.load();
        this.rootViewController = loader.getController();
        rootViewController.setPrimaryStage(primaryStage);

        rootViewController.addOrderTab(Day.MONDAY);
        rootViewController.addOrderTab(Day.TUESDAY);
        rootViewController.addOrderTab(Day.WEDNESDAY);
        rootViewController.addOrderTab(Day.THURSDAY);
        rootViewController.addOrderTab(Day.FRIDAY);
        rootViewController.addOrderTab(Day.ANY_DAY);
        //Загрузка меню из сериализированного файла
        rootViewController.deserializeMenuOnStartup();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
//        rootViewController.loadStartMenu();

    }
}
