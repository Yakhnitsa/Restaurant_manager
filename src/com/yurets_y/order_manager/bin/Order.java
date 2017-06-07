package com.yurets_y.order_manager.bin;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 25.05.2017.
 */
@XmlType(name = "order")
@XmlRootElement
public class Order implements Serializable
{
    private String name;
    @XmlElementWrapper(name="dishes")
    private Map<Day,List<Dish>> orderMap = new LinkedHashMap<>();

    public Map<Day, List<Dish>> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<Day, List<Dish>> orderMap) {
        this.orderMap = orderMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Order{" +
                "name='" + name + '\'' +
                ", orderMap=" + orderMap +
                '}';
    }
}
