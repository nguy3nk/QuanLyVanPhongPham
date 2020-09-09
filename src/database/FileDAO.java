/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Entities.File;
import Entities.Product;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apple
 */
public class FileDAO {

    Connection conn = null;

    public FileDAO() throws SQLException {
        conn = ConnectionDB.getConnect();
    }

    /**
     *
     * @return
     */
    public List<File> getAll() throws SQLException {
        List<File> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM File1 ");
        while (rs.next()) {
            File f = new File(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9));
            data.add(f);
        }
        return data;
    }

    public List<File> getByStaffId(String staff_id) throws SQLException {
        List<File> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM File1 where Account_Id = " + staff_id);
        while (rs.next()) {
            File f = new File(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9));
            data.add(f);
        }
        return data;
    }

    public List<File> getByProduct(String id) throws SQLException {
        List<File> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM File1 where Product_Id = " + id);
        while (rs.next()) {
            File f = new File(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9));
            data.add(f);
        }
        return data;
    }

    public List<File> searchByName(String name) throws SQLException {
        List<File> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM File1 WHERE Name like ? ");
        pst.setString(1, '%' + name + '%');
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            File f = new File(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9));
            data.add(f);
        }
        return data;
    }

    public List<File> searchByAll(int product_id, int staff_id, int manager_id, String date, int status) throws SQLException {
        String sqlPre = "";
        List<Integer> argument = new ArrayList<Integer>();
        if (product_id != -1) {
            sqlPre += " Product_Id = " + product_id;
            argument.add(product_id);
        }
        if (staff_id != -1) {
            if (!sqlPre.equals("")) {
                sqlPre += " AND ";
            }
            sqlPre += " Account_Id = " + staff_id;
            argument.add(staff_id);
        }
        if (manager_id != -1) {
            if (!sqlPre.equals("")) {
                sqlPre += " AND ";
            }
            sqlPre += " Manager_Id = " + manager_id;
            argument.add(manager_id);
        }
        if (status != -1) {
            if (!sqlPre.equals("")) {
                sqlPre += " AND ";
            }
            sqlPre += " Status = " + status;
            argument.add(status);
        }

        List<File> data = new ArrayList<>();
        String sql = "SELECT * FROM File1  ";
        if (!sqlPre.equals("")) {
            sql = sql + "WHERE " + sqlPre;
        }
        PreparedStatement pst = conn.prepareStatement(sql);
        System.out.println(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            File f = new File(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getInt(9));
            data.add(f);
        }
        return data;
    }

    public boolean insert(File f) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO File1(`Name`,`Product_Id`,`Account_Id`,`Manager_Id`,`Start_date`,`Note`,`Quantity`,`Status`) values(?,?,?,?,?,?,?,?)");
//        PreparedStatement pst = conn.prepareStatement("INSERT INTO File1 values(?,?,?,?,?,?,?,?,?)");
//        pst.setInt(1, f.getFileId());
        pst.setString(1, f.getName());
        pst.setInt(2, f.getProduct_Id());
        pst.setInt(3, f.getAccount_Id());
        pst.setInt(4, f.getManager_Id());
        pst.setString(5, f.getStart_date());
        pst.setString(6, f.getNote());
        pst.setInt(7, f.getQuantity());
        pst.setInt(8, f.getStatus());
        return pst.executeUpdate() > 0;
    }

    public boolean update(File f, int newStatus) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("update File1 set Name=?,Product_Id=?,Account_Id=?,Manager_Id=?,Start_date=?,Note=?,Quantity=?,Status=? where Id=?");
        pst.setInt(9, f.getFileId());
        pst.setString(1, f.getName());
        pst.setInt(2, f.getProduct_Id());
        pst.setInt(3, f.getAccount_Id());
        pst.setInt(4, f.getManager_Id());
        pst.setString(5, f.getStart_date());
        pst.setString(6, f.getNote());
        pst.setInt(7, f.getQuantity());
        pst.setInt(8, newStatus);

        ProductDAO ProductDAO = new ProductDAO();
        Product product = ProductDAO.searchById(f.getProduct_Id());
        boolean success = false;
        if (pst.executeUpdate() > 0) {
            if (f.getStatus() != 1) {
                if (newStatus == 1) {
                    if (product.getQuantity() > f.getQuantity()) {
                        product.setQuantity(product.getQuantity() - f.getQuantity());
                        PreparedStatement pst1 = conn.prepareStatement("Update Product set Quantity = ? Where Id = ?");
                        pst1.setInt(1, product.getQuantity());
                        pst1.setInt(2, product.getProduct_Id());
                        if (pst1.executeUpdate() > 0) {
                            success = true;
                            conn.commit();
                        } else {
                            conn.rollback();
                        }
                    }
                    conn.rollback();
                } else {
                    success = true;
                    conn.commit();
                }
            } else {
                if (newStatus != 1) {
                    product.setQuantity(product.getQuantity() + f.getQuantity());
                    PreparedStatement pst1 = conn.prepareStatement("Update Product set Quantity = ? Where Id = ?");
                    pst1.setInt(1, product.getQuantity());
                    pst1.setInt(2, product.getProduct_Id());
                    if (pst1.executeUpdate() > 0) {
                        success = true;
                        conn.commit();
                    } else {
                        conn.rollback();
                    }
                } else {
                    success = true;
                    conn.commit();
                }
            }
        }
        return success;
    }

    public boolean delete(String id) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("delete from File1 where Id=?");
        pst.setString(1, id);
        return pst.executeUpdate() > 0;

    }

}
