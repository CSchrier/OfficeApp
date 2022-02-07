package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.LinkedList;

public class MainController {
    LinkedList<Bay> bayList;
    LinkedList<Bay> SearchedList = new LinkedList<>();
    LinkedList<Bay> SortedList = new LinkedList<>();
    LinkedList<Bay> roomList = new LinkedList<>();
    ObservableList<Bay> data;
    ObservableList<Bay> sortedData;
    ObservableList<Bay> RoomData;
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
    public TableColumn<Bay, String> LocationColumn;

    @FXML
    public TableColumn<Bay, String> TimeColumn;
    @FXML
    public TextField JobToSearch;
    @FXML
    public Label OutLabel;
    @FXML
    public TableView<Bay> roomTable;
    @FXML
    public TableColumn<Bay, Integer> JobColumn2;
    @FXML
    public TableColumn<Bay, Integer> BinColumn2;
    @FXML
    public TableColumn<Bay, String> TimeColumn2;




    public void sendData(LinkedList<Bay> bays, LinkedList<Bay> room,File network, File local){
        networkPath = network;
        localPath = local;
        bayList = bays;
        roomList = room;
        data = FXCollections.observableList(bayList);
        RoomData = FXCollections.observableList(roomList);
        setBayTable(data);
        setRoomTable(RoomData);

    }

    public void sendData(LinkedList<Bay> input, LinkedList<Bay> room){
        bayList = input;
        roomList = room;
        data = FXCollections.observableList(bayList);
        RoomData = FXCollections.observableList(roomList);
        setBayTable(data);
        setRoomTable(RoomData);
    }


    public void setBayTable(ObservableList<Bay> tableSetter) {

        JobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));
        BinColumn.setCellValueFactory(new PropertyValueFactory<>("bin"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        bayTable.setItems(tableSetter);
    }
    public void setRoomTable(ObservableList<Bay> tableSetter) {

        JobColumn2.setCellValueFactory(new PropertyValueFactory<>("job"));
        BinColumn2.setCellValueFactory(new PropertyValueFactory<>("bin"));
        TimeColumn2.setCellValueFactory(new PropertyValueFactory<>("time"));
        roomTable.setItems(tableSetter);


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
                OutLabel.setText(SortedList.size()+" Bins");
                SearchedList.clear();
                sortedData = FXCollections.observableList(SortedList);
                setBayTable(sortedData);
            }
        }




    }

    public void ResetBtn(){
        OutLabel.setText("");
        jobToSearch="";
        sortedData.clear();
        setBayTable(data);
        setRoomTable(RoomData);
    }

    public void refresh() throws IOException {
        new FileCopy(networkPath,localPath);
        bayList.clear();
        roomList.clear();
        BufferedReader in = new BufferedReader(new FileReader(localPath)); //Local storage
        BufferedReader counter = new BufferedReader(new FileReader(localPath));
        int fileSizeCounter = 0;
        while(counter.readLine()!=null){
            fileSizeCounter++;
        }
        counter.close();
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
        sendData(bayList,roomList);
    }

    public void RemoveFromRoom() throws IOException {
        int jobToBeRemoved = roomTable.getSelectionModel().getSelectedItem().getJob();
        int binToBeRemoved = roomTable.getSelectionModel().getSelectedItem().getBin();
        System.out.println(jobToBeRemoved + " " + binToBeRemoved);

        for (int i=0;i<roomList.size();i++){
            if(roomList.get(i).getJob()==jobToBeRemoved){
                if(roomList.get(i).getBin()==binToBeRemoved){
                    System.out.println("Found it at spot "+ i);
                    PrintWriter writer = new PrintWriter(localPath);
                    for(int j = 0; j< bayList.size();j++){
                        writer.println(bayList.get(j).writeData());
                        System.out.println(bayList.get(j).writeData());
                    }
                    for(int h = 0;h< roomList.size();h++){
                        if(h!=i){
                            System.out.println(roomList.get(h).writeDataRoom());
                            writer.println(roomList.get(h).writeDataRoom());

                        }

                    }
                    writer.close();
                }
            }
        }

        roomTable.getItems().remove(roomTable.getSelectionModel().getSelectedItem());
        new FileCopy(localPath,networkPath);


    }


    public void RemoveAllBins() throws IOException {
        PrintWriter writer = new PrintWriter(localPath);
        for (Bay bay : bayList) {
            writer.println(bay.writeData());
            System.out.println(bay.writeData());
        }
        roomTable.getItems().clear();
        writer.close();
        new FileCopy(localPath,networkPath);

    }

    public void onEditChanged(TableColumn.CellEditEvent<Bay, Integer> bayIntegerCellEditEvent) {
        Bay bay = roomTable.getSelectionModel().getSelectedItem();
        bay.setJob(bayIntegerCellEditEvent.getNewValue());
    }

}
