package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractWorldMapTest {

    @Test
    void isJungle() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(3,3,13,0,10,10,10,1,false);

        //when
        boolean check = map.isJungle(new Vector2d(1,1));

        //then
        assertTrue(check);
    }

    @Test
    void place() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(4,4,67,3,10,10,10,1,false);

        //when
        map.place();

        //then
        assertEquals(map.getNumberOfAnimals(), 3);
    }

    @Test
    void canMoveTo() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(3,3,67,3,10,10,10,1,true);

        //when
        boolean firstCheck = map.canMoveTo(new Vector2d(3, 3));
        boolean secondCheck = map.canMoveTo(new Vector2d(2, 2));

        //then
        assertTrue(secondCheck);
        assertFalse(firstCheck);
    }

    @Test
    void isGrass() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(3,3,13,0,10,10,10,1,false);

        //when
        map.grow();
        boolean check = map.isGrass(new Vector2d(1,1));

        //then
        assertTrue(check);
    }

    @Test
    void grow() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(3,3,13,0,10,10,10,1,false);

        //when
        map.grow();
        int memory = map.getNumberOfGrass();
        map.grow();

        //then
        assertEquals(map.getNumberOfGrass(), 3);
        assertEquals(memory, 2);
    }

    @Test
    void eat() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(3,3,13,1,10,10,10,1,false);
        Vector2d vector = new Vector2d(1, 1);

        //when
        map.grow();
        boolean firstCheck = map.isGrass(vector);
        map.place();
        map.eat(vector);
        boolean secondCheck = map.isGrass(vector);

        //then
        assertFalse(secondCheck);
        assertTrue(firstCheck);
    }

    @Test
    void reproduce() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(3,3,13,2,10,10,10,1,false);
        Vector2d vector = new Vector2d(1, 1);

        //when
        map.place();
        int firstCheck = map.getNumberOfAnimalsOnTile(vector);
        map.reproduce(vector);
        int secondCheck = map.getNumberOfAnimalsOnTile(vector);

        //then
        assertEquals(secondCheck, 3);
        assertEquals(firstCheck, 2);
    }

    @Test
    void getStrongest() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(1,1,200,2,10,10,10,1,false);
        Vector2d vector = new Vector2d(0, 0);

        //when
        map.place();
        Animal animal = map.getStrongest(vector).get(0);
        animal.move();
        Animal otherAnimal = map.getStrongest(vector).get(0);

        //then
        assertNotEquals(animal, otherAnimal);
    }

    @Test
    void death() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(1,1,200,1,10,10,10,1,false);
        Vector2d vector = new Vector2d(0, 0);

        //when
        map.place();
        Animal animal = map.getStrongest(vector).get(0);
        map.death(animal, vector);

        //then
        assertEquals(map.getNumberOfAnimals(), 0);
    }

    @Test
    void objectAt() {
        //given
        AbstractWorldMap map = new AbstractWorldMap(2,1,200,1,10,10,10,1,false);
        Vector2d vector = new Vector2d(0, 0);

        //when
        map.grow();
        map.place();
        map.eat(vector);

        //then
        assertTrue(map.objectAt(vector) instanceof Animal);
        assertTrue(map.objectAt(new Vector2d(1, 0)) instanceof Grass);
    }
}