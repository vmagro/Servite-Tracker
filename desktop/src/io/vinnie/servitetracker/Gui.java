package io.vinnie.servitetracker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.LinkedList;

/**
 * Created by vmagro on 8/18/14.
 */
public class Gui extends JFrame implements DocumentListener {

    private static final int ID_LENGTH = 10;

    private LinkedList<IdEnteredListener> idEnteredListeners = new LinkedList<IdEnteredListener>();

    private JTextField idField = new JTextField();

    public Gui() {
        add(idField);

        idField.getDocument().addDocumentListener(this);

        setSize(100, 100);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
}
