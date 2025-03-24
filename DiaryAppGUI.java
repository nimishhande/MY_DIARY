import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class DiaryAppGUI {
    private JFrame frame;
    private JTextArea entryTextArea;
    private DefaultListModel<String> entryListModel;
    private JList<String> entryList;
    private static final String FILE_NAME = "diary_entries.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DiaryAppGUI() {
        frame = new JFrame("My Diary App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Top Panel - Entry Input
        entryTextArea = new JTextArea(3, 30);
        JButton addButton = new JButton("Add Entry");
        addButton.addActionListener(e -> addEntry());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(new JScrollPane(entryTextArea), BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        // Middle Panel - Entry List
        entryListModel = new DefaultListModel<>();
        loadEntries();
        entryList = new JList<>(entryListModel);
        JScrollPane scrollPane = new JScrollPane(entryList);

        // Bottom Panel - Buttons
        JButton showButton = new JButton("Show Entries");
        JButton deleteButton = new JButton("Delete Entry");
        showButton.addActionListener(e -> showEntries());
        deleteButton.addActionListener(e -> deleteEntry());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(showButton);
        bottomPanel.add(deleteButton);

        // Add Panels to Frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addEntry() {
        String text = entryTextArea.getText().trim();
        if (!text.isEmpty()) {
            String timestampedEntry = DATE_FORMAT.format(new Date()) + " - " + text;
            entryListModel.addElement(timestampedEntry);
            saveEntries();
            entryTextArea.setText("");
            JOptionPane.showMessageDialog(frame, "Entry added successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Please write something before adding.");
        }
    }

    private void showEntries() {
        if (entryListModel.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No entries found.");
        } else {
            StringBuilder entries = new StringBuilder();
            for (int i = 0; i < entryListModel.size(); i++) {
                entries.append(i + 1).append(". ").append(entryListModel.get(i)).append("\n");
            }
            JOptionPane.showMessageDialog(frame, entries.toString(), "Diary Entries", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteEntry() {
        int selectedIndex = entryList.getSelectedIndex();
        if (selectedIndex != -1) {
            entryListModel.remove(selectedIndex);
            saveEntries();
            JOptionPane.showMessageDialog(frame, "Entry deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an entry to delete.");
        }
    }

    private void loadEntries() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    entryListModel.addElement(line);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error reading file.");
            }
        }
    }

    private void saveEntries() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < entryListModel.size(); i++) {
                bw.write(entryListModel.get(i));
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving entries.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DiaryAppGUI::new);
    }
}
