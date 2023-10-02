package Animals;

import Animals.*;
import Field.*;
import Graph.*;

import java.io.Serializable;
import java.util.List;

/**
 * A simple model of a Bear. Beares age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kolling.  Modified by David Dobervich 2007-2022. Modified again by Ritvik Setty 2023
 */
public class Bear {
	// ----------------------------------------------------
	// Characteristics shared by all Beares (static fields).
	// ----------------------------------------------------
	private static int BREEDING_AGE = 10;
	// The age to which a Bear can live.
	private static int MAX_AGE = 100;
	// The likelihood of a Bear breeding.
	private static double BREEDING_PROBABILITY = 0.01;
	// The maximum number of births.
	private static int MAX_LITTER_SIZE = 2;
	// The food value of a single rabbit. In effect, this is the
	// number of steps a Bear can go before it has to eat again.
	private static int FOX_FOOD_VALUE = 70;
	// A shared random number generator to control breeding.

	// -----------------------------------------------------
	// Individual characteristics (attributes).
	// -----------------------------------------------------
	// The Bear's age.
	private int age;
	// Whether the Bear is alive or not.
	private boolean alive;
	// The Bear's position
	private Location location;
	// The Bear's food level, which is increased by eating rabbits.
	private int foodLevel;

	/**
	 * Create a Bear. A Bear can be created as a new born (age zero and not
	 * hungry) or with random age.
	 * 
	 * @param startWithRandomAge
	 *            If true, the Bear will have random age and hunger level.
	 */
	public Bear(boolean startWithRandomAge) {
		age = 0;
		alive = true;
		if (startWithRandomAge) {
			age = (int)(Math.random()*MAX_AGE)/2;
			foodLevel = FOX_FOOD_VALUE;
		} else {
			// leave age at 0
			foodLevel = FOX_FOOD_VALUE;
		}
	}

	/**
	 * This is what the Bear does most of the time: it hunts for rabbits. In the
	 * process, it might breed, die of hunger, or die of old age.
	 * 
	 * @param currentField
	 *            The field currently occupied.
	 * @param updatedField
	 *            The field to transfer to.
	 * @param babyBearStorage
	 *            A list to add newly born Beares to.
	 */
	public void hunt(Field currentField, Field updatedField, List<Bear> babyBearStorage) {
		incrementAge();
		incrementHunger();
		if (alive) {
			// New Beares are born into adjacent locations.
			int births = breed();
			for (int b = 0; b < births; b++) {
				Bear newBear = new Bear(false);
				newBear.setFoodLevel(this.foodLevel);
				babyBearStorage.add(newBear);
				Location loc = updatedField.randomAdjacentLocation(location);
				newBear.setLocation(loc);
				updatedField.put(newBear, loc);
			}
			// Move towards the source of food if found.
			Location newLocation = findFood(currentField, location);
			if (newLocation == null) { // no food found - move randomly
				newLocation = updatedField.freeAdjacentLocation(location);
			}
			if (newLocation != null) {
				setLocation(newLocation);
				updatedField.put(this, newLocation);
			} else {
				// can neither move nor stay - overcrowding - all locations
				// taken
				alive = false;
			}
		}
	}

	/**
	 * Increase the age. This could result in the Bear's death.
	 */
	private void incrementAge() {
		age++;
		if (age > MAX_AGE) {
			alive = false;
		}
	}

	/**
	 * Make this Bear more hungry. This could result in the Bear's death.
	 */
	private void incrementHunger() {
		foodLevel--;
		if (foodLevel <= 0) {
			alive = false;
		}
	}

	/**
	 * Tell the Bear to look for rabbits adjacent to its current location. Only
	 * the first live rabbit is eaten.
	 * 
	 * @param field
	 *            The field in which it must look.
	 * @param location
	 *            Where in the field it is located.
	 * @return Where food was found, or null if it wasn't.
	 */
	private Location findFood(Field field, Location location) {
		List<Location> adjacentLocations = field.adjacentLocations(location);

		for (Location where : adjacentLocations) {
			Object animal = field.getObjectAt(where);
			if (animal instanceof Fox) {
				Fox fox = (Fox) animal;
				if (fox.isAlive()) {
					fox.setEaten();
					foodLevel = FOX_FOOD_VALUE;
					return where;
				}
			}
		}
		return null;
	}

	/**
	 * Generate a number representing the number of births, if it can breed.
	 * 
	 * @return The number of births (may be zero).
	 */
	private int breed() {
		int numBirths = 0;
		if (canBreed() && Math.random() <= BREEDING_PROBABILITY) {
			numBirths = (int)(Math.random()*MAX_LITTER_SIZE) + 1;
		}
		return numBirths;
	}

	/**
	 * A Bear can breed if it has reached the breeding age.
	 */
	private boolean canBreed() {
		return age >= BREEDING_AGE;
	}

	/**
	 * Check whether the Bear is alive or not.
	 * 
	 * @return True if the Bear is still alive.
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Set the animal's location.
	 * 
	 * @param row
	 *            The vertical coordinate of the location.
	 * @param col
	 *            The horizontal coordinate of the location.
	 */
	public void setLocation(int row, int col) {
		this.location = new Location(row, col);
	}

	/**
	 * Set the Bear's location.
	 * 
	 * @param location
	 *            The Bear's location.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	public void setFoodLevel(int fl) {
		this.foodLevel = fl;
	}
}
