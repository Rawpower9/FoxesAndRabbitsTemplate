package Animals;

import java.util.List;
import Field.*;
import java.util.List;

public class Human extends Animal{
    /**
	 * Create a fox. A fox can be created as a new born (age zero and not
	 * hungry) or with random age.
	 * 
	 * @param startWithRandomAge
	 *                           If true, the fox will have random age and hunger
	 *                           level.
	 */
	private int moralCounter;
	private double GETTINGOVERKILLINGBEARS;

	public Human(boolean startWithRandomAge) {
		super();
		MAX_AGE = 1000000; // 50
		PREY_FOOD_VALUE = 500;
		BREEDING_AGE = 500;
		BREEDING_PROBABILITY = 0.001;
		MAX_LITTER_SIZE = 1;
		MAX_HUNGER = 1000000;
		moralCounter = 0;
		GETTINGOVERKILLINGBEARS =  0.001;
		super.setRandomAge(startWithRandomAge);
	}

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

			if (Math.random() < GETTINGOVERKILLINGBEARS){
				moralCounter = Math.max(moralCounter-1, 0);
			}
		}
	}

	private Location findFood(Field field, Location location) {
		List<Location> adjacentLocations = field.adjacentLocations(location);

		for (Location where : adjacentLocations) {
			Object animal = field.getObjectAt(where);
			if (animal instanceof Bear) {
				Bear bear = (Bear) animal;
				if (bear.isAlive() && GETTINGOVERKILLINGBEARS <= 2) {
					bear.setEaten();
					moralCounter++;
					foodLevel = PREY_FOOD_VALUE;
					return where;
				}
			}
		}
		return null;
	}
}
