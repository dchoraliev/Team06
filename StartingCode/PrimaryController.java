import java.util.*;

public class PrimaryController {
    private final int MAX_PETS = 2;
    private List<Pet> petList;
    private RandomGenerator randomInstance;
    private List<Battle> battleList;
    public PrimaryController() {}

    // runs the program
    public void run()
    {
        for(int i = 0; i < MAX_PETS; i++)
        {
            createPet();
        }
        createBattle();

        Battle currentBattle = battleList.getFirst();
        currentBattle.executeBattle();
    }

    // calls the input class to get the relevant inputs to make a pet class, and then adds it to petList
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

    // calls the input class to get the relevant inputs to make a battle class, and then adds it to battleList
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
