package io.vinnie.servitetracker;

import io.vinnie.servitetracker.storage.CsvStorage;
import io.vinnie.servitetracker.storage.Storage;
import io.vinnie.servitetracker.storage.StudentDb;

import java.io.File;
import java.io.FileNotFoundException;

public class Main implements IdEnteredListener, FileSelectedListener {

    private Storage storage;
    private StudentDb studentDb;

    public Main() {
        Gui gui = new Gui();
        gui.addIdEnteredListener(this);
        gui.addFileSelectedListener(this);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void onIdEntered(String id) {
        storage.write(studentDb.getStudent(id));
    }

    @Override
    public void onFileSelected(File file) {
        System.out.println(file);
        try {
            storage = new CsvStorage(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
