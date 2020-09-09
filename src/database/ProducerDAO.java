/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Entities.History;
import Entities.Producer;
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
public class ProducerDAO {

    Connection conn = null;

    public ProducerDAO() throws SQLException {
        conn = ConnectionDB.getConnect();
    }

    /**
     *
     * @return
     */
    public List<Producer> getAll() throws SQLException {
        List<Producer> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM Producer ");
        while (rs.next()) {
            Producer p = new Producer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5));
            data.add(p);
        }
        return data;
    }

    public List<Producer> searchByName(String name) throws SQLException {
        List<Producer> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Producer WHERE Name like ? ");
        pst.setString(1, '%' + name + '%');
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Producer p = new Producer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5));
            data.add(p);
        }
        return data;
    }

    public Producer searchById(int id) throws SQLException {
        Producer data = null;
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Producer WHERE Id = ? ");
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            data = new Producer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5));
        }
        return data;
    }

    public boolean insert(Producer f) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO Producer(`Name`,`Status`, `Note`, `Nation`) values(?,?,?,?)");
//        PreparedStatement pst = conn.prepareStatement("INSERT INTO producer values(?,?,?,?,?)");
//        pst.setInt(1, f.getProducer_Id());
        pst.setString(1, f.getName());
        pst.setInt(2, f.getStatus());
        pst.setString(3, f.getNote());
        pst.setString(4, f.getNation());
        return pst.executeUpdate() > 0;
    }

    public boolean update(Producer f) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("update Producer set Name=?,Status=?,Note=?,Nation=? where Id=?");
        pst.setInt(5, f.getProducer_Id());
        pst.setString(1, f.getName());
        pst.setInt(2, f.getStatus());
        pst.setString(3, f.getNote());
        pst.setString(4, f.getNation());
        return pst.executeUpdate() > 0;

    }

    public boolean delete(String id) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("delete from Producer where Id=?");
        pst.setString(1, id);
        boolean success = false;
        HistoryDAO HistoryDAO = new HistoryDAO();
        List<History> listHs = HistoryDAO.getByProducer(id);
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

    public List<Producer> sortByZA() throws SQLException {
        List<Producer> data = this.getAll();
        Collections.sort(data, new Comparator<Producer>() {
            @Override
            public int compare(Producer t, Producer t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        return data;
    }

    public List<Producer> sortByAZ() throws SQLException {
        List<Producer> data = this.getAll();
        Collections.sort(data, new Comparator<Producer>() {
            @Override
            public int compare(Producer t, Producer t1) {
                return t1.getName().compareTo(t.getName());
            }
        });
        return data;
    }
}
