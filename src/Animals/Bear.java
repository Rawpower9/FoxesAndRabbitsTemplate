package Animals;

import Field.*;
import java.util.List;

/**
 * A simple model of a Bear. Beares age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kolling.  Modified by David Dobervich 2007-2022. Modified again by Ritvik Setty 2023
 */
public class Bear extends Animal{
	// ----------------------------------------------------
	// Characteristics shared by all Beares (static fields).
	// ----------------------------------------------------
	// private static int BREEDING_AGE = 10;
	// // The age to which a Bear can live.
	// private static int MAX_AGE = 100;
	// // The likelihood of a Bear breeding.
	// private static double BREEDING_PROBABILITY = 0.01;
	// // The maximum number of births.
	// private static int MAX_LITTER_SIZE = 2;
	// // The food value of a single rabbit. In effect, this is the
	// // number of steps a Bear can go before it has to eat again.
	// private static int PREY_FOOD_VALUE = 70;
	// A shared random number generator to control breeding.

	/**
	 * Create a Bear. A Bear can be created as a new born (age zero and not
	 * hungry) or with random age.
	 * 
	 * @param startWithRandomAge
	 *            If true, the Bear will have random age and hunger level.
	 */
	public Bear(boolean startWithRandomAge) {
		super();
		Bear.MAX_AGE = 100;
		Bear.PREY_FOOD_VALUE = 70;
		Bear.BREEDING_AGE = 10;
		Bear.BREEDING_PROBABILITY = 0.015;
		Bear.MAX_LITTER_SIZE = 1;
		Bear.MAX_HUNGER = 100;
		super.setRandomAge(startWithRandomAge);
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
				super.setLocation(newLocation);
				updatedField.put(this, newLocation);
			} else {
				// can neither move nor stay - overcrowding - all locations
				// taken
				alive = false;
			}
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
					foodLevel = PREY_FOOD_VALUE;
					return where;
				}
			}
		}
		return null;
	}
}
