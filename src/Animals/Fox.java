package Animals;

import Field.*;
import java.util.List;

/**
 * A simple model of a fox. Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kolling. Modified by David Dobervich
 *         2007-2022
 */
public class Fox extends Animal{
	// ----------------------------------------------------
	// Characteristics shared by all foxes (static fields).
	// ----------------------------------------------------
	// private static int BREEDING_AGE = 10; // 10
	// // The age to which a fox can live.
	// private static int MAX_AGE = 50; // 25
	// // The likelihood of a fox breeding.
	// private static double BREEDING_PROBABILITY = 0.1; //0.05
	// // The maximum number of births.
	// private static int MAX_LITTER_SIZE = 3; // 3
	// // The food value of a single rabbit. In effect, this is the
	// // number of steps a fox can go before it has to eat again.
	// private static int PREY_FOOD_VALUE = 5; //20
	// // A shared random number generator to control breeding.
	// // private static int MAX_HUNGER = 30;
	// // -----------------------------------------------------
	// // Individual characteristics (attributes).
	// // -----------------------------------------------------
	// // The fox's age.
	// private int age;
	// // Whether the fox is alive or not.
	// private boolean alive;
	// // The fox's position
	// private Location location;
	// // The fox's food level, which is increased by eating rabbits.
	// private int foodLevel;

	/**
	 * Create a fox. A fox can be created as a new born (age zero and not
	 * hungry) or with random age.
	 * 
	 * @param startWithRandomAge
	 *                           If true, the fox will have random age and hunger
	 *                           level.
	 */
	public Fox(boolean startWithRandomAge) {
		super();
		MAX_AGE = 50; // 50
		PREY_FOOD_VALUE = 5;
		BREEDING_AGE = 5;
		BREEDING_PROBABILITY = 0.15;
		MAX_LITTER_SIZE = 3;
		MAX_HUNGER = 50;
		super.setRandomAge(startWithRandomAge);
	}

	/**
	 * This is what the fox does most of the time: it hunts for rabbits. In the
	 * process, it might breed, die of hunger, or die of old age.
	 * 
	 * @param currentField
	 *                       The field currently occupied.
	 * @param updatedField
	 *                       The field to transfer to.
	 * @param babyFoxStorage
	 *                       A list to add newly born foxes to.
	 */
	public void act(Field currentField, Field updatedField, List<Animal> babyStorage) {
		incrementAge();
		incrementHunger();
		if (alive) {
			int births = breed();
			for (int b = 0; b < births; b++) {
				Animal newFox = new Fox(false);
				newFox.setFoodLevel(this.foodLevel);
				babyStorage.add(newFox);
				Location loc = updatedField.randomAdjacentLocation(location);
				newFox.setLocation(loc);
				updatedField.put(newFox, loc);
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
	 * Tell the fox to look for rabbits adjacent to its current location. Only
	 * the first live rabbit is eaten.
	 * 
	 * @param field
	 *                 The field in which it must look.
	 * @param location
	 *                 Where in the field it is located.
	 * @return Where food was found, or null if it wasn't.
	 */
	private Location findFood(Field field, Location location) {
		List<Location> adjacentLocations = field.adjacentLocations(location);

		for (Location where : adjacentLocations) {
			Object animal = field.getObjectAt(where);
			if (animal instanceof Rabbit) {
				Rabbit rabbit = (Rabbit) animal;
				if (rabbit.isAlive()) {
					rabbit.setEaten();
					foodLevel = PREY_FOOD_VALUE;
					return where;
				}
			}
		}
		return null;
	}
}
