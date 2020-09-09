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
public class File {
    private int FileId;
    private String Name;
    private int Product_Id,Account_Id,Manager_Id;
    private String Start_date,Note;
    private int Quantity;
    private int Status;

    public File() {
    }

    public File(int FileId, String Name, int Product_Id, int Account_Id, int Manager_Id, String Start_date, String Note, int Quantity, int Status) {
        this.FileId = FileId;
        this.Name = Name;
        this.Product_Id = Product_Id;
        this.Account_Id = Account_Id;
        this.Manager_Id = Manager_Id;
        this.Start_date = Start_date;
        this.Note = Note;
        this.Quantity = Quantity;
        this.Status = Status;
    }

    public int getFileId() {
        return FileId;
    }

    public void setFileId(int FileId) {
        this.FileId = FileId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(int Product_Id) {
        this.Product_Id = Product_Id;
    }

    public int getAccount_Id() {
        return Account_Id;
    }

    public void setAccount_Id(int Account_Id) {
        this.Account_Id = Account_Id;
    }

    public int getManager_Id() {
        return Manager_Id;
    }

    public void setManager_Id(int Manager_Id) {
        this.Manager_Id = Manager_Id;
    }

    public String getStart_date() {
        return Start_date;
    }

    public void setStart_date(String Start_date) {
        this.Start_date = Start_date;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    @Override
    public String toString() {
        return "File{" + "FileId=" + FileId + ", Name=" + Name + ", Product_Id=" + Product_Id + ", Account_Id=" + Account_Id + ", Manager_Id=" + Manager_Id + ", Start_date=" + Start_date + ", Note=" + Note + ", Quantity=" + Quantity + ", Status=" + Status + '}';
    }
    
    
}
