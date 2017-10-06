
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class View extends JFrame implements ActionListener {

    private Modell modell;
    private DefaultTableModel dtm;
    private JTable tblResult = new JTable(dtm);
    private JButton btDepsNameChange = new JButton("Részlegek módosítása");
    private JButton btUpdate = new JButton("Frissít");
    private JLabel lbDeps = new JLabel("Részlegek");
    private TableRowSorter<DefaultTableModel> filter;
    private JComboBox cbDeps = new JComboBox();
    private JToolBar tbToolkit = new JToolBar();
    private DefaultComboBoxModel<String> cbm;

    public View(Modell modell) {
        this.modell = modell;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("HR");
        setSize(800, 500);
        setLocationRelativeTo(this);
        JPanel pnToolkit = new JPanel();
        tbToolkit.add(lbDeps);

        depsFill();
        cbDeps.setSelectedIndex(0);
        tbToolkit.add(cbDeps);
        tbToolkit.add(btDepsNameChange);

        tbToolkit.add(btUpdate);
        pnToolkit.add(tbToolkit);
        add(pnToolkit, BorderLayout.NORTH);
        dtm = modell.downloadDatas();
        try {
            tblResult.setModel(dtm);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        filter = new TableRowSorter<>(dtm);
        tblResult.setRowSorter(filter);

        btDepsNameChange.addActionListener((ActionEvent e) -> {
            String oldValue = (String) cbDeps.getSelectedItem();
            String newValue;
            do {
                newValue = (String) JOptionPane.showInputDialog(null, "Osztály neve :",
                        "Osztály nevének szerkesztése", JOptionPane.QUESTION_MESSAGE,
                        null, null, (String) cbDeps.getSelectedItem());
            } while (!isInputOK(newValue, oldValue, modell.depsListLoad()));
            if (newValue != null && (!newValue.equals(oldValue))) {
                modell.depsListUpdate(oldValue, newValue);

            }

        });
        btUpdate.addActionListener((ActionEvent e) -> {

            depsFill();

            dtm = modell.downloadDatas();
            tblResult.setModel(dtm);
            filter = new TableRowSorter<>(dtm);
            tblResult.setRowSorter(filter);

        });
        add(new JScrollPane(tblResult), BorderLayout.CENTER);
        cbDeps.addActionListener(this);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (cbDeps.getSelectedIndex() == 0) {
            filter = (TableRowSorter<DefaultTableModel>) tblResult.getRowSorter();
            filter.setRowFilter(null);
        } else {
            filter = (TableRowSorter<DefaultTableModel>) tblResult.getRowSorter();
            try {
                filter.setRowFilter(RowFilter.regexFilter((String) cbDeps.getSelectedItem(), 0));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ;
            }

        }
    }

    private boolean isInputOK(String newValue, String oldValue,
            ArrayList<String> depList) {
        return newValue == null || ((newValue.equals(oldValue)
                || !depList.contains(newValue)) && !newValue.isEmpty());
    }

    private void depsFill() {
        ArrayList<String> deps = modell.depsListLoad();
        //cmRészlegek.removeAllElements();
        cbDeps.removeAllItems();
        for (String string : deps) {
            cbDeps.addItem(string);
        }
    }

}
