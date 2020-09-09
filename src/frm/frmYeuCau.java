/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frm;

import Entities.Account;
import java.io.File;
import Entities.Product;
import database.AccountDAO;
import database.FileDAO;
import database.ProductDAO;
import java.awt.Image;
import java.awt.print.PrinterException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Lab06
 */
public class frmYeuCau extends javax.swing.JFrame {

    ProductDAO ProductDAO;
    FileDAO FileDAO;
    Account currentAccount;
    AccountDAO AccountDAO;
    Entities.File f_Selected;

    JFrame dangNhap;

    /**
     * Creates new form frmYeuCau
     */
    public frmYeuCau(Account account, JFrame dangNhap) {
        try {
            this.ProductDAO = new ProductDAO();
            this.AccountDAO = new AccountDAO();
            this.FileDAO = new FileDAO();
            this.dangNhap = dangNhap;
        } catch (SQLException ex) {
            Logger.getLogger(frmYeuCau.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
        this.currentAccount = account;
        try {
            showProduct();
            showFile(FileDAO.getByStaffId(String.valueOf(currentAccount.getAccountID())));
            loadRequest();

            cboGender_In.addItem("Nam");
            cboGender_In.addItem("Nu");
            loadInfomation();
        } catch (SQLException ex) {
            Logger.getLogger(frmYeuCau.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadInfomation() {

        txtUserName_In.setText(currentAccount.getUserName());
        txtName_In.setText(currentAccount.getName());
        txtEmail_In.setText(currentAccount.getMail());
        txtPhone_In.setText(currentAccount.getPhone());
        txtAddress_In.setText(currentAccount.getAddress());
        txtBirthday_In.setDateFormatString(currentAccount.getDatetime());
        int gender = currentAccount.getGender();
        String genderSelceted = gender == 0 ? "Nam" : "Nu";
        for (int i = 0; i < cboGender_In.getItemCount(); i++) {
            String genderStr = (String) cboGender_In.getItemAt(i);
            if (genderStr.equalsIgnoreCase(genderSelceted)) {
                break;
            }
        }

        ImageIcon imageIcon = new ImageIcon(currentAccount.getImg());
        lblimg.setIcon(imageIcon);
        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        String[] spliter = currentAccount.getImg().split("/");
        txtNameImg.setText(spliter[spliter.length - 1]);
        txtLink.setText(currentAccount.getImg());
    }

    private void loadRequest() throws SQLException {
        cboStatus_Rq_F.removeAllItems();
        cboManager_Rq_F.removeAllItems();
        cboNamePr_Rq_F.removeAllItems();

        cboStatus_Rq_F.addItem("All");
        cboStatus_Rq_F.addItem("Dang xet duyet");
        cboStatus_Rq_F.addItem("Da duyet");
        cboStatus_Rq_F.addItem("Huy");

        List<Account> listAc = AccountDAO.getAll();
        List<Product> listPr = ProductDAO.getAll();

        cboManager_Rq_F.addItem("All");
        cboNamePr_Rq_F.addItem("All");
        for (Account ac : listAc) {
            if (ac.getStatus() == 2 || ac.getStatus() == 3) {
                cboManager_Rq_F.addItem(String.valueOf(ac.getAccountID()));
            }
        }
        for (Product product : listPr) {
            cboNamePr_Rq_F.addItem(String.valueOf(product.getProduct_Id()));
        }
    }

    private void showFile(List<Entities.File> files) {

        Vector col = new Vector();
        col.add("ID");
        col.add("Product_Id");
        col.add("Staff_Id");
        col.add("Manager_Id");
        col.add("Quantity");
        col.add("Status");
        col.add("Date");
        col.add("Note");
        Vector data = new Vector();
        for (Entities.File f : files) {
            Vector row = new Vector();
            row.add(f.getFileId());
            row.add(f.getProduct_Id());
            row.add(f.getAccount_Id());
            row.add(f.getManager_Id());
            row.add(f.getQuantity());
            if (f.getStatus() == 0) {
                row.add("Dang xet duyet");
            } else if (f.getStatus() == 1) {
                row.add("Da duyet");
            } else if (f.getStatus() == 2) {
                row.add("Huy");
            }
            row.add(f.getStart_date());
            row.add(f.getNote());

            data.add(row);
        }
        DefaultTableModel tblModel = new DefaultTableModel(data, col);
        tblRequest1.setModel(tblModel);
    }

    private void showProduct() throws SQLException {
        List<Product> Products = ProductDAO.getAll();
        Vector col = new Vector();
        col.add("Image");
        col.add("Id");
        col.add("Name");
        col.add("Unit");
        col.add("Quanity");
        col.add("Status");
        col.add("Note");

        //DefaultTableModel model = (DefaultTableModel) tblProduct_Pr.getModel();
        //model.setRowCount(0);
        Vector data = new Vector();
        for (Product p : Products) {
            Vector row = new Vector();
            if (p.getImg() != null) {
                ImageIcon imageIcon = new ImageIcon(p.getImg());
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon imageIcon2 = new ImageIcon(image);
                imageIcon2.setDescription(p.getImg());
                row.add(imageIcon2);
            } else {
                row.add(null);
            }
            row.add(p.getProduct_Id());
            row.add(p);
            row.add(p.getUnit());
            row.add(p.getQuantity());
            row.add(p.getStatus());
            row.add(p.getNote());
            data.add(row);
        }
        CustomTableModel cModel = new CustomTableModel(col, data);
        tblProduct_Pr.setModel(cModel);
        tblProduct_Pr.setRowHeight(100);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        paneRequest = new javax.swing.JPanel();
        txtFind = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        lblNotePr = new javax.swing.JLabel();
        btnAddPr = new javax.swing.JButton();
        lblQuantityPr = new javax.swing.JLabel();
        lblUnitPr = new javax.swing.JLabel();
        lblProductIdPr = new javax.swing.JLabel();
        lblNamePr = new javax.swing.JLabel();
        txtUnit = new javax.swing.JLabel();
        spinQuantity = new javax.swing.JSpinner();
        txtProducId = new javax.swing.JLabel();
        txtProductName = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProduct_Pr = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        paneInfo = new javax.swing.JPanel();
        lblUsernameIn = new javax.swing.JLabel();
        lblEmailIn = new javax.swing.JLabel();
        lblAddressIn = new javax.swing.JLabel();
        lblImgIn = new javax.swing.JLabel();
        lblNameIn = new javax.swing.JLabel();
        lblPhoneIn = new javax.swing.JLabel();
        lblBirthdayIn = new javax.swing.JLabel();
        lblGenderIn = new javax.swing.JLabel();
        lblPasswordIn = new javax.swing.JLabel();
        lblConfirmIn = new javax.swing.JLabel();
        txtUserName_In = new javax.swing.JTextField();
        txtName_In = new javax.swing.JTextField();
        txtEmail_In = new javax.swing.JTextField();
        txtPhone_In = new javax.swing.JTextField();
        txtAddress_In = new javax.swing.JTextField();
        cboGender_In = new javax.swing.JComboBox();
        btnUpdateIn = new javax.swing.JButton();
        btnBlockIn = new javax.swing.JButton();
        btnAddIMG = new javax.swing.JButton();
        txtNameImg = new javax.swing.JTextField();
        txtLink = new javax.swing.JTextField();
        lblimg = new javax.swing.JLabel();
        txtPass_In = new javax.swing.JPasswordField();
        txtPassCon_In = new javax.swing.JPasswordField();
        txtBirthday_In = new com.toedter.calendar.JDateChooser();
        paneRequest3 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblRequest1 = new javax.swing.JTable();
        txtFind_Rq = new javax.swing.JTextField();
        btnFindRq = new javax.swing.JButton();
        lblPrName_Rq_F = new javax.swing.JLabel();
        lblManager_Rq_F = new javax.swing.JLabel();
        lblLoandate_Rq_F = new javax.swing.JLabel();
        lblStatus_Rq_F = new javax.swing.JLabel();
        lblNote_Rq = new javax.swing.JLabel();
        cboStatus_Rq_F = new javax.swing.JComboBox<>();
        cboNamePr_Rq_F = new javax.swing.JComboBox<>();
        cboManager_Rq_F = new javax.swing.JComboBox<>();
        txtLoanDate_Rq_F = new javax.swing.JFormattedTextField();
        btnClear_Rq = new javax.swing.JButton();
        btnUpdate_Rq = new javax.swing.JButton();
        btnExport_Rq = new javax.swing.JButton();
        lblHistory_Id = new javax.swing.JLabel();
        lblQuantity_Rq = new javax.swing.JLabel();
        spinQuantity_Rq = new javax.swing.JSpinner();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtNote_Rq = new javax.swing.JTextArea();
        btnExport_Rq1 = new javax.swing.JButton();
        btnExport_Rq2 = new javax.swing.JButton();
        txtId_Hs = new javax.swing.JLabel();
        tabLogOut = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        lblNotePr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNotePr.setText("Note :");

        btnAddPr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnAddPr.setText("Send");
        btnAddPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPrActionPerformed(evt);
            }
        });

        lblQuantityPr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblQuantityPr.setText("Quantity :");

        lblUnitPr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUnitPr.setText("Unit :");

        lblProductIdPr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProductIdPr.setText("ProductId :");

        lblNamePr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNamePr.setText("Name :");

        txtUnit.setText("    ");

        txtProducId.setText("     ");

        txtProductName.setText("   ");

        tblProduct_Pr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Image", "Id", "Name", "Unit", "Quantity", "Status", "Note"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProduct_Pr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProduct_PrMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblProduct_Pr);

        txtNote.setColumns(20);
        txtNote.setRows(5);
        jScrollPane4.setViewportView(txtNote);

        javax.swing.GroupLayout paneRequestLayout = new javax.swing.GroupLayout(paneRequest);
        paneRequest.setLayout(paneRequestLayout);
        paneRequestLayout.setHorizontalGroup(
            paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneRequestLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequestLayout.createSequentialGroup()
                        .addComponent(btnAddPr, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(paneRequestLayout.createSequentialGroup()
                        .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblProductIdPr)
                            .addComponent(lblNamePr))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProducId)
                            .addComponent(txtProductName))
                        .addGap(159, 159, 159)
                        .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblQuantityPr)
                            .addComponent(lblUnitPr))
                        .addGap(27, 27, 27)
                        .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUnit)
                            .addGroup(paneRequestLayout.createSequentialGroup()
                                .addComponent(spinQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                                .addComponent(lblNotePr)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(189, 189, 189))
                    .addGroup(paneRequestLayout.createSequentialGroup()
                        .addComponent(btnFind)
                        .addGap(18, 18, 18)
                        .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(paneRequestLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        paneRequestLayout.setVerticalGroup(
            paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneRequestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFind)
                    .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 423, Short.MAX_VALUE)
                .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequestLayout.createSequentialGroup()
                        .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblProductIdPr, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProducId)
                            .addComponent(lblQuantityPr)
                            .addComponent(spinQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNotePr))
                        .addGap(19, 19, 19)
                        .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNamePr, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductName)
                            .addComponent(lblUnitPr)
                            .addComponent(txtUnit)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(btnAddPr, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(paneRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(paneRequestLayout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(312, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Request", paneRequest);

        lblUsernameIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUsernameIn.setText("Username :");

        lblEmailIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEmailIn.setText("Email");

        lblAddressIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAddressIn.setText("Address :");

        lblImgIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblImgIn.setText("Image :");

        lblNameIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNameIn.setText("Name :");

        lblPhoneIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPhoneIn.setText("Phone");

        lblBirthdayIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBirthdayIn.setText("Birthday");

        lblGenderIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblGenderIn.setText("Gender");

        lblPasswordIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPasswordIn.setText("Password :");

        lblConfirmIn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblConfirmIn.setText("Confirm :");

        txtUserName_In.setEditable(false);

        txtAddress_In.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddress_InActionPerformed(evt);
            }
        });

        btnUpdateIn.setText("Update");
        btnUpdateIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateInActionPerformed(evt);
            }
        });

        btnBlockIn.setText("Block account");
        btnBlockIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlockInActionPerformed(evt);
            }
        });

        btnAddIMG.setText("IMG");
        btnAddIMG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddIMGActionPerformed(evt);
            }
        });

        txtNameImg.setEditable(false);

        txtLink.setEditable(false);

        lblimg.setText("jLabel1");

        javax.swing.GroupLayout paneInfoLayout = new javax.swing.GroupLayout(paneInfo);
        paneInfo.setLayout(paneInfoLayout);
        paneInfoLayout.setHorizontalGroup(
            paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(paneInfoLayout.createSequentialGroup()
                        .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsernameIn)
                            .addComponent(lblNameIn)
                            .addComponent(lblEmailIn))
                        .addGap(18, 18, 18)
                        .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmail_In, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(txtName_In, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(txtUserName_In, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(cboGender_In, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(lblGenderIn)
                    .addGroup(paneInfoLayout.createSequentialGroup()
                        .addComponent(lblPhoneIn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(txtPhone_In, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneInfoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneInfoLayout.createSequentialGroup()
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblBirthdayIn)
                                    .addComponent(lblPasswordIn)
                                    .addComponent(lblConfirmIn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtPass_In, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                    .addComponent(txtAddress_In, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPassCon_In)
                                    .addComponent(txtBirthday_In, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(lblAddressIn)))
                    .addGroup(paneInfoLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(btnUpdateIn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btnBlockIn)))
                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneInfoLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(lblImgIn)
                        .addGap(46, 46, 46)
                        .addComponent(btnAddIMG, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneInfoLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLink, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNameImg, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72)
                        .addComponent(lblimg, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        paneInfoLayout.setVerticalGroup(
            paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneInfoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsernameIn)
                    .addComponent(lblImgIn)
                    .addComponent(txtUserName_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddIMG)
                    .addComponent(lblAddressIn)
                    .addComponent(txtAddress_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneInfoLayout.createSequentialGroup()
                        .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneInfoLayout.createSequentialGroup()
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblNameIn)
                                    .addComponent(txtName_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblEmailIn)
                                    .addComponent(txtEmail_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboGender_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblGenderIn)))
                            .addGroup(paneInfoLayout.createSequentialGroup()
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblBirthdayIn)
                                        .addComponent(txtNameImg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtBirthday_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblPasswordIn)
                                    .addComponent(txtPass_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13)
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblConfirmIn)
                                    .addComponent(txtPassCon_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneInfoLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lblPhoneIn))
                            .addGroup(paneInfoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(paneInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPhone_In, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnUpdateIn)
                                    .addComponent(btnBlockIn)))))
                    .addComponent(lblimg, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Information", paneInfo);

        paneRequest3.setPreferredSize(new java.awt.Dimension(1019, 500));

        tblRequest1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Product_Id", "Staff_Id", "Manager_Id", "Quantity", "Status", "Date", "Note"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRequest1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRequest1MouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(tblRequest1);

        txtFind_Rq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFind_RqKeyReleased(evt);
            }
        });

        btnFindRq.setText("Find");
        btnFindRq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindRqActionPerformed(evt);
            }
        });

        lblPrName_Rq_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPrName_Rq_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrName_Rq_F.setText("Product");

        lblManager_Rq_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblManager_Rq_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblManager_Rq_F.setText("Manager ");

        lblLoandate_Rq_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLoandate_Rq_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoandate_Rq_F.setText("Loan date");

        lblStatus_Rq_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus_Rq_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus_Rq_F.setText("Status");

        lblNote_Rq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNote_Rq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNote_Rq.setText("Note");

        btnClear_Rq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnClear_Rq.setText("Clear");
        btnClear_Rq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear_RqActionPerformed(evt);
            }
        });

        btnUpdate_Rq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdate_Rq.setText("Update");
        btnUpdate_Rq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate_RqActionPerformed(evt);
            }
        });

        btnExport_Rq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnExport_Rq.setText("Export");

        lblHistory_Id.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblHistory_Id.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHistory_Id.setText("Id");

        lblQuantity_Rq.setText("Quantity");

        txtNote_Rq.setColumns(20);
        txtNote_Rq.setRows(5);
        jScrollPane10.setViewportView(txtNote_Rq);

        btnExport_Rq1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnExport_Rq1.setText("Import");

        btnExport_Rq2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnExport_Rq2.setText("Print");
        btnExport_Rq2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExport_Rq2ActionPerformed(evt);
            }
        });

        txtId_Hs.setText("   ");

        javax.swing.GroupLayout paneRequest3Layout = new javax.swing.GroupLayout(paneRequest3);
        paneRequest3.setLayout(paneRequest3Layout);
        paneRequest3Layout.setHorizontalGroup(
            paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneRequest3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane17))
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneRequest3Layout.createSequentialGroup()
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblHistory_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(paneRequest3Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(lblNote_Rq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(paneRequest3Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(lblQuantity_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(51, 51, 51)
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spinQuantity_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtId_Hs))
                                .addGap(117, 117, 117)
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnExport_Rq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnUpdate_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnExport_Rq1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnExport_Rq2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(btnClear_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(paneRequest3Layout.createSequentialGroup()
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneRequest3Layout.createSequentialGroup()
                                        .addGap(205, 205, 205)
                                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboNamePr_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblPrName_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(txtFind_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneRequest3Layout.createSequentialGroup()
                                        .addComponent(cboManager_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboStatus_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(paneRequest3Layout.createSequentialGroup()
                                        .addComponent(lblManager_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblStatus_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(40, 40, 40)
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblLoandate_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLoanDate_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(57, 57, 57)
                                .addComponent(btnFindRq, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 166, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneRequest3Layout.setVerticalGroup(
            paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneRequest3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addComponent(lblPrName_Rq_F)
                        .addGap(12, 12, 12)
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFind_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboNamePr_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLoandate_Rq_F, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblManager_Rq_F)
                                .addComponent(lblStatus_Rq_F)))
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneRequest3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboManager_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboStatus_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(paneRequest3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtLoanDate_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnFindRq))))))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHistory_Id)
                            .addComponent(btnUpdate_Rq)
                            .addComponent(txtId_Hs))
                        .addGap(18, 18, 18)
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblQuantity_Rq)
                            .addComponent(spinQuantity_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNote_Rq)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnClear_Rq)
                        .addGap(18, 18, 18)
                        .addComponent(btnExport_Rq)
                        .addGap(18, 18, 18)
                        .addComponent(btnExport_Rq1)
                        .addGap(18, 18, 18)
                        .addComponent(btnExport_Rq2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("History", paneRequest3);

        tabLogOut.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabLogOutStateChanged(evt);
            }
        });
        tabLogOut.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tabLogOutPropertyChange(evt);
            }
        });
        jTabbedPane1.addTab("Log out", tabLogOut);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFindActionPerformed

    private void tblProduct_PrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProduct_PrMouseClicked
        // TODO add your handling code here:
        CustomTableModel model = (CustomTableModel) tblProduct_Pr.getModel();
        int productId = Integer.parseInt(model.getValueAt(tblProduct_Pr.getSelectedRow(), 1).toString());
        txtProducId.setText(String.valueOf(productId));
        String productName = model.getValueAt(tblProduct_Pr.getSelectedRow(), 2).toString();
        txtProductName.setText(productName);
        int quantity = Integer.parseInt(spinQuantity.getValue().toString());
        txtUnit.setText(model.getValueAt(tblProduct_Pr.getSelectedRow(), 3).toString());
    }//GEN-LAST:event_tblProduct_PrMouseClicked

    private void btnAddPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPrActionPerformed
        // TODO add your handling code here:
        //Id	Name	Product_Id	Account_Id	Manager_Id	Start_date	Note	Quantity	Status
        String Name = "";
        int Product_Id = Integer.parseInt(txtProducId.getText());
        int Account_Id = this.currentAccount.getAccountID();
        int Manager_Id = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calobj = Calendar.getInstance();
        String Start_date = df.format(calobj.getTime());
        String Note = txtNote.getText();
        int Quantity = Integer.parseInt(spinQuantity.getValue().toString());
        int Status = 0;
        Entities.File file = new Entities.File(0, Name, Product_Id, Account_Id, Manager_Id, Start_date, Note, Quantity, Status);
        try {
            if (FileDAO.insert(file)) {
                JOptionPane.showMessageDialog(null, "Gui yeu cau thanh cong!");

                showFile(FileDAO.getByStaffId(String.valueOf(currentAccount.getAccountID())));
            } else {
                JOptionPane.showMessageDialog(null, "Gui yeu cau that bai!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmYeuCau.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddPrActionPerformed

    private void txtAddress_InActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddress_InActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddress_InActionPerformed

    private void btnUpdateInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateInActionPerformed
        // TODO add your handling code here:
        Account ac = new Account();
        ac.setUserName(txtUserName_In.getText());
        ac.setAccountID(currentAccount.getAccountID());
        ac.setPhone(txtPhone_In.getText());
        ac.setName(txtName_In.getText());
        ac.setMail(txtEmail_In.getText());
        ac.setAddress(txtAddress_In.getText());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String birthday = sdf.format(txtBirthday_In.getDate());

        ac.setDatetime(birthday);
        //  ac.setNote(txtNote_AC.getText());
        ac.setStatus(currentAccount.getStatus());
        int gender = 0;
        String genderSelceted = cboGender_In.getSelectedItem().toString();
        if (genderSelceted.equalsIgnoreCase("Nam")) {
            gender = 0;
        } else {
            gender = 1;
        }
        ac.setImg(txtLink.getText());
        String pass = String.valueOf(txtPass_In.getPassword());
        if (pass.equals(String.valueOf(txtPassCon_In.getPassword()))) {
            ac.setPassword(pass);
            try {
                if (ac.getPassword().equals(currentAccount.getPassword())) {
                    if (AccountDAO.update(ac)) {
                        JOptionPane.showMessageDialog(null, "Update Sucess");
                        txtPass_In.setText("");
                        txtPassCon_In.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Password incorrect!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Xac nhan mat khau phai giong nhau!");
        }

    }//GEN-LAST:event_btnUpdateInActionPerformed

    private void btnBlockInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlockInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBlockInActionPerformed

    private void btnAddIMGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddIMGActionPerformed
        // TODO add your handling code here:
        //Hiep
        //String currentDirectoryPath = "/Users/mac/Desktop";

        // TODO add your handling code here:
        String currentDirectoryPath = "";

        JFileChooser imageFileChooser = new JFileChooser(currentDirectoryPath);
        imageFileChooser.setDialogTitle("Chooser Image....");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES", "png", "jpg", "jpeg");
        imageFileChooser.setFileFilter(fnef);
        int imageChooser = imageFileChooser.showOpenDialog(null);
        if (imageChooser == JFileChooser.APPROVE_OPTION) {
            try {
                File imageFile = imageFileChooser.getSelectedFile();//display img on txt
                String imageFileName = imageFile.getName();
                txtNameImg.setText(imageFileName);
                File out = new File("image/" + imageFile.getName());
                Path sorce = Paths.get(imageFile.getAbsolutePath());
                // Path desk = Paths.get("/Users/mac/Downloads/Project2");
                FileOutputStream desk = new FileOutputStream(out);
                Files.copy(sorce, desk);

                //Display img on lbl
                String imageFilePath = imageFile.getAbsolutePath();
                ImageIcon imageIcon = new ImageIcon(imageFilePath);
                //resize img
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                ImageIcon resizedImageIcon = new ImageIcon(image);
                lblimg.setIcon(imageIcon);
                //display link img
                txtLink.setText(imageFilePath);
            } catch (IOException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //JFileChooser j = new JFileChooser();
        //j.showDialog(null, currentDirectoryPath);
        //
        //        if (j.getSelectedFile() !=null) {
        //            JOptionPane.showConfirmDialog(null, j.getSelectedFile().getName());
        //        }
    }//GEN-LAST:event_btnAddIMGActionPerformed

    private void tblRequest1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRequest1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblRequest1.getModel();
        model.getValueAt(tblRequest1.getSelectedRow(), 1).toString();
        int FileId = Integer.parseInt(model.getValueAt(tblRequest1.getSelectedRow(), 0).toString());
        String Name = "";
        txtId_Hs.setText(model.getValueAt(tblRequest1.getSelectedRow(), 0).toString());
        int Product_Id = Integer.parseInt(model.getValueAt(tblRequest1.getSelectedRow(), 1).toString());
        int Account_Id = Integer.parseInt(model.getValueAt(tblRequest1.getSelectedRow(), 2).toString());
        int Manager_Id = Integer.parseInt(model.getValueAt(tblRequest1.getSelectedRow(), 3).toString());
        int Quantity = Integer.parseInt(model.getValueAt(tblRequest1.getSelectedRow(), 4).toString());
        String StatusStr = model.getValueAt(tblRequest1.getSelectedRow(), 5).toString();
        int status = 0;
        if (StatusStr.equalsIgnoreCase("Dang xet duyet")) {
            status = 0;
        } else if (StatusStr.equalsIgnoreCase("Da duyet")) {
            status = 1;
        } else if (StatusStr.equalsIgnoreCase("Huy")) {
            status = 2;
        }

        String Start_date = model.getValueAt(tblRequest1.getSelectedRow(), 6).toString();
        String Note = model.getValueAt(tblRequest1.getSelectedRow(), 7).toString();
        f_Selected = new Entities.File(FileId, Name, Product_Id, Account_Id, Manager_Id, Start_date, Note, Quantity, status);
        txtNote_Rq.setText(Note);
        spinQuantity_Rq.setValue(Quantity);

    }//GEN-LAST:event_tblRequest1MouseClicked
    public void findInDefaultTable(JTable table, String string) {
        TableRowSorter rowSorter = new TableRowSorter(table.getModel());
        table.setRowSorter(rowSorter);
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + string));
    }
    private void txtFind_RqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFind_RqKeyReleased
        // TODO add your handling code here:
        findInDefaultTable(tblRequest1, txtFind_Rq.getText());
    }//GEN-LAST:event_txtFind_RqKeyReleased

    private void btnFindRqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindRqActionPerformed
        // TODO add your handling code here:
        int product_id = cboNamePr_Rq_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboNamePr_Rq_F.getSelectedItem().toString());
        // int account_id = cboAccount_Rq_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboAccount_Rq_F.getSelectedItem().toString());
        int staff_id = currentAccount.getAccountID();
        int manager_id = cboManager_Rq_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboManager_Rq_F.getSelectedItem().toString());
        int status = -1;
        String statusStr = cboStatus_Rq_F.getSelectedItem().toString();
        if (!statusStr.equals("All")) {
            if (statusStr.equals("Dang xet duyet")) {
                status = 0;
            } else if (statusStr.equals("Da duyet")) {
                status = 1;
            } else if (statusStr.equals("Huy")) {
                status = 2;
            }
        }
        String date = txtLoanDate_Rq_F.getText();
        try {
            List<Entities.File> dataFile = FileDAO.searchByAll(product_id, staff_id, manager_id, date, status);
            showFile(dataFile);
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnFindRqActionPerformed

    private void btnUpdate_RqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate_RqActionPerformed
        // TODO add your handling code here:
        if (f_Selected != null) {
            if (f_Selected.getStatus() == 0) {
                try {
                    f_Selected.setQuantity(Integer.parseInt(spinQuantity_Rq.getValue().toString()));
                    f_Selected.setNote(txtNote_Rq.getText());
                    if (FileDAO.update(f_Selected, 0)) {
                        JOptionPane.showMessageDialog(null, "Change successful!");
                        f_Selected = null;
                        txtId_Hs.setText("");
                        txtNote_Rq.setText("");
                        spinQuantity_Rq.setValue(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Change fail!");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "You can only change the unsigned request!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Choose 1 request to change!");
        }
        try {
            showFile(FileDAO.getAll());
            showProduct();
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnUpdate_RqActionPerformed
    public void print(JTable table) {
        try {
            boolean printJatble = table.print();
            if (printJatble) {
                JOptionPane.showMessageDialog(null, "print");
            }
        } catch (PrinterException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void btnExport_Rq2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExport_Rq2ActionPerformed
        // TODO add your handling code here:
        print(tblRequest1);
    }//GEN-LAST:event_btnExport_Rq2ActionPerformed

    private void tabLogOutStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabLogOutStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tabLogOutStateChanged

    private void tabLogOutPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tabLogOutPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tabLogOutPropertyChange

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Log out")) {
            if (JOptionPane.showConfirmDialog(null, "Ban chac chan muon dang xuat ?") == 0) {
                dispose();
                dangNhap.setVisible(true);
            }
        }

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void btnClear_RqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear_RqActionPerformed
        // TODO add your handling code here:
        f_Selected = null;
        txtId_Hs.setText("");
        txtNote_Rq.setText("");
        spinQuantity_Rq.setValue(0);
    }//GEN-LAST:event_btnClear_RqActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddIMG;
    private javax.swing.JButton btnAddPr;
    private javax.swing.JButton btnBlockIn;
    private javax.swing.JButton btnClear_Rq;
    private javax.swing.JButton btnExport_Rq;
    private javax.swing.JButton btnExport_Rq1;
    private javax.swing.JButton btnExport_Rq2;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnFindRq;
    private javax.swing.JButton btnUpdateIn;
    private javax.swing.JButton btnUpdate_Rq;
    private javax.swing.JComboBox cboGender_In;
    private javax.swing.JComboBox<String> cboManager_Rq_F;
    private javax.swing.JComboBox<String> cboNamePr_Rq_F;
    private javax.swing.JComboBox<String> cboStatus_Rq_F;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAddressIn;
    private javax.swing.JLabel lblBirthdayIn;
    private javax.swing.JLabel lblConfirmIn;
    private javax.swing.JLabel lblEmailIn;
    private javax.swing.JLabel lblGenderIn;
    private javax.swing.JLabel lblHistory_Id;
    private javax.swing.JLabel lblImgIn;
    private javax.swing.JLabel lblLoandate_Rq_F;
    private javax.swing.JLabel lblManager_Rq_F;
    private javax.swing.JLabel lblNameIn;
    private javax.swing.JLabel lblNamePr;
    private javax.swing.JLabel lblNotePr;
    private javax.swing.JLabel lblNote_Rq;
    private javax.swing.JLabel lblPasswordIn;
    private javax.swing.JLabel lblPhoneIn;
    private javax.swing.JLabel lblPrName_Rq_F;
    private javax.swing.JLabel lblProductIdPr;
    private javax.swing.JLabel lblQuantityPr;
    private javax.swing.JLabel lblQuantity_Rq;
    private javax.swing.JLabel lblStatus_Rq_F;
    private javax.swing.JLabel lblUnitPr;
    private javax.swing.JLabel lblUsernameIn;
    private javax.swing.JLabel lblimg;
    private javax.swing.JPanel paneInfo;
    private javax.swing.JPanel paneRequest;
    private javax.swing.JPanel paneRequest3;
    private javax.swing.JSpinner spinQuantity;
    private javax.swing.JSpinner spinQuantity_Rq;
    private javax.swing.JTabbedPane tabLogOut;
    private javax.swing.JTable tblProduct_Pr;
    private javax.swing.JTable tblRequest1;
    private javax.swing.JTextField txtAddress_In;
    private com.toedter.calendar.JDateChooser txtBirthday_In;
    private javax.swing.JTextField txtEmail_In;
    private javax.swing.JTextField txtFind;
    private javax.swing.JTextField txtFind_Rq;
    private javax.swing.JLabel txtId_Hs;
    private javax.swing.JTextField txtLink;
    private javax.swing.JFormattedTextField txtLoanDate_Rq_F;
    private javax.swing.JTextField txtNameImg;
    private javax.swing.JTextField txtName_In;
    private javax.swing.JTextArea txtNote;
    private javax.swing.JTextArea txtNote_Rq;
    private javax.swing.JPasswordField txtPassCon_In;
    private javax.swing.JPasswordField txtPass_In;
    private javax.swing.JTextField txtPhone_In;
    private javax.swing.JLabel txtProducId;
    private javax.swing.JLabel txtProductName;
    private javax.swing.JLabel txtUnit;
    private javax.swing.JTextField txtUserName_In;
    // End of variables declaration//GEN-END:variables
}
