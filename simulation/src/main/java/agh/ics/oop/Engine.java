package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.util.ArrayList;
import java.util.List;

public class Engine implements IEngine{
    private final AbstractWorldMap map;
    private final List<Animal> animals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    private final List<App> apps = new ArrayList<>();
    private boolean stopped = false;
    private boolean turnOff = false;
    private int age = 0;

    public Engine(AbstractWorldMap map){
        this.map = map;
        map.addObserver(this);
        map.place();
    }

    public int getAge() {
        return age;
    }

    public double getAvgEnergy(){
        int sum = 0;
        int counter = 0;
        for (Animal animal : animals) {
            sum = sum + animal.getEnergy();
            counter = counter + 1;
        }
        return (double)sum / (double)counter;
    }

    public double getAvgLifetime(){
        int sum = 0;
        int counter = 0;
        for (Animal animal : deadAnimals) {
            sum = sum + animal.getAge();
            counter = counter + 1;
        }
        if(sum == 0){
            return 0;
        }
        return (double)sum / (double)counter;
    }

    public double getAvgChildren(){
        int sum = 0;
        int counter = 0;
        for (Animal animal : animals) {
            sum = sum + animal.getNumberOfChildren();
            counter = counter + 1;
        }
        return (double)sum / (double)counter;
    }

    public boolean isStopped(){
        return stopped;
    }

    public void stop(){
        stopped = !stopped;
    }

    public void end(){
        turnOff = true;
    }

    @Override
    public void addAppObserver(App app){
        apps.add(app);
    }

    @Override
    public void plus(Animal animal) {
        animals.add(animal);
    }

    private void update(){
        for(App app : apps){
            app.update();
        }
        try {
            Thread.sleep(200);
        }
        catch(InterruptedException sth){
            //do nothing
        }
    }

    @Override
    public void run() {
        ArrayList<Animal> deathNote = new ArrayList<>();

        while(!turnOff) {
            if(!stopped) {
                for (Animal animal : animals) {
                    if (animal.getEnergy() <= 0)
                        deathNote.add(animal);
                }
                for (Animal animal : deathNote) {
                    deadAnimals.add(animal);
                    animals.remove(animal);
                    animal.removeObserver(animal.getMap());
                    animal.setAgeOfDeath(this.age);
                    map.death(animal, animal.getPosition());
                }
                deathNote.clear();

                //------------------------
                if(animals.size() < 2) {
                    update();
                    break;
                }
                //------------------------

                map.grow();

                for (Animal animal : animals) {
                    animal.move();
                }

                for (int j = 0; j < map.getWidth(); ++j) {
                    for (int k = 0; k < map.getHeight(); ++k) {
                        Vector2d vector = new Vector2d(j, k);
                        if (map.getNumberOfAnimals(vector) > 0 && map.isGrass(vector))
                            map.eat(vector);

                        if (map.getNumberOfAnimals(vector) >= 2)
                            map.reproduce(vector);
                    }
                }

                age = age + 1;
                update();
            }
            else{
                try {
                    Thread.sleep(300);
                }
                catch(InterruptedException x){
                    //do nothing
                }
            }
        }
    }
}
