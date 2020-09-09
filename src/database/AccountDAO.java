/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Entities.Account;
import Entities.Category;
import Entities.History;
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
public class AccountDAO {

    Connection conn = null;

    public AccountDAO() throws SQLException {
        conn = ConnectionDB.getConnect();
    }

    public List<Account> getAll() throws SQLException {
        List<Account> data = new ArrayList<>();
        Statement st = (Statement) conn.createStatement();
        ResultSet rs = st.executeQuery("SElECT * FROM Account ");
        while (rs.next()) {
            Account a = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), rs.getInt(11), rs.getString(12));
            data.add(a);
        }
        return data;

    }

    public List<Account> searchByName(String name) throws SQLException {
        List<Account> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Account WHERE Name like ? ");
        pst.setString(1, '%' + name + '%');
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Account a = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), rs.getInt(11), rs.getString(12));
            data.add(a);
        }
        return data;

    }

    public List<Account> searchByEmail(String email) throws SQLException {
        List<Account> data = new ArrayList<>();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Account WHERE Mail like ? ");
        pst.setString(1, '%' + email + '%');
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Account a = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), rs.getInt(11), rs.getString(12));
            data.add(a);
        }
        return data;

    }

    public Account searchById(int id) throws SQLException {
        Account data = null;
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Account WHERE Id = ? ");
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            data = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), rs.getInt(11), rs.getString(12));
        }
        return data;

    }

    public boolean insert(Account a) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO Account(`Name`,`Phone`,`Address`,`Mail`,`Img`,`UserName`,`PassWord`,`Status`,`Note`,`Gender`,`Birthday`) values(?,?,?,?,?,?,?,?,?,?,?)");
//    PreparedStatement pst = conn.prepareStatement("INSERT INTO Account values(?,?,?,?,?,?,?,?,?,?,?,?)");
//    pst.setInt(1,a.getAccountID());
        pst.setString(1, a.getName());
        pst.setString(2, a.getPhone());
        pst.setString(3, a.getAddress());
        pst.setString(4, a.getMail());
        pst.setString(5, a.getImg());
        pst.setString(6, a.getUserName());
        pst.setString(7, a.getPassword());
        pst.setInt(8, a.getStatus());
        pst.setString(9, a.getNote());
        pst.setInt(10, a.getGender());
        pst.setString(11, a.getDatetime());
        return pst.executeUpdate() > 0;

    }

    public boolean update(Account a) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("update Account set Name=?,Phone=?,Address=?,Mail=?,Img=?,UserName=?,PassWord=?,Status=?,Note=?,Gender=?,Birthday=? where Id=?");
        pst.setString(1, a.getName());
        pst.setString(2, a.getPhone());
        pst.setString(3, a.getAddress());
        pst.setString(4, a.getMail());
        pst.setString(5, a.getImg());
        pst.setString(6, a.getUserName());
        pst.setString(7, a.getPassword());
        pst.setInt(8, a.getStatus());
        pst.setString(9, a.getNote());
        pst.setInt(10, a.getGender());
        pst.setString(11, a.getDatetime());
        pst.setInt(12, a.getAccountID());
        return pst.executeUpdate() > 0;

    }

    public boolean delete(String id) throws SQLException {
        conn.setAutoCommit(false);
        PreparedStatement pst = conn.prepareStatement("delete from Account where Id = ?");
        pst.setString(1, id);
        FileDAO FileDAO = new FileDAO();
        HistoryDAO HistoryDAO = new HistoryDAO();
        boolean success = false;
        List<Entities.File> listFl = FileDAO.getByStaffId(id);
        List<History> listHs = HistoryDAO.getByAccount(id);
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

    public List<Account> sortByZA() throws SQLException {
        List<Account> data = this.getAll();
        Collections.sort(data, new Comparator<Account>() {
            @Override
            public int compare(Account t, Account t1) {
                String name1 = t.getName();
                String[] tmp = name1.split("\\s+");
                name1 = tmp[tmp.length - 1];
                String name2 = t1.getName();
                tmp = name2.split("\\s+");
                name2 = tmp[tmp.length - 1];
                return name1.compareTo(name2);
            }
        });
        return data;
    }

    public List<Account> sortByAZ() throws SQLException {
        List<Account> data = this.getAll();
        Collections.sort(data, new Comparator<Account>() {
            @Override
            public int compare(Account t, Account t1) {
                String name1 = t.getName();
                String[] tmp = name1.split("\\s+");
                name1 = tmp[tmp.length - 1];
                String name2 = t1.getName();
                tmp = name2.split("\\s+");
                name2 = tmp[tmp.length - 1];
                return name2.compareTo(name1);
            }
        });
        return data;
    }
}
