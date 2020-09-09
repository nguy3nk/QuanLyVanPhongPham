/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Entities.Category;
import Entities.Product;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author apple
 */
public class CategoryDAO {

    Connection conn = null;

    public CategoryDAO() throws SQLException {
        conn = ConnectionDB.getConnect();
    }

    /**
     *
     * @return
     */
    public List<Category> getAll() throws SQLException {
        List<Category> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM Category");
        while (rs.next()) {
            Category c = new Category(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
            data.add(c);
        }
        return data;

    }

    public Category getById(int Id) throws SQLException {
        Category c = null;
        PreparedStatement pst = conn.prepareStatement("SElECT * FROM Category Where Id = ?");
        ResultSet rs = pst.executeQuery("SElECT * FROM Category Where Id = ?");
        pst.setInt(1, Id);
        while (rs.next()) {
            c = new Category(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
        }
        return c;

    }

    public List<Category> searchByName(String name) throws SQLException {
        List<Category> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Category WHERE Name like ? ");
        pst.setString(1, '%' + name + '%');
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Category c = new Category(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
            data.add(c);
        }
        return data;
    }

    public boolean insert(Category c) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO Category (`Name`, `Note`, `Status`) values(?,?,?)");
        //PreparedStatement pst = conn.prepareStatement("INSERT INTO category values(?,?,?)");
        pst.setString(1, c.getName());
        pst.setString(2, c.getNote());
        pst.setInt(3, c.getStatus());
        return pst.executeUpdate() > 0;

    }

    public boolean update(Category c) throws SQLException {

        PreparedStatement pst = conn.prepareStatement("update Category set Name=?,Note=?,Status=? where Id=?");
        pst.setString(1, c.getName());
        pst.setString(2, c.getNote());
        pst.setInt(3, c.getStatus());
        pst.setInt(4, c.getCategory_Id());
        return pst.executeUpdate() > 0;

    }

    public boolean delete(String id) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("delete from Category where Id=?");
        pst.setString(1, id);
        ProductDAO ProductDAO = new ProductDAO();
        List<Product> listPr = ProductDAO.getByCatId(id);
        for (Product product : listPr) {
            ProductDAO.delete(String.valueOf(product.getProduct_Id()));
        }
        boolean success = false;
        success = pst.executeUpdate() > 0;
        if (success) {
            conn.commit();
        } else {
            conn.rollback();
        }
        return success;
    }

    public List<Category> sortByAZ() throws SQLException {
        List<Category> data = this.getAll();
        Collections.sort(data, new Comparator<Category>() {
            @Override
            public int compare(Category t, Category t1) {
                return t1.getName().compareTo(t.getName());
            }
        });
        return data;
    }

    public List<Category> sortByZA() throws SQLException {
        List<Category> data = this.getAll();
        Collections.sort(data, new Comparator<Category>() {
            @Override
            public int compare(Category t, Category t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        return data;
    }
}
