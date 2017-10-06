
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import oracle.jdbc.driver.OracleLog;

public class Modell implements Data {

    private ArrayList<String> depsList = new ArrayList<>();
    private ArrayList<String> depsListUpdate = new ArrayList<>();
    private DefaultTableModel tblResult = new DefaultTableModel();
    private DefaultTableModel tblResultUpdate = new DefaultTableModel();
    private ArrayList<String> columnName = new ArrayList<>();
    private ArrayList<String> columnHead = new ArrayList<>();
    private int numberOfEmps;
    private DefaultComboBoxModel<String> dcbmOld = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<String> dcbmUpdate = new DefaultComboBoxModel<>();

    public Modell() {

        columnHead = header();
        columnName = headerUse();
        // dolgozókSzáma=dolgozókSzáma();

    }

    public ArrayList<String> depsListLoad() {
        depsList.clear();
        depsList.add("Összes");
        try {
            Class.forName(DRIVER);
            Connection kapcsolat = DriverManager.getConnection(URL, USER, PASSWORD);
            ResultSet rs = kapcsolat.createStatement().executeQuery(SQL_NAME_OF_DEPS);
            while (rs.next()) {
                depsList.add(rs.getString(1));
            }
            kapcsolat.close();
        } catch (Exception e) {
        }
        return depsList;
    }

    public DefaultComboBoxModel<String> depsList() {
        DefaultComboBoxModel<String> dcbm = null;

        try {
            Class.forName(DRIVER);
            Connection kapcsolat = DriverManager.getConnection(URL, USER, PASSWORD);
            ResultSet rs = kapcsolat.createStatement().executeQuery(SQL_NAME_OF_DEPS);

            while (rs.next()) {
                dcbm.addElement(rs.getString(1));
            }
            kapcsolat.close();
        } catch (Exception e) {
        }
        return dcbm;
    }

    private ArrayList<String> headerUse() {
        ArrayList<String> header = new ArrayList<>();
        header.add("Részlegnév");
        header.add("Dolgozó azonosító");
        header.add("Vezeték név");
        header.add("Keresztnév");
        header.add("E-mail");
        header.add("Telefonszám");
        header.add("Belépési dátum");
        header.add("Foglalkozás");
        header.add("Fizetés");
        header.add("Menedzser azonosító");

        return header;

    }

    public ArrayList<String> getHeader() {
        return columnName;
    }

    private ArrayList<String> header() {
        ArrayList<String> head = new ArrayList<>();
        head.add("DEPARTMENT_NAME");
        head.add("EMPLOYEE_ID");
        head.add("LAST_NAME");
        head.add("FIRST_NAME");
        head.add("EMAIL");
        head.add("PHONE_NUMBER");
        head.add("HIRE_DATE");
        head.add("JOB_ID");
        head.add("SALARY");
        head.add("MANAGER_ID");

        return head;

    }

    public ArrayList<String> getHead() {
        return columnHead;
    }

    public DefaultTableModel downloadDatas() {
        DefaultTableModel dtm = new DefaultTableModel();
        try {
            Class.forName(DRIVER);
            Connection kapcsolat
                    = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stat = kapcsolat.createStatement();
            ResultSet rs = stat.executeQuery(SQL_EMPS_DEPS);

            ResultSetMetaData metaadat = rs.getMetaData();

            dtm.setColumnIdentifiers(columnName.toArray(new String[columnName.size()]));

            while (rs.next()) {
                Object[] rekord = new Object[columnName.size()];
                for (int i = 0; i < columnName.size(); i++) {
                    rekord[i] = rs.getObject(columnHead.get(i));
                }
                dtm.addRow(rekord);
            }

            kapcsolat.close();
        } catch (Exception e) {
            ;
        }

        return dtm;
    }

    public void depsListUpdate(String oldValue, String newValue) {
        String sql = "UPDATE DEPARTMENTS SET DEPARTMENT_NAME ='" + newValue
                + "' WHERE DEPARTMENT_NAME ='" + oldValue + "'";
        try {
            Class.forName(DRIVER);
            Connection connection
                    = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stat = connection.createStatement();
            stat.executeQuery(sql);
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
        }
    }

}
