/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frm;

import Entities.Account;
import database.ConnectionDB;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class frmDangNhap extends javax.swing.JFrame {

    /**
     * Creates new form frmDangNhap
     */
    Connection conn = null;
    File file = new File("save.txt");
    Account saveAc;

    public frmDangNhap() {
        initComponents();
        setLocationRelativeTo(null);
        try {
            conn = ConnectionDB.getConnect();
            saveAc = getAccount();
        } catch (SQLException ex) {
            Logger.getLogger(frmDangNhap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(frmDangNhap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbltieude = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        btnRegistration = new javax.swing.JButton();
        chkRemmenber = new javax.swing.JCheckBox();
        btnVietnamese = new javax.swing.JButton();
        btnEnglish = new javax.swing.JButton();
        btnFrench = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        lbltieude.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbltieude.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltieude.setText("Đăng Nhập");

        lblUsername.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsername.setText("Username:");

        lblPassword.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPassword.setText("Password:");

        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });
        txtUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserKeyReleased(evt);
            }
        });

        btnLogin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnRegistration.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRegistration.setText("Registration");
        btnRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrationActionPerformed(evt);
            }
        });

        chkRemmenber.setText("Remmenber Password");

        btnVietnamese.setText("Tiếng Việt");
        btnVietnamese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVietnameseActionPerformed(evt);
            }
        });

        btnEnglish.setText("English");
        btnEnglish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnglishActionPerformed(evt);
            }
        });

        btnFrench.setText("French");
        btnFrench.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrenchActionPerformed(evt);
            }
        });

        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(chkRemmenber))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(btnFrench)
                        .addGap(6, 6, 6)
                        .addComponent(btnEnglish)
                        .addGap(6, 6, 6)
                        .addComponent(btnVietnamese))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnRegistration))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(lbltieude, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(85, 85, 85))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lbltieude)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsername)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(chkRemmenber)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnRegistration))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFrench)
                    .addComponent(btnEnglish)
                    .addComponent(btnVietnamese)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public Account getAccount() throws FileNotFoundException {
        String user = null, password = null;
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                user = br.readLine();
                password = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(frmDangNhap.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(frmDangNhap.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Account saveAc = new Account();
        saveAc.setUserName(user);
        saveAc.setPassword(password);
        return saveAc;
    }

    public void savepass() throws IOException {
        if (!file.exists()) {
            file.createNewFile();  //if the file !exist create a new one
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
        bw.write(txtUser.getText()); //write the name
        bw.newLine(); //leave a new Line
        bw.write(txtPassword.getPassword()); //write the password
        bw.close(); //close the BufferdWriter

    }
    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserActionPerformed

    private void btnRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrationActionPerformed
        // TODO add your handling code here:
        frmDangKy fmk = new frmDangKy();
        fmk.setVisible(true);
    }//GEN-LAST:event_btnRegistrationActionPerformed

    private void btnVietnameseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVietnameseActionPerformed
        setLocaleInForm(new Locale("vi", "VN"));
    }//GEN-LAST:event_btnVietnameseActionPerformed

    private void btnEnglishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnglishActionPerformed
        setLocaleInForm(new Locale("en", "US"));
    }//GEN-LAST:event_btnEnglishActionPerformed

    private void btnFrenchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrenchActionPerformed
        setLocaleInForm(new Locale("cn", "CN"));
    }//GEN-LAST:event_btnFrenchActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:

        if (txtUser.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "vui lòng điền User Name");
        } else if (txtPassword.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "vui lòng điền Pass Word");
        } else {
            try {
                PreparedStatement ps = conn.prepareStatement("select * from Account WHERE UserName like ? AND PassWord like ?");
                ps.setString(1, txtUser.getText());
                ps.setString(2, String.valueOf(txtPassword.getPassword()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (chkRemmenber.isSelected()) {
                        savepass();
                    }
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công");
                    Account account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), rs.getInt(11), rs.getString(12));
                    if (account.getStatus() == 0) {
                        JOptionPane.showMessageDialog(this, "Tài khoản này bị khóa");
                    } else if (account.getStatus() == 1) {
                        frmYeuCau yeuCau = new frmYeuCau(account,this);
                        yeuCau.setVisible(true);
                    } else {
                        frmTrang_Chu trangChu = new frmTrang_Chu(account, this);
                        trangChu.setVisible(true);
                    }
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Đăng nhập thất bại");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyReleased

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtUser.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "vui lòng điền User Name");
            } else if (txtPassword.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "vui lòng điền Pass Word");
            } else {
                try {
                    PreparedStatement ps = conn.prepareStatement("select * from Account WHERE UserName like ? AND PassWord like ?");
                    ps.setString(1, txtUser.getText());
                    ps.setString(2, String.valueOf(txtPassword.getPassword()));
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Đăng nhập thành công");
                        Account account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), rs.getInt(11), rs.getString(12));
                        if (account.getStatus() == 0) {
                            JOptionPane.showMessageDialog(this, "Tài khoản này bị khóa");
                        } else if (account.getStatus() == 1) {
                            frmYeuCau yeuCau = new frmYeuCau(account,this);
                            yeuCau.setVisible(true);
                        } else {
                            frmTrang_Chu trangChu = new frmTrang_Chu(account, this);
                            trangChu.setVisible(true);
                        }
                        this.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Đăng nhập thất bại");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserKeyReleased
        // TODO add your handling code here:
        if (saveAc.getUserName() != null) {
            System.out.println(saveAc.getUserName());
            if (txtUser.getText().equals(saveAc.getUserName())) {
                txtPassword.setText(saveAc.getPassword());
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtUser.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "vui lòng điền User Name");
            } else if (txtPassword.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "vui lòng điền Pass Word");
            } else {
                try {
                    PreparedStatement ps = conn.prepareStatement("select * from Account WHERE UserName like ? AND PassWord like ?");
                    ps.setString(1, txtUser.getText());
                    ps.setString(2, String.valueOf(txtPassword.getPassword()));
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Đăng nhập thành công");
                        Account account = new Account(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getString(10), rs.getInt(11), rs.getString(12));
                        if (account.getStatus() == 0) {
                            JOptionPane.showMessageDialog(this, "Tài khoản này bị khóa");
                        } else if (account.getStatus() == 1) {
                            frmYeuCau yeuCau = new frmYeuCau(account,this);
                            yeuCau.setVisible(true);
                        } else {
                            frmTrang_Chu trangChu = new frmTrang_Chu(account, this);
                            trangChu.setVisible(true);
                        }
                        this.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Đăng nhập thất bại");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }//GEN-LAST:event_txtUserKeyReleased

    private void setLocaleInForm(Locale local) {
        setL10N(local);
//        System.out.println(locale.getDisplayCountry());
        ResourceBundle rb = ResourceBundle.getBundle("resources.resources", local);
        lblUsername.setText(rb.getString("userName"));
        lblPassword.setText(rb.getString("password"));
        btnLogin.setText(rb.getString("login"));
        //btnReset.setText(rb.getString("reset"));
    }

    private void setL10N(Locale local) {
        DateFormat formatDate = DateFormat.getDateInstance(DateFormat.FULL, local);
        NumberFormat formatCurrency = NumberFormat.getCurrencyInstance(local);
        NumberFormat formatNumber = NumberFormat.getNumberInstance(local);
        //    lblDateTime.setText(formatDate.format(Calendar.getInstance().getTime()));
        //    lblCurrency.setText(formatCurrency.format(696000.123));
        //    lblNumber.setText(formatNumber.format(969000.678));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmDangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmDangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmDangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmDangNhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmDangNhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnglish;
    private javax.swing.JButton btnFrench;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegistration;
    private javax.swing.JButton btnVietnamese;
    private javax.swing.JCheckBox chkRemmenber;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lbltieude;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}