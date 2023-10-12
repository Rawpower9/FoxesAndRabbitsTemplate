package Animals;

import Animals.*;
import Field.*;
import Graph.*;

import java.io.Serializable;
import java.util.List;

public class Animal {
    protected int BREEDING_AGE;
    protected int MAX_AGE;
    protected double BREEDING_PROBABILITY;
    protected int MAX_LITTER_SIZE;
    protected int PREY_FOOD_VALUE;
    protected int MAX_HUNGER;

    protected int age;
    protected boolean alive;
    protected Location location;
    protected int foodLevel;

    public Animal() {
        age = 0;
        alive = true;
    }

    protected void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            alive = false;
        }
    }

    protected void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            alive = false;
        }
    }

    protected void setRandomAge(boolean startWithRandomAge){
        if (startWithRandomAge) {
			age = (int)(Math.random()*MAX_AGE) / 2;
			foodLevel = PREY_FOOD_VALUE;
		} else {
			foodLevel = PREY_FOOD_VALUE;
		}
    }

    /**
     * Generate a number representing the number of births, if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    protected int breed() {
        int numBirths = 0;
        if (canBreed() && Math.random() <= BREEDING_PROBABILITY) {
            numBirths = (int) (Math.random() * MAX_LITTER_SIZE) + 1;
        }
        return numBirths;
    }

    protected boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFoodLevel(int fl) {
        this.foodLevel = fl;
    }   

	public void setEaten() {
        alive = false;
    }

    public void act(Field currentField, Field updatedField, List<Animal> babyStorage) {
        // incrementAge();
        // if (alive) {
        //     int births = breed();
        //     for (int b = 0; b < births; b++) {
        //         Rabbit newRabbit = new Rabbit(false);
        //         babyStorage.add(newRabbit);
        //         Location loc = updatedField.randomAdjacentLocation(location);
        //         newRabbit.setLocation(loc);
        //         updatedField.put(newRabbit, loc);
        //     }
        //     Location newLocation = updatedField.freeAdjacentLocation(location);
        //     // Only transfer to the updated field if there was a free location
        //     if (newLocation != null) {
        //         setLocation(newLocation);
        //         updatedField.put(this, newLocation);
        //     } else {
        //         // can neither move nor stay - overcrowding - all locations taken
        //         alive = false;
        //     }
        // }
    }
}
