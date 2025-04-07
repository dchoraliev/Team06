import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public Pet(String playerName, String petName, PlayerTypes playerType, List<Skills> skills, PetTypes petType, double startingHp) {
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
        this.skills = List.of(Skills.ROCK_THROW, Skills.SCISSORS_POKE, Skills.PAPER_CUT);
        this.skillRechargeTimes = new HashMap<>();
    }
    //name, type, starting hp, current hp, skill
    @Override
    public int getPlayableUid() {
        return playableUid;
    }

    @Override
    public void setPlayableUid(int playableUid) {
        this.playableUid = playableUid;
    }

    @Override
    public int getPlayableId() {
        return playableId;
    }
    public Skills getSkill() {
        return chosenSkill;
    }
    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String getPetName() {
        return petName;
    }

    @Override
    public PlayerTypes getPlayerType() {
        return playerType;
    }

    @Override
    public PetTypes getPetType() {
        return petType;
    }

    @Override
    public double getCurrentHp() {
        return currentHp;
    }

    @Override
    public Skills chooseSkill() {
        //game-controlling class
        // todo Have this function set chosenSkill
        return null;
    }

    @Override
    public void updateHp(double hp) {
        //Game-controlling classes
    }

    @Override
    public void resetHp() {
        currentHp = startingHp;
    }

    @Override
    public boolean isAwake() {
        return currentHp > 0;
    }

    @Override
    public Skills getSkillPrediction() {
        return null;
    }

    @Override
    public int getSkillRechargeTime(Skills skill) {
        return skillRechargeTimes.getOrDefault(skill, 0);
    }

    @Override
    public double calculateHpPercent() {
        return (currentHp / startingHp) * 100;
    }

    @Override
    public double getStartingHp() {
        return startingHp;
    }

    @Override
    public void reset() {
        //game-controlling classes
    }

    @Override
    public void decrementRechargeTimes() {
        skillRechargeTimes.replaceAll((skill, time) -> Math.max(0, time - 1));
    }

    @Override
    public void setRechargeTime(Skills skill, int rechargeTime) {
        skillRechargeTimes.put(skill, rechargeTime);
    }
}
