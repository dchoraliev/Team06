import java.util.ArrayList;
import java.util.List;

public class Round {
    private final List<Pet> petList = new ArrayList<>();
    private final RandomGenerator random;
    private final List<List<Damage>> roundHistory = new ArrayList<>();
    private final List<Damage> previousRoundDamages = new ArrayList<>();
    private List<Pet> awakeAtRoundStart = new ArrayList<>();



    /**
     * Creates the round object.
     * @param petList An array of pets to be in the fight
     * @param randomInstance The instance of RandomGenerator to be used
     */
    public Round(List<Pet> petList, RandomGenerator randomInstance) {
        this.petList.addAll(petList);
        this.random = randomInstance;
    }

    /**
     *
     * @return A history of the rounds.
     */
    public List<List<Damage>> getRoundHistory() {
        return roundHistory;
    }

    /**
     * @return A list of pets that are currently awake.
     */
    public List<Pet> getAwakeAtRoundStart() {
        return awakeAtRoundStart;
    }

    /**
     * Calculates the damage to be done to the receiver by the damager. The actual damage is applied elsewhere.
     * @param damager The pet doing the damage
     * @param receiver The pet being damaged
     * @return the damage object with info about the damager, receiver, base damage, and conditional damage
     */
    public Damage CalculateDamagePerPet(Pet damager, Pet receiver, List<Damage> previousRoundDamages) {
        double conditional = 0;
        double baseDamage = random.generateNum(5);
        if (damager.getPetType() == PetTypes.POWER) {
            if (damager.getSkill() == Skills.ROCK_THROW && receiver.getSkill() == Skills.SCISSORS_POKE) {
                conditional = baseDamage * 5;
            } else if (damager.getSkill() == Skills.SCISSORS_POKE && receiver.getSkill() == Skills.PAPER_CUT) {
                conditional = baseDamage * 5;
            } else if (damager.getSkill() == Skills.PAPER_CUT && receiver.getSkill() == Skills.ROCK_THROW) {
                conditional = baseDamage * 5;
            } else if (damager.getSkill() == Skills.SHOOT_THE_MOON && receiver.getSkill() == damager.getSkillPrediction()) {
                conditional += 20;
            }
        } else if (damager.getPetType() == PetTypes.SPEED) {
            if (damager.getSkill() == Skills.ROCK_THROW && receiver.calculateHpPercent() >= 75) {
                if (receiver.getSkill() == Skills.SCISSORS_POKE || receiver.getSkill() == Skills.PAPER_CUT) {
                    conditional = 12.5;
                }
            } else if (damager.getSkill() == Skills.SCISSORS_POKE && receiver.calculateHpPercent() >= 25 && receiver.calculateHpPercent() < 75) {
                if (receiver.getSkill() == Skills.ROCK_THROW || receiver.getSkill() == Skills.PAPER_CUT) {
                    conditional = 12.5;
                }
            } else if (damager.getSkill() == Skills.PAPER_CUT && receiver.calculateHpPercent() >= 0 && receiver.calculateHpPercent() < 25) {
                if (receiver.getSkill() == Skills.SCISSORS_POKE || receiver.getSkill() == Skills.ROCK_THROW) {
                    conditional = 12.5;
                }
            } else if (damager.getSkill() == Skills.SHOOT_THE_MOON && receiver.getSkill() == damager.getSkillPrediction()) {
                conditional += 20;
            }

        } else { // petType = Intelligence
            if (damager.getSkill() == Skills.ROCK_THROW) {
                if (receiver.getSkillRechargeTime(Skills.SCISSORS_POKE) > 0) {
                    conditional += 3;
                }
                if (receiver.getSkillRechargeTime(Skills.ROCK_THROW) > 0) {
                    conditional += 2;
                }
                if (receiver.getSkillRechargeTime(Skills.SHOOT_THE_MOON) > 0) {
                    conditional += 2;
                }
            } else if (damager.getSkill() == Skills.SCISSORS_POKE) {
                if (receiver.getSkillRechargeTime(Skills.PAPER_CUT) > 0) {
                    conditional += 3;
                }
                if (receiver.getSkillRechargeTime(Skills.SCISSORS_POKE) > 0) {
                    conditional += 2;
                }
                if (receiver.getSkillRechargeTime(Skills.SHOOT_THE_MOON) > 0) {
                    conditional += 2;
                }
            } else if (damager.getSkill() == Skills.PAPER_CUT) {
                if (receiver.getSkillRechargeTime(Skills.ROCK_THROW) > 0) {
                    conditional += 3;
                }
                if (receiver.getSkillRechargeTime(Skills.PAPER_CUT) > 0) {
                    conditional += 2;
                }
                if (receiver.getSkillRechargeTime(Skills.SHOOT_THE_MOON) > 0) {
                    conditional += 2;
                }
            } else if (damager.getSkill() == Skills.SHOOT_THE_MOON && receiver.getSkill() == damager.getSkillPrediction()) {
                conditional += 20;
            }
        }

        // Reversal of Fortune Logic (applies to all pet types)
        if (damager.getSkill() == Skills.REVERSAL_OF_FORTUNE) {
            double totalDelta = 0;

            for (List<Damage> round : roundHistory) {
                double dealt = 0;
                double taken = 0;

                for (Damage damage : round) {
                    if (damage.getDamager() == damager) {
                        dealt += damage.getRandomDamage();
                    }
                    if (damage.getReceiver() == damager) {
                        taken += damage.getRandomDamage();
                    }
                }

                double delta = dealt - taken;
                totalDelta += delta;

                System.out.println("[Reversal] Round Delta for " + damager.getPetName() + ": " +
                        delta + " (Dealt: " + dealt + ", Taken: " + taken + ")");
            }
            
            System.out.println("[Reversal] Total Delta for " + damager.getPetName() + ": " + totalDelta);

            baseDamage = Math.max(0, baseDamage - (int) totalDelta);
            conditional = Math.max(0, conditional - totalDelta);
        }

        Damage damage = new Damage(baseDamage, conditional, receiver, damager);
        return damage;
    }

