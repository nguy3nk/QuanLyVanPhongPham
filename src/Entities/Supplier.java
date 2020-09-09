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
public class Supplier {

    private int Supplier_Id;
    private String Name;
    private String Phone;
    private String Address, Email, Note;
    private int Status;
    private String Img;

    public Supplier() {
    }

    public Supplier(int Supplier_Id, String Name, String Phone, String Address, String Email, String Note, int Status, String Img) {
        this.Supplier_Id = Supplier_Id;
        this.Name = Name;
        this.Phone = Phone;
        this.Address = Address;
        this.Email = Email;
        this.Note = Note;
        this.Status = Status;
        this.Img = Img;
    }

    public int getSupplier_Id() {
        return Supplier_Id;
    }

    public void setSupplier_Id(int Supplier_Id) {
        this.Supplier_Id = Supplier_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
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

    public String getImg() {
        return Img;
    }

    public void setImg(String Img) {
        this.Img = Img;
    }

    @Override
    public String toString() {
        return Name;
    }

    


}
