import java.util.*;

/**
 * This class is the game's primary controller which creates pets, runs the game, and creates battles.
 */
public class PrimaryController {
//    private final int MAX_PETS = 2;
    private int numberOfPlayers;
    private List<Pet> petList;
    private RandomGenerator randomInstance;
    private List<Battle> battleList;
    public PrimaryController() {}

    /**
     * This method runs the whole game.
     */
    public void run()
    {
        petList = new ArrayList<>();
        battleList = new ArrayList<>();
        randomInstance = RandomGenerator.INSTANCE;

        boolean play = true;
        while(play == true) {
            getPlayerCount();
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
            Input boundary = new Input();
            play = boundary.checkContinue();
        }
    }

    public void getPlayerCount()
    {
        Input boundary = new Input();
        numberOfPlayers = boundary.getNumberPlayers();
    }

    /**
     * Creates a Pet from the Pet class with all its attributes.
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



}
