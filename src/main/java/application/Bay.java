package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bay {

    String aisle;
    public int bay;
    public int job;
    public int bin;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy-HH:mm");
    LocalDateTime now = LocalDateTime.now();
    String time;


    public DateTimeFormatter getDtf() {
        return dtf;
    }

    public void setDtf(LocalDateTime dtf) {
        dtf = dtf;
    }

    public String getTime() {
        return time;
    }

    public void setTime() {
        this.time = dtf.format(now);
    }

    //public int jobSize;   //Kept track of total size of a given job, blanking out for now as the information isn't important yet
    //public int skidType;  //Future idea to keep track of if a bin contains an unaudited job, scrap, an outgoing order, etc, using a numbering
    //system like 0 for empty, 1 for unaudited job, 2 for order, 3 scrap, and so on

    public String getAisle() {
        return aisle;
    }
    public int getBin(){return bin;}
    public int getBay(){return bay;}
    public int getJob() {
        return job;
    }
    public void setJob(int jobNumber) {
        this.job = jobNumber;
    }
    public int getJobSubNumber() {
        return bin;
    }
    public void setJobSubNumber(int jobSubNumber) {
        this.bin = jobSubNumber;
    }
    public int getBayLocation() {
        return bay;
    }
    public Bay(String aisle, int bay, int jobNum, int jobSub, String time) {
        this.aisle = aisle;
        this.bay = bay;
        this.job = jobNum;
        this.bin = jobSub;
        this.time = time;
        //this.jobSize = jobSize;
    }

    public String writeData() {
        String toBeWritten;
        if(job!=0){
            toBeWritten = aisle + " " + bay + " " + job + " " + bin + " " + time;
        }else{
            toBeWritten = aisle + " " + bay + " " + job + " " + bin + " " + 0;
        }
        return toBeWritten;


    }



}