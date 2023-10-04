package Animals;

import Animals.*;
import Field.*;
import Graph.*;

import java.io.Serializable;
import java.util.List;

public class Animal {
    protected static int BREEDING_AGE;
    protected static int MAX_AGE;
    protected static double BREEDING_PROBABILITY;
    protected static int MAX_LITTER_SIZE;
    protected static int PREY_FOOD_VALUE;
    protected static int MAX_HUNGER;

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
}
