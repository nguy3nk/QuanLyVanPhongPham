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
public class Category {
    private int Category_Id;
    private String Name,Note;
    private int Status;

    public Category() {
    }

    public Category(int Category_Id, String Name, String Note, int Status) {
        this.Category_Id = Category_Id;
        this.Name = Name;
        this.Note = Note;
        this.Status = Status;
    }

    public int getCategory_Id() {
        return Category_Id;
    }

    public void setCategory_Id(int Category_Id) {
        this.Category_Id = Category_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    @Override
    public String toString() {
        return Name;
    }
    
}
