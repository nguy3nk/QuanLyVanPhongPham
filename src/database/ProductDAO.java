/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Entities.History;
import Entities.Product;
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
public class ProductDAO {

    Connection conn = null;

    public ProductDAO() throws SQLException {
        conn = ConnectionDB.getConnect();
    }

    public List<Product> getAll() throws SQLException {
        List<Product> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM Product ");
        while (rs.next()) {
            Product p = new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8));
            data.add(p);
        }
        return data;

    }

    public List<Product> getByCatId(String id) throws SQLException {
        List<Product> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM Product where Category_Id = " + id);
        while (rs.next()) {
            Product p = new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8));
            data.add(p);
        }
        return data;
    }

    public List<Product> searchByName(String name) throws SQLException {
        List<Product> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Product WHERE Name like ? ");
        pst.setString(1, '%' + name + '%');
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Product p = new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8));
            data.add(p);
        }
        return data;

    }

    public Product searchById(int id) throws SQLException {
        Product data = null;
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Product WHERE Id = ? ");
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            data = new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8));
        }
        return data;
    }

    public boolean insert(Product a) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO Product(`Name`, `Quantity`, `Unit`,`Category_Id`, `Img`, `Note`, `Status`) values(?,?,?,?,?,?,?)");
        //PreparedStatement pst = conn.prepareStatement("INSERT INTO Product values(?,?,?,?,?,?,?)");
        pst.setString(1, a.getName());
        pst.setInt(2, a.getQuantity());
        pst.setString(3, a.getUnit());
        pst.setInt(4, a.getCategory_Id());
        pst.setString(5, a.getImg());
        pst.setString(6, a.getNote());
        pst.setInt(7, a.getStatus());
        return pst.executeUpdate() > 0;

    }

    public boolean update(Product a) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("update Product set Name=?,Quantity=?,Unit=?,Category_id=?,Img=?,Note=?,Status=? where Id=?");
        pst.setInt(8, a.getProduct_Id());
        pst.setString(1, a.getName());
        pst.setInt(2, a.getQuantity());
        pst.setString(3, a.getUnit());
        pst.setInt(4, a.getCategory_Id());
        pst.setString(5, a.getImg());
        pst.setString(6, a.getNote());
        pst.setInt(7, a.getStatus());
        return pst.executeUpdate() > 0;

    }

    public boolean delete(String id) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("delete from Product where Id=?");
        pst.setString(1, id);
        FileDAO FileDAO = new FileDAO();
        HistoryDAO HistoryDAO = new HistoryDAO();
        boolean success = false;
        List<Entities.File> listFl = FileDAO.getByProduct(id);
        List<History> listHs = HistoryDAO.getByProduct(id);
        for (History history : listHs) {
            HistoryDAO.delete(String.valueOf(history.getHistoryId()));
        }
        for (Entities.File file : listFl) {
            FileDAO.delete(String.valueOf(file.getFileId()));
        }
        success = pst.executeUpdate() > 0;
        if (success) {
            conn.commit();
        } else {
            conn.rollback();
        }
        return success;
    }

    public List<Product> sortByAZ() throws SQLException {
        List<Product> data = this.getAll();
        Collections.sort(data, new Comparator<Product>() {
            @Override
            public int compare(Product t, Product t1) {
                return t1.getName().compareTo(t.getName());
            }
        });
        return data;
    }

    public List<Product> sortByZA() throws SQLException {
        List<Product> data = this.getAll();
        Collections.sort(data, new Comparator<Product>() {
            @Override
            public int compare(Product t, Product t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        return data;
    }
}
