package io.vinnie.servitetracker;

import io.vinnie.servitetracker.storage.Student;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Created by vmagro on 8/18/14.
 */
public class Gui extends JFrame implements DocumentListener, ActionListener {

    private static final int ID_LENGTH = 10;

    private LinkedList<IdEnteredListener> idEnteredListeners = new LinkedList<IdEnteredListener>();
    private LinkedList<FileSelectedListener> fileSelectedListeners = new LinkedList<FileSelectedListener>();

    private JTextField idField = new JTextField();

    private JButton chooseInputFileButton = new JButton("Choose input file");
    private JFileChooser inputFileChooser = new JFileChooser();
    private JButton chooseOutputFileButton = new JButton("Choose output file");
    private JFileChooser outputFileChooser = new JFileChooser();
    private JLabel inputFileName = new JLabel("No input selected");
    private JLabel outputFileName = new JLabel("No output selected");

    private JLabel studentId = new JLabel();
    private JLabel studentName = new JLabel();
    private JLabel studentPriory = new JLabel();
    private JLabel studentGrade = new JLabel();

    public Gui() {
        setTitle("Servite Tracker");
        setLayout(new GridLayout(0, 2));

        add(chooseInputFileButton);
        add(inputFileName);

        add(chooseOutputFileButton);
        add(outputFileName);

        idField.setSize(200, idField.getHeight());
        add(idField);

        add(studentId);
        add(studentName);

        add(studentPriory);

        add(studentGrade);


        idField.getDocument().addDocumentListener(this);
        chooseInputFileButton.addActionListener(this);
        chooseOutputFileButton.addActionListener(this);
        inputFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        outputFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        inputFileChooser.addActionListener(this);
        outputFileChooser.addActionListener(this);


        setSize(500, 200);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        System.out.println("Initialized GUI");
    }


    @Override
    public void insertUpdate(DocumentEvent e) {
        idUpdated();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        idUpdated();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        idUpdated();
    }

    private void idUpdated() {
        if (idField.getText().length() == ID_LENGTH) {
            for (IdEnteredListener listener : idEnteredListeners) {
                listener.onIdEntered(idField.getText());
            }
        }
    }

    public void addIdEnteredListener(IdEnteredListener listener) {
        idEnteredListeners.add(listener);
    }

    public void addFileSelectedListener(FileSelectedListener listener) {
        fileSelectedListeners.add(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(chooseInputFileButton)) {
            inputFileChooser.showOpenDialog(this);
        }
        if (e.getSource().equals(chooseOutputFileButton)) {
            outputFileChooser.showSaveDialog(this);
        }
        if (e.getSource().equals(outputFileChooser)) {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                outputFileName.setText(outputFileChooser.getSelectedFile().getAbsolutePath());
                for (FileSelectedListener listener : fileSelectedListeners) {
                    listener.onOutputFileSelected(outputFileChooser.getSelectedFile());
                }
            }
        }
        if (e.getSource().equals(inputFileChooser)) {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                inputFileName.setText(outputFileChooser.getSelectedFile().getAbsolutePath());
                for (FileSelectedListener listener : fileSelectedListeners) {
                    listener.onStudentDbFileSelected(outputFileChooser.getSelectedFile());
                }
            }
        }
    }

    public void resetIdField() {
        idField.setText("");
        idField.grabFocus();
    }

    public void showStudent(Student s) {
        studentId.setText(s.id);
        studentName.setText(s.name);
        studentPriory.setText(s.priory);
        studentGrade.setText(s.grade);
    }

    public void error(String message) {
        error("Error", message);
    }

    public void error(final String title, final String message) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JDialog dialog = new JDialog(Gui.this, title);
                    dialog.add(new JLabel(message));
                    dialog.pack();
                    dialog.setSize(dialog.getWidth() + 20, dialog.getHeight() + 20);
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
