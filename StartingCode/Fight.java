import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fight {
    private List<Pet> petList;
    private List<Round> roundList = new ArrayList<>();
    private Random random;
    private boolean isFinished;
    private Pet roundWinner;


    /**
     * Creates the fight object.
     * @param petList An array of two pets to be in the fight
     * @param randomInstance The instance of Random to be used
     */
    public Fight(List<Pet> petList, Random randomInstance) {
        this.petList = petList;
        this.random = randomInstance;
    }


    /**
     * Executes the fight. Will run until only one of the pets is awake.
     */
    public void executeFight() {
        int awake = petList.size();
        int rounds = 0;
        while (awake != 1) {
            for (Pet pet : petList) {
                Round round = new Round(petList, random);
                rounds += 1;
                System.out.println("Playing round #" + rounds);
                round.executeRound();
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
