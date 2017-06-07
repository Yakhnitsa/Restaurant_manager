package com.yurets_y.order_manager.bin;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 25.05.2017.
 */
public class Dish implements Serializable{
    private String type;
    private String name;
    private String description;
    private List<Day> days;
    private int prise;

    public Dish(String type, String name, String description, List<Day> days, double prise) {
        this.type = type;
        this.name = name;
        this.days = days;
        this.description = description;
        this.prise = (int)(prise * 100);
    }

    public Dish() {
    }

    public Dish(String name,List<Day> days, double prise) {
        this("",name,"",days,prise);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return prise/100;
    }

    public void setPrise(double prise) {
        this.prise = (int)(prise*100);
    }

    public List<Day> getDays() {
        return days;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", days=" + days +
                ", prise=" + prise +
                '}';
    }
}