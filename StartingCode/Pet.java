    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Scanner;

    /**
        This class is the base class for Pet that implements the Playable interface.
        It has implementation for most of the classes that each Pet requires
        except some that need to be implemented by other classes.
        @author Dimitar Choraliev
     **/
    public class Pet implements Playable {
        private int playableUid;
        private int playableId;
        private String playerName;
        private PlayerTypes playerType;
        private String petName;
        private PetTypes petType;
        private double currentHp;
        private List<Skills> skills;
        private double startingHp;
        private Skills chosenSkill;
        private Map<Skills, Integer> skillRechargeTimes;


        /**
         * The constructor for the Pet class.
         * @param playerName - The player's name.
         * @param petName - the name of the pet. The pet's name cannot be null
         * @param playerType - The type of player - human or computer
         * @param petType - The type of pet
         * @param startingHp - The starting HP - cannot be below 0
         */
        public Pet(String playerName, String petName, PlayerTypes playerType, PetTypes petType, double startingHp) {
            if (petName == null || petName.trim().isEmpty()) {
                throw new IllegalArgumentException("Pet name cannot be empty");
            }
            if(startingHp < 0)
            {
                throw new IllegalArgumentException("Pet starting HP cannot be less than 0.0");
            }
            this.playerName = playerName;
            this.petName = petName;
            this.playerType = playerType;
            this.petType = petType;
            this.startingHp = startingHp;
            this.currentHp = startingHp;
            this.skillRechargeTimes = new HashMap<>();
        }
        //name, type, starting hp, current hp, skill

        /**
         * @return The unique ID for the Playable made.
         */

        @Override
        public int getPlayableUid() {
            return playableUid;
        }

        /**
         * @param playableUid The unique id to assign to the current playable object
         */
        @Override
        public void setPlayableUid(int playableUid) {
            this.playableUid = playableUid;
        }

        /**
         *
         * @return The playableID of the Playable.
         */
        @Override
        public int getPlayableId() {
            return playableId;
        }

        /**
         *
         * @return The skill chosen for the Pet.
         */
        public Skills getSkill() {
            return chosenSkill;
        }

        /**
         *
         * @return The Player's Name.
         */
        @Override
        public String getPlayerName() {
            return playerName;
        }

        /**
         *
         * @return The Pet's Name.
         */
        @Override
        public String getPetName() {
            return petName;
        }

        /**
         *
         * @return The Player's Type.
         */
        @Override
        public PlayerTypes getPlayerType() {
            return playerType;
        }

        /**
         *
         * @return The Pet's Type.
         */
        @Override
        public PetTypes getPetType() {
            return petType;
        }

        /**
         *
         * @return The current HP of the Pet.
         */
        @Override
        public double getCurrentHp() {
            return currentHp;
        }

        /**
         *
         * @return
         */
        @Override
        public Skills chooseSkill() {
            Input input = new Input();
            input.setCurrentPet(this);
            chosenSkill = input.getSkillType();
            return chosenSkill;
        }

        /**
         *
         * @param hp The hp which will be subtracted from the current hp
         */
        @Override
        public void updateHp(double hp) {
            currentHp = currentHp - hp;
            //Game-controlling classes
        }

        /**
         * Resetting the HP to the starting HP of the Pet.
         */
        @Override
        public void resetHp() {
            currentHp = startingHp;
        }

        /**
         *
         * @return Check whether the pet is awake. Return true if so, false otherwise.
         */
        @Override
        public boolean isAwake() {
            return currentHp > 0;
        }

        /**
         * Later implementation.
         * @return The predicted skill.
         */
        @Override
        public Skills getSkillPrediction() {
            /*Scanner scanner = new Scanner(System.in);

            System.out.println("\nPredict which skill your opponent will use: ");
            System.out.println("1. ROCK_THROW");
            System.out.println("2. SCISSORS_POKE");
            System.out.println("3. PAPER_CUT");
            System.out.println("4. SHOOT_THE_MOON");
            System.out.println("5. REVERSAL_OF_FORTUNE");

            while (true) {
                System.out.print("Enter the number of the predicted skill: ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= 5) {
                        return Skills.values()[choice - 1];
                    } else {
                        System.out.println("Invalid number. Please enter a value between 1 and 5.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a numeric value.");
                }
            }*/
            Input input = new Input();
            input.setCurrentPet(this);
            return input.predictSkill();     //implemented in Input class instead
        } // todo this iteration, just need to prompt the user for skill prediction and return it

        /**
         *
         * @param skill The current skill for which the recharge time will be given.
         * @return 1 if skill is on recharge, 0 otherwise
         */
        @Override
        public int getSkillRechargeTime(Skills skill) {
            return skillRechargeTimes.getOrDefault(skill, 0);
        }

        /**
         * This method returns the HP of the Pet as a percentage.
         * @return The percentage of the Pet's HP.
         */
        @Override
        public double calculateHpPercent() {
            return (currentHp / startingHp) * 100;
        }

        /**
         *
         * @return The starting HP of the Pet.
         */
        @Override
        public double getStartingHp() {
            return startingHp;
        }

        /**
         * Resets Pet's HP to starting HP, and all Skills to what they were at the
         * start of the fight.
         * Implemented by game-controlling classes.
         */
        @Override
        public void reset() {
            currentHp = startingHp;
            for (Skills skill : Skills.values()) {
                setRechargeTime(skill, 0);
            }
        }

        /**
         * Reduces the skill's recharge time by 1.
         */
        @Override
        public void decrementRechargeTimes() {
            skillRechargeTimes.replaceAll((skill, time) -> Math.max(0, time - 1));
        }

        /**
         * Sets the Skill's recharge time.
         * @param skill - The skill that needs its recharge time set.
         * @param rechargeTime - The recharge time that will be set to the skill.
         */
        @Override
        public void setRechargeTime(Skills skill, int rechargeTime) {
            if (skill == Skills.SHOOT_THE_MOON || skill == Skills.REVERSAL_OF_FORTUNE) {
                skillRechargeTimes.put(skill, rechargeTime);
            } else {
                skillRechargeTimes.put(skill, rechargeTime);
            }
        }
    }
