package edu.team06.control;

import edu.team06.boundary.Input;
import edu.team06.entity.Pet;
import edu.team06.entity.PetTypes;
import edu.team06.entity.PlayerTypes;
import edu.team06.util.RandomGenerator;

import java.util.*;

/**
 * This class is the game's primary controller which creates pets, runs the game, and creates battles.
 */
public class PrimaryController {
	private List<Pet> petList;
    private RandomGenerator randomInstance;
    private List<Battle> battleList;
    private List<Fight> fightList;
    //private List<Season> seasonList;
    public PrimaryController() {}

    /**
     * This method runs the whole game.
     */
    public void run()
    {
        petList = new ArrayList<>();
        battleList = new ArrayList<>();
        fightList = new ArrayList<>();
        //seasonList = new ArrayList<>();
        randomInstance = RandomGenerator.INSTANCE;

        boolean play = true;
        while(play) {
            Input boundary = new Input();
            int modeType = boundary.getMode();
            if (modeType == 1) //battle
            {
                int numberOfPlayers = boundary.getNumberPlayers();
                for (int i = 0; i < numberOfPlayers; i++) {
                    createPet();
                }
                createBattle();

                Battle currentBattle = battleList.getFirst();
                currentBattle.executeBattle();

                for (int i = 0; i < numberOfPlayers; i++) {
                    petList.removeFirst();
                }
                battleList.removeFirst();
            }
            /*
            else if (modeType == 2) // season
            {
                int numberOfPlayers = boundary.getNumberPlayers();
                for (int i = 0; i < numberOfPlayers; i++) {
                    createPet();
                }
                createSeason();

                Season currentSeason = seasonList.getFirst();
                currentSeason.executeSeason();

                for (int i = 0; i < numberOfPlayers; i++) {
                    petList.removeFirst();
                }
                seasonList.removeFirst();
            }
            */
            else if (modeType == 3) //  exhibition fight
            {
                int numberOfPlayers = boundary.getNumberPlayers();
                for (int i = 0; i < numberOfPlayers; i++) {
                    createPet();
                }
                createFight();

                Fight currentFight = fightList.getFirst();
                currentFight.executeFight();

                for (int i = 0; i < numberOfPlayers; i++) {
                    petList.removeFirst();
                }
                fightList.removeFirst();
            }
            play = boundary.checkContinue();
        }
    }

    /**
     * Creates a edu.team06.entities.Pet from the edu.team06.entities.Pet class with all its attributes.
     */
    public void createPet()
    {
        Input boundary = new Input();
        String playerName = boundary.getPlayerName();
        String petName = boundary.getPetName();
        PlayerTypes playerType = boundary.getPlayerType();
        PetTypes petType = boundary.getPetType();
        double startingHP = boundary.getStartingHP();
        Pet newPet = new Pet(playerName, petName, playerType, petType, startingHP); // need to convert int to enum first
        petList.add(newPet);
    }

    /**
     * Creates a battle with all necessary inputs.
     */
    public void createBattle()
    {
        Input boundary = new Input();
        long seed = boundary.getRandomSeed();
        int fightCount = boundary.getFightNumber();
        randomInstance.setSeed(seed);
        Battle newBattle = new Battle(petList, randomInstance, fightCount);
        battleList.add(newBattle);
    }

    /**
     * Creates a fight with all necessary inputs.
     */
    public void createFight()
    {
        Input boundary = new Input();
        long seed = boundary.getRandomSeed();
        randomInstance.setSeed(seed);
        Fight newFight = new Fight(petList, randomInstance);
        fightList.add(newFight);
    }

    /**
     * Creates a season with all necessary inputs.
     */
    /*
    public void createSeason()
    {
        Input boundary = new Input();
        long seed = boundary.getRandomSeed();
        randomInstance.setSeed(seed);
        Season newSeason = new Season(petList, randomInstance);
        battleList.add(newSeason);
    }
    */

}
