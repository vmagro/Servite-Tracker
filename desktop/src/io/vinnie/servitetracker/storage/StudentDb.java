package io.vinnie.servitetracker.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by vmagro on 8/18/14.
 */
public class StudentDb {

    private HashMap<String, Student> students = new HashMap<String, Student>(1000);

    public StudentDb(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                Student s = Student.fromCsv(line);
                students.put(s.id, s);
            }
            System.out.println("Loaded " + students.size() + " students");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Student getStudent(String id) {
        return students.get(id);
    }

}
