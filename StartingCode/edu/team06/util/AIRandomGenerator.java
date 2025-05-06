package edu.team06.util;

import edu.team06.entity.Pet;
import edu.team06.entity.Skills;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Singleton pattern Random class to ensure that only one instance can exist
public enum AIRandomGenerator {
	INSTANCE();

	private Random random;

	AIRandomGenerator() {
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

	/**
	 * Chooses a random skill to be used from the available skills for that pet.
	 * @param pet The pet to choose the skill for.
	 * @return Skills
	 */
	public Skills chooseSkill(Pet pet) {
		List<Skills> availableSkills = new ArrayList<>();
		for (Skills skill : Skills.values()) {
			if (pet.getSkillRechargeTime(skill) == 0) {
				availableSkills.add(skill);
			}
		}
//		for (Skills skill: availableSkills) {
//			System.out.println(skill.toString());
//		}
		Skills skill = availableSkills.get(generateInt(availableSkills.size()));
		//System.out.println("\n" +skill + "\n");
		return skill;
	}


	/**
	 * Returns a random skill prediction
	 * @return Skills
	 */
	public Skills predictSkill() {
		List<Skills> skillsList = new ArrayList<>();
        skillsList.addAll(Arrays.asList(Skills.values()));
		return skillsList.get(generateInt(skillsList.size()));
	}
}
