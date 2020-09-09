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
public class Product {
    private int Product_Id;
    private String Name;
    private int Quantity;
    private String Unit;
    private int Category_Id;
    private String Img,Note;
    private int Status;

    public Product() {
    }

    public Product(int Product_Id, String Name, int Quantity, String Unit, int Category_Id, String Img, String Note, int Status) {
        this.Product_Id = Product_Id;
        this.Name = Name;
        this.Quantity = Quantity;
        this.Unit = Unit;
        this.Category_Id = Category_Id;
        this.Img = Img;
        this.Note = Note;
        this.Status = Status;
    }

    public int getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(int Product_Id) {
        this.Product_Id = Product_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public int getCategory_Id() {
        return Category_Id;
    }

    public void setCategory_Id(int Category_Id) {
        this.Category_Id = Category_Id;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String Img) {
        this.Img = Img;
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
