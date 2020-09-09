/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Entities.History;
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
public class HistoryDAO {

    Connection conn = null;

    public HistoryDAO() throws SQLException {
        conn = ConnectionDB.getConnect();
    }

    /**
     *
     * @return
     */
    public List<History> getAll() throws SQLException {
        List<History> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM History ");
        while (rs.next()) {
            History h = new History(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7), rs.getInt(8), rs.getString(9), rs.getInt(10));
            data.add(h);
        }
        return data;
    }

    public List<History> getByProducer(String id) throws SQLException {
        List<History> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM History where Producer_Id = " + id);
        while (rs.next()) {
            History h = new History(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7), rs.getInt(8), rs.getString(9), rs.getInt(10));
            data.add(h);
        }
        return data;
    }

    public List<History> getByAccount(String id) throws SQLException {
        List<History> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM History where Account_Id = " + id);
        while (rs.next()) {
            History h = new History(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7), rs.getInt(8), rs.getString(9), rs.getInt(10));
            data.add(h);
        }
        return data;
    }
    public List<History> getBySupplier(String id) throws SQLException {
        List<History> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM History where Supplier_Id = " + id);
        while (rs.next()) {
            History h = new History(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7), rs.getInt(8), rs.getString(9), rs.getInt(10));
            data.add(h);
        }
        return data;
    }

    public List<History> getByProduct(String id) throws SQLException {
        List<History> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM History where Product_Id = " + id);
        while (rs.next()) {
            History h = new History(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7), rs.getInt(8), rs.getString(9), rs.getInt(10));
            data.add(h);
        }
        return data;
    }

    // Ngay nhap theo yyyy-mm-dd
    public List<History> searchByDate(String Start, String End) throws SQLException {
        List<History> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM History WHERE start_date >= ? AND start_date <= ?");
        pst.setString(1, Start);
        pst.setString(2, End);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            History h = new History(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7), rs.getInt(8), rs.getString(9), rs.getInt(10));
            data.add(h);
        }
        return data;
    }

    public boolean insert(History f) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("INSERT INTO History(`Quantity`,`Product_Id`,`Producer_Id`,`Supplier_Id`,`Note`,`Price`,`Status`,`Start_date`,`Account_Id`) values(?,?,?,?,?,?,?,?,?)");
//        PreparedStatement pst = conn.prepareStatement("INSERT INTO History values(?,?,?,?,?,?,?,?,?)");
//        pst.setInt(1, f.getHistoryId());
        pst.setInt(1, f.getQuantity());
        pst.setInt(2, f.getProduct_Id());
        pst.setInt(3, f.getProducer_Id());
        pst.setInt(4, f.getSupplier_Id());
        pst.setString(5, f.getNote());
        pst.setFloat(6, f.getPrice());
        pst.setInt(7, f.getStatus());
        pst.setString(8, f.getStart_date());
        pst.setInt(9, f.getAccount_Id());
        ProductDAO ProductDAO = new ProductDAO();
        Product product = ProductDAO.searchById(f.getProduct_Id());
        boolean success = false;
        if (pst.executeUpdate() > 0) {
            if (f.getStatus() == 1) {
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
        } else {
            success = true;
            conn.commit();
        }
        return success;
    }

    public boolean update(History f, int newStatus) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("update History set Quantity=?,Product_Id=?,Producer_Id=?,Supplier_Id=?,Note=?,Price=?,Status=?,Start_date=?,Account_Id=? where Id=?");
        pst.setInt(10, f.getHistoryId());
        pst.setInt(1, f.getQuantity());
        pst.setInt(2, f.getProduct_Id());
        pst.setInt(3, f.getProducer_Id());
        pst.setInt(4, f.getSupplier_Id());
        pst.setString(5, f.getNote());
        pst.setFloat(6, f.getPrice());
        pst.setInt(7, newStatus);
        pst.setString(8, f.getStart_date());
        pst.setInt(9, f.getAccount_Id());

        ProductDAO ProductDAO = new ProductDAO();
        Product product = ProductDAO.searchById(f.getProduct_Id());
        boolean success = false;
        if (f.getStatus() != 1) {
            if (newStatus == 1) {
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
                conn.commit();
            }
        } else {
            if (newStatus != 1) {
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
                } else {
                    conn.rollback();
                }
            }
        }

        return success;

    }

    public boolean delete(String id) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("delete from History where Id=?");
        pst.setString(1, id);
        return pst.executeUpdate() > 0;

    }

    public List<History> searchByAll(int product_id, int producer_id, int supplier_id, int account_id, String date, int status) throws SQLException {
        String sqlPre = "";
        List<Integer> argument = new ArrayList<Integer>();
        if (product_id != -1) {
            sqlPre += " Product_Id = " + product_id;
            argument.add(product_id);
        }
        if (producer_id != -1) {
            if (!sqlPre.equals("")) {
                sqlPre += " AND ";
            }
            sqlPre += " Producer_Id = " + producer_id;
            argument.add(producer_id);
        }
        if (supplier_id != -1) {
            if (!sqlPre.equals("")) {
                sqlPre += " AND ";
            }
            sqlPre += " Supplier_Id = " + supplier_id;
            argument.add(supplier_id);
        }
        if (account_id != -1) {
            if (!sqlPre.equals("")) {
                sqlPre += " AND ";
            }
            sqlPre += " Account_Id = " + account_id;
            argument.add(account_id);
        }
        if (status != -1) {
            if (!sqlPre.equals("")) {
                sqlPre += " AND ";
            }
            sqlPre += " Status = " + status;
            argument.add(status);
        }

        List<History> data = new ArrayList<>();
        String sql = "SELECT * FROM History  ";
        if (!sqlPre.equals("")) {
            sql = sql + "WHERE " + sqlPre;
        }
        PreparedStatement pst = conn.prepareStatement(sql);
        System.out.println(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            History h = new History(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7), rs.getInt(8), rs.getString(9), rs.getInt(10));
            data.add(h);
        }
        return data;
    }

}
