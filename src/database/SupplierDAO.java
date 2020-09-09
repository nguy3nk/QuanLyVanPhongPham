/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Entities.History;
import Entities.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author apple
 */
public class SupplierDAO {

    Connection conn = null;

    public SupplierDAO() throws SQLException {
        conn = ConnectionDB.getConnect();
    }

    public List<Supplier> getAll() throws SQLException {
        List<Supplier> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM Supplier ");
        while (rs.next()) {
            Supplier s = new Supplier(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8));
            data.add(s);
        }
        return data;

    }

    public List<Supplier> searchByName(String name) throws SQLException {
        List<Supplier> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Supplier WHERE Name like ? ");
        pst.setString(1, '%' + name + '%');
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Supplier s = new Supplier(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8));
            data.add(s);
        }
        return data;

    }

    public Supplier searchById(int id) throws SQLException {
        Supplier data = null;
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Supplier WHERE Id = ? ");
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            data = new Supplier(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8));
        }
        return data;

    }

    public List<Supplier> searchByEmail(String email) throws SQLException {
        List<Supplier> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Supplier WHERE Email like ? ");
        pst.setString(1, '%' + email + '%');
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Supplier s = new Supplier(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), rs.getString(8));
            data.add(s);
        }
        return data;

    }

    public boolean insert(Supplier a) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO Supplier(`Name`, `Phone`, `Address`,`Email`, `Note`, `Status`, `Img`) values(?,?,?,?,?,?,?)");
//        PreparedStatement pst = conn.prepareStatement("INSERT INTO Supplier values(?,?,?,?,?,?,?,?)");
//        pst.setInt(1, a.getSupplier_Id());
        pst.setString(1, a.getName());
        pst.setString(2, a.getPhone());
        pst.setString(3, a.getAddress());
        pst.setString(4, a.getEmail());
        pst.setString(5, a.getNote());
        pst.setInt(6, a.getStatus());
        pst.setString(7, a.getImg());
        return pst.executeUpdate() > 0;

    }

    public boolean update(Supplier a) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("update Supplier set Name=?,Phone=?,Address=?,Email=?,Note=?,Status=?,Img=? where Id=?");
        pst.setInt(8, a.getSupplier_Id());
        pst.setString(1, a.getName());
        pst.setString(2, a.getPhone());
        pst.setString(3, a.getAddress());
        pst.setString(4, a.getEmail());
        pst.setString(5, a.getNote());
        pst.setInt(6, a.getStatus());
        pst.setString(7, a.getImg());
        return pst.executeUpdate() > 0;

    }

    public boolean delete(String id) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("delete from Supplier where Id=?");
        pst.setString(1, id);
        boolean success = false;
        HistoryDAO HistoryDAO = new HistoryDAO();
        List<History> listHs = HistoryDAO.getBySupplier(id);
        for (History history : listHs) {
            HistoryDAO.delete(String.valueOf(history.getHistoryId()));
        }
        success = pst.executeUpdate() > 0;
        if (success) {
            conn.commit();
        } else {
            conn.rollback();
        }
        return success;

    }

    public List<Supplier> sortByAZ() throws SQLException {
        List<Supplier> data = this.getAll();
        Collections.sort(data, new Comparator<Supplier>() {
            @Override
            public int compare(Supplier t, Supplier t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        return data;
    }

    public List<Supplier> sortByZA() throws SQLException {
        List<Supplier> data = this.getAll();
        Collections.sort(data, new Comparator<Supplier>() {
            @Override
            public int compare(Supplier t, Supplier t1) {
                return t1.getName().compareTo(t.getName());
            }
        });
        return data;
    }
}
