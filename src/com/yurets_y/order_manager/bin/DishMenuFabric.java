package com.yurets_y.order_manager.bin;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 26.05.2017.
 */
public class DishMenuFabric {
    private static DishMenuFabric instance;

    private DishMenuFabric() {
    }

    public static DishMenuFabric getInstance() {
        if(instance == null){
            instance = new DishMenuFabric();
        }
        return instance;
    }

    public List<Dish> getMeetMenu() {
        List<Dish> dishes = Arrays.asList(
//                new Dish("Курица по мексикански",Day.ANY_DAY,35),
//                new Dish("Котлета по киевски",Day.ANY_DAY,30),
//                new Dish("Колбаска куриная",Day.ANY_DAY,25),
//                new Dish("Мясное рагу",Day.ANY_DAY,19),
//                new Dish("Бифштекс с яйцом",Day.ANY_DAY,25),
//                new Dish("Куриное бедрышко",Day.ANY_DAY,28),
//                new Dish("Куриный рулет в меду",Day.ANY_DAY,38)
        );
        return dishes;
    }

    public List<Dish> getSoupMenu() {
        List<Dish> dishes = Arrays.asList(
//                new Dish("Борщ",Day.ANY_DAY,25),
//                new Dish("Солянка",Day.ANY_DAY,15),
//                new Dish("Уха",Day.ANY_DAY,23),
//                new Dish("Зеленый борщ",Day.ANY_DAY,28)
        );
        return dishes;
    }

    public List<Dish> getGarnirMenu() {
        List<Dish> dishes = Arrays.asList(
//                new Dish("Картошка по селянски",Day.ANY_DAY,15),
//                new Dish("Пюре",Day.ANY_DAY,18),
//                new Dish("Булгур",Day.ANY_DAY,22),
//                new Dish("Гречка",Day.ANY_DAY,20),
//                new Dish("Плов",Day.ANY_DAY,25)
        );
        return dishes;
    }

    public List<Dish> getSalatMenu() {
        List<Dish> dishes = Arrays.asList(
//                new Dish("Цезарь",Day.ANY_DAY,35),
//                new Dish("Шуба",Day.ANY_DAY,30),
//                new Dish("Мимоза",Day.ANY_DAY,25),
//                new Dish("Днестровский",Day.ANY_DAY,19),
//                new Dish("Весенний",Day.ANY_DAY,25),
//                new Dish("Крабовый салат",Day.ANY_DAY,28)
        );
        return dishes;
    }

}
