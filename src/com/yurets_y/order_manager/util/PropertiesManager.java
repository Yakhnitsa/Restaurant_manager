package com.yurets_y.order_manager.util;

import java.io.*;
import java.util.Properties;

/**
 * Created by Admin on 28.05.2017.
 */
public class PropertiesManager {

    private static PropertiesManager instanse;
    private static String PROP_FILE_PATH = "res\\properties\\properties.properties";

    private Properties properties;

    private PropertiesManager() {

        properties = new Properties();

        try (FileInputStream fis = new FileInputStream(PROP_FILE_PATH);
             InputStreamReader innStrRead = new InputStreamReader(fis, "UTF-8")) {
            properties.load(innStrRead);
        } catch (IOException e) {
            String message = "Ошибка загрузки файла конфигурации";
            String content = "Проверьте доступ к файлу:" + System.lineSeparator() + PROP_FILE_PATH;
            MessageManager.getInstance().showErrorMessage(null, message, content);
        }
    }

    public static PropertiesManager getInstance() {
        if (instanse == null) {
            instanse = new PropertiesManager();
        }
        return instanse;
    }

    public File getDefaultAddFolder() {
        String filePath = properties.getProperty("def.add.folder");
        return getFile(filePath);
    }

    public File getDefaultMenuFile(){
        String filePath = properties.getProperty("def.menu.file");
        return new File(filePath);
    }

    public File getDefaultOrderFile(){
        String filePath = properties.getProperty("def.order.file");
        return getFile(filePath);
    }

    public void setDefaultMenuFile(File file){

    }

    public void setDefaultOrderFile(File file){

    }

    public File getDefaultSaveFolder() {
        String filePath = properties.getProperty("def.save.folder");
        return getFile(filePath);
    }

    public String getProgramName() {
        return properties.getProperty("prog.name");
    }

    public void setDefaultAddFolder(File file) {
        if (!file.exists()) return;
        properties.setProperty("def.add.folder", file.getAbsolutePath());
        savePropertyFile();
    }

    public void setDefaultSaveFolder(File file) {
        if (!file.exists()) return;
        properties.setProperty("def.save.folder", file.getAbsolutePath());
        savePropertyFile();
    }


    private void savePropertyFile() {
        try (FileOutputStream fileOut = new FileOutputStream(PROP_FILE_PATH);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOut, "UTF-8"));
        ) {
            properties.store(writer, "");
        } catch (IOException e) {
            String message = "Ошибка сохранения файла конфигурации";
            String content = "Проверьте доступ к файлу:" + System.lineSeparator() + PROP_FILE_PATH;
            MessageManager.getInstance().showErrorMessage(null, message, content);
        }
    }

    private File getFile(String filePath) {
        if ((filePath == null) || (filePath.equals(""))) {
            return null;
        }
        File file = new File(filePath);
        if (file.exists()) return file;
        return null;
    }

}

