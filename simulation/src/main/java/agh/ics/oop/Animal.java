package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Animal{
    private Vector2d position;
    private MapDirection orientation = MapDirection.NORTH.roll();
    private final AbstractWorldMap map;
    private final Genotype genotype;
    private final int startEnergy;
    private int energy;
    private final int cost;
    private int numberOfChildren = 0;
    private int numberOfCousins = 0;
    private int age = 0;
    private int ageOfDeath = -1;
    private final List<AbstractWorldMap> observers = new ArrayList<>();
    private final List<Animal> cousins = new ArrayList<>();

    public Animal(AbstractWorldMap map, Vector2d initialPosition){
        this.map = map;
        startEnergy = map.getStartEnergy();
        cost = map.getCost();
        energy = startEnergy;
        addObserver(this.map);
        this.position = initialPosition;
        this.genotype = new Genotype();
    }

    public Animal(Animal strongParent, Animal weakParent){
        float proportion = (float)(strongParent.getEnergy()) / (float)(strongParent.getEnergy() + weakParent.getEnergy());
        this.map = strongParent.getMap();
        startEnergy = map.getStartEnergy();
        cost = map.getCost();
        addObserver(this.map);
        this.position = strongParent.getPosition();
        this.genotype = new Genotype(strongParent.getGenotype(), weakParent.getGenotype(), (int)Math.floor(proportion * 32));
        this.energy = (int)Math.floor(strongParent.getEnergy() * 0.25 + weakParent.getEnergy() * 0.25);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return energy == animal.energy && Objects.equals(position, animal.position) && orientation == animal.orientation
                && Objects.equals(map, animal.map) && Objects.equals(genotype, animal.genotype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, orientation, map, genotype, energy);
    }

    public AbstractWorldMap getMap() {
        return map;
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public int getNumberOfCousins() {
        return numberOfCousins;
    }

    public int getAge() {
        return age;
    }

    public int getEnergy() {
        return energy;
    }

    public void deathEnergy(){
        energy = -100;
    }

    public int getAgeOfDeath() {
        return ageOfDeath;
    }

    public void setAgeOfDeath(int ageOfDeath) {
        this.ageOfDeath = ageOfDeath;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public List<Animal> getCousins() {
        return cousins;
    }

    @Override
    public String toString() {
        return switch(this.orientation) {
            case NORTH -> "N" + energy;
            case NORTHEAST -> "NE" + energy;
            case EAST -> "E" + energy;
            case NORTHWEST -> "NW" + energy;
            case WEST -> "W" + energy;
            case SOUTH -> "S" + energy;
            case SOUTHWEST -> "SW" + energy;
            case SOUTHEAST -> "SE" + energy;
        };
    }

    public void addEnergy(int energy){
        this.energy = this.energy + energy;
    }

    public void reduceEnergy() {
        this.energy = (int) (0.75 * this.energy);
    }

    public void child(){
        this.numberOfChildren = this.numberOfChildren + 1;
        this.numberOfCousins = this.numberOfCousins + 1;
    }

    public void cousin(){
        this.numberOfCousins = this.numberOfCousins + 1;
    }

    public void move(){
        Random generator = new Random();
        int animalMove = genotype.getGens()[generator.nextInt(32)];

        if(animalMove == 0){
            this.position = loopMove(inBordersMove(position.add(orientation.toUnitVector())));
        }
        else if(animalMove == 4){
            this.position = loopMove(inBordersMove(position.add((orientation.toUnitVector()).opposite())));
        }
        else{
            this.orientation = switch(animalMove){
                case 1 -> this.orientation.next1();
                case 2 -> this.orientation.next2();
                case 3 -> this.orientation.next3();
                case 5 -> this.orientation.next5();
                case 6 -> this.orientation.next6();
                case 7 -> this.orientation.next7();
                default -> this.orientation;
            };
        }
        this.energy = this.energy - cost;
        this.age = this.age + 1;
    }

    private Vector2d loopMove(Vector2d vector){
        Vector2d finalPosition;

        if(vector.x() < 0 || vector.x() >= map.getWidth() || vector.y() < 0 || vector.y() >= map.getHeight()){
            finalPosition = new Vector2d((vector.x() + map.getWidth()) % map.getWidth(), (vector.y() + map.getHeight()) % map.getHeight());
        }else {
            finalPosition = vector;
        }

        positionChanged(position, finalPosition);
        return finalPosition;
    }

    private Vector2d inBordersMove(Vector2d vector){
        if(map.canMoveTo(vector)) {
            return vector;
        } else
            return position;
    }

    public void addObserver(AbstractWorldMap observer){
        observers.add(observer);
    }

    public void removeObserver(AbstractWorldMap observer){
        observers.add(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(AbstractWorldMap observer : observers){
            observer.change(oldPosition, newPosition, this);
        }
    }

    public void addCousin(Animal observer){
        cousins.add(observer);
    }

    public void informCousins(){
        for(Animal observer : cousins){
            observer.cousin();
        }
    }
}
