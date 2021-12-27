package agh.ics.oop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class AbstractWorldMap{
    private final int width;
    private final int height;
    private final int startAnimals;
    private final int grassValue;
    private final int minReproductionEnergy;
    private final int startEnergy;
    private final int cost;
    private final boolean isBordered;
    private final int jungleWidthStart;
    private final int jungleHeightStart;
    private final int jungleWidthEnd;
    private final int jungleHeightEnd;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    private final Map<String, Integer> genotypes = new HashMap<>();
    private final Random generator = new Random();
    private final ArrayList<Vector2d> jungleFields = new ArrayList<>();
    private final ArrayList<Vector2d> savannaFields = new ArrayList<>();
    private final List<IEngine> observers = new ArrayList<>();
    private int numberOfGrass = 0;
    private int numberOfAnimals = 0;

    public int getNumberOfGrass() {
        return numberOfGrass;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumberOfAnimals(Vector2d vector){return animals.get(vector).size();}

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getCost() {
        return cost;
    }

    public AbstractWorldMap(int width, int height, int jungleRatio, int startAnimals, int grassValue, int minReproductionEnergy, int startEnergy, int cost, boolean isBordered){
        this.width = width;
        this.height = height;
        double jungleRatio1 = (double) jungleRatio / 100;
        this.startAnimals = startAnimals;
        this.grassValue = grassValue;
        this.minReproductionEnergy = minReproductionEnergy;
        this.startEnergy = startEnergy;
        this.cost = cost;
        this.isBordered = isBordered;

        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j)
                animals.put(new Vector2d(i, j), new TreeSet<>(new AnimalComparator()));
        }

        int jungleWidth = (int) Math.floor((float) width * Math.sqrt(jungleRatio1 / (jungleRatio1 + 1)));
        int jungleHeight = (int) Math.floor((float) height * Math.sqrt(jungleRatio1 / (jungleRatio1 + 1)));
        jungleWidthStart = (int)Math.floor(((float)width - (float) jungleWidth) / 2);
        jungleWidthEnd = width - (int)Math.ceil(((float)width - (float) jungleWidth) / 2);
        jungleHeightStart = (int)Math.floor(((float)height - (float) jungleHeight) / 2);
        jungleHeightEnd = height - (int)Math.ceil(((float)height - (float) jungleHeight) / 2);

        for (int i = jungleWidthStart; i < jungleWidthEnd; ++i) {
            for (int j = jungleHeightStart; j < jungleHeightEnd; ++j)
                jungleFields.add(new Vector2d(i, j));
        }

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < jungleHeightStart; ++j)
                savannaFields.add(new Vector2d(i, j));
        }
        for (int i = 0; i < width; ++i) {
            for (int j = jungleHeightEnd; j < height; ++j)
                savannaFields.add(new Vector2d(i, j));
        }
        for (int i = 0; i < jungleWidthStart; ++i) {
            for (int j = jungleHeightStart; j < jungleHeightEnd; ++j)
                savannaFields.add(new Vector2d(i, j));
        }
        for (int i = jungleWidthEnd; i < width; ++i) {
            for (int j = jungleHeightStart; j < jungleHeightEnd; ++j)
                savannaFields.add(new Vector2d(i, j));
        }
    }

    public boolean isJungle(Vector2d vector){
        return vector.x() < jungleWidthEnd && vector.x() >= jungleWidthStart && vector.y() >= jungleHeightStart && vector.y() < jungleHeightEnd;
    }

    public void place(){
        for(int i = 0; i < startAnimals; ++i){
            Vector2d vector = new Vector2d(jungleWidthStart + generator.nextInt(jungleWidthEnd - jungleWidthStart),
                    jungleHeightStart + generator.nextInt(jungleHeightEnd - jungleHeightStart));
            jungleFields.remove(vector);
            Animal animal = new Animal(this, vector);
            animals.get(vector).add(animal);
            if(genotypes.get(animal.getGenotype().toString()) == null){
                genotypes.put(animal.getGenotype().toString(), 1);
            }else{
                genotypes.replace(animal.getGenotype().toString(), genotypes.get(animal.getGenotype().toString()) + 1);
            }
            plus(animal);
            numberOfAnimals = numberOfAnimals + 1;
        }
    }

    public boolean canMoveTo(Vector2d vector){
        if(isBordered){
            return vector.x() >= 0 && vector.x() < width && vector.y() >= 0 && vector.y() < height;
        }
        return true;
    }

    public boolean isGrass(Vector2d vector){
        return grasses.get(vector) != null;
    }

    public void grow(){
        if(jungleFields.size() != 0) {
            int j = generator.nextInt(jungleFields.size());
            Vector2d jungleGrass = jungleFields.remove(j);
            grasses.put(jungleGrass, new Grass());
            numberOfGrass = numberOfGrass + 1;
        }

        if(savannaFields.size() != 0) {
            int s = generator.nextInt(savannaFields.size());
            Vector2d savannaGrass = savannaFields.remove(s);
            grasses.put(savannaGrass, new Grass());
            numberOfGrass = numberOfGrass + 1;
        }
    }

    public void eat(Vector2d vector){
        ArrayList<Animal> eaters = getStrongest(vector);
        for(Animal eater : eaters){
            eater.addEnergy((int) Math.floor((float) grassValue / (float) eaters.size()));
        }
        grasses.remove(vector);
        numberOfGrass = numberOfGrass - 1;
    }

    public void change(Vector2d oldPosition, Vector2d newPosition, Animal animal){
        int x = animals.get(oldPosition).size();

        animals.get(oldPosition).remove(animal);

        int y = animals.get(oldPosition).size();

        animals.get(newPosition).add(animal);

        if(x == y){
            animals.get(oldPosition).removeIf(animal::equals); //bo TreeSet jest jaki jest :(
        }

        if(animals.get(oldPosition).isEmpty()) {
            if (oldPosition.x() >= jungleWidthStart && oldPosition.x() < jungleWidthEnd && oldPosition.y() >= jungleHeightStart && oldPosition.y() < jungleHeightEnd) {
                jungleFields.add(oldPosition);
            } else {
                savannaFields.add(oldPosition);
            }
        }

        if(newPosition.x() >= jungleWidthStart && newPosition.x() < jungleWidthEnd && newPosition.y() >= jungleHeightStart && newPosition.y() < jungleHeightEnd){
            jungleFields.remove(newPosition);
        }else{
            savannaFields.remove(newPosition);
        }
    }

    public void reproduce(Vector2d vector){
        int x = minReproductionEnergy - 1;
        int y = minReproductionEnergy - 1;
        Animal xAnimal = null;
        Animal yAnimal = null;

        for (Animal animal: animals.get(vector)) {
            if (animal.getEnergy() > x) {
                x = animal.getEnergy();
                xAnimal = animal;
            } else if (animal.getEnergy() > y) {
                y = animal.getEnergy();
                yAnimal = animal;
            }
        }

        if(y >= minReproductionEnergy && x >= minReproductionEnergy){
            assert xAnimal != null;
            xAnimal.reduceEnergy();
            assert yAnimal != null;
            yAnimal.reduceEnergy();
            xAnimal.child();
            yAnimal.child();
            xAnimal.informCousins();
            yAnimal.informCousins();
            Animal animal = new Animal(xAnimal, yAnimal);
            animal.addCousin(xAnimal);
            animal.addCousin(yAnimal);
            if(genotypes.get(animal.getGenotype().toString()) == null){
                genotypes.put(animal.getGenotype().toString(), 1);
            }else{
                genotypes.replace(animal.getGenotype().toString(), genotypes.get(animal.getGenotype().toString()) + 1);
            }
            plus(animal);
            numberOfAnimals = numberOfAnimals + 1;
            animals.get(vector).add(animal);
        }
    }

    public ArrayList<Animal> getStrongest(Vector2d vector){
        int maxEnergy = -2137;
        ArrayList<Animal> predators = new ArrayList<>();
        for (Animal animal: animals.get(vector)){
            if(animal.getEnergy() > maxEnergy){
                maxEnergy = animal.getEnergy();
                predators.clear();
            }
            if(animal.getEnergy() == maxEnergy){
                predators.add(animal);
            }
        }
        return predators;
    }

    public String getDominantGenotype(){
        int counter = 0;
        String dominant = "";
        for (Map.Entry<String, Integer> entry : genotypes.entrySet()) {
            if(entry.getValue() > counter){
                counter = entry.getValue();
                dominant = entry.getKey();
            }
        }
        return dominant;
    }

    public void death(Animal animal, Vector2d position) {
        int x = animals.get(position).size();

        animals.get(position).remove(animal);
        animal.deathEnergy();
        numberOfAnimals = numberOfAnimals - 1;
        genotypes.replace(animal.getGenotype().toString(), genotypes.get(animal.getGenotype().toString()) - 1);

        int y = animals.get(position).size();

        if(x == y){
            animals.get(position).removeIf(animal::equals); //bo TreeSet jest jaki jest :(
        }

        if (animals.get(position).isEmpty()){
            if (position.x() >= jungleWidthStart && position.x() < jungleWidthEnd && position.y() >= jungleHeightStart && position.y() < jungleHeightEnd) {
                jungleFields.add(position);
            } else {
                savannaFields.add(position);
            }
        }
    }

    public void addObserver(IEngine observer){
        observers.add(observer);
    }

    private void plus(Animal animal){
        for(IEngine observer : observers){
            observer.plus(animal);
        }
    }

    public Object objectAt(Vector2d vector){
        if(animals.get(vector).isEmpty()){
            return grasses.get(vector);
        }else{
            return getStrongest(vector).get(0);
        }
    }

    static class AnimalComparator implements Comparator<Animal>{

        @Override
        public int compare(Animal firstAnimal, Animal secondAnimal){
            if(firstAnimal.getEnergy() != secondAnimal.getEnergy()){
                return - firstAnimal.getEnergy() + secondAnimal.getEnergy();
            }else{
                return firstAnimal.hashCode() - secondAnimal.hashCode();
            }
        }
    }
}