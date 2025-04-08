import java.util.ArrayList;
import java.util.List;

public class Fight {
    private List<Pet> petList;
    private List<Round> roundList = new ArrayList<>();
    private RandomGenerator random;
    private boolean isFinished;
    private Pet roundWinner;


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
        int rounds = 0;
        while (awake > 1) {
                Round round = new Round(petList, random);
                rounds += 1;
                System.out.println("\n_____________________");
                System.out.println("Playing round #" + rounds);
                for (Pet pet1 : petList) {
                    System.out.println(pet1.getPetName() + "\nHP: " + pet1.getCurrentHp());
                    for (Skills skill : Skills.values()) {
                        System.out.println(skill.toString() + " CD: " + pet1.getSkillRechargeTime(skill));
                    }
                }
                round.executeRound();
                for (Pet pet : petList) {
                    if (!pet.isAwake()) {
                        awake -= 1;
                    }
                }
        }

        for (Pet pet : petList) {
            if (pet.isAwake()) {
                roundWinner = pet;
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
