package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MainController {
    LinkedList<Bay> bayList;
    LinkedList<Bay> filledBayList = new LinkedList<>();
    LinkedList<Bay> SearchedList = new LinkedList<>();
    LinkedList<Bay> SortedList = new LinkedList<>();
    LinkedList<Bay> roomList = new LinkedList<>();
    LinkedList<Bay> finishedList = new LinkedList<>();

    LinkedList<Bay> selectedItems = new LinkedList<>();

    ObservableList<Bay> data;
    ObservableList<Bay> sortedData;
    ObservableList<Bay> RoomData;
    ObservableList<Bay> finishedData;
    String jobToSearch = "";
    String str;
    Socket s;
    PrintWriter pw;
    InputStreamReader in;
    BufferedReader br;

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

    @FXML
    public TableView<Bay> GoodsTable;
    @FXML
    public TableColumn<Bay, Integer> GoodsID;
    @FXML
    public TableColumn<Bay, Integer> GoodsLocation;
    @FXML
    public TableColumn<Bay, String> GoodsTime;

    String ip = "192.168.29.49";


    public void sendData(LinkedList<Bay> bays, LinkedList<Bay> room,File network, File local){
        networkPath = network;
        localPath = local;
        bayList = bays;
        roomList = room;
        //uncomment the following line for empty bays to show up in the table
        //data = FXCollections.observableList(bayList);
        RoomData = FXCollections.observableList(roomList);
        for(int i = 0; i < bayList.size();i++){
            if((bayList.get(i).getJob())!=0){
                filledBayList.add(bayList.get(i));
            }
            data = FXCollections.observableList(filledBayList);
            OutLabel.setText(filledBayList.size()+" Bins");
        }
        setBayTable(data);
        setRoomTable(RoomData);

    }

    public void sendData(LinkedList<Bay> input, LinkedList<Bay> room,LinkedList<Bay> finished){
        bayList = input;
        roomList = room;
        finishedList = finished;
        //uncomment the following line for empty bays to show up in the table
        //data = FXCollections.observableList(bayList);
        for(int i = 0; i < bayList.size();i++){
            if((bayList.get(i).getJob())!=0){
                filledBayList.add(bayList.get(i));
            }
            data = FXCollections.observableList(filledBayList);
            OutLabel.setText(filledBayList.size()+" Bins");

        }
        RoomData = FXCollections.observableList(roomList);
        finishedData = FXCollections.observableList(finishedList);
        setBayTable(data);

        //setRoomTable(RoomData);
        //setFinishedTable(finishedData);
    }

    private void setFinishedTable(ObservableList<Bay> finishedData) {
        GoodsID.setCellValueFactory(new PropertyValueFactory<>("job"));

        GoodsLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        GoodsTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        GoodsTable.setItems(finishedData);
    }


    public void setBayTable(ObservableList<Bay> tableSetter) {

        JobColumn.setCellValueFactory(new PropertyValueFactory<>("job"));

        BinColumn.setCellValueFactory(new PropertyValueFactory<>("bin"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        bayTable.setItems(tableSetter);
        bayTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }
    public void setRoomTable(ObservableList<Bay> tableSetter) {

        JobColumn2.setCellValueFactory(new PropertyValueFactory<>("job"));
        BinColumn2.setCellValueFactory(new PropertyValueFactory<>("bin"));
        TimeColumn2.setCellValueFactory(new PropertyValueFactory<>("time"));
        roomTable.setItems(tableSetter);


    }

    public void SearchBtn(){
        SortedList.clear();
        if(!jobToSearch.equals(JobToSearch.getText())){
            jobToSearch=JobToSearch.getText();
            if(jobToSearch.equals("")){
                System.out.println("Empty Search");
            }else{

                int jobNum = Integer.parseInt(JobToSearch.getText());
                System.out.println(jobNum);


                for (Bay bay : bayList) {       //Adds all bay items into a new list
                    if (bay.getJob() == jobNum) {                     // but puts the searched for bays at the front
                        SearchedList.add(bay);
                    }else{
                        //SearchedList.add(bay); dont uncomment, this adds all bays even empty to list
                    }
                }


                /* may delete everything in this comment if above's sorting works better
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

                 */

                sortedData = FXCollections.observableList(SearchedList);
                setBayTable(sortedData);

            }
        }

        JobToSearch.clear();


    }

    public void ResetBtn(){
        JobToSearch.clear();
        OutLabel.setText("");
        jobToSearch="";
        sortedData.clear();
        setBayTable(data);

        OutLabel.setText(filledBayList.size()+" Bins");

    }

    public void sendSkype() throws IOException {
        if(selectedItems!=null){
            selectedItems.clear();
        }

        String bayInfo = "";

        for(int i = 0; i<bayTable.getSelectionModel().getSelectedItems().size();i++){
            Bay selected = bayTable.getItems().get(i);
            bayInfo += ("\""+selected.job+"-"+selected.bin+" "+selected.aisle+"-"+selected.bay+"\""+" ");
        }
        //System.out.println(bayInfo);

        s = new Socket(ip, 4999);
        pw = new PrintWriter(s.getOutputStream());
        pw.println("3 " + bayInfo);
        pw.flush();




    }


    public void refresh() throws IOException {
        bayList.clear();
        filledBayList.clear();
        roomList.clear();
        s = new Socket(ip, 4999);
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

            } else if(data.length == 3){

                job = Integer.parseInt(data[0]);
                bin = Integer.parseInt(data[1]);
                time = data[2];
                Bay temp = new Bay (job, bin, time);
                roomList.add(temp);
            }


        }
        br.close();
        System.out.println("Data Loaded!");

        sendData(bayList,roomList,finishedList);
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
        s = new Socket(ip, 4999);
        pw = new PrintWriter(s.getOutputStream());
        pw.println("1 1");
        pw.flush();
        refresh();

    }

    public void onEditChanged(TableColumn.CellEditEvent<Bay, Integer> bayIntegerCellEditEvent) {
        Bay bay = roomTable.getSelectionModel().getSelectedItem();
        bay.setJob(bayIntegerCellEditEvent.getNewValue());
    }

}
