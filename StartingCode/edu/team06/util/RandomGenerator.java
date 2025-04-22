package edu.team06.util;

import java.util.Random;

// Singleton pattern Random class to ensure that only one instance can exist
public enum RandomGenerator {
	INSTANCE();

	private Random random;

	RandomGenerator() {
		random = new Random();
	}

	/**
	 * Sets the seed of the random generator; Useful when starting the game with a set seed
	 * @param seed New seed
	 */
	public void setSeed(long seed) {
		random.setSeed(seed);
	}

	/**
	 * Generates a random double, any number is possible
	 * @return Random double
	 */
	public double generateDouble()
	{
		return random.nextInt();
	}

	/**
	 * Generates a random double with a max value
	 * @param max Highest number that can be generated
	 * @return Random double
	 */
	public double generateDouble(int max)
	{
		return random.nextDouble(max);
	}

	/**
	 * Generates a random int, any number is possible
	 * @return Random int
	 */
	public int generateInt()
	{
		return random.nextInt();
	}

	/**
	 * Generates a random int with a max value
	 * @param max Highest number that can be generated
	 * @return Random int
	 */
	public int generateInt(int max)
	{
		return random.nextInt(max);
	}
}
