package edu.team06.control;

import edu.team06.entity.Pet;
import edu.team06.util.RandomGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Battle {
    private List<Pet> petList = new ArrayList<>();
    private List<Fight> fightList = new ArrayList<>();
    private RandomGenerator random;
    private int fightNumber;
    private HashMap<Pet, Integer> fightWins;

    public HashMap<Pet, Integer> getFightWins() {
        return fightWins;
    }

    /**
     * Executes the battle with the pets provided.
     * @return The winner of the battle; edu.team06.entities.Pet
     */
    public Pet executeBattle() {
        for (int i = 0; i < fightNumber; i++) {
            Fight fight = new Fight(petList, random);
            int fightNum = fightList.size() + 1;
            System.out.println("\n\033[0;35mExecuting Fight " + fightNum + "/" + fightNumber + "\033[0m");
            System.out.println("Pets in fight:");
            for (Pet pet : petList) {
                System.out.println("Name: " + pet.getPetName() + ", Type: " + pet.getPetType());
            }
            fight.executeFight();
            fightList.add(fight);
            System.out.println("\n\033[1;35m" + fight.getWinner().getPetName() + " won fight " + fightNum + "!\033[0m");
            for (Pet pet : petList) {
                pet.reset();
            }

        }

        fightWins = new HashMap<Pet, Integer>();

        for (Pet pet : petList) {
            fightWins.put(pet, 0);
        }

        for (Fight fight : fightList) {
            fightWins.replace(fight.getWinner(), fightWins.get(fight.getWinner()) + 1);
        }

        Pet maxWins = null;
        for (Pet pet : fightWins.keySet()) {
            System.out.println("\033[0;36mPet " + pet.getPetName() + " wins: " + fightWins.get(pet) + "\033[0m");
            if (maxWins == null) {
                maxWins = pet;
            } else {
                if (fightWins.get(maxWins) < fightWins.get(pet)) {
                    maxWins = pet;
                }
            }
        }

        boolean tie = false;

        for (Pet pet : fightWins.keySet()) {
            if (pet != maxWins && Objects.equals(fightWins.get(pet), fightWins.get(maxWins))) {
                tie = true;
            }
        }

        if (tie) {
            System.out.println("It was a tie... :(\nMaybe try having an odd number of fights next time");
            return null;
        }

        System.out.println("\n\033[0;36mPet " + maxWins.getPetName() + " won the battle!" + "\033[0m\n_____________________");
        return maxWins;

//        int petOneWins = 0;
//        int petTwoWins = 0;
//        for (Fight fight : fightList) {
//            if (fight.getWinner() == petList.get(0)) {
//                petOneWins += 1;
//            } else if (fight.getWinner() == petList.get(1)) {
//                petTwoWins += 1;
//            }
//        }
//
//        System.out.println("\033[0;36mPet " + petList.get(0).getPetName() + " wins: " + petOneWins + "\033[0m");
//        System.out.println("\033[0;33mPet " + petList.get(1).getPetName() + " wins: " + petTwoWins + "\033[0m");
//        if (petOneWins > petTwoWins) {
//            System.out.println("\n\033[0;36mPet " + petList.get(0).getPetName() + " won the battle!" + "\033[0m\n_____________________");
//            return petList.get(0);
//        } else if (petTwoWins > petOneWins) {
//            System.out.println("\n\033[0;33mPet " + petList.get(1).getPetName() + " won the battle!" + "\033[0m\n_____________________");
//            return petList.get(1);
//        }
//        else {
//            System.out.println("It was a tie... :(\nMaybe try having an odd number of fights next time");
//            return null;
//        }
    }


    /**
     * Creates the battle object.
     * @param petList An array of two pets to be in the battle
     * @param randomInstance The instance of edu.team06.entities.RandomGenerator to be used
     * @param fightNumber The number of fights to play in the edu.team06.controller.Battle, ideally should be odd.
     */
    public Battle(List<Pet> petList, RandomGenerator randomInstance, int fightNumber) {
        this.random = randomInstance;
        this.fightNumber = fightNumber;
        for (Pet pet : petList) {
            this.petList.add(pet);
        }

    }
}
