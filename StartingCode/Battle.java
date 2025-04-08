import java.util.ArrayList;
import java.util.List;

public class Battle {
    private List<Pet> petList = new ArrayList<>();
    private List<Fight> fightList = new ArrayList<>();
    private RandomGenerator random;
    private int fightNumber;


    /**
     * Executes the battle with the pets provided.
     * @return The winner of the battle; Pet
     */
    public Pet executeBattle() {
        for (int i = 0; i < fightNumber; i++) {
            Fight fight = new Fight(petList, random);
            int fightNum = fightList.size() + 1;
            System.out.println("\nExecuting Fight " + fightNum + "/" + fightNumber);
            System.out.println("Pets in fight:");
            for (Pet pet : petList) {
                System.out.println("Name: " + pet.getPetName() + ", Type: " + pet.getPetType());
            }
            fight.executeFight();
            fightList.add(fight);
            System.out.println(fight.getWinner().getPetName() + " won fight " + fightNum + "!");
            for (Pet pet : petList) {
                pet.reset();
            }

        }

        int petOneWins = 0;
        int petTwoWins = 0;
        for (Fight fight : fightList) {
            if (fight.getWinner() == petList.get(0)) {
                petOneWins += 1;
            } else if (fight.getWinner() == petList.get(1)) {
                petTwoWins += 1;
            }
        }

        System.out.println("Pet " + petList.get(0).getPetName() + " wins: " + petOneWins);
        System.out.println("Pet " + petList.get(1).getPetName() + " wins: " + petTwoWins);
        if (petOneWins > petTwoWins) {
            System.out.println("\n_____________________" + "Pet " + petList.get(0).getPetName() + " won the battle!" + "\n_____________________");
            return petList.get(0);
        } else if (petTwoWins > petOneWins) {
            System.out.println("\n_____________________" + "Pet " + petList.get(1).getPetName() + " won the battle!" + "\n_____________________");
            return petList.get(1);
        }
        else {
            System.out.println("It was a tie... :(\nMaybe try having an odd number of fights next time");
            return null;
        }
    }


    /**
     * Creates the battle object.
     * @param petList An array of two pets to be in the battle
     * @param randomInstance The instance of RandomGenerator to be used
     * @param fightNumber The number of fights to play in the Battle, ideally should be odd.
     */
    public Battle(List<Pet> petList, RandomGenerator randomInstance, int fightNumber) {
        this.random = randomInstance;
        this.fightNumber = fightNumber;
        for (Pet pet : petList) {
            this.petList.add(pet);
        }

    }
}
