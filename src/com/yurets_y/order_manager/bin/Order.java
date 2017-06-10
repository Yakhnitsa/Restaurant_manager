package com.yurets_y.order_manager.bin;

import com.yurets_y.order_manager.bin.xml_serialization.MapAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 25.05.2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Order")
public class Order implements Serializable
{
    private String name;
    @XmlElement
    @XmlJavaTypeAdapter(MapAdapter.class)
    private Map<Day,List<Dish>> orderMap = new LinkedHashMap<>();

    public Map<Day, List<Dish>> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<Day, List<Dish>> orderMap) {
        this.orderMap = orderMap;
    }
    @XmlElement(name = "OrderName")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (name != null ? !name.equals(order.name) : order.name != null) return false;
        return orderMap != null ? orderMap.equals(order.orderMap) : order.orderMap == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (orderMap != null ? orderMap.hashCode() : 0);
        return result;
    }
}
