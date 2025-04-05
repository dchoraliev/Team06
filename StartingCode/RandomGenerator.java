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
	 * Generates a number, any number is possible
	 * @return Random int
	 */
	public int generateNum()
	{
		return random.nextInt();
	}

	/**
	 * Generates a number with a max value
	 * @param max Highest number that can be generated
	 * @return Random int
	 */
	public int generateNum(int max)
	{
		return random.nextInt(max);
	}
}
