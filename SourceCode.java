package text;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SimpleTextEditor extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;

    public SimpleTextEditor() {
        // Set up the JFrame
        setTitle("Simple Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the JTextArea and add it to a JScrollPane
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Create the JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Create the File menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Add menu items to the File menu
        addMenuItem(fileMenu, "New", KeyEvent.VK_N, e -> newFile());
        addMenuItem(fileMenu, "Open", KeyEvent.VK_O, e -> openFile());
        addMenuItem(fileMenu, "Save", KeyEvent.VK_S, e -> saveFile());
        addMenuItem(fileMenu, "Save As", KeyEvent.VK_A, e -> saveFileAs());

        // Set the JMenuBar for the JFrame
        setJMenuBar(menuBar);

        // Create a JFileChooser
        fileChooser = new JFileChooser();

        // Show the JFrame
        setVisible(true);
    }

    private void addMenuItem(JMenu menu, String name, int keyEvent, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(listener);
        menu.add(menuItem);
    }

    private void newFile() {
        textArea.setText("");
        currentFile = null;
        setTitle("Simple Text Editor - New File");
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
                setTitle("Simple Text Editor - " + currentFile.getName());
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Error opening file.");
            }
        }
    }

    private void saveFile() {
        if (currentFile != null) {
            writeFile(currentFile);
        } else {
            saveFileAs();
        }
    }

    private void saveFileAs() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            writeFile(currentFile);
        }
    }

    private void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            textArea.write(writer);
            setTitle("Simple Text Editor - " + file.getName());
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Error saving file.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Unused, as we're using lambda expressions for action listeners
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleTextEditor::new);
    }
}
