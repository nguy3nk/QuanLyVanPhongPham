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
public class Account {
    private int AccountID ;
    private String Name ;
    private String Phone;
    private String Address ,Mail,Img,UserName,Password;
    private int Status ;
    private String Note ;
    private int Gender ;
    private String Datetime ;

    public Account() {
    }

    public Account(String Name, String Phone, String Address, String Mail, String Img, int Status, String Note, int Gender, String Datetime) {
        this.Name = Name;
        this.Phone = Phone;
        this.Address = Address;
        this.Mail = Mail;
        this.Img = Img;
        this.Status = Status;
        this.Note = Note;
        this.Gender = Gender;
        this.Datetime = Datetime;
    }

    public Account(int AccountID, String Name, String Phone, String Address, String Mail, String Img, String UserName, String Password, int Status, String Note, int Gender, String Datetime) {
        this.AccountID = AccountID;
        this.Name = Name;
        this.Phone = Phone;
        this.Address = Address;
        this.Mail = Mail;
        this.Img = Img;
        this.UserName = UserName;
        this.Password = Password;
        this.Status = Status;
        this.Note = Note;
        this.Gender = Gender;
        this.Datetime = Datetime;
    }

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int AccountID) {
        this.AccountID = AccountID;
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

    public String getMail() {
        return Mail;
    }

    public void setMail(String Mail) {
        this.Mail = Mail;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String Img) {
        this.Img = Img;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int Gender) {
        this.Gender = Gender;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String Datetime) {
        this.Datetime = Datetime;
    }

    @Override
    public String toString() {
        return Name;
    }
    
    public String chuoi(){
        return AccountID+"|"+Name+"|"+Address+"|"+Mail+"|"+Img+"|"+UserName+"|"+Password+"|"+Status+"|"+Note+"|"+Gender+"|"+Datetime;
    }
}
