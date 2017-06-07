package com.yurets_y.order_manager.bin;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 29.05.2017.
 */
public enum Day implements Serializable
    {
    MONDAY("Понедельник","ПН","ПНД","ПОН"),
    TUESDAY("Вторник","ВТ","Втор","ВТР"),
    WEDNESDAY("Среда","СР","СРД"),
    THURSDAY("Четверг","ЧТ","ЧТВ","ЧЕТВ"),
    FRIDAY("Пятница","ПТ","ПТН","Пятн"),
    ANY_DAY("Все дни","Все","");

    Day(String... names){
        this.names = names;
    }
    //Любое из возможных представлений имен:
    private String[] names;

    public static Day getDayFromString(String name){
        for(Day day: Day.values()){
            for(String dayName: day.names){
                if(name.equalsIgnoreCase(dayName)){
                    return day;
                }
            }
        }
        return null;
    }

    public static List<Day> getDaysFromString(String daysString){
        List<Day> days = new ArrayList<Day>();
        String[] daysArray = daysString.split("([,./]\\s)|([,./])|(\\s)");
        for(String dayString: daysArray){
            Day day = getDayFromString(dayString);
            if(day != null){
                days.add(day);
            }
        }
        return days;
    }
    @Override
    public String toString(){
        return names[0];
    }
    public String toShortString(){
        return names[1];
    }


}
