package io.vinnie.servitetracker.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by vmagro on 8/18/14.
 */
public class CsvStorage implements Storage {

    private final PrintStream output;

    public CsvStorage(File f) throws FileNotFoundException {
        output = new PrintStream(new FileOutputStream(f));
    }

    @Override
    public void write(Student s) {
        output.println(s.toCsv());
    }
}
