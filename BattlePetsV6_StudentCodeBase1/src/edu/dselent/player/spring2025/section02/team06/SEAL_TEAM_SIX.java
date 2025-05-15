package edu.dselent.player.spring2025.section02.team06;

import edu.dselent.event.*;
import edu.dselent.player.PetInstance;
import edu.dselent.player.defaultspeed.OpponentTracker;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;

import java.util.*;

public class SEAL_TEAM_SIX extends PetInstance {
    private final Random rng = new Random(7331);
    private final ArrayList<Skills> availableSkills = new ArrayList<>();
    private OpponentTracker tracker;
    private int opponentIndex;
    private int selfIndex;
    private Skills skillPrediction;
    private int round = 0;
    private double totalRandomDealt = 0;
    private double totalRandomTaken = 0;

    public SEAL_TEAM_SIX(int playableUid, PlayerSettings info) {
        super(playableUid, info);
    }

    private boolean isOpponentSpeedType() {
        return tracker.getPetInfo().get(opponentIndex).toString().equalsIgnoreCase("SPEED");
    }
    private boolean isOpponentPowerType() {
        return tracker.getPetInfo().get(opponentIndex).toString().equalsIgnoreCase("POWER");
    }


    private boolean isOpponentIntelligenceType() {
        return tracker.getPetInfo().get(opponentIndex).toString().equalsIgnoreCase("INTELLIGENCE");
    }

    @Override
    public Skills chooseSkill() {
        availableSkills.clear();
        for (Skills skill : Skills.values()) {
            if (getSkillRechargeTime(skill) == 0) {
                availableSkills.add(skill);
            }
        }

        double oppHp = tracker.getPetHps().get(opponentIndex);
        Map<Skills, Integer> oppCooldowns = tracker.getPetCooldowns().get(opponentIndex);
        Skills predictedOppSkill = predictLikelySkill(oppCooldowns);

        // Round 0: always open with Shoot the Moon
        if (getCurrentRound() == 0 && availableSkills.contains(Skills.SHOOT_THE_MOON)) {
            skillPrediction = Skills.SHOOT_THE_MOON;
            return Skills.SHOOT_THE_MOON;
        }


        if (isOpponentPowerType()) {
            if (getCurrentRound() == 0 && availableSkills.contains(Skills.SHOOT_THE_MOON)) {
                skillPrediction = Skills.SHOOT_THE_MOON;
                return Skills.SHOOT_THE_MOON;
            }
        }

        if (!isOpponentIntelligenceType())
        {
            if (getCurrentRound() == 0 && availableSkills.contains(Skills.SHOOT_THE_MOON))
            {
                skillPrediction = Skills.ROCK_THROW;
                return Skills.SHOOT_THE_MOON;
            }

            if (availableSkills.contains(Skills.SHOOT_THE_MOON)) {
                skillPrediction = predictLikelySkill(tracker.getPetCooldowns().get(opponentIndex));
                return Skills.SHOOT_THE_MOON;
            }
        }


        // Use Shoot the Moon whenever it's off cooldown
        if (availableSkills.contains(Skills.SHOOT_THE_MOON)) {
            skillPrediction = predictedOppSkill;
            return Skills.SHOOT_THE_MOON;
        }

        // Use Reversal of Fortune when significantly behind
        if ((totalRandomTaken - totalRandomDealt) >= 3 && availableSkills.contains(Skills.REVERSAL_OF_FORTUNE)) {
            return Skills.REVERSAL_OF_FORTUNE;
        }

        // Conditional Skill Logic (based on prediction)
        if (oppHp >= 75 &&
                (predictedOppSkill == Skills.SCISSORS_POKE || predictedOppSkill == Skills.PAPER_CUT) &&
                availableSkills.contains(Skills.ROCK_THROW)) {
            return Skills.ROCK_THROW;
        }

        if (oppHp >= 25 && oppHp < 75 &&
                (predictedOppSkill == Skills.ROCK_THROW || predictedOppSkill == Skills.PAPER_CUT) &&
                availableSkills.contains(Skills.SCISSORS_POKE)) {
            return Skills.SCISSORS_POKE;
        }

        if (oppHp < 25 &&
                (predictedOppSkill == Skills.ROCK_THROW || predictedOppSkill == Skills.SCISSORS_POKE) &&
                availableSkills.contains(Skills.PAPER_CUT)) {
            return Skills.PAPER_CUT;
        }

        // Fallback to HP-based guess if prediction doesn't help
        if (oppHp >= 75 && availableSkills.contains(Skills.ROCK_THROW)) {
            return Skills.ROCK_THROW;
        } else if (oppHp >= 25 && availableSkills.contains(Skills.SCISSORS_POKE)) {
            return Skills.SCISSORS_POKE;
        } else if (availableSkills.contains(Skills.PAPER_CUT)) {
            return Skills.PAPER_CUT;
        }

        // Secondary fallback based on cooldowns
        if (oppCooldowns.getOrDefault(Skills.ROCK_THROW, 0) > 1 && availableSkills.contains(Skills.SCISSORS_POKE)) {
            return Skills.SCISSORS_POKE;
        }
        if (oppCooldowns.getOrDefault(Skills.SCISSORS_POKE, 0) > 1 && availableSkills.contains(Skills.PAPER_CUT)) {
            return Skills.PAPER_CUT;
        }
        if (oppCooldowns.getOrDefault(Skills.PAPER_CUT, 0) > 1 && availableSkills.contains(Skills.ROCK_THROW)) {
            return Skills.ROCK_THROW;
        }

        // Pure fallback
        return availableSkills.get(rng.nextInt(availableSkills.size()));
    }

    private Skills predictLikelySkill(Map<Skills, Integer> oppCooldowns) {
        if (getCurrentRound() == 0 && isOpponentSpeedType())
        {
            return Skills.ROCK_THROW;
        }
        if (getCurrentRound() == 0 && isOpponentPowerType())
        {
            return Skills.SHOOT_THE_MOON;
        }

        return oppCooldowns.entrySet().stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(Skills.ROCK_THROW);
    }

    @Override
    public Skills getSkillPrediction() {
        return skillPrediction;
    }

    @Override
    public void update(Object event) {
        if (event instanceof BaseEvent base) {
            switch (base.getEventType()) {
                case FIGHT_START -> {
                    handleFightStart((FightStartEvent) base);
                    round = 0;
                }
                case ROUND_START -> {
                    tracker.updateCooldowns();
                    round++;
                }
                case ATTACK -> {
                    AttackEvent attack = (AttackEvent) base;
                    tracker.track(attack);

                    if (attack.getAttackingPlayableUid() == getPlayableUid()) {
                        totalRandomDealt += attack.getDamage().getRandomDamage();
                    }
                    if (attack.getVictimPlayableUid() == getPlayableUid()) {
                        totalRandomTaken += attack.getDamage().getRandomDamage();
                    }
                }
            }
        }
    }

    private int getCurrentRound() {
        return round;
    }

    private void handleFightStart(FightStartEvent fightStart) {
        for (int i = 0; i < fightStart.getPlayerEventInfoList().size(); i++) {
            if (fightStart.getPlayerEventInfoList().get(i).getPlayableUid() == getPlayableUid()) {
                selfIndex = i;
                opponentIndex = (i + 1) % fightStart.getPlayerEventInfoList().size();
                break;
            }
        }
        tracker = new OpponentTracker(fightStart.getPlayerEventInfoList());
        totalRandomDealt = 0;
        totalRandomTaken = 0;
        skillPrediction = Skills.ROCK_THROW;
    }
}
