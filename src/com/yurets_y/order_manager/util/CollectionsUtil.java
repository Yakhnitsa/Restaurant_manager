package com.yurets_y.order_manager.util;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Admin on 30.05.2017.
 * Утилитарный клас по работе с коллекциями
 */
public class CollectionsUtil {

    public static Set<String> getMenusSet(List<Dish> menu){
       return menu.stream().map(dish -> dish.getType()).collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public static List<Dish> getMenuByType(List<Dish> menu,String dishType){
        return menu.stream().filter(dish-> dish.getType().equals(dishType)).collect(Collectors.toList());
    }

    /**
     * Пролучение фильтрующего списка из списка и наблюдаемого значения
     * @param list - Коллекция исходных данных
     * @param filter - Объект-фильтр
     * @return Коллекция с фильтром по дням
     */
    public static FilteredList<Dish> getFilteredList(List<Dish> list, ObjectProperty<Day> filter){
        ObservableList<Dish> observList = FXCollections.observableArrayList(list);
        FilteredList<Dish> filteredData = new FilteredList<>(observList, p -> true);
        filter.addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(dish -> {
                // If filter text is empty, display all persons.
                if ((newValue == null)||(newValue == Day.ANY_DAY)) {
                    return true;
                }
                if (dish.getDays().contains(Day.ANY_DAY)) {
                    return true; // фильтр для поля все дни
                } else if (dish.getDays().contains(newValue)) {
                    return true; // фильтр определенных дней
                }
                return false; // Does not match.
            });
        });
        return filteredData;
    }
}
