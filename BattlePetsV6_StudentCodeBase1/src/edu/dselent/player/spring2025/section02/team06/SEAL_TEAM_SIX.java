package edu.dselent.player.spring2025.section02.team06;

import edu.dselent.event.*;
import edu.dselent.player.PetInstance;
import edu.dselent.player.defaultspeed.OpponentTracker;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;

import java.util.*;

public class SEAL_TEAM_SIX extends PetInstance {
    private final Random rng = new Random(1337);
    private ArrayList<Skills> availableSkills = new ArrayList<>();
    private OpponentTracker tracker;
    private int opponentIndex;
    private int selfIndex;
    private Skills skillPrediction;

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

        double myDmgTaken = tracker.getPetRandDmgsTaken().get(selfIndex);
        double myDmgGiven = tracker.getPetRandDmgsGiven().get(selfIndex);
        double oppHp = tracker.getPetHps().get(opponentIndex);
        Map<Skills, Integer> oppCooldowns = tracker.getPetCooldowns().get(opponentIndex);

        // Opponent cooldown analysis
        int rockCD = oppCooldowns.getOrDefault(Skills.ROCK_THROW, 0);
        int paperCD = oppCooldowns.getOrDefault(Skills.PAPER_CUT, 0);
        int scissorCD = oppCooldowns.getOrDefault(Skills.SCISSORS_POKE, 0);
        int moonCD = oppCooldowns.getOrDefault(Skills.SHOOT_THE_MOON, 0);
        int reversalCD = oppCooldowns.getOrDefault(Skills.REVERSAL_OF_FORTUNE, 0);

        long oppSkillCDs = oppCooldowns.values().stream().filter(cd -> cd > 1).count();

        // Use Shoot the Moon if 3+ opponent skills are on cooldown and we're not in danger
        if (oppHp == 100.0 || (oppSkillCDs >= 3 && oppHp > 50)) {
            if (availableSkills.contains(Skills.SHOOT_THE_MOON)) {
                skillPrediction = predictLikelySkill(oppCooldowns);
                return Skills.SHOOT_THE_MOON;
            }
        }

        // Use Reversal if behind or opponent likely just used theirs
        if ((myDmgTaken - myDmgGiven) >= 3 || reversalCD >= 5) {
            if (availableSkills.contains(Skills.REVERSAL_OF_FORTUNE)) {
                return Skills.REVERSAL_OF_FORTUNE;
            }
        }

        // Conditional skill logic: SPEED pet damage rules
        if (oppHp >= 75 && availableSkills.contains(Skills.ROCK_THROW)) {
            return Skills.ROCK_THROW;
        } else if (oppHp >= 25 && oppHp < 75 && availableSkills.contains(Skills.SCISSORS_POKE)) {
            return Skills.SCISSORS_POKE;
        } else if (oppHp < 25 && availableSkills.contains(Skills.PAPER_CUT)) {
            return Skills.PAPER_CUT;
        }

        // RPS counter logic based on opponent cooldowns
        if (rockCD > 1 && availableSkills.contains(Skills.SCISSORS_POKE)) {
            return Skills.SCISSORS_POKE;
        }
        if (scissorCD > 1 && availableSkills.contains(Skills.PAPER_CUT)) {
            return Skills.PAPER_CUT;
        }
        if (paperCD > 1 && availableSkills.contains(Skills.ROCK_THROW)) {
            return Skills.ROCK_THROW;
        }

        // Fallback
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
                case ATTACK -> tracker.track((AttackEvent) base);
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
    }
}