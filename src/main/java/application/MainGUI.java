package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class MainGUI extends Application {
    static LinkedList<Bay> bayList = new LinkedList<>();
    //static File networkPath = new File("\\Z:\\\\Tech\\Bin Data\\data.txt"); //Work Z drive
    //static File networkPath = new File("C:\\Program Files\\Keystone\\data.txt"); //home pc for testing


    static File networkPath = new File("BinData.txt");
    static File newNetworkPath = new File(networkPath.getAbsolutePath());

    static File loadFrom = new File("data.txt");

    static File localPath = new File(loadFrom.getAbsolutePath());





    public static void main(String[] args) throws IOException {


        launch(args); //start the application!
    }
    @Override
    public void start(Stage stage) throws IOException {

        if(!networkPath.exists()){
            System.out.println("Doesn't exist");
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());

            newNetworkPath = file;
            String path = newNetworkPath.getAbsolutePath();
            PrintWriter writer = new PrintWriter("BinData.txt", StandardCharsets.UTF_8);
            writer.println(path);
            writer.close();







            //newNetworkPath.createNewFile();
           // MasterFilePath getMaster = new MasterFilePath();
            //getMaster.main(args);
           // newNetworkPath=getMaster.getFile();

        }else{
            BufferedReader in = new BufferedReader(new FileReader(networkPath));
            String path = in.readLine();
            newNetworkPath = new File(path);
        }


        new FileCopy(newNetworkPath,localPath);

        BufferedReader in = new BufferedReader(new FileReader(localPath)); //Local storage
        int fileSize = 720;

        for (int i = 0; i < fileSize; i++) {
            String aisle;
            int bay;
            int job;
            int bin;
            String time;
            String line = "";
            try {
                line = in.readLine();
            } catch (IOException e) {
                System.out.println("I/O Error");
                System.exit(0);
            }
            if (line == null) {
                return;
            } else {
                String[] data = line.split("\\s");
                aisle = data[0];
                bay = Integer.parseInt(data[1]);
                job = Integer.parseInt(data[2]);
                bin = Integer.parseInt(data[3]);
                time = data[4];
                Bay temp = new Bay(aisle, bay, job, bin, time);
                bayList.add(temp);

            }

        }

        System.out.println("Data loaded");








        Parent root;

        //Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));


        root = loader.load();
        MainController mainController = loader.getController();
        mainController.sendData(bayList,newNetworkPath,localPath);

        Scene scene = new Scene(root);

        Image icon = new Image("keystone.png");
        ImageView imageView = new ImageView();
        imageView.setImage(icon);


        stage.setTitle("Keystone Bin Application");
        stage.getIcons().add(icon);
        stage.setScene(scene);

        stage.show();
    }


}