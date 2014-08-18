package io.vinnie.servitetracker;

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
    private JButton chooseFileButton = new JButton("Choose a file");
    private JFileChooser fileChooser = new JFileChooser();
    private JLabel outputFileName = new JLabel();

    public Gui() {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(chooseFileButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(outputFileName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(idField, constraints);


        chooseFileButton.addActionListener(this);
        idField.getDocument().addDocumentListener(this);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        fileChooser.addActionListener(this);


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

    public void removeIdEnteredListener(IdEnteredListener listener) {
        idEnteredListeners.remove(listener);
    }

    public void addFileSelectedListener(FileSelectedListener listener) {
        fileSelectedListeners.add(listener);
    }

    public void removeFileSelectedListener(FileSelectedListener listener) {
        fileSelectedListeners.remove(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(chooseFileButton)) {
            fileChooser.showSaveDialog(this);
        }
        if (e.getSource().equals(fileChooser)) {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                outputFileName.setText(fileChooser.getSelectedFile().getAbsolutePath());
                for (FileSelectedListener listener : fileSelectedListeners) {
                    listener.onFileSelected(fileChooser.getSelectedFile());
                }
            }
        }
    }
}
