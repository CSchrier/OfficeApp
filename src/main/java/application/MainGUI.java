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


        Scanner scan = new Scanner(System.in);
        String str;
        Socket s;
        PrintWriter pw;
        InputStreamReader in;
        BufferedReader br;
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








        new FileCopy(networkPath,localPath);

        BufferedReader in = new BufferedReader(new FileReader(localPath)); //Local storage
        BufferedReader counter = new BufferedReader(new FileReader(localPath));
        int fileSizeCounter = 0;
        while(counter.readLine()!=null){
            fileSizeCounter++;
        }
        System.out.println(fileSizeCounter);


        for (int i = 0; i < fileSizeCounter; i++) {
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

            }
            String[] data = line.split("\\s");
            if(data.length == 5) {

                aisle = data[0];
                bay = Integer.parseInt(data[1]);
                job = Integer.parseInt(data[2]);
                bin = Integer.parseInt(data[3]);
                time = data[4];
                Bay temp = new Bay(aisle, bay, job, bin, time);
                bayList.add(temp);

            } else if(data.length == 3){
                //String[] data = line.split("\\s");
                job = Integer.parseInt(data[0]);
                bin = Integer.parseInt(data[1]);
                time = data[2];
                Bay temp = new Bay (job, bin, time);
                roomList.add(temp);
            }

        }

        System.out.println("Data loaded");








        Parent root;

        //Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));



        root = loader.load();
        MainController mainController = loader.getController();
        mainController.sendData(bayList,roomList,networkPath,localPath);

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