package com.yurets_y.order_manager.util;

import com.yurets_y.order_manager.bin.Order;

import java.io.*;

/**
 * Created by Admin on 01.06.2017.
 */
public class OrderSerializer {

    public static void serilizeOrder(File file, Order order) throws IOException {
        try(ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file))){
            objOut.writeObject(order);
        }

    }
    public static Order deserializeOrder(File file) throws IOException {
        Order order = null;
        try(ObjectInputStream objInn = new ObjectInputStream(new FileInputStream(file)))
        {
            order = (Order) objInn.readObject();
        }catch (IOException e){
            throw new IOException("Ошибка чтения объекта",e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return order;
    }
}
