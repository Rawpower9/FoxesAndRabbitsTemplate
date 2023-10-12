package Animals;

import Field.*;
import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kolling.  Modified by David Dobervich 2007-2022
 */
public class Rabbit extends Animal {
    // ----------------------------------------------------
    // Characteristics shared by all rabbits (static fields).
    // ----------------------------------------------------
	// private static int BREEDING_AGE = 3; // 1
	
    // // The age to which all rabbits can live.
    // private static int MAX_AGE = 10; // 10
    
    // // The likelihood of a rabbit breeding.
    // private static double BREEDING_PROBABILITY = 0.2; // 0.1
    
    // // The maximum number of births.
    // private static int MAX_LITTER_SIZE = 5; // 5

    // -----------------------------------------------------
    // Individual characteristics (attributes).
    // -----------------------------------------------------
    // The rabbit's age.
    // private int age;
    
    // // Whether the rabbit is alive or not.
    // private boolean alive;
    
    // // The rabbit's position
    // private Location location;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param startWithRandomAge If true, the rabbit will have a random age.
     */
    public Rabbit(boolean startWithRandomAge)
    {
        super();
        MAX_AGE = 10; //10
        BREEDING_AGE = 3; //3
        BREEDING_PROBABILITY = 0.2; //0.2
        MAX_LITTER_SIZE = 5; //5
        age = 0;
        alive = true;
        super.setRandomAge(startWithRandomAge);
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param updatedField The field to transfer to.
     * @param babyRabbitStorage A list to add newly born rabbits to.
     */
	public void act(Field currentField, Field updatedField, List<Animal> babyStorage) {
        incrementAge();
        if(alive) {
            int births = breed();
            for(int b = 0; b < births; b++) {
                Animal newRabbit = new Rabbit(false);
                babyStorage.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(location);
                newRabbit.setLocation(loc);
                updatedField.put(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(location);
            // Only transfer to the updated field if there was a free location
            if(newLocation != null) {
                super.setLocation(newLocation);
                updatedField.put(this, newLocation);
            }
            else {
                // can neither move nor stay - overcrowding - all locations taken
                alive = false;
            }
        }
    }
}
