package com.mycompany.app;

import java.util.ArrayList;

public class Response {
    
    public String result;
    public ArrayList<Record> errorRecords = new ArrayList<Record>();


    public void setResult(String s) {
        result = s;
    }

    public void addRecord(Record r) {
        errorRecords.add(r);
    }
}
