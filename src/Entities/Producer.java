/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author apple
 */
public class Producer {
    private int Producer_Id;
    private String Name;
    private String nation;
    private int status;
    private String note;

    public Producer() {
    }

    public Producer(int Producer_Id, String Name, String nation, int status, String Note) {
        this.Producer_Id = Producer_Id;
        this.Name = Name;
        this.nation = nation;
        this.status = status;
        this.note = Note;
    }


    public int getProducer_Id() {
        return Producer_Id;
    }

    public void setProducer_Id(int Producer_Id) {
        this.Producer_Id = Producer_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    @Override
    public String toString() {
        return Name;
    }
    
}
