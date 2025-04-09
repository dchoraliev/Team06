import java.util.*;

/**
 * This class receives all user input and handles exceptions.
 */
public class Input {
    private Scanner scanner;
    private Pet currentPet;

    public Input()
    {
        scanner = new Scanner(System.in);
    }

    /**
     *  Returns true if user wants to start another battle, false otherwise.
     */
    public boolean checkContinue()
    {
        int userInput = 0;
        boolean validInput = false;
        while(validInput == false) {
            try {
                System.out.println("Input a 1 to restart the program, or another number to exit.");
                userInput = scanner.nextInt();
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }
        if (userInput == 1)
            return true;
        else
            return false;
    }

    public void setCurrentPet(Pet currentPet) {
        this.currentPet = currentPet;
    }

    /**
     *  Used to set the random seed at the start of the program.
     */
    public long getRandomSeed()
    {
        long userInput = 0;
        boolean validInput = false;
        while(validInput == false) {
            try {
                System.out.println("Please input a number used to set a random seed");
                userInput = scanner.nextLong();
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }

        return userInput;
    }

    /**
     *  Gets a name for a pet from user input.
     */
    public String getPetName()
    {
        System.out.println("Please input a name for your pet.");
        String userInput = scanner.nextLine();
        userInput = Utils.convertEnumString(userInput);

        return userInput;
    }

    /**
     *  Gets a name for a player from user input.
     */
    public String getPlayerName()
    {
        System.out.println("Please input your player name.");
        String userInput = scanner.nextLine();
        userInput = Utils.convertEnumString(userInput);

        return userInput;
    }

    /**
     *  Gets the number of fights in the battle from user input.
     */
    public int getFightNumber()
    {
        int userInput = 0;
        boolean validInput = false;
        while(validInput == false) {
            try {
                System.out.println("Please input an number of fights to happen in the battle");
                userInput = scanner.nextInt();

                while (userInput <= 0) {
                    System.out.println("Please input a positive number.");
                    userInput = scanner.nextInt();
                }

                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }

        return userInput;
    }

    /**
     *  Gets the starting HP of a single pet from user input.
     */
    public double getStartingHP()
    {
        double userInput = 0;
        boolean validInput = false;
        while(validInput == false) {
            try {
                System.out.println("Please input a decimal number for the amount of HP your pet will have.");
                userInput = scanner.nextDouble();

                while (userInput <= 0.0) {
                    System.out.println("Please input a positive number.");
                    userInput = scanner.nextDouble();
                }

                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }

        return userInput;
    }

    /**
     *  Gets the type of a pet based on user input.
     */
    public PetTypes getPetType()
    {
        int userInput = 0;
        boolean validInput = false;
        while(validInput == false) {
            try {
                System.out.println("Please input a number to determine the type of your pet. (1 = Power, 2 = Speed, 3 = Intelligence)");
                userInput = scanner.nextInt();

                while (userInput != 1 && userInput != 2 && userInput != 3) {
                    System.out.println("Please enter a valid number");
                    userInput = scanner.nextInt();
                }

                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }
        if (userInput == 1)
            return PetTypes.POWER;
        if (userInput == 2)
            return PetTypes.SPEED;
        if (userInput == 3)
            return PetTypes.INTELLIGENCE;
        return null;
    }

    /**
     *  Determines if a pet is controlled by a player or not based on user input.
     *  For now, all pets are controlled by user input, so this will always return HUMAN type.
     */
    public PlayerTypes getPlayerType()
    {
        int userInput = 0;
        boolean validInput = false;
        while(validInput == false) {
            try {
                System.out.println("Please input a number to determine your player type. (1 = Human, 2 = Computer)  Computer currently not supported.");
                userInput = scanner.nextInt();

                while (userInput != 1 && userInput != 2) {
                    System.out.println("Please enter a valid number");
                    userInput = scanner.nextInt();
                }

                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }
        if (userInput == 1)
            return PlayerTypes.HUMAN;
        else
            return PlayerTypes.HUMAN;
    }

    /**
     *  Determines which skill a pet will use each round based on user input.
     *  Checks for skill recharge times first to make sure the input is valid.
     */
    public Skills getSkillType()
    {
        int userInput = 0;
        boolean validType = false;
        while(validType == false) {
            try {
                System.out.println("Please input a number to determine the move of " + currentPet.getPetName() + ". (1 = Rock Throw, 2 = Scissors Poke, 3 = Paper Cut");
                userInput = scanner.nextInt();
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

                while (validInput == false) {
                    if (userInput == 1 || userInput == 2 || userInput == 3)
                        System.out.println("That skill is currently on cooldown, please choose a different one.");
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

                validType = true;
            } catch (Exception e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }
        if(userInput == 1)
            return Skills.ROCK_THROW;
        if(userInput == 2)
            return Skills.SCISSORS_POKE;
        if(userInput == 3)
            return Skills.PAPER_CUT;
        return null;
    }




}
