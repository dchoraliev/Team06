import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Round {
    private List<Pet> petList = new ArrayList<>();
    private Random random;


    /**
     * Creates the round object.
     * @param petList An array of pets to be in the fight
     * @param randomInstance The instance of Random to be used
     */
    public Round(List<Pet> petList, Random randomInstance) {
        this.petList.addAll(petList);
        this.random = randomInstance;
    }


    /**
     * Calculates the damage to be done to the receiver by the damager. The actual damage is applied elsewhere.
     * @param damager The pet doing the damage
     * @param receiver The pet being damaged
     * @return the damage object with info about the damager, receiver, base damage, and conditional damage
     */
    public Damage CalculateDamagePerPet(Pet damager, Pet receiver) {
        double conditional = 0;
        int baseDamage = 5; // todo impliment random here, damage [0-5)
        if (damager.getPetType() == PetTypes.POWER) {
            if (damager.getSkill() == Skills.ROCK_THROW && receiver.getSkill() == Skills.SCISSORS_POKE) {
                    conditional = baseDamage * 5;
            } else if (damager.getSkill() == Skills.SCISSORS_POKE && receiver.getSkill() == Skills.PAPER_CUT) {
                    conditional = baseDamage * 5;
            } else if (damager.getSkill() == Skills.PAPER_CUT && receiver.getSkill() == Skills.ROCK_THROW) {
                conditional = baseDamage * 5;
            }
        } else if (damager.getPetType() == PetTypes.SPEED) {
            if (damager.getSkill() == Skills.ROCK_THROW && receiver.calculateHpPercent() >= .75) {
                if (receiver.getSkill() == Skills.SCISSORS_POKE || receiver.getSkill() == Skills.PAPER_CUT) {
                    conditional = 12.5;
                }
            } else if (damager.getSkill() == Skills.SCISSORS_POKE && receiver.calculateHpPercent() >= .25 && receiver.calculateHpPercent() < .75) {
                if (receiver.getSkill() == Skills.ROCK_THROW || receiver.getSkill() == Skills.PAPER_CUT) {
                    conditional = 12.5;
                }
            } else if (damager.getSkill() == Skills.PAPER_CUT && receiver.calculateHpPercent() >= 0 && receiver.calculateHpPercent() < .25) {
                if (receiver.getSkill() == Skills.SCISSORS_POKE || receiver.getSkill() == Skills.ROCK_THROW) {
                    conditional = 12.5;
                }
            }
        } else { // petType = Intelligence
            if (damager.getSkill() == Skills.ROCK_THROW) {
                if (receiver.getSkillRechargeTime(Skills.SCISSORS_POKE) > 0) {
                    conditional += 3;
                }
                if (receiver.getSkillRechargeTime(Skills.ROCK_THROW) > 0) {
                    conditional += 2;
                }
            } else if (damager.getSkill() == Skills.SCISSORS_POKE) {
                if (receiver.getSkillRechargeTime(Skills.PAPER_CUT) > 0) {
                    conditional += 3;
                }
                if (receiver.getSkillRechargeTime(Skills.SCISSORS_POKE) > 0) {
                    conditional += 2;
                }
            } else if (damager.getSkill() == Skills.PAPER_CUT) {
                if (receiver.getSkillRechargeTime(Skills.ROCK_THROW) > 0) {
                    conditional += 3;
                }
                if (receiver.getSkillRechargeTime(Skills.PAPER_CUT) > 0) {
                    conditional += 2;
                }
            }
        }

        Damage damage = new Damage(baseDamage, conditional, damager, receiver);
        return damage;
    }

    public void ApplyDamage(double damageDealt) {}


    /**
     * Executes the round.
     * Steps:
     * 1. Choose the skill for each pet
     * 2. Calculate the damage done in the round
     * 3. Set skill recharge times
     * 4. Apply damage and set health for each pet.
     */
    public void executeRound() {
        List<Damage> damageList = new ArrayList<>();

        for (Pet pet : petList) {
            pet.chooseSkill();
        }

        for (Pet pet : petList) {
            for (Pet pet2 : petList) {
                if (pet != pet2) {
                    damageList.add(CalculateDamagePerPet(pet, pet2));
                }
            }
        }

        for (Pet pet : petList) {
            pet.decrementRechargeTimes();
            pet.setRechargeTime(pet.getSkill(), 1);
        }

        for (Damage damage : damageList) {
            Pet receiver = damage.getReciever();
            Pet damager = damage.getDamager();
            receiver.updateHp(receiver.getCurrentHp() - damage.calculateTotalDamage());
            System.out.println(damager.getPetName() + " hurt " + receiver.getPetName() + " using " + damager.getSkill() + " for " + damage.calculateTotalDamage() + " damage.");
            System.out.println("Random Damage: " + damage.getRandomDamage() + "\nConditional Damage: " + damage.getConditionalDamage());
        }

    };
}
