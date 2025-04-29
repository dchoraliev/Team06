package edu.team06.control;

import edu.team06.entity.Damage;
import edu.team06.entity.Pet;
import edu.team06.entity.Skills;
import edu.team06.util.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class Fight {
    private List<Pet> petList;
    private RandomGenerator random;
    private Pet roundWinner;
    private List<List<Damage>> roundsDamage = new ArrayList<>();


    /**
     * Creates the fight object.
     * @param petList An array of two pets to be in the fight
     * @param randomInstance The instance of edu.team06.entities.RandomGenerator to be used
     */
    public Fight(List<Pet> petList, RandomGenerator randomInstance) {
        this.petList = petList;
        this.random = randomInstance;
    }


    /**
     * Executes the fight. Will run until only one of the pets is awake.
     */
    public void executeFight() {
        int awake = petList.size();
        Round lastRound = null;
        int rounds = 0;
        List<Pet> sleepingPets = new ArrayList<>();
        while (awake > 1) {
                List<Damage> allPreviousDamages = new ArrayList<>();
                for (List<Damage> rd : roundsDamage) {
                    allPreviousDamages.addAll(rd);
                }
                Round round = new Round(petList, random, allPreviousDamages);
                lastRound = round;
                rounds += 1;
                System.out.println("\n_____________________");
                System.out.println("\033[1;37mPlaying round #" + rounds + "\033[0m");

                for (Pet pet1 : petList) {
                    System.out.println("\033[32m" + pet1.getPetName() + "\nHP: " + pet1.getCurrentHp() + "\033[0m");
                    for (Skills skill : Skills.values()) {
                        System.out.println("\033[34m" + skill.toString() + " CD: " + pet1.getSkillRechargeTime(skill) + "\033[0m");
                    }
                }
                List<Damage> damageList = round.executeRound();
                roundsDamage.add(damageList);
                for (Pet pet : petList) {
                    if (!pet.isAwake() && !sleepingPets.contains(pet)) {
                        awake -= 1;
                        sleepingPets.add(pet);
                    }
                }
        }
        if (lastRound != null) {
            List<Pet> candidates = lastRound.getAwakeAtRoundStart();

            double maxHp = candidates.stream()
                    .mapToDouble(Pet::getCurrentHp)
                    .max()
                    .orElse(0);

            List<Pet> topPets = candidates.stream()
                    .filter(p -> p.getCurrentHp() == maxHp)
                    .toList();

            if (topPets.size() == 1) {
                roundWinner = topPets.get(0);
            } else {
                System.out.println("It's a tie between:");
                for (Pet p : topPets) {
                    System.out.println("- " + p.getPetName() + " (" + p.getCurrentHp() + " HP)");
                }

                int index = random.generateInt(topPets.size());
                roundWinner = topPets.get(index);
                System.out.println("Randomly selected winner: " + roundWinner.getPetName());
            }
        }

    }


    /**
     * @return The winner of the fight
     */
    public Pet getWinner() {
        return roundWinner;
    }
}
