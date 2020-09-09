/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frm;

import Entities.Account;
import Entities.Category;
import Entities.History;
import Entities.Producer;
import Entities.Product;
import Entities.Supplier;
import database.AccountDAO;
import database.CategoryDAO;
import database.FileDAO;
import database.HistoryDAO;
import database.ProducerDAO;
import database.ProductDAO;
import database.SupplierDAO;
import java.awt.Image;
import java.awt.print.PrinterException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import static org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory.model;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class frmTrang_Chu extends javax.swing.JFrame {

    CategoryDAO CategoryDAO;
    AccountDAO AccountDAO;
    FileDAO FileDAO;
    HistoryDAO HistoryDAO;
    ProducerDAO ProducerDAO;
    ProductDAO ProductDAO;
    SupplierDAO SupplierDAO;

    Category c_Selected;
    Product pr_Selected;
    Producer pd_Selected;
    Supplier sp_Selected;
    Account ac_Selected;
    Entities.File f_Selected;
    History ht_Selected;

    Account currentAccount;
    ImportDataFromExecel ImportDataFromExecel = new ImportDataFromExecel();

    /**
     * Creates new form frmTrang_Chu
     */
    JFrame dangNhap;

    public frmTrang_Chu(Account ac, JFrame dangNhap) throws SQLException {
        // public frmTrang_Chu() throws SQLException {
        initComponents();
        this.dangNhap = dangNhap;
        this.currentAccount = ac;
        setLocationRelativeTo(null);
        if (ac.getStatus() != 3) {
            paneAdmin_Ac.setVisible(false);
            paneAdmin_Pd.setVisible(false);
            paneAdmin_Sp.setVisible(false);
            btnUpdate_Ht.setVisible(false);
        }
        CategoryDAO = new CategoryDAO();
        AccountDAO = new AccountDAO();
        FileDAO = new FileDAO();
        HistoryDAO = new HistoryDAO();
        ProducerDAO = new ProducerDAO();
        ProductDAO = new ProductDAO();
        SupplierDAO = new SupplierDAO();
        showCategory();
        showProduct();
        showHistory();
        showAccount();
        showProducer();
        showSupplier();
        showFile(FileDAO.getAll());
        setCommand();
        loadRequest();
        loadPaneProduct();
        loadInfomation();
        loadPaneHistory();
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

    private void setCommand() {
        rdoShow_Ca.setActionCommand("show");
        rdoHide_Ca.setActionCommand("hide");
        rdoShow_Pr.setActionCommand("show");
        rdoHide_Pr.setActionCommand("hide");
        rdoShow_Sp.setActionCommand("show");
        rdoHide_Sp.setActionCommand("hide");
        rdoShow_Pd.setActionCommand("show");
        rdoHide_Pd.setActionCommand("hide");

        cboGender_Ac.addItem("Nam");
        cboGender_Ac.addItem("Nu");

        cboGender_In.addItem("Nam");
        cboGender_In.addItem("Nu");

        cboStatus_Ac.addItem("Khoa");
        cboStatus_Ac.addItem("Nhan Vien");
        cboStatus_Ac.addItem("Quan Ly");
        cboStatus_Ac.addItem("Admin");

    }

    private void loadRequest() throws SQLException {
        cboStatus_Rq_F.removeAllItems();
        cboStatus_Rq.removeAllItems();
        cboStaff_Rq_F.removeAllItems();
        cboManager_Rq_F.removeAllItems();
        cboNamePr_Rq_F.removeAllItems();

        cboStatus_Rq_F.addItem("All");
        cboStatus_Rq_F.addItem("Dang xet duyet");
        cboStatus_Rq_F.addItem("Da duyet");
        cboStatus_Rq_F.addItem("Huy");
        cboStatus_Rq.addItem("Dang xet duyet");
        cboStatus_Rq.addItem("Da duyet");
        cboStatus_Rq.addItem("Huy");
        List<Account> listAc = AccountDAO.getAll();
        List<Product> listPr = ProductDAO.getAll();

        cboStaff_Rq_F.addItem("All");
        cboManager_Rq_F.addItem("All");
        cboNamePr_Rq_F.addItem("All");
        for (Account ac : listAc) {
            if (ac.getStatus() == 1) {
                cboStaff_Rq_F.addItem(String.valueOf(ac.getAccountID()));
            } else if (ac.getStatus() == 2 || ac.getStatus() == 3) {
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

    private void showCategory() throws SQLException {
        List<Category> Categories = CategoryDAO.getAll();
        Vector col = new Vector();
        col.add("ID");
        col.add("Name");
        col.add("Status");
        col.add("Note");
        Vector data = new Vector();
        for (Category c : Categories) {
            Vector row = new Vector();
            row.add(c.getCategory_Id());
            row.add(c);
            int status = c.getStatus();
            if (status == 0) {
                row.add("Hide");
            } else {
                row.add("Show");
            }
            row.add(c.getNote());
            data.add(row);
        }
        DefaultTableModel tblModel = new DefaultTableModel(data, col);
        tblCategory_Ca.setModel(tblModel);

    }

    private void showCategory(List<Category> Categories) throws SQLException {
        Vector col = new Vector();
        col.add("ID");
        col.add("Name");
        col.add("Status");
        col.add("Note");
        Vector data = new Vector();
        for (Category c : Categories) {
            Vector row = new Vector();
            row.add(c.getCategory_Id());
            row.add(c);
            int status = c.getStatus();
            if (status == 0) {
                row.add("Hide");
            } else {
                row.add("Show");
            }
            row.add(c.getNote());
            data.add(row);
        }
        DefaultTableModel tblModel = new DefaultTableModel(data, col);
        tblCategory_Ca.setModel(tblModel);

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
            int status = p.getStatus();
            if (status == 0) {
                row.add("Hide");
            } else {
                row.add("Show");
            }
            row.add(p.getNote());
            data.add(row);
        }
        CustomTableModel cModel = new CustomTableModel(col, data);
        tblProduct_Pr.setModel(cModel);
        tblProduct_Pr.setRowHeight(100);
    }

    private void showProduct(List<Product> Products) throws SQLException {
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
            int status = p.getStatus();
            if (status == 0) {
                row.add("Hide");
            } else {
                row.add("Show");
            }
            row.add(p.getNote());
            data.add(row);
        }
        CustomTableModel cModel = new CustomTableModel(col, data);
        tblProduct_Pr.setModel(cModel);
        tblProduct_Pr.setRowHeight(100);
    }

    private void showHistory() throws SQLException {
        List<History> Histories = HistoryDAO.getAll();
        Vector col = new Vector();
        col.add("ID");
        col.add("Product Id");
        col.add("Producer Id");
        col.add("Supplier Id");
        col.add("Price ");
        col.add("Quantity");
        col.add("Status");
        col.add("Date");
        col.add("Note");
        col.add("Account Id");

        Vector data = new Vector();
        for (History h : Histories) {
            Vector row = new Vector();
            row.add(h.getHistoryId());
            row.add(h.getProduct_Id());
            row.add(h.getProducer_Id());
            row.add(h.getSupplier_Id());
            row.add(h.getPrice());
            row.add(h.getQuantity());
            if (h.getStatus() == 0) {
                row.add("Dang xet duyet");
            } else if (h.getStatus() == 1) {
                row.add("Da duyet");
            } else if (h.getStatus() == 2) {
                row.add("Huy");
            }
            row.add(h.getStart_date());
            row.add(h.getNote());
            row.add(h.getAccount_Id());
            data.add(row);

        }
        DefaultTableModel tblModel = new DefaultTableModel(data, col);
        tblRequest_Ht.setModel(tblModel);
        //   loadPaneHistory();
    }

    private void showHistory(List<History> Histories) throws SQLException {
        Vector col = new Vector();
        col.add("ID");
        col.add("Product Id");
        col.add("Producer Id");
        col.add("Supplier Id");
        col.add("Price ");
        col.add("Quantity");
        col.add("Status");
        col.add("Date");
        col.add("Note");
        col.add("Account Id");

        Vector data = new Vector();
        for (History h : Histories) {
            Vector row = new Vector();
            row.add(h.getHistoryId());
            row.add(h.getProduct_Id());
            row.add(h.getProducer_Id());
            row.add(h.getSupplier_Id());
            row.add(h.getPrice());
            row.add(h.getQuantity());
            if (h.getStatus() == 0) {
                row.add("Dang xet duyet");
            } else if (h.getStatus() == 1) {
                row.add("Da duyet");
            } else if (h.getStatus() == 2) {
                row.add("Huy");
            }
            row.add(h.getStart_date());
            row.add(h.getNote());
            row.add(h.getAccount_Id());
            data.add(row);

        }
        DefaultTableModel tblModel = new DefaultTableModel(data, col);
        tblRequest_Ht.setModel(tblModel);
        // loadPaneHistory();
    }

    private void showAccount() throws SQLException {
        List<Account> Accounts = AccountDAO.getAll();
        Vector col = new Vector();
        col.add("Image");
        col.add("ID");
        col.add("UserName");
        col.add("Name");
        col.add("Phone ");
        col.add("Email");
        col.add("Address");
        col.add("Gender");
        col.add("Birthday");
        col.add("Status");
        col.add("Note");
        Vector data = new Vector();
        for (Account a : Accounts) {
            Vector row = new Vector();
            if (a.getImg() != null) {
                ImageIcon imageIcon = new ImageIcon(a.getImg());
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon imageIcon2 = new ImageIcon(image);
                imageIcon2.setDescription(a.getImg());
                row.add(imageIcon2);
            } else {
                row.add(null);
            }
            row.add(a.getAccountID());
            row.add(a.getUserName());
            row.add(a.getName());
            row.add(a.getPhone());
            row.add(a.getMail());
            row.add(a.getAddress());
            if (a.getGender() == 0) {
                row.add("Nam");
            } else {
                row.add("Nu");
            }
            row.add(a.getDatetime());
            if (a.getStatus() == 0) {
                row.add("Khoa");
            } else if (a.getStatus() == 1) {
                row.add("Nhan vien");
            } else if (a.getStatus() == 2) {
                row.add("Quan Ly");
            } else if (a.getStatus() == 3) {
                row.add("Admin");
            }
            row.add(a.getNote());

            data.add(row);

        }
        CustomTableModel tblModel = new CustomTableModel(col, data);
        tblAccount_acc.setModel(tblModel);
        tblAccount_acc.setRowHeight(100);

    }

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

    private void showAccount(List<Account> Accounts) throws SQLException {
        Vector col = new Vector();
        col.add("Image");
        col.add("ID");
        col.add("UserName");
        col.add("Name");
        col.add("Phone ");
        col.add("Email");
        col.add("Address");
        col.add("Gender");
        col.add("Birthday");
        col.add("Status");
        col.add("Note");
        Vector data = new Vector();
        for (Account a : Accounts) {
            Vector row = new Vector();
            if (a.getImg() != null) {
                ImageIcon imageIcon = new ImageIcon(a.getImg());
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon imageIcon2 = new ImageIcon(image);
                imageIcon2.setDescription(a.getImg());
                row.add(imageIcon2);
            } else {
                row.add(null);
            }
            row.add(a.getAccountID());
            row.add(a.getUserName());
            row.add(a.getName());
            row.add(a.getPhone());
            row.add(a.getMail());
            row.add(a.getAddress());
            if (a.getGender() == 0) {
                row.add("Nam");
            } else {
                row.add("Nu");
            }
            row.add(a.getDatetime());
            if (a.getStatus() == 0) {
                row.add("Khoa");
            } else if (a.getStatus() == 1) {
                row.add("Nhan vien");
            } else if (a.getStatus() == 2) {
                row.add("Quan Ly");
            } else if (a.getStatus() == 3) {
                row.add("Admin");
            }
            row.add(a.getNote());

            data.add(row);

        }
        CustomTableModel tblModel = new CustomTableModel(col, data);
        tblAccount_acc.setModel(tblModel);
        tblAccount_acc.setRowHeight(100);

    }

    private void showProducer() throws SQLException {
        List<Producer> Producers = ProducerDAO.getAll();
        Vector col = new Vector();
        col.add("ID");
        col.add("Name");
        col.add("Nation");
        col.add("Status");
        col.add("Note");
        Vector data = new Vector();
        for (Producer ps : Producers) {
            Vector row = new Vector();
            row.add(ps.getProducer_Id());
            row.add(ps);
            row.add(ps.getNation());
            int status = ps.getStatus();
            if (status == 0) {
                row.add("Hide");
            } else {
                row.add("Show");
            }
            row.add(ps.getNote());
            data.add(row);

        }
        DefaultTableModel tblModel = new DefaultTableModel(data, col);
        tblProducer_Per.setModel(tblModel);

    }

    private void showProducer(List<Producer> Producers) throws SQLException {
        Vector col = new Vector();
        col.add("ID");
        col.add("Name");
        col.add("Nation");
        col.add("Status");
        col.add("Note");
        Vector data = new Vector();
        for (Producer ps : Producers) {
            Vector row = new Vector();
            row.add(ps.getProducer_Id());
            row.add(ps);
            row.add(ps.getNation());
            int status = ps.getStatus();
            if (status == 0) {
                row.add("Hide");
            } else {
                row.add("Show");
            }
            row.add(ps.getNote());
            data.add(row);

        }
        DefaultTableModel tblModel = new DefaultTableModel(data, col);
        tblProducer_Per.setModel(tblModel);

    }

    private void showSupplier() throws SQLException {
        List<Supplier> Suppliers = SupplierDAO.getAll();
        Vector col = new Vector();
        col.add("Image");
        col.add("ID");
        col.add("Name");
        col.add("Phone");
        col.add("Address");
        col.add("Email");
        col.add("Status");
        col.add("Note");
        Vector data = new Vector();
        for (Supplier s : Suppliers) {
            Vector row = new Vector();
            if (s.getImg() != null) {
                ImageIcon imageIcon = new ImageIcon(s.getImg());
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon imageIcon2 = new ImageIcon(image);
                imageIcon2.setDescription(s.getImg());
                row.add(imageIcon2);
            } else {
                row.add(null);
            }
            row.add(s.getSupplier_Id());
            row.add(s);
            row.add(s.getPhone());
            row.add(s.getAddress());
            row.add(s.getEmail());
            row.add(s.getStatus());
            row.add(s.getNote());

            data.add(row);

        }
        CustomTableModel tblModel = new CustomTableModel(col, data);
        tblSupplier_Sup.setModel(tblModel);
        tblSupplier_Sup.setRowHeight(100);

    }

    private void showSupplier(List<Supplier> Suppliers) throws SQLException {
        Vector col = new Vector();
        col.add("Image");
        col.add("ID");
        col.add("Name");
        col.add("Phone");
        col.add("Address");
        col.add("Email");
        col.add("Status");
        col.add("Note");
        Vector data = new Vector();
        for (Supplier s : Suppliers) {
            Vector row = new Vector();
            if (s.getImg() != null) {
                ImageIcon imageIcon = new ImageIcon(s.getImg());
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon imageIcon2 = new ImageIcon(image);
                imageIcon2.setDescription(s.getImg());
                row.add(imageIcon2);
            } else {
                row.add(null);
            }
            row.add(s.getSupplier_Id());
            row.add(s);
            row.add(s.getPhone());
            row.add(s.getAddress());
            row.add(s.getEmail());
            row.add(s.getStatus());
            row.add(s.getNote());

            data.add(row);

        }
        CustomTableModel tblModel = new CustomTableModel(col, data);
        tblSupplier_Sup.setModel(tblModel);
        tblSupplier_Sup.setRowHeight(100);

    }

    public void exportCustomTable(JTable table) {
        FileOutputStream excelFOU = null;
        BufferedOutputStream excelBOU = null;
        XSSFWorkbook excelJTableExport = null;

        CustomTableModel model = (CustomTableModel) table.getModel();

        JFileChooser excelFileChooser = new JFileChooser("/Users/mac/Desktop");

        FileNameExtensionFilter fnef = new FileNameExtensionFilter("EXCEL FILTERS", "xls", "xlsx", "xlsm");

        excelFileChooser.setFileFilter(fnef);
        excelFileChooser.setDialogTitle("Save As ..");
        int chooser = excelFileChooser.showSaveDialog(null);

        if (chooser == JFileChooser.APPROVE_OPTION) {
            try {
                excelJTableExport = new XSSFWorkbook();
                XSSFSheet excelSheet = excelJTableExport.createSheet("Jtable Export");

                for (int i = 0; i < model.getRowCount(); i++) {
                    XSSFRow excelRow = excelSheet.createRow(i);

                    for (int j = 0; j < model.getColumnCount(); j++) {
                        XSSFCell excelCell = excelRow.createCell(j);
                        if (model.getColumnName(j).equalsIgnoreCase("Image")) {
                            Path currentPath = Paths.get("");
                            String imageAbsPath = currentPath.toAbsolutePath().toString();
                            imageAbsPath = imageAbsPath + "/" + model.getValueAt(i, j).toString();
                            excelCell.setCellValue(imageAbsPath);

                        } else {
                            excelCell.setCellValue(model.getValueAt(i, j).toString());
                        }
                    }
                }
                excelFOU = new FileOutputStream(excelFileChooser.getSelectedFile() + ".xlsx");

                excelBOU = new BufferedOutputStream(excelFOU);

                excelJTableExport.write(excelBOU);

                JOptionPane.showMessageDialog(null, "Exported Successfully");

            } catch (FileNotFoundException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (excelBOU != null) {
                        excelBOU.close();
                    }
                    if (excelFOU != null) {
                        excelFOU.close();
                    }
                    if (excelJTableExport != null) {
                        excelJTableExport.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void exportDefaultTable(JTable table) {
        FileOutputStream excelFOU = null;
        BufferedOutputStream excelBOU = null;
        XSSFWorkbook excelJTableExport = null;

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        JFileChooser excelFileChooser = new JFileChooser("/Users/mac/Desktop");

        FileNameExtensionFilter fnef = new FileNameExtensionFilter("EXCEL FILTERS", "xls", "xlsx", "xlsm");

        excelFileChooser.setFileFilter(fnef);
        excelFileChooser.setDialogTitle("Save As ..");
        int chooser = excelFileChooser.showSaveDialog(null);

        if (chooser == JFileChooser.APPROVE_OPTION) {
            try {
                excelJTableExport = new XSSFWorkbook();
                XSSFSheet excelSheet = excelJTableExport.createSheet("Jtable Export");

                for (int i = 0; i < model.getRowCount(); i++) {
                    XSSFRow excelRow = excelSheet.createRow(i);

                    for (int j = 0; j < model.getColumnCount(); j++) {
                        XSSFCell excelCell = excelRow.createCell(j);
                        if (model.getColumnName(j).equalsIgnoreCase("Image")) {
                            Path currentPath = Paths.get("");
                            String imageAbsPath = currentPath.toAbsolutePath().toString();
                            imageAbsPath = imageAbsPath + "/" + model.getValueAt(i, j).toString();
                            excelCell.setCellValue(imageAbsPath);

                        } else {
                            excelCell.setCellValue(model.getValueAt(i, j).toString());
                        }
                    }
                }
                excelFOU = new FileOutputStream(excelFileChooser.getSelectedFile() + ".xlsx");

                excelBOU = new BufferedOutputStream(excelFOU);

                excelJTableExport.write(excelBOU);

                JOptionPane.showMessageDialog(null, "Exported Successfully");

            } catch (FileNotFoundException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (excelBOU != null) {
                        excelBOU.close();
                    }
                    if (excelFOU != null) {
                        excelFOU.close();
                    }
                    if (excelJTableExport != null) {
                        excelJTableExport.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void findInDefaultTable(JTable table, String string) {
        TableRowSorter rowSorter = new TableRowSorter(table.getModel());
        table.setRowSorter(rowSorter);
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + string));
    }

    public boolean checkName(String string) {
        String regexName = "^[\\p{L}]+([\\s]+[\\p{L}]+)*$";
        return string.matches(regexName);
    }

    public boolean checkPhone(String string) {
        String regexPhone = "^0{1,1}\\d{9,10}$";
        return string.matches(regexPhone);
    }

    public boolean checkEmail(String string) {
        String regexEmail = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
        return string.matches(regexEmail);
    }

    public void searchDefaultTable(JTable talbe, String string) {
        DefaultTableModel model = (DefaultTableModel) talbe.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
        talbe.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(string));
    }

    public void searchCustomTable(JTable talbe, String string) {
        CustomTableModel model = (CustomTableModel) talbe.getModel();
        TableRowSorter<CustomTableModel> sorter = new TableRowSorter<CustomTableModel>(model);
        talbe.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(string));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupStatus_Ca = new javax.swing.ButtonGroup();
        groupStatus_Pr = new javax.swing.ButtonGroup();
        groupStatus_Sp = new javax.swing.ButtonGroup();
        groupStatus_Pd = new javax.swing.ButtonGroup();
        tbljpane = new javax.swing.JTabbedPane();
        tabProducerMain = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProducer_Per = new javax.swing.JTable();
        btnFind_Pd = new javax.swing.JButton();
        txtFind_Pd = new javax.swing.JTextField();
        btnAZ_Pd = new javax.swing.JButton();
        paneAdmin_Pd = new javax.swing.JPanel();
        btnAdd_Pd = new javax.swing.JButton();
        btnClear_Pd = new javax.swing.JButton();
        rdoHide_Pd = new javax.swing.JRadioButton();
        rdoShow_Pd = new javax.swing.JRadioButton();
        lblNote_Pd = new javax.swing.JLabel();
        btnRemove_Pd = new javax.swing.JButton();
        lblNation_Pd = new javax.swing.JLabel();
        lblStatus_Pd = new javax.swing.JLabel();
        btnUpdate_Pd = new javax.swing.JButton();
        lblName_Pd = new javax.swing.JLabel();
        txtName_Pd = new javax.swing.JTextField();
        btnExport_Pd = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtNote_Pd = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        cboNation_Pd = new javax.swing.JTextField();
        btnZA_Pd = new javax.swing.JButton();
        tabProductMain = new javax.swing.JTabbedPane();
        paneCategory = new javax.swing.JPanel();
        lblNoteCa = new javax.swing.JLabel();
        lblNameCa = new javax.swing.JLabel();
        txtName_Ca = new javax.swing.JTextField();
        btnUpdate_Ca = new javax.swing.JButton();
        btnAdd_Ca = new javax.swing.JButton();
        btnRemove_Ca = new javax.swing.JButton();
        btnClear_Ca = new javax.swing.JButton();
        btnExport_Ca = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCategory_Ca = new javax.swing.JTable();
        btnFind_Ca = new javax.swing.JButton();
        txtFind_Ca = new javax.swing.JTextField();
        btnFinddow_Ca = new javax.swing.JButton();
        btnFindup_Ca = new javax.swing.JButton();
        lblStatusCa = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtNote_Ca = new javax.swing.JTextArea();
        rdoHide_Ca = new javax.swing.JRadioButton();
        rdoShow_Ca = new javax.swing.JRadioButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        paneProduct = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduct_Pr = new javax.swing.JTable();
        lblProductIdPr = new javax.swing.JLabel();
        lblNamePr = new javax.swing.JLabel();
        lblCategoryPr = new javax.swing.JLabel();
        txtProductId_Pr = new javax.swing.JTextField();
        txtName_Pr = new javax.swing.JTextField();
        cboCategory_Pr = new javax.swing.JComboBox();
        lblUnitPr = new javax.swing.JLabel();
        txtUnit_Pr = new javax.swing.JTextField();
        lblNotePr = new javax.swing.JLabel();
        txtNote_Pr = new javax.swing.JTextField();
        btnAdd_Pr = new javax.swing.JButton();
        btnUpdate_Pr = new javax.swing.JButton();
        btnDelete_Pr = new javax.swing.JButton();
        btnReset_Pr = new javax.swing.JButton();
        txtFind_Pr = new javax.swing.JTextField();
        btnFind_Pr = new javax.swing.JButton();
        rdoShow_Pr = new javax.swing.JRadioButton();
        rdoHide_Pr = new javax.swing.JRadioButton();
        lblNotePr1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblImg_Pr = new javax.swing.JLabel();
        btnAddImg_Pr = new javax.swing.JButton();
        lblShowImg_Pr = new javax.swing.JLabel();
        txtNameImg_Pr = new javax.swing.JTextField();
        txtLink_Pr = new javax.swing.JTextField();
        btnExport_Pr = new javax.swing.JButton();
        btnImport_Prd = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        tabRequestMain = new javax.swing.JTabbedPane();
        paneRequest3 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblRequest1 = new javax.swing.JTable();
        txtFind_Rq = new javax.swing.JTextField();
        btnFindRq = new javax.swing.JButton();
        lblPrName_Rq_F = new javax.swing.JLabel();
        lblStaff_Rq_F = new javax.swing.JLabel();
        lblManager_Rq_F = new javax.swing.JLabel();
        lblLoandate_Rq_F = new javax.swing.JLabel();
        lblStatus_Rq_F = new javax.swing.JLabel();
        lblNote_Rq = new javax.swing.JLabel();
        cboStatus_Rq_F = new javax.swing.JComboBox<>();
        cboNamePr_Rq_F = new javax.swing.JComboBox<>();
        cboStaff_Rq_F = new javax.swing.JComboBox<>();
        cboManager_Rq_F = new javax.swing.JComboBox<>();
        txtLoanDate_Rq_F = new javax.swing.JFormattedTextField();
        btnClear_Rq = new javax.swing.JButton();
        btnUpdate_Rq = new javax.swing.JButton();
        btnExport_Rq = new javax.swing.JButton();
        lblStatus_Rq = new javax.swing.JLabel();
        cboStatus_Rq = new javax.swing.JComboBox<>();
        lblQuantity_Rq = new javax.swing.JLabel();
        spinQuantity_Rq = new javax.swing.JSpinner();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtNote_Rq = new javax.swing.JTextArea();
        btnExport_Rq1 = new javax.swing.JButton();
        btnExport_Rq2 = new javax.swing.JButton();
        paneRequest2 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblRequest_Ht = new javax.swing.JTable();
        lblNote_Ht = new javax.swing.JLabel();
        btnAdd_Ht = new javax.swing.JButton();
        lblUnit_Ht = new javax.swing.JLabel();
        lblProductId_Ht = new javax.swing.JLabel();
        spinQuantity_Ht = new javax.swing.JSpinner();
        jScrollPane16 = new javax.swing.JScrollPane();
        txtNote_Ht = new javax.swing.JTextArea();
        lblQuantity_Ht = new javax.swing.JLabel();
        lblProducer_Ht = new javax.swing.JLabel();
        cboProducer_Ht = new javax.swing.JComboBox<>();
        lblSuplier_Ht = new javax.swing.JLabel();
        cboSuplier_Ht = new javax.swing.JComboBox<>();
        lblPrice_ht = new javax.swing.JLabel();
        txtPrice_ht = new javax.swing.JTextField();
        lblStatus_Ht = new javax.swing.JLabel();
        cboStatus_Ht = new javax.swing.JComboBox<>();
        lblDate_Ht = new javax.swing.JLabel();
        btnClear_Ht = new javax.swing.JButton();
        btnExprot = new javax.swing.JButton();
        cboProduct_Ht = new javax.swing.JComboBox<>();
        txtUnit_Ht = new javax.swing.JLabel();
        lblPrName_Ht = new javax.swing.JLabel();
        txtPrName_Ht = new javax.swing.JLabel();
        lblPdName_Ht = new javax.swing.JLabel();
        txtPdName_Ht = new javax.swing.JLabel();
        lblSpName_Ht = new javax.swing.JLabel();
        txtSpName_Ht = new javax.swing.JLabel();
        btnUpdate_Ht = new javax.swing.JButton();
        txtAcName_Ht = new javax.swing.JLabel();
        lblAcName_Ht = new javax.swing.JLabel();
        lblPrName_Ht_F = new javax.swing.JLabel();
        cboNamePr_Ht_F = new javax.swing.JComboBox<>();
        cboProducer_Ht_F = new javax.swing.JComboBox<>();
        lblProducer_Ht_F = new javax.swing.JLabel();
        lblSupplier_Ht_F = new javax.swing.JLabel();
        cboSupplier_Ht_F = new javax.swing.JComboBox<>();
        cboStatus_Ht_F = new javax.swing.JComboBox<>();
        lblStatus_Ht_F = new javax.swing.JLabel();
        lblLoandate_Ht_F = new javax.swing.JLabel();
        txtLoanDate_Ht_F = new javax.swing.JFormattedTextField();
        btnFindHt = new javax.swing.JButton();
        txtFind_Ht = new javax.swing.JTextField();
        cboAccount_Ht_F = new javax.swing.JComboBox<>();
        lblAccount_Ht_F = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        txtDate_Ht = new com.toedter.calendar.JDateChooser();
        tabAccountMain = new javax.swing.JTabbedPane();
        paneAcc = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAccount_acc = new javax.swing.JTable();
        txtFind_Ac = new javax.swing.JTextField();
        btnFindAc = new javax.swing.JButton();
        paneAdmin_Ac = new javax.swing.JPanel();
        txtPassCon_Ac = new javax.swing.JTextField();
        cboGender_Ac = new javax.swing.JComboBox();
        lblImgAc = new javax.swing.JLabel();
        lblUsernameAc = new javax.swing.JLabel();
        btnRemoveAc = new javax.swing.JButton();
        lblPasswordAc = new javax.swing.JLabel();
        lblConfirmAc = new javax.swing.JLabel();
        btnUpdateAc = new javax.swing.JButton();
        btnAddImg_Acc = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        lblBirthdayAc = new javax.swing.JLabel();
        btnClearAc = new javax.swing.JButton();
        txtImgName_Acc = new javax.swing.JTextField();
        txtEmail_Ac = new javax.swing.JTextField();
        txtPhone_Ac = new javax.swing.JTextField();
        txtAddress_AC = new javax.swing.JTextField();
        lblEmailAc = new javax.swing.JLabel();
        lblNameAc = new javax.swing.JLabel();
        txtImgLink_acc = new javax.swing.JTextField();
        lblshowImg_Acc = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtNote_AC = new javax.swing.JTextArea();
        lblGenderAc = new javax.swing.JLabel();
        txtPass_Ac = new javax.swing.JTextField();
        cboStatus_Ac = new javax.swing.JComboBox<>();
        lblStatus_Ac = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblAddressAc = new javax.swing.JLabel();
        lblPhoneAc = new javax.swing.JLabel();
        btnAddAc = new javax.swing.JButton();
        txtName_ac = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        txtUserName_Ac = new javax.swing.JTextField();
        btnExportAc = new javax.swing.JButton();
        txtBirtday_AC = new com.toedter.calendar.JDateChooser();
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
        tabSupplierMain = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblSupplier_Sup = new javax.swing.JTable();
        btnFind_Sp = new javax.swing.JButton();
        txtFind_Sp = new javax.swing.JTextField();
        txtAZ_Sp = new javax.swing.JButton();
        txtZA_Sp = new javax.swing.JButton();
        paneAdmin_Sp = new javax.swing.JPanel();
        btnAdd_Sp = new javax.swing.JButton();
        lblAddress_Sp = new javax.swing.JLabel();
        txtName_Sp = new javax.swing.JTextField();
        lblName_Sp = new javax.swing.JLabel();
        btnClear_Sp = new javax.swing.JButton();
        lblEmail_Sp = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtNote_Sp = new javax.swing.JTextArea();
        txtEmail_Sp = new javax.swing.JTextField();
        rdoHide_Sp = new javax.swing.JRadioButton();
        lblPhone_Sp = new javax.swing.JLabel();
        btnUpdate_Sp = new javax.swing.JButton();
        txtPhone_Sp = new javax.swing.JTextField();
        lblImg_Sp = new javax.swing.JLabel();
        rdoShow_Sp = new javax.swing.JRadioButton();
        lblNote_Sp = new javax.swing.JLabel();
        btnRemove_Sp = new javax.swing.JButton();
        btnExport_Sp = new javax.swing.JButton();
        lblStatus_Sp = new javax.swing.JLabel();
        txtAddress_Sp = new javax.swing.JTextField();
        btnAddImg_Sp = new javax.swing.JButton();
        txtNameImg_Sp = new javax.swing.JTextField();
        txtLink_Sp = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        lblIconIMG_Sp = new javax.swing.JLabel();
        tabLogOut = new javax.swing.JTabbedPane();
        tabAboutMe = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuEdit = new javax.swing.JMenu();
        menuLanguage = new javax.swing.JMenu();
        itemVn = new javax.swing.JMenuItem();
        itemEn = new javax.swing.JMenuItem();
        itemCn = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbljpane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tbljpane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tbljpaneStateChanged(evt);
            }
        });
        tbljpane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbljpaneMouseClicked(evt);
            }
        });

        tblProducer_Per.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Name", "Nation", "Status", "Note"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProducer_Per.getTableHeader().setResizingAllowed(false);
        tblProducer_Per.getTableHeader().setReorderingAllowed(false);
        tblProducer_Per.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProducer_PerMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblProducer_Per);

        btnFind_Pd.setText("Find");
        btnFind_Pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFind_PdActionPerformed(evt);
            }
        });

        txtFind_Pd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFind_PdKeyReleased(evt);
            }
        });

        btnAZ_Pd.setText("A-Z");
        btnAZ_Pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAZ_PdActionPerformed(evt);
            }
        });

        btnAdd_Pd.setText("Add");
        btnAdd_Pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd_PdActionPerformed(evt);
            }
        });

        btnClear_Pd.setText("Clear");
        btnClear_Pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear_PdActionPerformed(evt);
            }
        });

        groupStatus_Pd.add(rdoHide_Pd);
        rdoHide_Pd.setText("Hide");

        groupStatus_Pd.add(rdoShow_Pd);
        rdoShow_Pd.setText("show");

        lblNote_Pd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNote_Pd.setText("Note :");

        btnRemove_Pd.setText("Remove");
        btnRemove_Pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove_PdActionPerformed(evt);
            }
        });

        lblNation_Pd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNation_Pd.setText("Nation  :");

        lblStatus_Pd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus_Pd.setText("Status :");

        btnUpdate_Pd.setText("Update");
        btnUpdate_Pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate_PdActionPerformed(evt);
            }
        });

        lblName_Pd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblName_Pd.setText("Name :");

        btnExport_Pd.setText("Export");
        btnExport_Pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExport_PdActionPerformed(evt);
            }
        });

        txtNote_Pd.setColumns(20);
        txtNote_Pd.setRows(5);
        jScrollPane8.setViewportView(txtNote_Pd);

        jButton5.setText("Print");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Import");

        javax.swing.GroupLayout paneAdmin_PdLayout = new javax.swing.GroupLayout(paneAdmin_Pd);
        paneAdmin_Pd.setLayout(paneAdmin_PdLayout);
        paneAdmin_PdLayout.setHorizontalGroup(
            paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneAdmin_PdLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName_Pd)
                    .addComponent(lblNote_Pd)
                    .addComponent(lblStatus_Pd)
                    .addComponent(lblNation_Pd))
                .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAdmin_PdLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cboNation_Pd, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtName_Pd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))))
                    .addGroup(paneAdmin_PdLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(rdoShow_Pd)
                        .addGap(18, 18, 18)
                        .addComponent(rdoHide_Pd)))
                .addGap(38, 38, 38)
                .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAdmin_PdLayout.createSequentialGroup()
                        .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnAdd_Pd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdate_Pd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnExport_Pd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)))
                    .addGroup(paneAdmin_PdLayout.createSequentialGroup()
                        .addComponent(btnRemove_Pd, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnClear_Pd, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(184, Short.MAX_VALUE))
        );
        paneAdmin_PdLayout.setVerticalGroup(
            paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneAdmin_PdLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName_Pd)
                    .addComponent(txtName_Pd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd_Pd)
                    .addComponent(btnExport_Pd))
                .addGap(18, 18, 18)
                .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNation_Pd)
                    .addComponent(btnUpdate_Pd)
                    .addComponent(jButton6)
                    .addComponent(cboNation_Pd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStatus_Pd)
                    .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdoHide_Pd)
                        .addComponent(rdoShow_Pd)
                        .addComponent(btnRemove_Pd)
                        .addComponent(jButton5)))
                .addGap(18, 18, 18)
                .addGroup(paneAdmin_PdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClear_Pd)
                    .addComponent(lblNote_Pd)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        btnZA_Pd.setText("Z-A");
        btnZA_Pd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZA_PdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabProducerMainLayout = new javax.swing.GroupLayout(tabProducerMain);
        tabProducerMain.setLayout(tabProducerMainLayout);
        tabProducerMainLayout.setHorizontalGroup(
            tabProducerMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabProducerMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1009, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 124, Short.MAX_VALUE))
            .addGroup(tabProducerMainLayout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(tabProducerMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paneAdmin_Pd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tabProducerMainLayout.createSequentialGroup()
                        .addComponent(btnFind_Pd)
                        .addGap(26, 26, 26)
                        .addComponent(txtFind_Pd, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAZ_Pd)
                        .addGap(29, 29, 29)
                        .addComponent(btnZA_Pd)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabProducerMainLayout.setVerticalGroup(
            tabProducerMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabProducerMainLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabProducerMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFind_Pd)
                    .addComponent(txtFind_Pd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAZ_Pd)
                    .addComponent(btnZA_Pd))
                .addGap(18, 18, 18)
                .addComponent(paneAdmin_Pd, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(554, Short.MAX_VALUE))
        );

        tbljpane.addTab("Producer", tabProducerMain);

        tabProductMain.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabProductMainStateChanged(evt);
            }
        });

        paneCategory.setPreferredSize(new java.awt.Dimension(1137, 500));

        lblNoteCa.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNoteCa.setText("Note :");

        lblNameCa.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNameCa.setText("Name :");

        btnUpdate_Ca.setText("Update");
        btnUpdate_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate_CaActionPerformed(evt);
            }
        });

        btnAdd_Ca.setText("Add");
        btnAdd_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd_CaActionPerformed(evt);
            }
        });

        btnRemove_Ca.setText("Remove");
        btnRemove_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove_CaActionPerformed(evt);
            }
        });

        btnClear_Ca.setText("Clear");
        btnClear_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear_CaActionPerformed(evt);
            }
        });

        btnExport_Ca.setText("Export");
        btnExport_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExport_CaActionPerformed(evt);
            }
        });

        tblCategory_Ca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Name", "Status", "Note"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCategory_Ca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCategory_CaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblCategory_Ca);

        btnFind_Ca.setText("Find");
        btnFind_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFind_CaActionPerformed(evt);
            }
        });

        txtFind_Ca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFind_CaKeyReleased(evt);
            }
        });

        btnFinddow_Ca.setText("A-Z");
        btnFinddow_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinddow_CaActionPerformed(evt);
            }
        });

        btnFindup_Ca.setText("Z-A");
        btnFindup_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindup_CaActionPerformed(evt);
            }
        });

        lblStatusCa.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatusCa.setText("Status :");

        txtNote_Ca.setColumns(20);
        txtNote_Ca.setRows(5);
        jScrollPane3.setViewportView(txtNote_Ca);

        groupStatus_Ca.add(rdoHide_Ca);
        rdoHide_Ca.setText("Hien");

        groupStatus_Ca.add(rdoShow_Ca);
        rdoShow_Ca.setText("An");

        jButton8.setText("Import");

        jButton9.setText("Print");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneCategoryLayout = new javax.swing.GroupLayout(paneCategory);
        paneCategory.setLayout(paneCategoryLayout);
        paneCategoryLayout.setHorizontalGroup(
            paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneCategoryLayout.createSequentialGroup()
                .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(paneCategoryLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNameCa)
                            .addComponent(lblNoteCa)
                            .addComponent(lblStatusCa))
                        .addGap(28, 28, 28)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(paneCategoryLayout.createSequentialGroup()
                                .addComponent(rdoShow_Ca)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoHide_Ca))
                            .addComponent(jScrollPane3)
                            .addComponent(txtName_Ca))
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneCategoryLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnExport_Ca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(paneCategoryLayout.createSequentialGroup()
                                .addComponent(btnRemove_Ca, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnClear_Ca, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paneCategoryLayout.createSequentialGroup()
                                .addComponent(btnAdd_Ca, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnUpdate_Ca, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneCategoryLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 186, Short.MAX_VALUE))
                    .addGroup(paneCategoryLayout.createSequentialGroup()
                        .addComponent(btnFind_Ca)
                        .addGap(18, 18, 18)
                        .addComponent(txtFind_Ca, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFinddow_Ca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFindup_Ca)
                        .addContainerGap(408, Short.MAX_VALUE))))
        );
        paneCategoryLayout.setVerticalGroup(
            paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneCategoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneCategoryLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFind_Ca)
                            .addComponent(txtFind_Ca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFinddow_Ca)
                            .addComponent(btnFindup_Ca)))
                    .addGroup(paneCategoryLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNameCa)
                            .addComponent(txtName_Ca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStatusCa)
                            .addComponent(rdoHide_Ca)
                            .addComponent(rdoShow_Ca))
                        .addGap(26, 26, 26)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNoteCa)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd_Ca)
                            .addComponent(btnUpdate_Ca))
                        .addGap(18, 18, 18)
                        .addGroup(paneCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRemove_Ca)
                            .addComponent(btnClear_Ca))
                        .addGap(21, 21, 21)
                        .addComponent(btnExport_Ca)
                        .addGap(18, 18, 18)
                        .addComponent(jButton8)))
                .addGap(18, 18, 18)
                .addComponent(jButton9)
                .addContainerGap(422, Short.MAX_VALUE))
        );

        tabProductMain.addTab("Category", paneCategory);

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
        jScrollPane1.setViewportView(tblProduct_Pr);

        lblProductIdPr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProductIdPr.setText("ProductId :");

        lblNamePr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNamePr.setText("Name :");

        lblCategoryPr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCategoryPr.setText("Category :");

        txtProductId_Pr.setEditable(false);

        cboCategory_Pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategory_PrActionPerformed(evt);
            }
        });

        lblUnitPr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUnitPr.setText("Unit :");

        lblNotePr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNotePr.setText("Note :");

        txtNote_Pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNote_PrActionPerformed(evt);
            }
        });

        btnAdd_Pr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnAdd_Pr.setText("Add");
        btnAdd_Pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd_PrActionPerformed(evt);
            }
        });

        btnUpdate_Pr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUpdate_Pr.setText("Update");
        btnUpdate_Pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate_PrActionPerformed(evt);
            }
        });

        btnDelete_Pr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDelete_Pr.setText("Delete");
        btnDelete_Pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_PrActionPerformed(evt);
            }
        });

        btnReset_Pr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnReset_Pr.setText("Reset");
        btnReset_Pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReset_PrActionPerformed(evt);
            }
        });

        txtFind_Pr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFind_PrKeyReleased(evt);
            }
        });

        btnFind_Pr.setText("Find");

        groupStatus_Pr.add(rdoShow_Pr);
        rdoShow_Pr.setText("Show");

        groupStatus_Pr.add(rdoHide_Pr);
        rdoHide_Pr.setText("Hiide");

        lblNotePr1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNotePr1.setText("Status :");

        jButton1.setText("A-Z");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Z-A");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lblImg_Pr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblImg_Pr.setText("Image :");

        btnAddImg_Pr.setText("IMG");
        btnAddImg_Pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddImg_PrActionPerformed(evt);
            }
        });

        lblShowImg_Pr.setText("jLabel1");

        txtNameImg_Pr.setEditable(false);

        txtLink_Pr.setEditable(false);

        btnExport_Pr.setText("Export");
        btnExport_Pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExport_PrActionPerformed(evt);
            }
        });

        btnImport_Prd.setText("Import");
        btnImport_Prd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImport_PrdActionPerformed(evt);
            }
        });

        jButton7.setText("Print");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneProductLayout = new javax.swing.GroupLayout(paneProduct);
        paneProduct.setLayout(paneProductLayout);
        paneProductLayout.setHorizontalGroup(
            paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(paneProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProductIdPr)
                    .addComponent(lblNamePr)
                    .addComponent(lblCategoryPr)
                    .addComponent(lblUnitPr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboCategory_Pr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName_Pr, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                    .addComponent(txtUnit_Pr)
                    .addComponent(txtProductId_Pr))
                .addGap(33, 33, 33)
                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneProductLayout.createSequentialGroup()
                        .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneProductLayout.createSequentialGroup()
                                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneProductLayout.createSequentialGroup()
                                        .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblNotePr1, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblImg_Pr))
                                        .addGap(44, 44, 44)
                                        .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnAddImg_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(paneProductLayout.createSequentialGroup()
                                                .addComponent(rdoShow_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rdoHide_Pr))))
                                    .addGroup(paneProductLayout.createSequentialGroup()
                                        .addComponent(lblNotePr)
                                        .addGap(44, 44, 44)
                                        .addComponent(txtNote_Pr)))
                                .addGap(33, 33, 33))
                            .addGroup(paneProductLayout.createSequentialGroup()
                                .addComponent(txtNameImg_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUpdate_Pr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAdd_Pr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete_Pr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReset_Pr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneProductLayout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(txtFind_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(btnFind_Pr)
                                .addContainerGap(103, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneProductLayout.createSequentialGroup()
                                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnExport_Pr, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnImport_Prd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(384, 384, 384))))
                    .addGroup(paneProductLayout.createSequentialGroup()
                        .addComponent(txtLink_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(lblShowImg_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        paneProductLayout.setVerticalGroup(
            paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneProductLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblProductIdPr, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductId_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd_Pr)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addComponent(lblNotePr)
                        .addComponent(txtNote_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFind_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnFind_Pr)))
                .addGap(18, 18, 18)
                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneProductLayout.createSequentialGroup()
                        .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblNamePr, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtName_Pr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnUpdate_Pr)
                                .addComponent(btnExport_Pr)))
                        .addGap(18, 18, 18)
                        .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCategory_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCategoryPr)
                            .addComponent(btnDelete_Pr)
                            .addComponent(lblImg_Pr)
                            .addComponent(btnAddImg_Pr)
                            .addComponent(btnImport_Prd)))
                    .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNotePr1)
                        .addComponent(rdoShow_Pr)
                        .addComponent(rdoHide_Pr)))
                .addGap(18, 18, 18)
                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReset_Pr)
                        .addComponent(txtUnit_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblUnitPr)
                        .addComponent(jButton7))
                    .addComponent(txtNameImg_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneProductLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(txtLink_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblShowImg_Pr, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(237, Short.MAX_VALUE))
        );

        tabProductMain.addTab("Product", paneProduct);

        tbljpane.addTab("Product", tabProductMain);

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

        lblStaff_Rq_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStaff_Rq_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStaff_Rq_F.setText("Staff");

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

        lblStatus_Rq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus_Rq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus_Rq.setText("Status");

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
                                    .addComponent(lblStatus_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(paneRequest3Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(lblNote_Rq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(paneRequest3Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(lblQuantity_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(51, 51, 51)
                                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cboStatus_Rq, 0, 132, Short.MAX_VALUE)
                                        .addComponent(spinQuantity_Rq))
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(paneRequest3Layout.createSequentialGroup()
                                                .addComponent(cboNamePr_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cboStaff_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(paneRequest3Layout.createSequentialGroup()
                                                .addComponent(lblPrName_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(80, 80, 80)
                                                .addComponent(lblStaff_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(txtFind_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
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
                        .addGap(0, 137, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneRequest3Layout.setVerticalGroup(
            paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneRequest3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLoandate_Rq_F, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPrName_Rq_F)
                        .addComponent(lblStaff_Rq_F)
                        .addComponent(lblManager_Rq_F)
                        .addComponent(lblStatus_Rq_F)))
                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFind_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboNamePr_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboStaff_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboManager_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboStatus_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLoanDate_Rq_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFindRq))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStatus_Rq)
                            .addComponent(cboStatus_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate_Rq))
                        .addGap(18, 18, 18)
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblQuantity_Rq)
                            .addComponent(spinQuantity_Rq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClear_Rq))
                        .addGap(22, 22, 22)
                        .addGroup(paneRequest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNote_Rq)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneRequest3Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(btnExport_Rq)
                        .addGap(18, 18, 18)
                        .addComponent(btnExport_Rq1)
                        .addGap(18, 18, 18)
                        .addComponent(btnExport_Rq2)))
                .addContainerGap(260, Short.MAX_VALUE))
        );

        tabRequestMain.addTab("Request", paneRequest3);

        paneRequest2.setMaximumSize(new java.awt.Dimension(32767, 32700));
        paneRequest2.setPreferredSize(new java.awt.Dimension(1019, 600));

        tblRequest_Ht.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Product Id", "Producer Id", "Supplier Id", "Price", "Quantity", "Status", "Date", "Note", "Account Id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRequest_Ht.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRequest_HtMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(tblRequest_Ht);

        lblNote_Ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNote_Ht.setText("Note :");

        btnAdd_Ht.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnAdd_Ht.setText("Add");
        btnAdd_Ht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd_HtActionPerformed(evt);
            }
        });

        lblUnit_Ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUnit_Ht.setText("Unit :");

        lblProductId_Ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProductId_Ht.setText("Product Id:");

        txtNote_Ht.setColumns(20);
        txtNote_Ht.setRows(5);
        jScrollPane16.setViewportView(txtNote_Ht);

        lblQuantity_Ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblQuantity_Ht.setText("Quantity :");

        lblProducer_Ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProducer_Ht.setText("Producer Id:");

        cboProducer_Ht.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboProducer_HtPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        lblSuplier_Ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSuplier_Ht.setText("Suplier Id:");

        cboSuplier_Ht.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboSuplier_HtPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        lblPrice_ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPrice_ht.setText("Price :");

        lblStatus_Ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus_Ht.setText("Status :");

        lblDate_Ht.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDate_Ht.setText("Date");

        btnClear_Ht.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnClear_Ht.setText("Clear");
        btnClear_Ht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear_HtActionPerformed(evt);
            }
        });

        btnExprot.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnExprot.setText("Export");
        btnExprot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExprotActionPerformed(evt);
            }
        });

        cboProduct_Ht.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboProduct_HtPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        txtUnit_Ht.setText("   ");

        lblPrName_Ht.setText("Product Name");

        txtPrName_Ht.setText("     ");

        lblPdName_Ht.setText("Producer Name");

        txtPdName_Ht.setText("     ");

        lblSpName_Ht.setText("Supplier Name");

        txtSpName_Ht.setText("     ");

        btnUpdate_Ht.setText("Update");
        btnUpdate_Ht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate_HtActionPerformed(evt);
            }
        });

        txtAcName_Ht.setText("   ");

        lblAcName_Ht.setText("Account Name");

        lblPrName_Ht_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPrName_Ht_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrName_Ht_F.setText("Product");

        lblProducer_Ht_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblProducer_Ht_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProducer_Ht_F.setText("Producer");

        lblSupplier_Ht_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSupplier_Ht_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSupplier_Ht_F.setText("Supplier");

        lblStatus_Ht_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus_Ht_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus_Ht_F.setText("Status");

        lblLoandate_Ht_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLoandate_Ht_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoandate_Ht_F.setText("Loan date");

        btnFindHt.setText("Find");
        btnFindHt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindHtActionPerformed(evt);
            }
        });

        txtFind_Ht.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFind_HtKeyReleased(evt);
            }
        });

        lblAccount_Ht_F.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAccount_Ht_F.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAccount_Ht_F.setText("Account");

        jButton12.setText("Import");

        jButton13.setText("Print");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneRequest2Layout = new javax.swing.GroupLayout(paneRequest2);
        paneRequest2.setLayout(paneRequest2Layout);
        paneRequest2Layout.setHorizontalGroup(
            paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneRequest2Layout.createSequentialGroup()
                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblProductId_Ht)
                            .addComponent(lblPrName_Ht)
                            .addComponent(lblDate_Ht)
                            .addComponent(lblUnit_Ht)
                            .addComponent(lblAcName_Ht))
                        .addGap(21, 21, 21)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUnit_Ht)
                            .addComponent(txtPrName_Ht)
                            .addComponent(txtAcName_Ht)
                            .addComponent(txtDate_Ht, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboProduct_Ht, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(40, 40, 40)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addComponent(lblProducer_Ht)
                                .addGap(18, 18, 18)
                                .addComponent(cboProducer_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addComponent(lblQuantity_Ht)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinQuantity_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addComponent(lblPdName_Ht)
                                .addGap(18, 18, 18)
                                .addComponent(txtPdName_Ht)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addComponent(lblSuplier_Ht)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboSuplier_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneRequest2Layout.createSequentialGroup()
                                        .addComponent(lblPrice_ht)
                                        .addGap(68, 68, 68))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneRequest2Layout.createSequentialGroup()
                                        .addComponent(lblSpName_Ht)
                                        .addGap(18, 18, 18)))
                                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrice_ht, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSpName_Ht))))
                        .addGap(42, 42, 42)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus_Ht)
                            .addComponent(lblNote_Ht))
                        .addGap(18, 18, 18)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboStatus_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(paneRequest2Layout.createSequentialGroup()
                                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                                .addGap(205, 205, 205)
                                                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(paneRequest2Layout.createSequentialGroup()
                                                        .addComponent(cboNamePr_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cboProducer_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(paneRequest2Layout.createSequentialGroup()
                                                        .addComponent(lblPrName_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(80, 80, 80)
                                                        .addComponent(lblProducer_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addComponent(txtFind_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(33, 33, 33)
                                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                                .addComponent(cboSupplier_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(cboStatus_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                                .addComponent(lblSupplier_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblStatus_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(29, 29, 29)
                                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cboAccount_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                                .addGap(3, 3, 3)
                                                .addComponent(lblAccount_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblLoandate_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtLoanDate_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(btnFindHt, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addGap(488, 488, 488)
                                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(paneRequest2Layout.createSequentialGroup()
                                        .addComponent(btnExprot, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(paneRequest2Layout.createSequentialGroup()
                                        .addComponent(btnAdd_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnUpdate_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnClear_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        paneRequest2Layout.setVerticalGroup(
            paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneRequest2Layout.createSequentialGroup()
                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPrName_Ht_F)
                            .addComponent(lblProducer_Ht_F)
                            .addComponent(lblSupplier_Ht_F)
                            .addComponent(lblStatus_Ht_F))
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFind_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cboNamePr_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cboProducer_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cboSupplier_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cboStatus_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(btnFindHt))))
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addComponent(lblLoandate_Ht_F)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtLoanDate_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addComponent(lblAccount_Ht_F)
                        .addGap(12, 12, 12)
                        .addComponent(cboAccount_Ht_F, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSuplier_Ht)
                            .addComponent(cboSuplier_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSpName_Ht)
                            .addComponent(txtSpName_Ht)))
                    .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblStatus_Ht)
                        .addComponent(cboStatus_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNote_Ht)))
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblProductId_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboProduct_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblProducer_Ht)
                            .addComponent(cboProducer_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPrName_Ht)
                            .addComponent(txtPrName_Ht)
                            .addComponent(lblPdName_Ht)
                            .addComponent(txtPdName_Ht))
                        .addGap(21, 21, 21)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblQuantity_Ht)
                                    .addComponent(spinQuantity_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPrice_ht)
                                    .addComponent(txtPrice_ht, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lblDate_Ht))
                            .addGroup(paneRequest2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(txtDate_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUnit_Ht)
                            .addComponent(txtUnit_Ht))))
                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClear_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate_Ht, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneRequest2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAcName_Ht)
                            .addComponent(lblAcName_Ht))))
                .addGap(26, 26, 26)
                .addGroup(paneRequest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExprot, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(419, 419, 419))
        );

        tabRequestMain.addTab("Phiếu Nhập Hàng", paneRequest2);

        tbljpane.addTab("Request", tabRequestMain);

        tblAccount_acc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Image", "Id", "Username", "Name", "Phone", "Email", "Address", "Gender", "Birthday", "Status", "Note"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAccount_acc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAccount_accMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblAccount_acc);

        txtFind_Ac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFind_AcKeyReleased(evt);
            }
        });

        btnFindAc.setText("Find");
        btnFindAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindAcActionPerformed(evt);
            }
        });

        lblImgAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblImgAc.setText("Image :");

        lblUsernameAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUsernameAc.setText("Username :");

        btnRemoveAc.setText("Delete");
        btnRemoveAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveAcActionPerformed(evt);
            }
        });

        lblPasswordAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPasswordAc.setText("Password :");

        lblConfirmAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblConfirmAc.setText("Confirm :");

        btnUpdateAc.setText("Update");
        btnUpdateAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateAcActionPerformed(evt);
            }
        });

        btnAddImg_Acc.setText("IMG");
        btnAddImg_Acc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddImg_AccActionPerformed(evt);
            }
        });

        jButton4.setText("Import");

        lblBirthdayAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBirthdayAc.setText("Birthday");

        btnClearAc.setText("Clear");
        btnClearAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAcActionPerformed(evt);
            }
        });

        txtImgName_Acc.setEditable(false);

        lblEmailAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEmailAc.setText("Email");

        lblNameAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNameAc.setText("Name :");

        txtImgLink_acc.setEditable(false);
        txtImgLink_acc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImgLink_accActionPerformed(evt);
            }
        });

        lblshowImg_Acc.setText("jLabel2");

        txtNote_AC.setColumns(20);
        txtNote_AC.setRows(5);
        jScrollPane9.setViewportView(txtNote_AC);

        lblGenderAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblGenderAc.setText("Gender");

        lblStatus_Ac.setText("Status");

        jLabel1.setText("Note");

        lblAddressAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAddressAc.setText("Address :");

        lblPhoneAc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPhoneAc.setText("Phone");

        btnAddAc.setText("Add");
        btnAddAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAcActionPerformed(evt);
            }
        });

        jButton3.setText("Export");

        btnExportAc.setText("Print");
        btnExportAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportAcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneAdmin_AcLayout = new javax.swing.GroupLayout(paneAdmin_Ac);
        paneAdmin_Ac.setLayout(paneAdmin_AcLayout);
        paneAdmin_AcLayout.setHorizontalGroup(
            paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblUsernameAc)
                                    .addComponent(lblNameAc))
                                .addGap(18, 18, 18)
                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtUserName_Ac)
                                    .addComponent(txtName_ac, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                        .addGap(105, 105, 105)
                                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneAdmin_AcLayout.createSequentialGroup()
                                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lblAddressAc)
                                                    .addComponent(lblBirthdayAc))
                                                .addGap(30, 30, 30)
                                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtBirtday_AC, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                                    .addComponent(txtAddress_AC))
                                                .addGap(32, 32, 32)
                                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtImgName_Acc)
                                                    .addComponent(txtImgLink_acc, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(lblGenderAc)
                                                            .addComponent(lblImgAc))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(cboGender_Ac, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(btnAddImg_Acc, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneAdmin_AcLayout.createSequentialGroup()
                                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lblConfirmAc)
                                                    .addComponent(lblPasswordAc)
                                                    .addComponent(jLabel1))
                                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(176, 176, 176)
                                                        .addComponent(lblshowImg_Acc, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(txtPassCon_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneAdmin_AcLayout.createSequentialGroup()
                                                            .addGap(18, 18, 18)
                                                            .addComponent(txtPass_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                                .addComponent(btnAddAc, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(80, 80, 80)
                                                .addComponent(btnUpdateAc, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(71, 71, 71)
                                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                                .addComponent(btnClearAc, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                                                .addComponent(btnRemoveAc, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblEmailAc)
                                    .addComponent(lblPhoneAc)
                                    .addComponent(lblStatus_Ac, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(54, 54, 54)
                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtEmail_Ac)
                                    .addComponent(txtPhone_Ac)
                                    .addComponent(cboStatus_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneAdmin_AcLayout.createSequentialGroup()
                        .addComponent(btnExportAc, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(326, 326, 326))))
        );
        paneAdmin_AcLayout.setVerticalGroup(
            paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUsernameAc)
                            .addComponent(txtUserName_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNameAc)
                            .addComponent(txtName_ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(lblBirthdayAc))
                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAddressAc)
                            .addComponent(txtAddress_AC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGenderAc)
                            .addComponent(cboGender_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblImgAc)
                                    .addComponent(btnAddImg_Acc)))
                            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(txtBirtday_AC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(24, 24, 24)
                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEmailAc)
                            .addComponent(txtEmail_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPhoneAc)
                            .addComponent(txtPhone_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblPasswordAc)
                                    .addComponent(txtPass_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneAdmin_AcLayout.createSequentialGroup()
                                .addComponent(txtImgName_Acc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)))
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblConfirmAc)
                            .addComponent(txtPassCon_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImgLink_acc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAdmin_AcLayout.createSequentialGroup()
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblStatus_Ac)
                                .addComponent(cboStatus_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                            .addComponent(lblshowImg_Acc, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddAc)
                            .addComponent(btnUpdateAc)
                            .addComponent(btnClearAc)
                            .addComponent(btnRemoveAc)))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(paneAdmin_AcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExportAc))
        );

        javax.swing.GroupLayout paneAccLayout = new javax.swing.GroupLayout(paneAcc);
        paneAcc.setLayout(paneAccLayout);
        paneAccLayout.setHorizontalGroup(
            paneAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneAccLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(paneAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAccLayout.createSequentialGroup()
                        .addComponent(btnFindAc)
                        .addGap(32, 32, 32)
                        .addComponent(txtFind_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1036, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paneAdmin_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        paneAccLayout.setVerticalGroup(
            paneAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneAccLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(paneAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFindAc)
                    .addComponent(txtFind_Ac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(paneAdmin_Ac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(178, 178, 178))
        );

        tabAccountMain.addTab("Account", paneAcc);

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
                .addContainerGap(183, Short.MAX_VALUE))
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
                .addContainerGap(637, Short.MAX_VALUE))
        );

        tabAccountMain.addTab("Information", paneInfo);

        tbljpane.addTab("Account", tabAccountMain);

        tblSupplier_Sup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Image", "Id", "Name", "Phone", "Address", "Email", "Status", "Note"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplier_Sup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplier_SupMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblSupplier_Sup);

        btnFind_Sp.setText("Find");
        btnFind_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFind_SpActionPerformed(evt);
            }
        });

        txtFind_Sp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFind_SpKeyReleased(evt);
            }
        });

        txtAZ_Sp.setText("A-Z");
        txtAZ_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAZ_SpActionPerformed(evt);
            }
        });

        txtZA_Sp.setText("Z-A");
        txtZA_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtZA_SpActionPerformed(evt);
            }
        });

        btnAdd_Sp.setText("Add");
        btnAdd_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd_SpActionPerformed(evt);
            }
        });

        lblAddress_Sp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblAddress_Sp.setText("Address :");

        lblName_Sp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblName_Sp.setText("Name :");

        btnClear_Sp.setText("Clear");
        btnClear_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear_SpActionPerformed(evt);
            }
        });

        lblEmail_Sp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEmail_Sp.setText("Email :");

        txtNote_Sp.setColumns(20);
        txtNote_Sp.setRows(5);
        jScrollPane6.setViewportView(txtNote_Sp);

        txtEmail_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmail_SpActionPerformed(evt);
            }
        });

        groupStatus_Sp.add(rdoHide_Sp);
        rdoHide_Sp.setText("Hide");

        lblPhone_Sp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPhone_Sp.setText("Phone :");

        btnUpdate_Sp.setText("Update");
        btnUpdate_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate_SpActionPerformed(evt);
            }
        });

        lblImg_Sp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblImg_Sp.setText("Image :");

        groupStatus_Sp.add(rdoShow_Sp);
        rdoShow_Sp.setText("Show");

        lblNote_Sp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNote_Sp.setText("Note :");

        btnRemove_Sp.setText("Remove");
        btnRemove_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemove_SpActionPerformed(evt);
            }
        });

        btnExport_Sp.setText("Export");
        btnExport_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExport_SpActionPerformed(evt);
            }
        });

        lblStatus_Sp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus_Sp.setText("Status :");

        btnAddImg_Sp.setText("IMG");
        btnAddImg_Sp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddImg_SpActionPerformed(evt);
            }
        });

        txtNameImg_Sp.setEditable(false);

        txtLink_Sp.setEditable(false);

        jButton10.setText("Improt");

        jButton11.setText("Print");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneAdmin_SpLayout = new javax.swing.GroupLayout(paneAdmin_Sp);
        paneAdmin_Sp.setLayout(paneAdmin_SpLayout);
        paneAdmin_SpLayout.setHorizontalGroup(
            paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName_Sp)
                    .addComponent(lblPhone_Sp)
                    .addComponent(lblAddress_Sp)
                    .addComponent(lblImg_Sp))
                .addGap(28, 28, 28)
                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtName_Sp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAddress_Sp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPhone_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 46, Short.MAX_VALUE)
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus_Sp)
                            .addComponent(lblEmail_Sp))
                        .addGap(28, 28, 28)
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                                .addComponent(rdoShow_Sp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoHide_Sp))))
                    .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddImg_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtLink_Sp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                    .addComponent(txtNameImg_Sp, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addComponent(lblNote_Sp)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6)))
                .addGap(84, 84, 84)
                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                        .addComponent(btnRemove_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClear_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneAdmin_SpLayout.createSequentialGroup()
                        .addComponent(btnAdd_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdate_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExport_Sp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        paneAdmin_SpLayout.setVerticalGroup(
            paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd_Sp)
                            .addComponent(btnUpdate_Sp))
                        .addGap(18, 18, 18)
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRemove_Sp)
                            .addComponent(btnClear_Sp))
                        .addGap(18, 18, 18)
                        .addComponent(btnExport_Sp)
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(lblImg_Sp))
                            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton10)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblName_Sp)
                                    .addComponent(txtName_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblPhone_Sp)
                                    .addComponent(txtPhone_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblEmail_Sp)
                                    .addComponent(txtEmail_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblStatus_Sp)
                                    .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(rdoShow_Sp)
                                        .addComponent(rdoHide_Sp)))))
                        .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneAdmin_SpLayout.createSequentialGroup()
                                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAddress_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblAddress_Sp))
                                .addGap(18, 18, 18)
                                .addComponent(btnAddImg_Sp)
                                .addGap(18, 18, 18)
                                .addComponent(txtNameImg_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtLink_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneAdmin_SpLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(paneAdmin_SpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNote_Sp)))))))
        );

        lblIconIMG_Sp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblIconIMG_Sp.setText("Image ");

        javax.swing.GroupLayout tabSupplierMainLayout = new javax.swing.GroupLayout(tabSupplierMain);
        tabSupplierMain.setLayout(tabSupplierMainLayout);
        tabSupplierMainLayout.setHorizontalGroup(
            tabSupplierMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSupplierMainLayout.createSequentialGroup()
                .addGroup(tabSupplierMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabSupplierMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1018, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabSupplierMainLayout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(btnFind_Sp)
                        .addGap(18, 18, 18)
                        .addComponent(txtFind_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(txtAZ_Sp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtZA_Sp))
                    .addGroup(tabSupplierMainLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(paneAdmin_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabSupplierMainLayout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(lblIconIMG_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        tabSupplierMainLayout.setVerticalGroup(
            tabSupplierMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSupplierMainLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(tabSupplierMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFind_Sp)
                    .addComponent(txtFind_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAZ_Sp)
                    .addComponent(txtZA_Sp))
                .addGap(18, 18, 18)
                .addComponent(paneAdmin_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblIconIMG_Sp, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(174, Short.MAX_VALUE))
        );

        tbljpane.addTab("Supplier", tabSupplierMain);

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
        tbljpane.addTab("Log out", tabLogOut);
        tbljpane.addTab("About Me", tabAboutMe);

        menuFile.setText("File");
        menuBar.add(menuFile);

        menuEdit.setText("Edit");
        menuBar.add(menuEdit);

        menuLanguage.setText("Language");

        itemVn.setText("Vietnamese");
        itemVn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemVnActionPerformed(evt);
            }
        });
        menuLanguage.add(itemVn);

        itemEn.setText("English");
        itemEn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemEnActionPerformed(evt);
            }
        });
        menuLanguage.add(itemEn);

        itemCn.setText("Chinese");
        itemCn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCnActionPerformed(evt);
            }
        });
        menuLanguage.add(itemCn);

        menuBar.add(menuLanguage);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbljpane, javax.swing.GroupLayout.PREFERRED_SIZE, 1230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbljpane, javax.swing.GroupLayout.PREFERRED_SIZE, 899, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbljpaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbljpaneMouseClicked

    }//GEN-LAST:event_tbljpaneMouseClicked

    private void tabLogOutPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tabLogOutPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tabLogOutPropertyChange

    private void tabLogOutStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabLogOutStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_tabLogOutStateChanged

    private void tbljpaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tbljpaneStateChanged
        // TODO add your handling code here:
        if (tbljpane.getTitleAt(tbljpane.getSelectedIndex()).equals("Log out")) {
            if (JOptionPane.showConfirmDialog(null, "Ban chac chan muon dang xuat ?") == 0) {
                dispose();
                dangNhap.setVisible(true);
            }
        }
    }//GEN-LAST:event_tbljpaneStateChanged

    private void btnBlockInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlockInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBlockInActionPerformed

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
       // ac.setNote(txtNote_AC.getText());
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
                        JOptionPane.showMessageDialog(paneProduct, "Update Sucess");
                        txtPass_In.setText("");
                        txtPassCon_In.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(paneProduct, "Password incorrect!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(paneProduct, "Xac nhan mat khau phai giong nhau!");
        }

    }//GEN-LAST:event_btnUpdateInActionPerformed

    private void btnUpdateAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateAcActionPerformed
        // TODO add your handling code here:
        if (ac_Selected != null) {
            Account ac = new Account();
            ac.setAccountID(ac_Selected.getAccountID());
            ac.setPhone(txtPhone_Ac.getText());
            ac.setName(txtName_ac.getText());
            ac.setUserName(txtUserName_Ac.getText());
            ac.setMail(txtEmail_Ac.getText());
            ac.setAddress(txtAddress_AC.getText());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String birthday = sdf.format(txtBirtday_AC.getDate());
            ac.setDatetime(birthday);
            ac.setNote(txtNote_AC.getText());
            ac.setImg(txtImgLink_acc.getText());
            ac.setStatus(0);
            if (cboStatus_Ac.getSelectedItem().toString().equalsIgnoreCase("khoa")) {
                ac.setStatus(0);
            } else if (cboStatus_Ac.getSelectedItem().toString().equalsIgnoreCase("nhan vien")) {
                ac.setStatus(1);
            } else if (cboStatus_Ac.getSelectedItem().toString().equalsIgnoreCase("quan ly")) {
                ac.setStatus(2);
            } else if (cboStatus_Ac.getSelectedItem().toString().equalsIgnoreCase("admin")) {
                ac.setStatus(3);
            }
            if (cboGender_Ac.getSelectedItem().toString().equalsIgnoreCase("nam")) {
                ac.setGender(0);
            } else {
                ac.setGender(1);
            }
            String pass = txtPass_Ac.getText();
            if (pass.equals(txtPassCon_Ac.getText())) {
                ac.setPassword(pass);
                try {
                    if (AccountDAO.update(ac)) {
                        JOptionPane.showMessageDialog(paneProduct, "Update Sucess");
                        txtUserName_Ac.setText("");
                        txtName_ac.setText("");
                        txtEmail_Ac.setText("");
                        txtPhone_Ac.setText("");
                        txtAddress_AC.setText("");
                        txtBirtday_AC.setDateFormatString("");
                        txtPass_Ac.setText("");
                        txtPassCon_Ac.setText("");
                        txtNote_AC.setText("");
                        txtImgName_Acc.setText("");
                        txtImgLink_acc.setText("");
                        cboStatus_Ac.setSelectedIndex(0);
                        txtBirtday_AC.setDate(new Date());
                        cboGender_Ac.setSelectedIndex(0);
                        ac_Selected = null;
                        showAccount();
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(paneProduct, "Xac nhan mat khau phai giong nhau!");
            }

        } else {
            JOptionPane.showMessageDialog(paneProduct, "Slect 1 account to change!");
        }

    }//GEN-LAST:event_btnUpdateAcActionPerformed

    private void btnAddAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAcActionPerformed
        // TODO add your handling code here:

        int gender;
        if (cboGender_Ac.getSelectedItem().toString().equalsIgnoreCase("nam")) {
            gender = 0;
        } else {
            gender = 1;
        }
        String userName = txtUserName_Ac.getText();
        String name = txtName_ac.getText();
        String email = txtEmail_Ac.getText();
        String phone = txtPhone_Ac.getText();
        String address = txtAddress_AC.getText();
        String pass = txtPass_Ac.getText();
        int status = 0;
        if (cboStatus_Ac.getSelectedItem().toString().equalsIgnoreCase("khoa")) {
            status = 0;
        } else if (cboStatus_Ac.getSelectedItem().toString().equalsIgnoreCase("nhan vien")) {
            status = 1;
        } else if (cboStatus_Ac.getSelectedItem().toString().equalsIgnoreCase("quan ly")) {
            status = 2;
        } else if (cboStatus_Ac.getSelectedItem().toString().equalsIgnoreCase("admin")) {
            status = 3;
        }
        String note = txtNote_AC.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String birthday = sdf.format(txtBirtday_AC.getDate());

        String img = "image/" + txtImgName_Acc.getText();
        if (pass.equals(txtPassCon_Ac.getText())) {
            Account account = new Account(0, name, phone, address, email, img, userName, pass, status, note, gender, birthday);
            try {

                if (AccountDAO.insert(account)) {
                    JOptionPane.showMessageDialog(paneProduct, "Update Sucess");
                    txtUserName_Ac.setText("");
                    txtName_ac.setText("");
                    txtEmail_Ac.setText("");
                    txtPhone_Ac.setText("");
                    txtAddress_AC.setText("");
                    txtBirtday_AC.setDateFormatString("");
                    txtPass_Ac.setText("");
                    txtPassCon_Ac.setText("");
                    txtNote_AC.setText("");
                    txtImgName_Acc.setText("");
                    txtImgLink_acc.setText("");
                    cboStatus_Ac.setSelectedIndex(0);
                    txtBirtday_AC.setDate(new Date());
                    cboGender_Ac.setSelectedIndex(0);
                    ac_Selected = null;
                    showAccount();
                    showProduct();
                }
            } catch (SQLException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Xac nhan lai mat khau va mat khau phai giong nhau!");
        }


    }//GEN-LAST:event_btnAddAcActionPerformed

    private void btnRemoveAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveAcActionPerformed
        try {
            // TODO add your handling code here:
            CustomTableModel model = (CustomTableModel) tblAccount_acc.getModel();
            String id = model.getValueAt(tblAccount_acc.getSelectedRow(), 1).toString();
            if (AccountDAO.delete(id)) {
                JOptionPane.showMessageDialog(paneProduct, "Delete sucess");
                txtUserName_Ac.setText("");
                txtName_ac.setText("");
                txtEmail_Ac.setText("");
                txtPhone_Ac.setText("");
                txtAddress_AC.setText("");
                txtBirtday_AC.setDateFormatString("");
                txtPass_Ac.setText("");
                txtPassCon_Ac.setText("");
                txtNote_AC.setText("");
                txtImgName_Acc.setText("");
                txtImgLink_acc.setText("");
                cboStatus_Ac.setSelectedIndex(0);
                txtBirtday_AC.setDate(new Date());
                cboGender_Ac.setSelectedIndex(0);
                ac_Selected = null;
                showAccount();

            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnRemoveAcActionPerformed

    private void btnClearAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAcActionPerformed
        try {
            // TODO add your handling code here:
            txtUserName_Ac.setText("");
            txtName_ac.setText("");
            txtEmail_Ac.setText("");
            txtPhone_Ac.setText("");
            txtAddress_AC.setText("");
            txtBirtday_AC.setDateFormatString("");
            txtPass_Ac.setText("");
            txtPassCon_Ac.setText("");
            txtNote_AC.setText("");
            txtImgName_Acc.setText("");
            txtImgLink_acc.setText("");
            cboStatus_Ac.setSelectedIndex(0);
            txtBirtday_AC.setDate(new Date());
            cboGender_Ac.setSelectedIndex(0);
            ac_Selected = null;
            showAccount();
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnClearAcActionPerformed

    private void btnExportAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportAcActionPerformed
        // TODO add your handling code here:
        print(tblAccount_acc);
    }//GEN-LAST:event_btnExportAcActionPerformed

    private void btnExport_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExport_CaActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        exportDefaultTable(tblCategory_Ca);

    }//GEN-LAST:event_btnExport_CaActionPerformed

    private void btnClear_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear_CaActionPerformed
        try {
            // TODO add your handling code here:
            groupStatus_Ca.clearSelection();
            txtName_Ca.setText("");
            txtNote_Ca.setText("");
            c_Selected = null;
            txtName_Ca.requestFocusInWindow();
            showCategory();
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnClear_CaActionPerformed

    private void btnRemove_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove_CaActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            DefaultTableModel model = (DefaultTableModel) tblCategory_Ca.getModel();
            String id = model.getValueAt(tblCategory_Ca.getSelectedRow(), 0).toString();
            if (CategoryDAO.delete(id)) {
                JOptionPane.showMessageDialog(paneCategory, "Delete sucess");
                groupStatus_Ca.clearSelection();
                txtName_Ca.setText("");
                txtNote_Ca.setText("");
                c_Selected = null;
                showCategory();
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
    }//GEN-LAST:event_btnRemove_CaActionPerformed
    }
    private void btnAdd_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd_CaActionPerformed
        // TODO add your handling code here:
        String statusStr = groupStatus_Ca.getSelection().getActionCommand();
        int status;
        if (statusStr.equalsIgnoreCase("hide")) {
            status = 0;
        } else {
            status = 1;
        }
        String name = txtName_Ca.getText();
        String note = txtNote_Ca.getText();
        Category c = new Category(0, name, note, status);
        try {
            if (CategoryDAO.insert(c)) {
                JOptionPane.showMessageDialog(paneProduct, "Add Sussess");
                groupStatus_Ca.clearSelection();
                txtName_Ca.setText("");
                txtNote_Ca.setText("");
                c_Selected = null;
                loadPaneProduct();
                showCategory();
            }

        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnAdd_CaActionPerformed

    private void btnUpdate_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate_CaActionPerformed
        try {
            // TODO add your handling code here:
            if (c_Selected != null) {
                Category c = new Category();
                c.setCategory_Id(c_Selected.getCategory_Id());
                c.setName(txtName_Ca.getText());
                c.setNote(txtNote_Ca.getText());
                String statusStr = groupStatus_Ca.getSelection().getActionCommand();
                int status;
                if (statusStr.equalsIgnoreCase("hide")) {
                    status = 0;
                } else {
                    status = 1;
                }
                c.setStatus(status);
                if (CategoryDAO.update(c)) {
                    JOptionPane.showMessageDialog(paneProduct, "Update Sussess");
                    groupStatus_Ca.clearSelection();
                    txtName_Ca.setText("");
                    txtNote_Ca.setText("");
                    c_Selected = null;
                    showCategory();
                }
            } else {
                JOptionPane.showMessageDialog(paneCategory, "Slect 1 category to change!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnUpdate_CaActionPerformed

    private void cboCategory_PrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategory_PrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCategory_PrActionPerformed

    private void btnFind_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFind_CaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFind_CaActionPerformed

    private void itemEnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEnActionPerformed
        // TODO add your handling code here:
        setLocaleInForm(new Locale("en", "US"));
    }//GEN-LAST:event_itemEnActionPerformed

    private void itemVnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemVnActionPerformed
        // TODO add your handling code here:
        setLocaleInForm(new Locale("vi", "VN"));
    }//GEN-LAST:event_itemVnActionPerformed

    private void itemCnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCnActionPerformed
        // TODO add your handling code here:
        setLocaleInForm(new Locale("cn", "CN"));
    }//GEN-LAST:event_itemCnActionPerformed

    private void btnFindAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindAcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFindAcActionPerformed

    private void btnFinddow_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinddow_CaActionPerformed
        try {
            // TODO add your handling code here:
            showCategory(CategoryDAO.sortByAZ());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnFinddow_CaActionPerformed

    private void btnUpdate_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate_SpActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            if (sp_Selected != null) {
                Supplier sp = new Supplier();
                sp.setSupplier_Id(sp_Selected.getSupplier_Id());
                sp.setName(txtName_Sp.getText());
                sp.setPhone(txtPhone_Sp.getText());
                sp.setAddress(txtAddress_Sp.getText());
                sp.setEmail(txtEmail_Sp.getText());
                sp.setImg("image/" + txtNameImg_Sp.getText());
                sp.setNote(txtNote_Sp.getText());
                String statusStr = groupStatus_Sp.getSelection().getActionCommand();
                int status;
                if (statusStr.equalsIgnoreCase("hide")) {
                    status = 0;
                } else {
                    status = 1;
                }
                sp.setStatus(status);
                if (SupplierDAO.update(sp)) {
                    txtName_Sp.setText(null);
                    txtPhone_Sp.setText(null);
                    txtAddress_Sp.setText(null);
                    txtEmail_Sp.setText(null);
                    txtNote_Sp.setText(null);
                    groupStatus_Sp.clearSelection();
                    txtNote_Sp.setText(null);
                    sp_Selected = null;
                    txtNameImg_Sp.setText("");
                    txtLink_Sp.setText("");
                    lblIconIMG_Sp.setIcon(null);
                    showSupplier();
                JOptionPane.showMessageDialog(tabSupplierMain, "Update success!");
                } else 
                JOptionPane.showMessageDialog(tabSupplierMain, "Update fail!");
            } else {
                JOptionPane.showMessageDialog(tabSupplierMain, "Slect 1 supplier to change!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnUpdate_SpActionPerformed

    private void btnAdd_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd_SpActionPerformed
        // TODO add your handling code here:
        String statusStr = groupStatus_Sp.getSelection().getActionCommand();
        int status;
        if (statusStr.equalsIgnoreCase("hide")) {
            status = 0;
        } else {
            status = 1;
        }
        //String nation = (cboNation_Pd.getSelectedItem() != null) ? cboNation_Pd.getSelectedItem().toString() : " ";
        String name = txtName_Sp.getText();
        String phone = txtPhone_Sp.getText();
        String address = txtAddress_Sp.getText();
        String email = txtEmail_Sp.getText();
        String img = "image/" + txtName_Sp.getText();
        String note = txtNote_Sp.getText();
        Supplier sp = new Supplier(0, name, phone, address, email, note, status, img);
        try {
            if (SupplierDAO.insert(sp)) {
                txtName_Sp.setText(null);
                txtPhone_Sp.setText(null);
                txtAddress_Sp.setText(null);
                txtEmail_Sp.setText(null);
                txtNote_Sp.setText(null);
                groupStatus_Sp.clearSelection();
                txtNote_Sp.setText(null);
                showSupplier();
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadPaneHistory();
    }//GEN-LAST:event_btnAdd_SpActionPerformed

    private void btnRemove_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove_SpActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            CustomTableModel model = (CustomTableModel) tblSupplier_Sup.getModel();
            String id = model.getValueAt(tblSupplier_Sup.getSelectedRow(), 1).toString();
            if (SupplierDAO.delete(id)) {
                JOptionPane.showMessageDialog(null, "Delete Success");
                txtName_Sp.setText(null);
                txtPhone_Sp.setText(null);
                txtAddress_Sp.setText(null);
                txtEmail_Sp.setText(null);
                txtNote_Sp.setText(null);
                groupStatus_Sp.clearSelection();
                txtNote_Sp.setText(null);
                sp_Selected = null;
                txtNameImg_Sp.setText("");
                txtLink_Sp.setText("");
                lblIconIMG_Sp.setIcon(null);
                showSupplier();
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRemove_SpActionPerformed

    private void btnClear_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear_SpActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            txtName_Sp.setText(null);
            txtPhone_Sp.setText(null);
            txtAddress_Sp.setText(null);
            txtEmail_Sp.setText(null);
            txtNote_Sp.setText(null);
            groupStatus_Sp.clearSelection();
            txtNote_Sp.setText(null);
            sp_Selected = null;
            txtNameImg_Sp.setText("");
            txtLink_Sp.setText("");
            lblIconIMG_Sp.setIcon(null);
            showSupplier();
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnClear_SpActionPerformed

    private void btnExport_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExport_SpActionPerformed
        // TODO add your handling code here:

        exportCustomTable(tblSupplier_Sup);
    }//GEN-LAST:event_btnExport_SpActionPerformed

    private void btnFind_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFind_SpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFind_SpActionPerformed

    private void txtAZ_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAZ_SpActionPerformed
        // TODO add your handling code here:
        try {
            showSupplier(SupplierDAO.sortByAZ());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtAZ_SpActionPerformed

    private void btnUpdate_PdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate_PdActionPerformed
        try {
            // TODO add your handling code here:
            if (pd_Selected != null) {
                Producer pd = new Producer();
                pd.setProducer_Id(pd_Selected.getProducer_Id());
                pd.setName(txtName_Pd.getText());
                pd.setNote(txtNote_Pd.getText());
                //  String nation = (cboNation_Pd.getSelectedItem() != null) ? cboNation_Pd.getSelectedItem().toString() : " ";
                String nation = cboNation_Pd.getText();
                pd.setNation(nation);
                String statusStr = groupStatus_Pd.getSelection().getActionCommand();
                int status;
                if (statusStr.equalsIgnoreCase("hide")) {
                    status = 0;
                } else {
                    status = 1;
                }
                pd.setStatus(status);

                if (ProducerDAO.update(pd)) {
                    JOptionPane.showMessageDialog(paneProduct, "Update success");

                    groupStatus_Pd.clearSelection();
                    txtName_Pr.setText("");
                    txtNote_Pr.setText("");
                    txtUnit_Pr.setText("");
                    txtNameImg.setText("");
                    txtProductId_Pr.setText("");
                    groupStatus_Pd.clearSelection();
                    txtNameImg_Pr.setText("");
                    txtLink_Pr.setText(null);
                    lblShowImg_Pr.setIcon(null);
                    groupStatus_Pd.clearSelection();
                    txtName_Pd.setText("");
                    cboNation_Pd.setText("");
                    txtNote_Pd.setText("");
                    pd_Selected = null;
                    showProduct();
                }
            } else {
                JOptionPane.showMessageDialog(tabProducerMain, "Slect 1 producer to change!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            showProducer();
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUpdate_PdActionPerformed

    private void btnAdd_PdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd_PdActionPerformed
        String statusStr = groupStatus_Pd.getSelection().getActionCommand();
        int status;
        if (statusStr.equalsIgnoreCase("hide")) {
            status = 0;
        } else {
            status = 1;
        }
        //String nation = (cboNation_Pd.getSelectedItem() != null) ? cboNation_Pd.getSelectedItem().toString() : " ";
        String nation = cboNation_Pd.getText();
        String name = txtName_Pd.getText();
        String note = txtNote_Pd.getText();
        Producer pd = new Producer(0, name, nation, status, note);
        try {

            if (ProducerDAO.insert(pd)) {
                JOptionPane.showMessageDialog(paneProduct, "insert success");
                groupStatus_Pd.clearSelection();
                txtName_Pd.setText("");
                cboNation_Pd.setText("");
                txtNote_Pd.setText("");
                pd_Selected = null;
                txtName_Pr.setText("");
                txtNote_Pr.setText("");
                txtUnit_Pr.setText("");
                txtNameImg.setText("");
                txtProductId_Pr.setText("");
                groupStatus_Pd.clearSelection();
                txtNameImg_Pr.setText("");
                txtLink_Pr.setText(null);
                lblShowImg_Pr.setIcon(null);
                showProducer();
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAdd_PdActionPerformed

    private void btnRemove_PdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemove_PdActionPerformed
        try {
            if (pd_Selected != null) {
                DefaultTableModel model = (DefaultTableModel) tblProducer_Per.getModel();
                String id = model.getValueAt(tblProducer_Per.getSelectedRow(), 0).toString();
                if (ProducerDAO.delete(id)) {
                    JOptionPane.showMessageDialog(paneProduct, "Delete Success");
                    groupStatus_Pd.clearSelection();
                    txtName_Pr.setText("");
                    txtNote_Pr.setText("");
                    txtUnit_Pr.setText("");
                    txtNameImg.setText("");
                    txtProductId_Pr.setText("");
                    groupStatus_Pd.clearSelection();
                    txtNameImg_Pr.setText("");
                    txtLink_Pr.setText(null);
                    lblShowImg_Pr.setIcon(null);
                    groupStatus_Pd.clearSelection();
                    txtName_Pd.setText("");
                    cboNation_Pd.setText("");
                    txtNote_Pd.setText("");
                    pd_Selected = null;
                    showProducer();
                }
            } else {
                JOptionPane.showMessageDialog(null, "pless choose 1 producer to remove!");
            }
            // TODO add your handling code here:

        } catch (SQLException ex) {

            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRemove_PdActionPerformed

    private void btnClear_PdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear_PdActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            groupStatus_Pd.clearSelection();
            txtName_Pr.setText("");
            txtNote_Pr.setText("");
            txtUnit_Pr.setText("");
            txtNameImg.setText("");
            txtProductId_Pr.setText("");
            groupStatus_Pd.clearSelection();
            txtNameImg_Pr.setText("");
            txtLink_Pr.setText(null);
            lblShowImg_Pr.setIcon(null);
            groupStatus_Pd.clearSelection();
            txtName_Pd.setText("");
            cboNation_Pd.setText("");
            txtNote_Pd.setText("");
            pd_Selected = null;
            showProduct();
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnClear_PdActionPerformed

    private void btnExport_PdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExport_PdActionPerformed
        // TODO add your handling code here:
        exportDefaultTable(tblProducer_Per);
    }//GEN-LAST:event_btnExport_PdActionPerformed

    private void btnFind_PdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFind_PdActionPerformed
        // TODO add your handling code here:
        String string = txtFind_Pd.getText();
        DefaultTableModel model = (DefaultTableModel) tblProducer_Per.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
        tblProducer_Per.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(string));
    }//GEN-LAST:event_btnFind_PdActionPerformed

    private void btnAZ_PdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAZ_PdActionPerformed
        try {
            // TODO add your handling code here:
            showProducer(ProducerDAO.sortByAZ());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAZ_PdActionPerformed

    private void txtEmail_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmail_SpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmail_SpActionPerformed

    private void txtNote_PrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNote_PrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNote_PrActionPerformed

    private void btnFindRqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindRqActionPerformed
        // TODO add your handling code here:
        int product_id = cboNamePr_Rq_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboNamePr_Rq_F.getSelectedItem().toString());
        // int account_id = cboAccount_Rq_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboAccount_Rq_F.getSelectedItem().toString());
        int staff_id = cboStaff_Rq_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboStaff_Rq_F.getSelectedItem().toString());
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

    private void btnExprotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExprotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExprotActionPerformed

    private void btnClear_HtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear_HtActionPerformed
        // TODO add your handling code here:
        cboProducer_Ht.setSelectedIndex(0);
        cboProduct_Ht.setSelectedIndex(0);
        cboStatus_Ht.setSelectedIndex(0);
        cboSuplier_Ht.setSelectedIndex(0);
        spinQuantity_Ht.setValue(0);
        txtPrice_ht.setText("");
        txtNote_Ht.setText("");
        txtUnit_Ht.setText("");
        txtDate_Ht.setDate(new Date());
        ht_Selected = null;
    }//GEN-LAST:event_btnClear_HtActionPerformed

    private void txtAddress_InActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddress_InActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddress_InActionPerformed

    private void tblCategory_CaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCategory_CaMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblCategory_Ca.getModel();
        txtName_Ca.setText(model.getValueAt(tblCategory_Ca.getSelectedRow(), 1).toString());
        String status = model.getValueAt(tblCategory_Ca.getSelectedRow(), 2).toString();
        if (status.equalsIgnoreCase("hide")) {
            rdoHide_Ca.setSelected(true);
        } else {
            rdoShow_Ca.setSelected(true);
        }
        txtNote_Ca.setText(model.getValueAt(tblCategory_Ca.getSelectedRow(), 3).toString());
        c_Selected = (Category) model.getValueAt(tblCategory_Ca.getSelectedRow(), 1);
    }//GEN-LAST:event_tblCategory_CaMouseClicked

    private void btnAdd_PrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd_PrActionPerformed
        // TODO add your handling code here: String statusStr = buttonGroup1.getSelection().getActionCommand();
        String statusStr = groupStatus_Pr.getSelection().getActionCommand();
        int status;
        if (statusStr.equalsIgnoreCase("hide")) {
            status = 0;
        } else {
            status = 1;
        }
        String name = txtName_Pr.getText();
        String note = txtNote_Pr.getText();
        int quantity = 0;
        Category category = (Category) cboCategory_Pr.getSelectedItem();
        int cat_Id = category.getCategory_Id();
        String unit = txtUnit_Pr.getText();
        String img = "image/" + txtNameImg_Pr.getText();
        Product p = new Product(0, name, quantity, unit, cat_Id, img, note, status);

        try {

            if (ProductDAO.insert(p)) {
                JOptionPane.showMessageDialog(paneProduct, "Add Success");
                groupStatus_Pr.clearSelection();
                txtName_Pr.setText("");
                txtNote_Pr.setText("");
                txtUnit_Pr.setText("");
                txtNameImg.setText("");
                txtProductId_Pr.setText("");
                cboCategory_Pr.setSelectedIndex(0);
                txtLink_Pr.setText("");
                txtNameImg_Pr.setText("");
                pr_Selected = null;
                showProduct();
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnAdd_PrActionPerformed

    private void tabProductMainStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabProductMainStateChanged


    }//GEN-LAST:event_tabProductMainStateChanged

    private void btnUpdate_PrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate_PrActionPerformed
        // TODO add your handling code here:

        // TODO add your handling code here:
        if (pr_Selected != null) {
            Product pr = new Product();
            pr.setProduct_Id(pr_Selected.getCategory_Id());
            pr.setProduct_Id(pr_Selected.getProduct_Id());
            pr.setName(txtName_Pr.getText());
            pr.setNote(txtNote_Pr.getText());
            pr.setImg("test");
            pr.setUnit(txtUnit_Pr.getText());
            Category category = (Category) cboCategory_Pr.getSelectedItem();
            int cat_Id = category.getCategory_Id();
            pr.setCategory_Id(cat_Id);
            String statusStr = groupStatus_Pr.getSelection().getActionCommand();
            int status;
            if (statusStr.equalsIgnoreCase("hide")) {
                status = 0;
            } else {
                status = 1;
            }
            pr.setStatus(status);
            try {

                if (ProductDAO.update(pr)) {
                    JOptionPane.showMessageDialog(paneProduct, "Update Success");
                    groupStatus_Pr.clearSelection();
                    txtName_Pr.setText("");
                    txtNote_Pr.setText("");
                    txtUnit_Pr.setText("");
                    txtNameImg.setText("");
                    txtProductId_Pr.setText("");
                    cboCategory_Pr.setSelectedIndex(0);
                    txtLink_Pr.setText("");
                    txtNameImg_Pr.setText("");
                    pr_Selected = null;
                    showProduct();
                }
            } catch (SQLException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(paneProduct, "Slect 1 product to change!");
        }

    }//GEN-LAST:event_btnUpdate_PrActionPerformed

    private void tblProduct_PrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProduct_PrMouseClicked
        // TODO add your handling code here: 

        CustomTableModel model = (CustomTableModel) tblProduct_Pr.getModel();
        txtName_Pr.setText(model.getValueAt(tblProduct_Pr.getSelectedRow(), 2).toString());
        txtProductId_Pr.setText(model.getValueAt(tblProduct_Pr.getSelectedRow(), 1).toString());
        String status = model.getValueAt(tblProduct_Pr.getSelectedRow(), 5).toString();
        if (status.equalsIgnoreCase("hide")) {
            rdoHide_Pr.setSelected(true);
        } else {
            rdoShow_Pr.setSelected(true);
        }
        ImageIcon imageIcon = (ImageIcon) model.getValueAt(tblProduct_Pr.getSelectedRow(), 0);
        lblShowImg_Pr.setIcon(imageIcon);
        System.out.println(imageIcon.getDescription());
        String[] spliter = imageIcon.getDescription().split("/");
        txtNameImg_Pr.setText(spliter[spliter.length - 1]);
        txtLink_Pr.setText(imageIcon.getDescription());
        txtNote_Pr.setText(model.getValueAt(tblProduct_Pr.getSelectedRow(), 6).toString());
        pr_Selected = (Product) model.getValueAt(tblProduct_Pr.getSelectedRow(), 2);
        txtUnit_Pr.setText(model.getValueAt(tblProduct_Pr.getSelectedRow(), 3).toString());
        int cat_Id = pr_Selected.getCategory_Id();
        Category c = null;
        for (int i = 0; i < cboCategory_Pr.getItemCount(); i++) {
            c = (Category) cboCategory_Pr.getItemAt(i);
            if (c.getCategory_Id() == cat_Id) {
                break;
            }
        }
        cboCategory_Pr.setSelectedItem(c);
    }//GEN-LAST:event_tblProduct_PrMouseClicked

    private void tblSupplier_SupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplier_SupMouseClicked
        // TODO add your handling code here:
        CustomTableModel model = (CustomTableModel) tblSupplier_Sup.getModel();
        txtName_Sp.setText(model.getValueAt(tblSupplier_Sup.getSelectedRow(), 2).toString());
        txtPhone_Sp.setText(model.getValueAt(tblSupplier_Sup.getSelectedRow(), 3).toString());
        txtAddress_Sp.setText(model.getValueAt(tblSupplier_Sup.getSelectedRow(), 4).toString());
        txtEmail_Sp.setText(model.getValueAt(tblSupplier_Sup.getSelectedRow(), 5).toString());
        int status = Integer.parseInt(model.getValueAt(tblSupplier_Sup.getSelectedRow(), 6).toString());
        if (status == 0) {
            rdoHide_Sp.setSelected(true);
        } else {
            rdoShow_Sp.setSelected(true);
        }
        txtNote_Sp.setText(model.getValueAt(tblSupplier_Sup.getSelectedRow(), 7).toString());
        sp_Selected = (Supplier) model.getValueAt(tblSupplier_Sup.getSelectedRow(), 2);

    }//GEN-LAST:event_tblSupplier_SupMouseClicked

    private void tblProducer_PerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProducer_PerMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblProducer_Per.getModel();
        txtName_Pd.setText(model.getValueAt(tblProducer_Per.getSelectedRow(), 1).toString());
        String status = model.getValueAt(tblProducer_Per.getSelectedRow(), 3).toString();
        if (status.equalsIgnoreCase("hide")) {
            rdoHide_Pd.setSelected(true);
        } else {
            rdoShow_Pd.setSelected(true);
        }
//      txtNation_Pd.setText(model.getValueAt(tblProducer_Per.getSelectedRow(), 2).toString());
        cboNation_Pd.setText(model.getValueAt(tblProducer_Per.getSelectedRow(), 2).toString());
        txtNote_Pd.setText(model.getValueAt(tblProducer_Per.getSelectedRow(), 4).toString());
        pd_Selected = (Producer) model.getValueAt(tblProducer_Per.getSelectedRow(), 1);
    }//GEN-LAST:event_tblProducer_PerMouseClicked

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

    private void btnAddImg_PrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddImg_PrActionPerformed
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
                txtNameImg_Pr.setText(imageFileName);
                File out = new File("image/" + imageFile.getName());
                Path sorce = Paths.get(imageFile.getAbsolutePath());
                // Path desk = Paths.get("/Users/mac/Downloads");
                FileOutputStream desk = new FileOutputStream(out);
                Files.copy(sorce, desk);

                //Display img on lbl
                String imageFilePath = imageFile.getAbsolutePath();
                ImageIcon imageIcon = new ImageIcon(imageFilePath);
                //resize img
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                ImageIcon resizedImageIcon = new ImageIcon(image);
                lblShowImg_Pr.setIcon(resizedImageIcon);
                //display link img
                txtLink_Pr.setText(imageFilePath);
            } catch (IOException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnAddImg_PrActionPerformed

    private void btnExport_PrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExport_PrActionPerformed
        // TODO add your handling code here:   
        exportCustomTable(tblProduct_Pr);
    }//GEN-LAST:event_btnExport_PrActionPerformed

    private void txtFind_PrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFind_PrKeyReleased
        // TODO add your handling code here:
        findInDefaultTable(tblProduct_Pr, txtFind_Pr.getText());
    }//GEN-LAST:event_txtFind_PrKeyReleased

    private void tblAccount_accMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAccount_accMouseClicked
        // TODO add your handling code here:

        CustomTableModel model = (CustomTableModel) tblAccount_acc.getModel();
        txtUserName_Ac.setText(model.getValueAt(tblAccount_acc.getSelectedRow(), 2).toString());
        txtName_ac.setText(model.getValueAt(tblAccount_acc.getSelectedRow(), 3).toString());
        txtPhone_Ac.setText(model.getValueAt(tblAccount_acc.getSelectedRow(), 4).toString());
        txtEmail_Ac.setText(model.getValueAt(tblAccount_acc.getSelectedRow(), 5).toString());
        txtAddress_AC.setText(model.getValueAt(tblAccount_acc.getSelectedRow(), 6).toString());
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(tblAccount_acc.getSelectedRow(), 8).toString());
        } catch (ParseException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtBirtday_AC.setDate(date);
        ImageIcon imageIcon = (ImageIcon) model.getValueAt(tblAccount_acc.getSelectedRow(), 0);
        lblshowImg_Acc.setIcon(imageIcon);
        String[] spliter = imageIcon.getDescription().split("/");
        txtImgName_Acc.setText(spliter[spliter.length - 1]);
        txtImgLink_acc.setText(imageIcon.getDescription());

        //  txtNote_Pr.setText(model.getValueAt(tblAccount_acc.getSelectedRow(), 10).toString());
        String genderSelceted = model.getValueAt(tblAccount_acc.getSelectedRow(), 7).toString();
        String gender = null;
        for (int i = 0; i < cboGender_Ac.getItemCount(); i++) {
            gender = (String) cboGender_Ac.getItemAt(i);
            if (gender.equalsIgnoreCase(genderSelceted)) {
                break;
            }
        }
        String statusSelceted = model.getValueAt(tblAccount_acc.getSelectedRow(), 9).toString();
        String status = null;
        for (int i = 0; i < cboStatus_Ac.getItemCount(); i++) {
            status = (String) cboStatus_Ac.getItemAt(i);
            if (status.equalsIgnoreCase(statusSelceted)) {
                break;
            }
        }
        cboGender_Ac.setSelectedItem(gender);
        cboStatus_Ac.setSelectedItem(status);
        ac_Selected = new Account();
        ac_Selected.setAccountID(Integer.parseInt(model.getValueAt(tblAccount_acc.getSelectedRow(), 1).toString()));
    }//GEN-LAST:event_tblAccount_accMouseClicked

    private void tblRequest_HtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRequest_HtMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblRequest_Ht.getModel();
        int productSl = Integer.parseInt(model.getValueAt(tblRequest_Ht.getSelectedRow(), 1).toString());
        int producerSl = Integer.parseInt(model.getValueAt(tblRequest_Ht.getSelectedRow(), 2).toString());
        int supplierSl = Integer.parseInt(model.getValueAt(tblRequest_Ht.getSelectedRow(), 3).toString());
        int accountSl = Integer.parseInt(model.getValueAt(tblRequest_Ht.getSelectedRow(), 9).toString());
        txtPrice_ht.setText(model.getValueAt(tblRequest_Ht.getSelectedRow(), 4).toString());
        Float price = Float.parseFloat(model.getValueAt(tblRequest_Ht.getSelectedRow(), 4).toString());
        spinQuantity_Ht.setValue(model.getValueAt(tblRequest_Ht.getSelectedRow(), 5));
        String note = model.getValueAt(tblRequest_Ht.getSelectedRow(), 8).toString();
        try {
            txtAcName_Ht.setText(AccountDAO.searchById(accountSl).getName());
        } catch (SQLException ex) {
            System.out.println("khong co tai khoan nay");
        }
        try {
            Product product = ProductDAO.searchById(productSl);
            txtPrName_Ht.setText(product.getName());
            txtPdName_Ht.setText(ProducerDAO.searchById(producerSl).getName());
            txtSpName_Ht.setText(SupplierDAO.searchById(supplierSl).getName());
            txtUnit_Ht.setText(product.getUnit());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

        //.setText(model.getValueAt(tblRequest_Ht.getSelectedRow(), 6).toString());
        String date = model.getValueAt(tblRequest_Ht.getSelectedRow(), 7).toString();

        Date date1 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(tblRequest_Ht.getSelectedRow(), 7).toString());
        } catch (ParseException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtDate_Ht.setDate(date1);
        txtNote_Ht.setText(model.getValueAt(tblRequest_Ht.getSelectedRow(), 8).toString());

        for (int i = 0; i < cboProduct_Ht.getItemCount(); i++) {
            if (cboProduct_Ht.getItemAt(i).equalsIgnoreCase(String.valueOf(productSl))) {
                cboProduct_Ht.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < cboProducer_Ht.getItemCount(); i++) {
            if (cboProducer_Ht.getItemAt(i).equalsIgnoreCase(String.valueOf(producerSl))) {
                cboProducer_Ht.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < cboSuplier_Ht.getItemCount(); i++) {
            if (cboSuplier_Ht.getItemAt(i).equalsIgnoreCase(String.valueOf(supplierSl))) {
                cboSuplier_Ht.setSelectedIndex(i);
                break;
            }
        }
        String StatusStr = model.getValueAt(tblRequest_Ht.getSelectedRow(), 6).toString();
        int status = 0;
        if (StatusStr.equalsIgnoreCase("Dang xet duyet")) {
            status = 0;
        } else if (StatusStr.equalsIgnoreCase("Da duyet")) {
            status = 1;
        } else if (StatusStr.equalsIgnoreCase("Huy")) {
            status = 2;
        }
        for (int i = 0; i < cboStatus_Ht.getItemCount(); i++) {
            if (cboStatus_Ht.getItemAt(i).equals(StatusStr)) {
                cboStatus_Ht.setSelectedIndex(i);
                break;
            }
        }
        int ht_Id = Integer.parseInt(model.getValueAt(tblRequest_Ht.getSelectedRow(), 0).toString());
        int Quantity = Integer.parseInt(model.getValueAt(tblRequest_Ht.getSelectedRow(), 5).toString());
        ht_Selected = new History(ht_Id, Quantity, productSl, producerSl, supplierSl, note, price, accountSl, date, status);
    }//GEN-LAST:event_tblRequest_HtMouseClicked

    private void txtFind_CaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFind_CaKeyReleased
        // TODO add your handling code here:
        findInDefaultTable(tblCategory_Ca, txtFind_Ca.getText());
    }//GEN-LAST:event_txtFind_CaKeyReleased

    private void txtFind_AcKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFind_AcKeyReleased
        // TODO add your handling code here:
        findInDefaultTable(tblAccount_acc, txtFind_Ac.getText());
    }//GEN-LAST:event_txtFind_AcKeyReleased

    private void txtFind_SpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFind_SpKeyReleased
        // TODO add your handling code here:
        findInDefaultTable(tblSupplier_Sup, txtFind_Sp.getText());
    }//GEN-LAST:event_txtFind_SpKeyReleased

    private void txtFind_PdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFind_PdKeyReleased
        // TODO add your handling code here:
        findInDefaultTable(tblProducer_Per, txtFind_Pd.getText());
    }//GEN-LAST:event_txtFind_PdKeyReleased

    private void btnAddImg_AccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddImg_AccActionPerformed
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
                txtImgName_Acc.setText(imageFileName);
                File out = new File("image/" + imageFile.getName());
                Path sorce = Paths.get(imageFile.getAbsolutePath());
                // Path desk = Paths.get("/Users/mac/Downloads");
                FileOutputStream desk = new FileOutputStream(out);
                Files.copy(sorce, desk);

                //Display img on lbl
                String imageFilePath = imageFile.getAbsolutePath();
                ImageIcon imageIcon = new ImageIcon(imageFilePath);
                //resize img
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                ImageIcon resizedImageIcon = new ImageIcon(image);
                lblshowImg_Acc.setIcon(resizedImageIcon);
                //display link img
                txtImgLink_acc.setText(imageFilePath);
            } catch (IOException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnAddImg_AccActionPerformed

    public boolean checkPrice(String price) {
        String regex = "^[0-9]+";
        return price.matches(regex);
    }
    private void btnAdd_HtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd_HtActionPerformed
        // TODO add your handling code here:
        Float price = Float.parseFloat(txtPrice_ht.getText());
        if (price > 0) {
            int Quantity = Integer.parseInt(spinQuantity_Ht.getValue().toString());
            if (Quantity > 0) {
                int productSl = Integer.parseInt(cboProduct_Ht.getSelectedItem().toString());
                int producerSl = Integer.parseInt(cboProducer_Ht.getSelectedItem().toString());
                int supplierSl = Integer.parseInt(cboSuplier_Ht.getSelectedItem().toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String date = sdf.format(txtDate_Ht.getDate());

                String note = txtNote_Ht.getText();
                String StatusStr = cboStatus_Ht.getSelectedItem().toString();
                int status = 0;
                if (StatusStr.equalsIgnoreCase("Dang xet duyet")) {
                    status = 0;
                } else if (StatusStr.equalsIgnoreCase("Da duyet")) {
                    status = 1;
                } else if (StatusStr.equalsIgnoreCase("Huy")) {
                    status = 2;
                }
                History h = new History(0, Quantity, productSl, producerSl, supplierSl, note, price, currentAccount.getAccountID(), date, status);
                try {
                    if (HistoryDAO.insert(h)) {
                        cboProducer_Ht.setSelectedIndex(0);
                        cboProduct_Ht.setSelectedIndex(0);
                        cboStatus_Ht.setSelectedIndex(0);
                        cboSuplier_Ht.setSelectedIndex(0);
                        spinQuantity_Ht.setValue(0);
                        txtPrice_ht.setText("");
                        txtNote_Ht.setText("");
                        txtUnit_Ht.setText("");
                        txtDate_Ht.setDate(new Date());
                        ht_Selected = null;
                        JOptionPane.showMessageDialog(null, "Yeu cau nhap hang thanh cong!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Yeu cau nhap hang that bai!");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nhap sai so luong!");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Nhap sai gia!");
        }
        try {
            showHistory();
            showProduct();
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
        ht_Selected = null;
    }//GEN-LAST:event_btnAdd_HtActionPerformed

    private void btnDelete_PrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_PrActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            CustomTableModel model = (CustomTableModel) tblProduct_Pr.getModel();
            String id = model.getValueAt(tblProduct_Pr.getSelectedRow(), 1).toString();
            if (ProductDAO.delete(id)) {
                JOptionPane.showMessageDialog(paneProduct, "Delete Success");
                groupStatus_Pr.clearSelection();
                txtName_Pr.setText("");
                txtNote_Pr.setText("");
                txtUnit_Pr.setText("");
                txtNameImg.setText("");
                txtProductId_Pr.setText("");
                cboCategory_Pr.setSelectedIndex(0);
                txtLink_Pr.setText("");
                txtNameImg_Pr.setText("");
                pr_Selected = null;
                showProduct();
                showHistory();
                showFile(FileDAO.getAll());
            } else {
                JOptionPane.showMessageDialog(paneProduct, "Delete fail");
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDelete_PrActionPerformed

    private void btnReset_PrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReset_PrActionPerformed
        // TODO add your handlinssg code here:
        try {
            // TODO add your handling code here:
            groupStatus_Pr.clearSelection();
            txtName_Pr.setText("");
            txtNote_Pr.setText("");
            txtUnit_Pr.setText("");
            txtNameImg.setText("");
            txtProductId_Pr.setText("");
            cboCategory_Pr.setSelectedIndex(0);
            txtLink_Pr.setText("");
            txtNameImg_Pr.setText("");
            pr_Selected = null;
            showProduct();
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReset_PrActionPerformed

    private void btnImport_PrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImport_PrdActionPerformed
        // TODO add your handling code here:

        ImportDataFromExecel importer = new ImportDataFromExecel(tblProduct_Pr);
        importer.setVisible(true);
        importer.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_btnImport_PrdActionPerformed

    private void txtImgLink_accActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImgLink_accActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImgLink_accActionPerformed

    private void btnUpdate_RqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate_RqActionPerformed
        // TODO add your handling code here:
        if (f_Selected != null) {
            try {
                int newStatus = 0;
                String status = cboStatus_Rq.getSelectedItem().toString();
                if (status.equalsIgnoreCase("Dang xet duyet")) {
                    newStatus = 0;
                } else if (status.equalsIgnoreCase("Da duyet")) {
                    newStatus = 1;
                } else if (status.equalsIgnoreCase("Huy")) {
                    newStatus = 2;
                }
                f_Selected.setQuantity(Integer.parseInt(spinQuantity_Rq.getValue().toString()));
                f_Selected.setNote(txtNote_Rq.getText());
                f_Selected.setManager_Id(currentAccount.getAccountID());
                if (FileDAO.update(f_Selected, newStatus)) {
                    JOptionPane.showMessageDialog(null, "Change successful!");
                    cboStatus_Rq.setSelectedIndex(0);
                    spinQuantity_Rq.setValue(0);
                    txtNote_Rq.setText("");
                    f_Selected = null;
                } else {
                    JOptionPane.showMessageDialog(null, "Change fail!");
                }
            } catch (SQLException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
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

    private void tblRequest1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRequest1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblRequest1.getModel();
        model.getValueAt(tblRequest1.getSelectedRow(), 1).toString();
        int FileId = Integer.parseInt(model.getValueAt(tblRequest1.getSelectedRow(), 0).toString());
        String Name = "";
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
        for (int i = 0; i < cboStatus_Rq.getItemCount(); i++) {
            if (cboStatus_Rq.getItemAt(i).equals(StatusStr)) {
                cboStatus_Rq.setSelectedIndex(i);
                break;
            }
        }
        String Start_date = model.getValueAt(tblRequest1.getSelectedRow(), 6).toString();
        String Note = model.getValueAt(tblRequest1.getSelectedRow(), 7).toString();
        f_Selected = new Entities.File(FileId, Name, Product_Id, Account_Id, Manager_Id, Start_date, Note, Quantity, status);
        txtNote_Rq.setText(Note);
        spinQuantity_Rq.setValue(Quantity);


    }//GEN-LAST:event_tblRequest1MouseClicked

    private void cboProduct_HtPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboProduct_HtPopupMenuWillBecomeInvisible
        try {
            Product product = ProductDAO.searchById(Integer.parseInt(cboProduct_Ht.getSelectedItem().toString()));
            txtPrName_Ht.setText(product.getName());
            txtUnit_Ht.setText(product.getUnit());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cboProduct_HtPopupMenuWillBecomeInvisible

    private void cboProducer_HtPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboProducer_HtPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
        try {
            txtPdName_Ht.setText(ProducerDAO.searchById(Integer.parseInt(cboProducer_Ht.getSelectedItem().toString())).getName());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cboProducer_HtPopupMenuWillBecomeInvisible

    private void cboSuplier_HtPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboSuplier_HtPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
        try {
            txtSpName_Ht.setText(SupplierDAO.searchById(Integer.parseInt(cboSuplier_Ht.getSelectedItem().toString())).getName());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cboSuplier_HtPopupMenuWillBecomeInvisible

    private void btnUpdate_HtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate_HtActionPerformed
        // TODO add your handling code here:

// TODO add your handling code here:
        if (ht_Selected != null) {
            Float price = Float.parseFloat(txtPrice_ht.getText());
            if (price > 0) {
                int Quantity = Integer.parseInt(spinQuantity_Ht.getValue().toString());
                if (Quantity > 0) {
                    int productSl = Integer.parseInt(cboProduct_Ht.getSelectedItem().toString());
                    int producerSl = Integer.parseInt(cboProducer_Ht.getSelectedItem().toString());
                    int supplierSl = Integer.parseInt(cboSuplier_Ht.getSelectedItem().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String date = sdf.format(txtDate_Ht.getDate());
                    String note = txtNote_Ht.getText();
                    String StatusStr = cboStatus_Ht.getSelectedItem().toString();
                    int status = 0;
                    if (StatusStr.equalsIgnoreCase("Dang xet duyet")) {
                        status = 0;
                    } else if (StatusStr.equalsIgnoreCase("Da duyet")) {
                        status = 1;
                    } else if (StatusStr.equalsIgnoreCase("Huy")) {
                        status = 2;
                    }
                    History h = new History(ht_Selected.getHistoryId(), Quantity, productSl, producerSl, supplierSl, note, price, ht_Selected.getAccount_Id(), date, ht_Selected.getStatus());
                    try {
                        if (HistoryDAO.update(h, status)) {
                            cboProducer_Ht.setSelectedIndex(0);
                            cboProduct_Ht.setSelectedIndex(0);
                            cboStatus_Ht.setSelectedIndex(0);
                            cboSuplier_Ht.setSelectedIndex(0);
                            spinQuantity_Ht.setValue(0);
                            txtPrice_ht.setText("");
                            txtNote_Ht.setText("");
                            txtUnit_Ht.setText("");
                            txtDate_Ht.setDate(new Date());
                            ht_Selected = null;
                            JOptionPane.showMessageDialog(null, "Yeu cau nhap hang thanh cong!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Yeu cau nhap hang that bai!");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nhap sai so luong!");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Nhap sai gia!");
            }
            try {
                showHistory();
                showProduct();
            } catch (SQLException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Choose 1 to change!");
        }

    }//GEN-LAST:event_btnUpdate_HtActionPerformed

    private void btnFindHtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindHtActionPerformed
        // TODO add your handling code here:
        int product_id = cboNamePr_Ht_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboNamePr_Ht_F.getSelectedItem().toString());
        // int account_id = cboAccount_Rq_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboAccount_Rq_F.getSelectedItem().toString());
        int producer_id = cboProducer_Ht_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboProducer_Ht_F.getSelectedItem().toString());
        int supplier_id = cboSupplier_Ht_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboSupplier_Ht_F.getSelectedItem().toString());
        int account_id = cboAccount_Ht_F.getSelectedItem().toString().equals("All") ? -1 : Integer.parseInt(cboAccount_Ht_F.getSelectedItem().toString());
        int status = -1;
        String statusStr = cboStatus_Ht_F.getSelectedItem().toString();
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
            List<History> dataHistory = HistoryDAO.searchByAll(product_id, producer_id, supplier_id, account_id, date, status);
            showHistory(dataHistory);
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnFindHtActionPerformed

    private void btnZA_PdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZA_PdActionPerformed
        try {
            // TODO add your handling code here:
            showProducer(ProducerDAO.sortByZA());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnZA_PdActionPerformed

    private void btnFindup_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindup_CaActionPerformed

        try {
            showCategory(CategoryDAO.sortByZA());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnFindup_CaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            showProduct(ProductDAO.sortByAZ());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            showProduct(ProductDAO.sortByZA());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtZA_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtZA_SpActionPerformed
        // TODO add your handling code here:
        try {
            showSupplier(SupplierDAO.sortByZA());
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtZA_SpActionPerformed

    private void txtFind_HtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFind_HtKeyReleased
        // TODO add your handling code here:
        findInDefaultTable(tblRequest_Ht, txtFind_Ht.getText());
    }//GEN-LAST:event_txtFind_HtKeyReleased

    private void txtFind_RqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFind_RqKeyReleased
        // TODO add your handling code here:
        findInDefaultTable(tblRequest1, txtFind_Rq.getText());
    }//GEN-LAST:event_txtFind_RqKeyReleased

    private void btnAddImg_SpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddImg_SpActionPerformed
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
                txtNameImg_Sp.setText(imageFileName);
                File out = new File("image/" + imageFile.getName());
                Path sorce = Paths.get(imageFile.getAbsolutePath());
                // Path desk = Paths.get("/Users/mac/Downloads");
                FileOutputStream desk = new FileOutputStream(out);
                Files.copy(sorce, desk);

                //Display img on lbl
                String imageFilePath = imageFile.getAbsolutePath();
                ImageIcon imageIcon = new ImageIcon(imageFilePath);
                //resize img
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                ImageIcon resizedImageIcon = new ImageIcon(image);
                lblIconIMG_Sp.setIcon(resizedImageIcon);
                //display link img
                txtLink_Sp.setText(imageFilePath);
            } catch (IOException ex) {
                Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnAddImg_SpActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        print(tblProducer_Per);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        print(tblCategory_Ca);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        print(tblProduct_Pr);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void btnExport_Rq2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExport_Rq2ActionPerformed
        // TODO add your handling code here:
        print(tblRequest1);
    }//GEN-LAST:event_btnExport_Rq2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        print(tblRequest_Ht);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        print(tblSupplier_Sup);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void btnClear_RqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear_RqActionPerformed
        // TODO add your handling code here:
        cboStatus_Rq.setSelectedIndex(0);
        spinQuantity_Rq.setValue(0);
        txtNote_Rq.setText("");
        f_Selected = null;
    }//GEN-LAST:event_btnClear_RqActionPerformed
    private void setLocaleInForm(Locale local) {
        setL10N(local);
//        System.out.println(locale.getDisplayCountry());
        ResourceBundle rb = ResourceBundle.getBundle("resources.resources", local);
        menuLanguage.setText(rb.getString("language"));
        //  itemVn.setText(rb.getString("vietnamese"));
//        itemEn.setText(rb.getString("chinese"));
//        itemCn.setText(rb.getString("english"));
        tbljpane.setTitleAt(0, rb.getString("account"));
        tbljpane.setTitleAt(1, rb.getString("supplier"));
//        tbljpane.setTitleAt(2, rb.getString("producer"));
        tbljpane.setTitleAt(3, rb.getString("logout"));
        tbljpane.setTitleAt(4, rb.getString("aboutme"));
        tbljpane.setTitleAt(5, rb.getString("product"));

        tabAccountMain.setTitleAt(0, rb.getString("information"));
        lblUsernameIn.setText(rb.getString("userName"));
        lblNameIn.setText(rb.getString("name"));
        lblEmailIn.setText(rb.getString("email"));
        lblPhoneIn.setText(rb.getString("phone"));
        lblAddressIn.setText(rb.getString("address"));
        lblBirthdayIn.setText(rb.getString("birthday"));
        lblGenderIn.setText(rb.getString("gender"));
        lblPasswordIn.setText(rb.getString("password"));
        lblConfirmIn.setText(rb.getString("confirm"));
        lblImgIn.setText(rb.getString("image"));
        btnUpdateIn.setText(rb.getString("image"));
        btnBlockIn.setText(rb.getString("block"));

        tabAccountMain.setTitleAt(1, rb.getString("account"));
        lblUsernameAc.setText(rb.getString("userName"));
        lblNameAc.setText(rb.getString("name"));
        lblEmailAc.setText(rb.getString("email"));
        lblPhoneAc.setText(rb.getString("phone"));
        lblAddressAc.setText(rb.getString("address"));
        lblBirthdayAc.setText(rb.getString("birthday"));
        lblGenderAc.setText(rb.getString("gender"));
        lblPasswordAc.setText(rb.getString("password"));
        lblConfirmAc.setText(rb.getString("confirm"));
        lblImgAc.setText(rb.getString("image"));
        btnAddAc.setText(rb.getString("add"));
        btnUpdateAc.setText(rb.getString("update"));
        btnRemoveAc.setText(rb.getString("remove"));
        btnClearAc.setText(rb.getString("clear"));
        btnExportAc.setText(rb.getString("export"));
        btnFindAc.setText(rb.getString("find"));

        tabProductMain.setTitleAt(0, rb.getString("product"));
//        lblProductIdPr.setText(rb.getString("productid"));
        lblNamePr.setText(rb.getString("name"));
        lblCategoryPr.setText(rb.getString("category"));
//        lblUnitPr.setText(rb.getString("unit"));
        //  lblInventoryPr.setText(rb.getString("inventory"));
        //  lblInventoryPr.setText(rb.getString("inventory"));
        lblNotePr.setText(rb.getString("note"));
        btnAdd_Pr.setText(rb.getString("add"));
        btnUpdate_Pr.setText(rb.getString("update"));
        btnDelete_Pr.setText(rb.getString("remove"));
        btnReset_Pr.setText(rb.getString("reset"));
        btnFind_Pr.setText(rb.getString("find"));

        tabProductMain.setTitleAt(1, rb.getString("category"));
        lblNameCa.setText(rb.getString("name"));
//        lblStatusCa.setText(rb.getString("status"));
        lblNoteCa.setText(rb.getString("note"));
        btnAdd_Ca.setText(rb.getString("add"));
        btnUpdate_Ca.setText(rb.getString("update"));
        btnRemove_Ca.setText(rb.getString("remove"));
        btnClear_Ca.setText(rb.getString("clear"));
        btnExport_Ca.setText(rb.getString("export"));
        btnFind_Ca.setText(rb.getString("find"));
        //lblUsername.setText(rb.getString("userName"));
        //lblPassword.setText(rb.getString("password"));
        //btnLogin.setText(rb.getString("login"));
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

    private void loadPaneProduct() throws SQLException {
        try {
            cboCategory_Pr.removeAllItems();
            List<Category> Categories = CategoryDAO.getAll();
            for (Category c : Categories) {
                cboCategory_Pr.addItem(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadPaneHistory() {
        try {
            cboStatus_Ht.removeAllItems();
            cboStatus_Ht.addItem("Dang xet duyet");
            if (currentAccount.getStatus() == 3) {
                cboStatus_Ht.addItem("Da duyet");
                cboStatus_Ht.addItem("Huy");
            }
            cboProduct_Ht.removeAllItems();
            List<Product> products = ProductDAO.getAll();
            for (Product p : products) {
                cboProduct_Ht.addItem(String.valueOf(p.getProduct_Id()));
            }
            cboProducer_Ht.removeAllItems();
            List<Producer> producers = ProducerDAO.getAll();
            for (Producer per : producers) {
                cboProducer_Ht.addItem(String.valueOf(per.getProducer_Id()));
            }
            cboSuplier_Ht.removeAllItems();
            List<Supplier> suppliers = SupplierDAO.getAll();
            for (Supplier sup : suppliers) {
                cboSuplier_Ht.addItem(String.valueOf(sup.getSupplier_Id()));
            }
            if (products.size() >= 0) {
                txtPrName_Ht.setText(products.get(0).getName());
                txtUnit_Pr.setText(products.get(0).getUnit());
            }
            if (producers.size() >= 0) {
                txtPdName_Ht.setText(producers.get(0).getName());
            }
            if (suppliers.size() >= 0) {
                txtSpName_Ht.setText(suppliers.get(0).getName());
            }

            cboNamePr_Ht_F.removeAllItems();
            cboProducer_Ht_F.removeAllItems();
            cboStatus_Ht_F.removeAllItems();
            cboSupplier_Ht_F.removeAllItems();
            cboAccount_Ht_F.removeAllItems();

            cboStatus_Ht_F.addItem("All");
            cboStatus_Ht_F.addItem("Dang xet duyet");
            cboStatus_Ht_F.addItem("Da duyet");
            cboStatus_Ht_F.addItem("Huy");

            List<Account> listAc = AccountDAO.getAll();
            List<Product> listPr = ProductDAO.getAll();
            List<Supplier> listSp = SupplierDAO.getAll();
            List<Producer> listPd = ProducerDAO.getAll();

            cboNamePr_Ht_F.addItem("All");
            cboProducer_Ht_F.addItem("All");
            cboSupplier_Ht_F.addItem("All");
            cboAccount_Ht_F.addItem("All");

            for (Account ac : listAc) {
                if (ac.getStatus() != 1) {
                    cboAccount_Ht_F.addItem(String.valueOf(ac.getAccountID()));
                }
            }
            for (Product product : listPr) {
                cboNamePr_Ht_F.addItem(String.valueOf(product.getProduct_Id()));
            }
            for (Supplier supplier : listSp) {
                cboSupplier_Ht_F.addItem(String.valueOf(supplier.getSupplier_Id()));
            }
            for (Producer producer : listPd) {
                cboProducer_Ht_F.addItem(String.valueOf(producer.getProducer_Id()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmTrang_Chu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAZ_Pd;
    private javax.swing.JButton btnAddAc;
    private javax.swing.JButton btnAddIMG;
    private javax.swing.JButton btnAddImg_Acc;
    private javax.swing.JButton btnAddImg_Pr;
    private javax.swing.JButton btnAddImg_Sp;
    private javax.swing.JButton btnAdd_Ca;
    private javax.swing.JButton btnAdd_Ht;
    private javax.swing.JButton btnAdd_Pd;
    private javax.swing.JButton btnAdd_Pr;
    private javax.swing.JButton btnAdd_Sp;
    private javax.swing.JButton btnBlockIn;
    private javax.swing.JButton btnClearAc;
    private javax.swing.JButton btnClear_Ca;
    private javax.swing.JButton btnClear_Ht;
    private javax.swing.JButton btnClear_Pd;
    private javax.swing.JButton btnClear_Rq;
    private javax.swing.JButton btnClear_Sp;
    private javax.swing.JButton btnDelete_Pr;
    private javax.swing.JButton btnExportAc;
    private javax.swing.JButton btnExport_Ca;
    private javax.swing.JButton btnExport_Pd;
    private javax.swing.JButton btnExport_Pr;
    private javax.swing.JButton btnExport_Rq;
    private javax.swing.JButton btnExport_Rq1;
    private javax.swing.JButton btnExport_Rq2;
    private javax.swing.JButton btnExport_Sp;
    private javax.swing.JButton btnExprot;
    private javax.swing.JButton btnFindAc;
    private javax.swing.JButton btnFindHt;
    private javax.swing.JButton btnFindRq;
    private javax.swing.JButton btnFind_Ca;
    private javax.swing.JButton btnFind_Pd;
    private javax.swing.JButton btnFind_Pr;
    private javax.swing.JButton btnFind_Sp;
    private javax.swing.JButton btnFinddow_Ca;
    private javax.swing.JButton btnFindup_Ca;
    private javax.swing.JButton btnImport_Prd;
    private javax.swing.JButton btnRemoveAc;
    private javax.swing.JButton btnRemove_Ca;
    private javax.swing.JButton btnRemove_Pd;
    private javax.swing.JButton btnRemove_Sp;
    private javax.swing.JButton btnReset_Pr;
    private javax.swing.JButton btnUpdateAc;
    private javax.swing.JButton btnUpdateIn;
    private javax.swing.JButton btnUpdate_Ca;
    private javax.swing.JButton btnUpdate_Ht;
    private javax.swing.JButton btnUpdate_Pd;
    private javax.swing.JButton btnUpdate_Pr;
    private javax.swing.JButton btnUpdate_Rq;
    private javax.swing.JButton btnUpdate_Sp;
    private javax.swing.JButton btnZA_Pd;
    private javax.swing.JComboBox<String> cboAccount_Ht_F;
    private javax.swing.JComboBox cboCategory_Pr;
    private javax.swing.JComboBox cboGender_Ac;
    private javax.swing.JComboBox cboGender_In;
    private javax.swing.JComboBox<String> cboManager_Rq_F;
    private javax.swing.JComboBox<String> cboNamePr_Ht_F;
    private javax.swing.JComboBox<String> cboNamePr_Rq_F;
    private javax.swing.JTextField cboNation_Pd;
    private javax.swing.JComboBox<String> cboProducer_Ht;
    private javax.swing.JComboBox<String> cboProducer_Ht_F;
    private javax.swing.JComboBox<String> cboProduct_Ht;
    private javax.swing.JComboBox<String> cboStaff_Rq_F;
    private javax.swing.JComboBox<String> cboStatus_Ac;
    private javax.swing.JComboBox<String> cboStatus_Ht;
    private javax.swing.JComboBox<String> cboStatus_Ht_F;
    private javax.swing.JComboBox<String> cboStatus_Rq;
    private javax.swing.JComboBox<String> cboStatus_Rq_F;
    private javax.swing.JComboBox<String> cboSuplier_Ht;
    private javax.swing.JComboBox<String> cboSupplier_Ht_F;
    private javax.swing.ButtonGroup groupStatus_Ca;
    private javax.swing.ButtonGroup groupStatus_Pd;
    private javax.swing.ButtonGroup groupStatus_Pr;
    private javax.swing.ButtonGroup groupStatus_Sp;
    private javax.swing.JMenuItem itemCn;
    private javax.swing.JMenuItem itemEn;
    private javax.swing.JMenuItem itemVn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblAcName_Ht;
    private javax.swing.JLabel lblAccount_Ht_F;
    private javax.swing.JLabel lblAddressAc;
    private javax.swing.JLabel lblAddressIn;
    private javax.swing.JLabel lblAddress_Sp;
    private javax.swing.JLabel lblBirthdayAc;
    private javax.swing.JLabel lblBirthdayIn;
    private javax.swing.JLabel lblCategoryPr;
    private javax.swing.JLabel lblConfirmAc;
    private javax.swing.JLabel lblConfirmIn;
    private javax.swing.JLabel lblDate_Ht;
    private javax.swing.JLabel lblEmailAc;
    private javax.swing.JLabel lblEmailIn;
    private javax.swing.JLabel lblEmail_Sp;
    private javax.swing.JLabel lblGenderAc;
    private javax.swing.JLabel lblGenderIn;
    private javax.swing.JLabel lblIconIMG_Sp;
    private javax.swing.JLabel lblImgAc;
    private javax.swing.JLabel lblImgIn;
    private javax.swing.JLabel lblImg_Pr;
    private javax.swing.JLabel lblImg_Sp;
    private javax.swing.JLabel lblLoandate_Ht_F;
    private javax.swing.JLabel lblLoandate_Rq_F;
    private javax.swing.JLabel lblManager_Rq_F;
    private javax.swing.JLabel lblNameAc;
    private javax.swing.JLabel lblNameCa;
    private javax.swing.JLabel lblNameIn;
    private javax.swing.JLabel lblNamePr;
    private javax.swing.JLabel lblName_Pd;
    private javax.swing.JLabel lblName_Sp;
    private javax.swing.JLabel lblNation_Pd;
    private javax.swing.JLabel lblNoteCa;
    private javax.swing.JLabel lblNotePr;
    private javax.swing.JLabel lblNotePr1;
    private javax.swing.JLabel lblNote_Ht;
    private javax.swing.JLabel lblNote_Pd;
    private javax.swing.JLabel lblNote_Rq;
    private javax.swing.JLabel lblNote_Sp;
    private javax.swing.JLabel lblPasswordAc;
    private javax.swing.JLabel lblPasswordIn;
    private javax.swing.JLabel lblPdName_Ht;
    private javax.swing.JLabel lblPhoneAc;
    private javax.swing.JLabel lblPhoneIn;
    private javax.swing.JLabel lblPhone_Sp;
    private javax.swing.JLabel lblPrName_Ht;
    private javax.swing.JLabel lblPrName_Ht_F;
    private javax.swing.JLabel lblPrName_Rq_F;
    private javax.swing.JLabel lblPrice_ht;
    private javax.swing.JLabel lblProducer_Ht;
    private javax.swing.JLabel lblProducer_Ht_F;
    private javax.swing.JLabel lblProductIdPr;
    private javax.swing.JLabel lblProductId_Ht;
    private javax.swing.JLabel lblQuantity_Ht;
    private javax.swing.JLabel lblQuantity_Rq;
    private javax.swing.JLabel lblShowImg_Pr;
    private javax.swing.JLabel lblSpName_Ht;
    private javax.swing.JLabel lblStaff_Rq_F;
    private javax.swing.JLabel lblStatusCa;
    private javax.swing.JLabel lblStatus_Ac;
    private javax.swing.JLabel lblStatus_Ht;
    private javax.swing.JLabel lblStatus_Ht_F;
    private javax.swing.JLabel lblStatus_Pd;
    private javax.swing.JLabel lblStatus_Rq;
    private javax.swing.JLabel lblStatus_Rq_F;
    private javax.swing.JLabel lblStatus_Sp;
    private javax.swing.JLabel lblSuplier_Ht;
    private javax.swing.JLabel lblSupplier_Ht_F;
    private javax.swing.JLabel lblUnitPr;
    private javax.swing.JLabel lblUnit_Ht;
    private javax.swing.JLabel lblUsernameAc;
    private javax.swing.JLabel lblUsernameIn;
    private javax.swing.JLabel lblimg;
    private javax.swing.JLabel lblshowImg_Acc;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuLanguage;
    private javax.swing.JPanel paneAcc;
    private javax.swing.JPanel paneAdmin_Ac;
    private javax.swing.JPanel paneAdmin_Pd;
    private javax.swing.JPanel paneAdmin_Sp;
    private javax.swing.JPanel paneCategory;
    private javax.swing.JPanel paneInfo;
    private javax.swing.JPanel paneProduct;
    private javax.swing.JPanel paneRequest2;
    private javax.swing.JPanel paneRequest3;
    private javax.swing.JRadioButton rdoHide_Ca;
    private javax.swing.JRadioButton rdoHide_Pd;
    private javax.swing.JRadioButton rdoHide_Pr;
    private javax.swing.JRadioButton rdoHide_Sp;
    private javax.swing.JRadioButton rdoShow_Ca;
    private javax.swing.JRadioButton rdoShow_Pd;
    private javax.swing.JRadioButton rdoShow_Pr;
    private javax.swing.JRadioButton rdoShow_Sp;
    private javax.swing.JSpinner spinQuantity_Ht;
    private javax.swing.JSpinner spinQuantity_Rq;
    private javax.swing.JTabbedPane tabAboutMe;
    private javax.swing.JTabbedPane tabAccountMain;
    private javax.swing.JTabbedPane tabLogOut;
    private javax.swing.JPanel tabProducerMain;
    private javax.swing.JTabbedPane tabProductMain;
    private javax.swing.JTabbedPane tabRequestMain;
    private javax.swing.JPanel tabSupplierMain;
    private javax.swing.JTable tblAccount_acc;
    private javax.swing.JTable tblCategory_Ca;
    private javax.swing.JTable tblProducer_Per;
    private javax.swing.JTable tblProduct_Pr;
    private javax.swing.JTable tblRequest1;
    private javax.swing.JTable tblRequest_Ht;
    private javax.swing.JTable tblSupplier_Sup;
    private javax.swing.JTabbedPane tbljpane;
    private javax.swing.JButton txtAZ_Sp;
    private javax.swing.JLabel txtAcName_Ht;
    private javax.swing.JTextField txtAddress_AC;
    private javax.swing.JTextField txtAddress_In;
    private javax.swing.JTextField txtAddress_Sp;
    private com.toedter.calendar.JDateChooser txtBirtday_AC;
    private com.toedter.calendar.JDateChooser txtBirthday_In;
    private com.toedter.calendar.JDateChooser txtDate_Ht;
    private javax.swing.JTextField txtEmail_Ac;
    private javax.swing.JTextField txtEmail_In;
    private javax.swing.JTextField txtEmail_Sp;
    private javax.swing.JTextField txtFind_Ac;
    private javax.swing.JTextField txtFind_Ca;
    private javax.swing.JTextField txtFind_Ht;
    private javax.swing.JTextField txtFind_Pd;
    private javax.swing.JTextField txtFind_Pr;
    private javax.swing.JTextField txtFind_Rq;
    private javax.swing.JTextField txtFind_Sp;
    private javax.swing.JTextField txtImgLink_acc;
    private javax.swing.JTextField txtImgName_Acc;
    private javax.swing.JTextField txtLink;
    private javax.swing.JTextField txtLink_Pr;
    private javax.swing.JTextField txtLink_Sp;
    private javax.swing.JFormattedTextField txtLoanDate_Ht_F;
    private javax.swing.JFormattedTextField txtLoanDate_Rq_F;
    private javax.swing.JTextField txtNameImg;
    private javax.swing.JTextField txtNameImg_Pr;
    private javax.swing.JTextField txtNameImg_Sp;
    private javax.swing.JTextField txtName_Ca;
    private javax.swing.JTextField txtName_In;
    private javax.swing.JTextField txtName_Pd;
    private javax.swing.JTextField txtName_Pr;
    private javax.swing.JTextField txtName_Sp;
    private javax.swing.JTextField txtName_ac;
    private javax.swing.JTextArea txtNote_AC;
    private javax.swing.JTextArea txtNote_Ca;
    private javax.swing.JTextArea txtNote_Ht;
    private javax.swing.JTextArea txtNote_Pd;
    private javax.swing.JTextField txtNote_Pr;
    private javax.swing.JTextArea txtNote_Rq;
    private javax.swing.JTextArea txtNote_Sp;
    private javax.swing.JTextField txtPassCon_Ac;
    private javax.swing.JPasswordField txtPassCon_In;
    private javax.swing.JTextField txtPass_Ac;
    private javax.swing.JPasswordField txtPass_In;
    private javax.swing.JLabel txtPdName_Ht;
    private javax.swing.JTextField txtPhone_Ac;
    private javax.swing.JTextField txtPhone_In;
    private javax.swing.JTextField txtPhone_Sp;
    private javax.swing.JLabel txtPrName_Ht;
    private javax.swing.JTextField txtPrice_ht;
    private javax.swing.JTextField txtProductId_Pr;
    private javax.swing.JLabel txtSpName_Ht;
    private javax.swing.JLabel txtUnit_Ht;
    private javax.swing.JTextField txtUnit_Pr;
    private javax.swing.JTextField txtUserName_Ac;
    private javax.swing.JTextField txtUserName_In;
    private javax.swing.JButton txtZA_Sp;
    // End of variables declaration//GEN-END:variables
}
