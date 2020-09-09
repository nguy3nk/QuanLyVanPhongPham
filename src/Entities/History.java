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
public class History {
    private int HistoryId,Quantity,Product_Id,Producer_Id,Supplier_Id;
    private String Note;
    private float Price;
    private int Status;
    //ngay nhap theo thu tu yyyy-mm-dd
    private String Start_date ;
    private int Account_Id;

    public History() {
    }

    public History(int HistoryId, int Quantity, int Product_Id, int Producer_Id, int Supplier_Id, String Note, float Price, int Account_Id, String Start_date, int Status) {
        this.HistoryId = HistoryId;
        this.Quantity = Quantity;
        this.Product_Id = Product_Id;
        this.Producer_Id = Producer_Id;
        this.Supplier_Id = Supplier_Id;
        this.Note = Note;
        this.Price = Price;
        this.Status = Status;
        this.Start_date = Start_date;
        this.Account_Id = Account_Id;
    }

    public int getHistoryId() {
        return HistoryId;
    }

    public void setHistoryId(int HistoryId) {
        this.HistoryId = HistoryId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public int getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(int Product_Id) {
        this.Product_Id = Product_Id;
    }

    public int getProducer_Id() {
        return Producer_Id;
    }

    public void setProducer_Id(int Producer_Id) {
        this.Producer_Id = Producer_Id;
    }

    public int getSupplier_Id() {
        return Supplier_Id;
    }

    public void setSupplier_Id(int Supplier_Id) {
        this.Supplier_Id = Supplier_Id;
    }

    public int getAccount_Id() {
        return Account_Id;
    }

    public void setAccount_Id(int Account_Id) {
        this.Account_Id = Account_Id;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float Price) {
        this.Price = Price;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getStart_date() {
        return Start_date;
    }

    public void setStart_date(String Start_date) {
        this.Start_date = Start_date;
    }

    @Override
    public String toString() {
        return "history{" + "HistoryId=" + HistoryId + ", Quantity=" + Quantity + ", Product_Id=" + Product_Id + ", Producer_Id=" + Producer_Id + ", Supplier_Id=" + Supplier_Id + ", Account_Id=" + Account_Id + ", Note=" + Note + ", Price=" + Price + ", Status=" + Status + ", Start_date=" + Start_date + '}';
    }
    
}
