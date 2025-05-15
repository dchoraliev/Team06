package edu.dselent.player.spring2025.section02.team06;

import edu.dselent.event.*;
import edu.dselent.player.PetInstance;
import edu.dselent.player.defaultspeed.OpponentTracker;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;

import java.util.*;

public class SEAL_TEAM_SIX extends PetInstance {
    private final Random rng = new Random(7331);
    private ArrayList<Skills> availableSkills = new ArrayList<>();
    private OpponentTracker tracker;
    private int opponentIndex;
    private int selfIndex;
    private Skills skillPrediction;
    private Skills lastOppSkill = null;

    private double totalRandomDealt = 0;
    private double totalRandomTaken = 0;

    public SEAL_TEAM_SIX(int playableUid, PlayerSettings info) {
        super(playableUid, info);
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

        // Shoot the Moon as soon as it is ready, always
        if (availableSkills.contains(Skills.SHOOT_THE_MOON)) {
            skillPrediction = predictLikelySkill(oppCooldowns);
            return Skills.SHOOT_THE_MOON;
        }

        // Reversal of Fortune: use when behind
        if ((totalRandomTaken - totalRandomDealt) >= 3 && availableSkills.contains(Skills.REVERSAL_OF_FORTUNE)) {
            return Skills.REVERSAL_OF_FORTUNE;
        }

        // Conditional Speed Skill Usage
        if (oppHp >= 75 && lastOppSkill != null &&
                (lastOppSkill == Skills.SCISSORS_POKE || lastOppSkill == Skills.PAPER_CUT) &&
                availableSkills.contains(Skills.ROCK_THROW)) {
            return Skills.ROCK_THROW;
        }

        if (oppHp < 75 && oppHp >= 25 && lastOppSkill != null &&
                (lastOppSkill == Skills.ROCK_THROW || lastOppSkill == Skills.PAPER_CUT) &&
                availableSkills.contains(Skills.SCISSORS_POKE)) {
            return Skills.SCISSORS_POKE;
        }

        if (oppHp < 25 && lastOppSkill != null &&
                (lastOppSkill == Skills.ROCK_THROW || lastOppSkill == Skills.SCISSORS_POKE) &&
                availableSkills.contains(Skills.PAPER_CUT)) {
            return Skills.PAPER_CUT;
        }

        // If we don’t have last skill, use fallback based on HP only
        if (oppHp >= 75 && availableSkills.contains(Skills.ROCK_THROW)) {
            return Skills.ROCK_THROW;
        } else if (oppHp >= 25 && oppHp < 75 && availableSkills.contains(Skills.SCISSORS_POKE)) {
            return Skills.SCISSORS_POKE;
        } else if (oppHp < 25 && availableSkills.contains(Skills.PAPER_CUT)) {
            return Skills.PAPER_CUT;
        }

        // RPS-style backup based on cooldowns
        if (oppCooldowns.getOrDefault(Skills.ROCK_THROW, 0) > 1 && availableSkills.contains(Skills.SCISSORS_POKE)) {
            return Skills.SCISSORS_POKE;
        }
        if (oppCooldowns.getOrDefault(Skills.SCISSORS_POKE, 0) > 1 && availableSkills.contains(Skills.PAPER_CUT)) {
            return Skills.PAPER_CUT;
        }
        if (oppCooldowns.getOrDefault(Skills.PAPER_CUT, 0) > 1 && availableSkills.contains(Skills.ROCK_THROW)) {
            return Skills.ROCK_THROW;
        }

        // Fallback random
        return availableSkills.get(rng.nextInt(availableSkills.size()));
    }

    private Skills predictLikelySkill(Map<Skills, Integer> oppCooldowns) {
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
                case FIGHT_START -> handleFightStart((FightStartEvent) base);
                case ROUND_START -> tracker.updateCooldowns();
                case ATTACK -> {
                    AttackEvent attack = (AttackEvent) base;
                    tracker.track(attack);

                    if (attack.getAttackingPlayableUid() == selfIndex) {
                        totalRandomDealt += attack.getDamage().getRandomDamage();
                    }
                    if (attack.getVictimPlayableUid() == selfIndex) {
                        totalRandomTaken += attack.getDamage().getRandomDamage();
                    }

                    if (attack.getAttackingPlayableUid() == opponentIndex) {
                        lastOppSkill = attack.getAttackingSkillChoice();
                    }
                }
            }
        }
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
        lastOppSkill = null;
        totalRandomDealt = 0;
        totalRandomTaken = 0;
    }
}
