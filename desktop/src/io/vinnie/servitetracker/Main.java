package io.vinnie.servitetracker;

public class Main implements IdEnteredListener {

    public Main() {
        Gui gui = new Gui();
        gui.addIdEnteredListener(this);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void onIdEntered(String id) {
        System.out.println(id);
    }
}
