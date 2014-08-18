package io.vinnie.servitetracker;

import java.io.File;

public class Main implements IdEnteredListener, FileSelectedListener {

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
        System.out.println(id);
    }

    @Override
    public void onFileSelected(File file) {
        System.out.println(file);
    }
}
