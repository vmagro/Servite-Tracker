package io.vinnie.servitetracker;

import io.vinnie.servitetracker.storage.CsvStorage;
import io.vinnie.servitetracker.storage.Storage;
import io.vinnie.servitetracker.storage.Student;
import io.vinnie.servitetracker.storage.StudentDb;

import java.io.File;
import java.io.FileNotFoundException;

public class Main implements IdEnteredListener, FileSelectedListener {

    private Gui gui;
    private Storage storage;
    private StudentDb studentDb;

    public Main() {
        gui = new Gui();
        gui.addIdEnteredListener(this);
        gui.addFileSelectedListener(this);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void onIdEntered(String id) {
        if (studentDb == null) {
            gui.error("Student database not loaded");
        } else if (storage == null) {
            gui.error("Output file not selected");
        } else {
            Student student = studentDb.getStudent(id);
            gui.showStudent(student);
            storage.write(student);
            gui.resetIdField();
        }
    }

    @Override
    public void onOutputFileSelected(File file) {
        System.out.println(file);
        try {
            storage = new CsvStorage(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStudentDbFileSelected(File file) {
        studentDb = new StudentDb(file);
    }
}
