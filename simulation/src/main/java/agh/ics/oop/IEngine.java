package agh.ics.oop;

import agh.ics.oop.gui.App;

public interface IEngine extends Runnable {

    void run();

    void plus(Animal animal);

    void addAppObserver(App app);

}