    private Pet getNextAliveTarget(Pet attacker) {
        int index = petList.indexOf(attacker);
        int size = petList.size();

        for (int i = 1; i < size; i++) {
            Pet target = petList.get((index + i) % size);
            if (target.isAwake()) return target;
        }
        return attacker;
    }


    /**
     * Executes the round.
     * Steps:
     * 1. Choose the skill for each pet
     * 2. Calculate the damage done in the round
     * 3. Set skill recharge times
     * 4. Apply damage and set health for each pet.
     */
    public List<Damage> executeRound() {
        List<Damage> damageList = new ArrayList<>();
        List<Pet> awakeAtRoundStart = new ArrayList<>();

        for (Pet pet : petList) {
            if (pet.isAwake()) {
                awakeAtRoundStart.add(pet);
            }
        }
        this.awakeAtRoundStart = new ArrayList<>(awakeAtRoundStart);

        for (Pet pet : petList) {
            if (pet.isAwake()) {
                pet.chooseSkill();
            }
        }

        for (Pet damager : petList) {
            if (!damager.isAwake())
            {
                continue;
            }

            Pet receiver = getNextAliveTarget(damager);
            if (receiver == damager)
            {
                continue;
            }

            Damage damage = CalculateDamagePerPet(damager, receiver, previousRoundDamages);
            damageList.add(damage);
        }

        for (Pet pet : petList) {
            pet.decrementRechargeTimes();

            Skills skill = pet.getSkill();
            if (skill == Skills.REVERSAL_OF_FORTUNE || skill == Skills.SHOOT_THE_MOON) {
                pet.setRechargeTime(skill, 6);
            } else {
                pet.setRechargeTime(skill, 1);
            }
        }

        for (Damage damage : damageList) {
            Pet receiver = damage.getReceiver();
            Pet damager = damage.getDamager();

            receiver.updateHp(damage.calculateTotalDamage());

            System.out.println("\n" + damager.getPetName() + " hurt " + receiver.getPetName() + " using "
                    + damager.getSkill() + " for " + damage.calculateTotalDamage() + " damage.");
            System.out.println("Random Damage: " + damage.getRandomDamage()
                    + "\nConditional Damage: " + damage.getConditionalDamage());
        }

        roundHistory.add(new ArrayList<>(damageList));
        previousRoundDamages.addAll(damageList);

        return damageList;
    }




}
