/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frm;

import java.util.Vector;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author khanh
 */
public class CustomTableModel extends AbstractTableModel {

    private Vector columns;
    private Vector rows;

    public CustomTableModel() {
    }

    public CustomTableModel(Vector columns, Vector rows) {
        this.columns = columns;
        this.rows = rows;
    }

    @Override
    public Class getColumnClass(int columnIndex){
        if (getColumnName(columnIndex).equalsIgnoreCase("image")) {
            return Icon.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }
    
    public String getColumnName(int columnIndex){
        return this.columns.get(columnIndex).toString();
        //return this.columns[columnIndex];
    }

    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    @Override
    public int getColumnCount() {
        return this.columns.size();
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Vector row = (Vector) rows.get(i);
        return row.get(i1);
    }

}
