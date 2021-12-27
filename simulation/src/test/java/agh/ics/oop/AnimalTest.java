package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalTest {

    @Test
    void addEnergy() {
        //given
        Animal animal = new Animal(new AbstractWorldMap(5,5,100,0,10,10,10,1,false), new Vector2d(0,0));
        int energy = 10;

        //when
        animal.addEnergy(energy);

        //then
        assertEquals(animal.getEnergy(), 20);
    }

    @Test
    void reduceEnergy() {
        //given
        Animal animal = new Animal(new AbstractWorldMap(5,5,100,0,10,10,10,1,false), new Vector2d(0,0));

        //when
        int energyBeforeReduce = animal.getEnergy();
        animal.reduceEnergy();

        //then
        assertEquals(animal.getEnergy(), (int)(0.75 * energyBeforeReduce));
    }

    @Test
    void child() {
        //given
        Animal animal = new Animal(new AbstractWorldMap(5,5,100,0,10,10,10,1,false), new Vector2d(0,0));

        //when
        animal.child();

        //then
        assertEquals(animal.getNumberOfChildren(), 1);
        assertEquals(animal.getNumberOfCousins(), 1);
    }

    @Test
    void cousin() {
        //given
        Animal animal = new Animal(new AbstractWorldMap(5,5,100,0,10,10,10,1,false), new Vector2d(0,0));

        //when
        animal.cousin();

        //then
        assertEquals(animal.getNumberOfCousins(), 1);
    }

    @Test
    void move() {
        //given
        Animal animal = new Animal(new AbstractWorldMap(5,5,100,0,10,10,10,1,false), new Vector2d(0,0));

        //when
        MapDirection orientation = animal.getOrientation();
        Vector2d position = animal.getPosition();
        animal.move();

        //then
        assertTrue(animal.getPosition() != position ^ animal.getOrientation() != orientation);
    }

    @Test
    void addCousin() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(5,5,100,0,10,10,10,1,false);
        Animal firstAnimal = new Animal(map, new Vector2d(0,0));
        Animal secondAnimal = new Animal(map, new Vector2d(0,1));

        //when
        firstAnimal.addCousin(secondAnimal);

        //then
        assertEquals(firstAnimal.getCousins().size(), 1);
    }

    @Test
    void informCousins() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(5,5,100,0,10,10,10,1,false);
        Animal firstAnimal = new Animal(map, new Vector2d(0,0));
        Animal secondAnimal = new Animal(map, new Vector2d(0,1));

        //when
        secondAnimal.child();
        firstAnimal.addCousin(secondAnimal);
        firstAnimal.informCousins();

        //then
        assertEquals(secondAnimal.getNumberOfCousins(), 2);
        assertEquals(secondAnimal.getNumberOfChildren(), 1);
    }
}