import java.util.*;

public class Input {
    private Scanner scanner;
    private Pet currentPet;

    public Input()
    {
        scanner = new Scanner(System.in);
    }

    public boolean checkContinue()
    {
        System.out.println("Input a 1 to restart the program, or anything else to close the program.");
        String userInput = scanner.nextLine();
        if (userInput == "1")
            return true;
        else
            return false;
    }

    public void setCurrentPet(Pet currentPet) {
        this.currentPet = currentPet;
    }

    // used to set the random seed at the start of the program
    public long getRandomSeed()
    {
        System.out.println("Please input a number used to set a random seed");
        long userInput = scanner.nextLong();

        return userInput;
    }

    // gets a name for a pet from user input
    public String getPetName()
    {
        System.out.println("Please input a name for your pet.");
        String userInput = scanner.nextLine();
        userInput = Utils.convertEnumString(userInput);

        return userInput;
    }

    // gets a name for a player from user input
    public String getPlayerName()
    {
        System.out.println("Please input your player name.");
        String userInput = scanner.nextLine();
        userInput = Utils.convertEnumString(userInput);

        return userInput;
    }

    // gets the number of fights in the battle from user input
    public int getFightNumber()
    {
        System.out.println("Please input an integer of fights to happen in the battle");
        int userInput = scanner.nextInt();

        return userInput;
    }

    // gets the starting HP of a single pet from user input
    public double getStartingHP()
    {
        System.out.println("Please input the amount of HP your pet will have.");
        double userInput = scanner.nextDouble();

        while(userInput <= 0.0)
        {
            System.out.println("Please input a positive number.");
            userInput = scanner.nextDouble();
        }

        return userInput;
    }

    // gets the type of a pet based on user input
    public PetTypes getPetType()
    {
        System.out.println("Please input a number to determine the type of your pet. (1 = Power, 2 = Speed, 3 = Intelligence");
        int userInput = scanner.nextInt();

        while(userInput != 1 && userInput != 2 && userInput != 3)
        {
            System.out.println("Please enter a valid number");
            userInput = scanner.nextInt();
        }

        if(userInput == 1)
            return PetTypes.POWER;
        else if(userInput == 2)
            return PetTypes.SPEED;
        else
            return PetTypes.INTELLIGENCE;
    }

    // determines if a pet is controlled by a player or not based on user input
    public PlayerTypes getPlayerType()
    {
        System.out.println("Please input a number to determine your player type. (1 = Human, 2 = Computer)  Computer currently not supported.");
        int userInput = scanner.nextInt();

        while(userInput != 1 && userInput != 2)
        {
            System.out.println("Please enter a valid number");
            userInput = scanner.nextInt();
        }

        if(userInput == 1)
            return PlayerTypes.HUMAN;
        else
            return PlayerTypes.HUMAN;   // for now there are no computers, so this will always return human because computers are not implemented this iteration
    }

    // determines which skill a pet will use each round
    public Skills getSkillType()
    {
        System.out.println("Please input a number to determine the move of your pet. (1 = Rock Throw, 2 = Scissors Poke, 3 = Paper Cut");
        int userInput = scanner.nextInt();
        int rockCooldown = currentPet.getSkillRechargeTime(Skills.ROCK_THROW);
        int scissorCooldown = currentPet.getSkillRechargeTime(Skills.SCISSORS_POKE);
        int paperCooldown = currentPet.getSkillRechargeTime(Skills.PAPER_CUT);
        boolean validInput = false;
        if (userInput == 1 && rockCooldown == 0)
            validInput = true;
        if (userInput == 2 && scissorCooldown == 0)
            validInput = true;
        if (userInput == 3 && paperCooldown == 0)
            validInput = true;

        while(validInput == false)
        {
            if (userInput == 1 || userInput == 2 || userInput == 3)
                System.out.println("Skill currently on cooldown.");
            else
                System.out.println("Please enter a valid number.");
            userInput = scanner.nextInt();

            if (userInput == 1 && rockCooldown == 0)
                validInput = true;
            if (userInput == 2 && scissorCooldown == 0)
                validInput = true;
            if (userInput == 3 && paperCooldown == 0)
                validInput = true;
        }

        if(userInput == 1)
            return Skills.ROCK_THROW;
        else if(userInput == 2)
            return Skills.SCISSORS_POKE;
        else
            return Skills.PAPER_CUT;
    }




}
