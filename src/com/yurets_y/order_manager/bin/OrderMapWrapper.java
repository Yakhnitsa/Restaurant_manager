package com.yurets_y.order_manager.bin;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Admin on 27.05.2017.
 */
public class OrderMapWrapper {

    private Map<String,Dish> orderMap;

    public Map<String, Dish> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<String, Dish> orderMap) {
        this.orderMap = orderMap;
    }


}
