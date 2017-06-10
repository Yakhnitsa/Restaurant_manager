package com.yurets_y.order_manager.bin.xml_serialization;

import com.yurets_y.order_manager.bin.Day;
import com.yurets_y.order_manager.bin.Dish;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;

/**
 * Created by Admin on 07.06.2017.
 * Адаптер для демаршаллинга карты заказа
 */
public class MapAdapter extends XmlAdapter<MapElement[], Map<Day, List<Dish>>> {
    @Override
    public Map<Day, List<Dish>> unmarshal(MapElement[] v) throws Exception {
        Map<Day,List<Dish>> result = new LinkedHashMap<>();
        Arrays.stream(v).forEach(element ->
                result.put(element.getKey(),Arrays.asList(element.getDishes())));
        return result;
    }

    @Override
    public MapElement[] marshal(Map<Day, List<Dish>> v) throws Exception {
        List<MapElement> elements = new LinkedList<>();
        v.forEach((day,dishes)->{
            elements.add(new MapElement(day,dishes.toArray(new Dish[0])));
        });
        return elements.toArray(new MapElement[0]);
    }
}
