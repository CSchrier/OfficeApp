package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bay2 {

    private SimpleStringProperty aisle;
    private SimpleIntegerProperty bayLocation;
    private SimpleIntegerProperty  job;
    private SimpleIntegerProperty  jobSubNumber;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy-HH:mm");
    LocalDateTime now = LocalDateTime.now();
    private SimpleStringProperty time;


    public DateTimeFormatter getDtf() {
        return dtf;
    }

    public void setDtf(LocalDateTime dtf) {
        dtf = dtf;
    }

    public String getTime() {
        return time.get();
    }

    public void setTime() {
        //this.time = dtf.format(now);
    }

    //public int jobSize;   //Kept track of total size of a given job, blanking out for now as the information isn't important yet
    //public int skidType;  //Future idea to keep track of if a bin contains an unaudited job, scrap, an outgoing order, etc, using a numbering
    //system like 0 for empty, 1 for unaudited job, 2 for order, 3 scrap, and so on

    public String getAisle() {
        return aisle.get();
    }

    public int getJob() {
        return job.get();
    }
    public void setJobNumber(int jobNumber) {
      //  this.jobNumber = jobNumber;
    }
    public int getJobSubNumber() {
        return jobSubNumber.get();
    }
    public void setJobSubNumber(int jobSubNumber) {
      //  this.jobSubNumber = jobSubNumber;
    }
    public int getBayLocation() {
        return bayLocation.get();
    }
    public Bay2(String aisle, int bay, int jobNum, int jobSub, String time) {
        this.aisle = new SimpleStringProperty(aisle);
        this.bayLocation = new SimpleIntegerProperty(bay);
        this.job = new SimpleIntegerProperty(jobNum);
        this.jobSubNumber = new SimpleIntegerProperty(jobSub);
        this.time = new SimpleStringProperty(time);
        //this.jobSize = jobSize;
    }
    /*
    public String writeData() {
        String toBeWritten;
        if(jobNumber!=0){
            toBeWritten = aisle + " " + bayLocation + " " + jobNumber + " " + jobSubNumber + " " + time;
        }else{
            toBeWritten = aisle + " " + bayLocation + " " + jobNumber + " " + jobSubNumber + " " + 0;
        }
        return toBeWritten;


    }
*/


}