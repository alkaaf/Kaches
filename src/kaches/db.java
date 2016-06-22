/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaches;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alkaaf
 */
public class db {

    String path;
    Connection c;
    Statement st;
    ResultSet rs;

    public db(String path) {
        this.path = path;
        try {
            c = DriverManager.getConnection("jdbc:sqlite://" + path);
            createTable();
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DefaultTableModel select(String day, String month, String year) {
        DefaultTableModel model = null;
        Vector col = null;
        Vector vrow = null, vcol = null, temp = null;
        StringBuilder query = new StringBuilder();
        query.append("select * from kewud where 1=1 ");
        if (!day.equals("*")) {
            query.append("and strftime('%d', timestamp)='" + day + "' ");
        }
        if (!month.equals("*")) {
            query.append("and strftime('%m', timestamp)='" + month + "' ");
        }
        if (!year.equals("*")) {
            query.append("and strftime('%Y', timestamp)='" + year + "' ");
        }
        int nrow, ncol;
        try {
            st = c.createStatement();
            rs = st.executeQuery(query.toString());
            ncol = rs.getMetaData().getColumnCount();
            col = new Vector();
            for (int i = 1; i <= ncol; i++) {
                col.add(rs.getMetaData().getColumnLabel(i).toUpperCase());
            }
            vrow = new Vector();
            while (rs.next()) {
//                Date picker = new Date(rs.getLong("timestamp"));
                vcol = new Vector();
                vcol.add(rs.getInt("id"));
                vcol.add(rs.getString("timestamp"));
                vcol.add(rs.getString("properti"));
                vcol.add(rs.getString("deskripsi"));
                vcol.add(rs.getDouble("masuk"));
                vcol.add(rs.getDouble("keluar"));
                vcol.add(rs.getString("keterangan"));
                if (!vcol.isEmpty()) {
                    vrow.add(vcol);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
        model = new DefaultTableModel(vrow, col);
        return model;
    }

    public double getBalances() {
        double balance = 0;
        try {
            st = c.createStatement();
            rs = st.executeQuery("select sum(masuk)-sum(keluar) from kewud");
            balance = rs.getDouble(1);
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }
    public double getBalances(String day, String month, String year){
        double balance = 0;
        StringBuilder query = new StringBuilder();
        query.append("select sum(masuk)-sum(keluar) from kewud where 1=1 ");
        if (!day.equals("*")) {
            query.append("and strftime('%d', timestamp)='" + day + "' ");
        }
        if (!month.equals("*")) {
            query.append("and strftime('%m', timestamp)='" + month + "' ");
        }
        if (!year.equals("*")) {
            query.append("and strftime('%Y', timestamp)='" + year + "' ");
        }
        try {
            st = c.createStatement();
            rs = st.executeQuery(query.toString());
            balance = rs.getDouble(1);
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }

    public void insert(String timestamp, String barang, String deskripsi, double masuk, double keluar, String ket) {
        try {
            st = c.createStatement();
            st.executeUpdate("insert into kewud values (null, '" + timestamp + "','" + barang + "', '" + deskripsi + "'," + masuk + "," + keluar + ", '" + ket + "')");
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(int id) {
        try {
            st = c.createStatement();
            st.executeUpdate("delete from kewud where id=" + id);
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createTable() {
        try {
            st = c.createStatement();
            String query
                    = "create table if not exists kewud("
                    + "id integer primary key autoincrement,"
                    + "timestamp varchar(20),"
                    + "properti varchar(50),"
                    + "deskripsi varchar(255),"
                    + "masuk double,"
                    + "keluar double,"
                    + "keterangan varchar(200))";
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void deleteAllRecord(){
        try {
            st = c.createStatement();
            st.executeUpdate("delete from kewud");
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    public void dropTable(){
        try {
            st = c.createStatement();
            st.executeUpdate("drop table kewud");
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    public int getRowSize() {
        int rowsize = 0;
        try {
            st = c.createStatement();
            ResultSet rs = st.executeQuery("select count(id) from kewud");
            rowsize = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rowsize;
    }

    public DefaultListModel getColName() {
        DefaultListModel lm = new DefaultListModel();
        try {
            st = c.createStatement();
            ResultSet rs = st.executeQuery("select * from kewud");
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                lm.addElement(rs.getMetaData().getColumnLabel(i));
            }
        } catch (SQLException ex) {
            Logger.getLogger(db.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lm;
    }
}
