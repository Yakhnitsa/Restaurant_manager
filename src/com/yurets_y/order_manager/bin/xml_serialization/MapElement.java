package com.yurets_y.order_manager.bin.xml_serialization;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Admin on 09.06.2017.
 */
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class MapElement{
    @XmlElement(name="day")
    private Day key;
    @XmlElementWrapper(name="dishes", nillable = true)
    private Dish[] dishes;
    public MapElement(){

    }

    public MapElement(Day key, Dish[] dishes) {
        this.key = key;
        this.dishes = dishes;
    }

    public Day getKey() {
        return key;
    }

    public void setKey(Day key) {
        this.key = key;
    }


    public Dish[] getDishes() {
        return dishes;
    }

    public void setDishes(Dish[] dishes) {
        this.dishes = dishes;
    }

}
