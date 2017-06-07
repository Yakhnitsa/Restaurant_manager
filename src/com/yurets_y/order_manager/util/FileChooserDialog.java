package com.yurets_y.order_manager.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by Admin on 27.05.2017.
 */
public class FileChooserDialog {
    private static FileChooserDialog instance = new FileChooserDialog();
    private FileChooserDialog(){}

    public static FileChooserDialog getInstance(){
        return instance;
    }

    public List<File> getXMLFilesToLoad(Stage primaryStage) {
        File defFile = PropertiesManager.getInstance().getDefaultAddFolder();
        return getFilesListToLoadFX(primaryStage, defFile, "*.xml", "xml files");
    }

    public File getXMLFileToSave(Stage primaryStage, File defaultFile) {
        return getFileToSaveFX(primaryStage, defaultFile, "*.xml", "xml file");
    }

    public File getXMLFileToLoad(Stage primaryStage, File defaultFile) {
        return getFileToLoadFX(primaryStage, defaultFile, "*.xml", "xml file");
    }

    public File getExelFileToLoad(Stage primaryStage, File defaultFile) {
        return getFileToLoadFX(primaryStage, defaultFile, "*.xlsx", "Excel 2007 file");
    }

    public File getExelFileToSave(Stage primaryStage, File defaultFile) {
        return getFileToSaveFX(primaryStage, defaultFile, "*.xlsx", "Excel 2007 file");
    }

    public File getXLSFileToSave(Stage primaryStage) {
        File defSavepath = PropertiesManager.getInstance().getDefaultSaveFolder();
        return getFileToSaveFX(primaryStage, defSavepath, "*.xls", "exccel xls files");
    }

    public File getFolderToLoad(Stage primaryStage) {
        File defaultLoadFolder = PropertiesManager.getInstance().getDefaultAddFolder();
        return getFolder(primaryStage,defaultLoadFolder);
    }

    public File getFolder(Stage primaryStage,File defaultPath){

        DirectoryChooser directoryChooser = new DirectoryChooser();
        if ((defaultPath != null)&&(defaultPath.exists())) {
            if(!defaultPath.isDirectory()){
                defaultPath = defaultPath.getParentFile();
            }
            directoryChooser.setInitialDirectory(defaultPath);
        }
        File file = directoryChooser.showDialog(primaryStage);
        return file;
    }

    private File getFileToSaveFX(Stage primaryStage, File defaultPath, String fileExtension, String fileDescription) {
        FileChooser fileChooser = new FileChooser();

        checkNullReferenseAndSetFilter(defaultPath, fileExtension, fileDescription, fileChooser);

        // Показываем диалог сохранения файла
        File file = fileChooser.showSaveDialog(primaryStage);
    //TODO дописать confirm dialog если файл существует
        return file;
    }

    private List<File> getFilesListToLoadFX(Stage primaryStage, File defaultPath, String fileExtension, String fileDescription) {

        FileChooser fileChooser = new FileChooser();

        checkNullReferenseAndSetFilter(defaultPath, fileExtension, fileDescription, fileChooser);

        // Показываем диалог сохранения файла
        List<File> file = fileChooser.showOpenMultipleDialog(primaryStage);

        return file;
    }

    private static void checkNullReferenseAndSetFilter(File defaultPath, String fileExtension, String fileDescription, FileChooser fileChooser) {
        if ((defaultPath != null)&&(defaultPath.exists())) {
            if(!defaultPath.isDirectory()){
                defaultPath = defaultPath.getParentFile();
            }
            fileChooser.setInitialDirectory(defaultPath);
        }
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                fileDescription, fileExtension);
        fileChooser.getExtensionFilters().add(extFilter);
    }

    private static File getFileToLoadFX(Stage primaryStage, File defaultPath, String fileExtension, String fileDescription) {

        FileChooser fileChooser = new FileChooser();
        checkNullReferenseAndSetFilter(defaultPath, fileExtension, fileDescription, fileChooser);

        // Показываем диалог сохранения файла
        return fileChooser.showOpenDialog(primaryStage);
    }

}
