package io.vinnie.servitetracker.storage;

/**
 * Created by vmagro on 8/18/14.
 */
public class Student {

    public String name;
    public String id;
    public String priory;
    public String grade;

    public String toCsv() {
        return id + "," + name + "," + priory + "," + grade;
    }

    public static Student fromCsv(String csv) {
        String[] columns = csv.split(",");

        Student s = new Student();
        s.id = columns[0];
        s.name = columns[1];
        s.priory = columns[2];
        s.grade = columns[3];

        return s;
    }

}
