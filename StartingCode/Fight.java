import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fight {
    private List<Pet> petList;
    private RandomGenerator random;
    private Pet roundWinner;
    private List<List<Damage>> roundsDamage = new ArrayList<>();


    /**
     * Creates the fight object.
     * @param petList An array of two pets to be in the fight
     * @param randomInstance The instance of RandomGenerator to be used
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
                Round round = new Round(petList, random);
                lastRound = round;
                rounds += 1;
                System.out.println("\n_____________________");
                System.out.println("Playing round #" + rounds);
                for (Pet pet1 : petList) {
                    System.out.println(pet1.getPetName() + "\nHP: " + pet1.getCurrentHp());
                    for (Skills skill : Skills.values()) {
                        System.out.println(skill.toString() + " CD: " + pet1.getSkillRechargeTime(skill));
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

                int index = random.generateNum(topPets.size());
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
