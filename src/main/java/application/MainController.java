package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class MainController {
    LinkedList<Bay> bayList;
    LinkedList<Bay> SearchedList = new LinkedList<>();
    LinkedList<Bay> SortedList = new LinkedList<>();
    ObservableList<Bay> data;
    ObservableList<Bay> sortedData;
    String jobToSearch = "";

    File networkPath;
    File localPath;

    @FXML
    public TableView<Bay> bayTable;
    @FXML
    public TableColumn<Bay, Integer> JobColumn;
    @FXML
    public TableColumn<Bay, Integer> BinColumn;
    @FXML
    public TableColumn<Bay, String> AisleColumn;
    @FXML
    public TableColumn<Bay, Integer> BayColumn;
    @FXML
    public TableColumn<Bay, String> TimeColumn;
    @FXML
    public TextField JobToSearch;
    @FXML
    public Label OutLabel;





    public void sendData(LinkedList<Bay> input,File network, File local){
        networkPath = network;
        localPath = local;
        bayList = input;
        data = FXCollections.observableList(bayList);
        setTable(data);
    }

    public void sendData(LinkedList<Bay> input){
        bayList = input;
        data = FXCollections.observableList(bayList);
        setTable(data);
    }


    public void setTable(ObservableList<Bay> tableSetter) {

        JobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
        BinColumn.setCellValueFactory(new PropertyValueFactory<>("bin"));
        AisleColumn.setCellValueFactory(new PropertyValueFactory<>("aisle"));
        BayColumn.setCellValueFactory(new PropertyValueFactory<>("bay"));
        TimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        bayTable.setItems(tableSetter);
    }

    public void SearchBtn(){

        if(!jobToSearch.equals(JobToSearch.getText())){
            jobToSearch=JobToSearch.getText();
            if(jobToSearch.equals("")){
                System.out.println("Empty Search");
            }else{
                int jobNum = Integer.parseInt(JobToSearch.getText());
                System.out.println(jobNum);


                for (Bay bay : bayList) {
                    if (bay.getJob() == jobNum) {
                        SearchedList.add(bay);
                        System.out.println("Added bin "+ bay.getBin());
                    }
                }
                System.out.println("sorting "+ SearchedList.size()+" items");

                for(int j = 1; j<30;j++) {
                    for (Bay bay : SearchedList){

                        if (bay.getBin() != j) {
                            continue;
                        }
                        SortedList.add(bay);
                        System.out.println("Added bin " + bay.getBin());
                    }
                }
            /*
            for(int i = 0; i<bayList.size();i++){
                if(bayList.get(i).getJob()==jobNum){

                }
            }
            */
                OutLabel.setText(SortedList.size()+" Bins");
                SearchedList.clear();
                sortedData = FXCollections.observableList(SortedList);
                setTable(sortedData);
            }
        }




    }

    public void ResetBtn(){
        OutLabel.setText("");
        jobToSearch="";
        sortedData.clear();
        setTable(data);
    }

    public void refresh() throws IOException {
        new FileCopy(networkPath,localPath);
        bayList.clear();
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
        sendData(bayList);
    }


}
