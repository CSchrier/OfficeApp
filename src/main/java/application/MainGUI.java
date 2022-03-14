package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;

public class MainGUI extends Application {






    public static void main(String[] args) {


        launch(args); //start the application!
    }
    @Override
    public void start(Stage stage) throws IOException {
        LinkedList<Bay> bayList = new LinkedList<>();
        LinkedList<Bay> roomList = new LinkedList<>();
        LinkedList<Bay> finishedList = new LinkedList<>();



        String str;
        Socket s;
        PrintWriter pw;
        InputStreamReader in;
        BufferedReader br;
        /*
        while(true){
            str = scan.nextLine();
            s = new Socket("192.168.1.5", 4999);
            pw = new PrintWriter(s.getOutputStream());
            pw.println(str);
            pw.flush();
            in = new InputStreamReader(s.getInputStream());
            br = new BufferedReader(in);
            str = br.readLine();
            System.out.println(str);
        }
        */
        s = new Socket("192.168.1.5", 4999);
        pw = new PrintWriter(s.getOutputStream());
        pw.println("1 0");
        pw.flush();
        in = new InputStreamReader(s.getInputStream());
        br = new BufferedReader(in);
        System.out.println("Data received From server");
        System.out.println("Data sorting starting");
        while((str = br.readLine()) != null) {
            String aisle;
            int bay;
            int job;
            int bin;
            String time;
            String[] data = str.split("\\s");
            if(data.length == 5) {

                aisle = data[0];
                bay = Integer.parseInt(data[1]);
                job = Integer.parseInt(data[2]);
                bin = Integer.parseInt(data[3]);
                time = data[4];
                Bay temp = new Bay(aisle, bay, job, bin, time);
                bayList.add(temp);

            }else if(data.length == 4){
                aisle = data[0];
                bay = Integer.parseInt(data[1]);
                job = Integer.parseInt(data[2]);
                time = data[3];
                Bay temp = new Bay(aisle,bay,job,time);
                finishedList.add(temp);

            }else if(data.length == 3){

                job = Integer.parseInt(data[0]);
                bin = Integer.parseInt(data[1]);
                time = data[2];
                Bay temp = new Bay (job, bin, time);
                roomList.add(temp);
            }


        }
        br.close();
        System.out.println("Data loaded");


        Parent root;

        //Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));



        root = loader.load();
        MainController mainController = loader.getController();
        mainController.sendData(bayList,roomList,finishedList);

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