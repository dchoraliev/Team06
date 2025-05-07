package edu.team06.control;

import edu.team06.entity.Pair;
import edu.team06.entity.Pet;
import edu.team06.entity.Playable;
import edu.team06.util.RandomGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class Season {
	private final List<Pet> petList;
	private final RandomGenerator random;
	private final int fightNumber;
	private final List<Battle> battleList = new ArrayList<>();
	private Map<Pet,Pair<Integer>> battleResultMap = new HashMap<>();
	private final Map<Pet,Integer> fightResultMap = new HashMap<>();

	public Season(List<Pet> petList, RandomGenerator randomInstance, int fightCount) {
		this.petList = petList;
		this.random = randomInstance;
		this.fightNumber = fightCount;
		battleResultMap = new HashMap<>();

		for (Pet p : petList) {
			battleResultMap.put(p,new Pair<>(0,0));
		}
		for (Pet p : petList) {
			fightResultMap.put(p,0);
		}
	}

	public void executeSeason() {
		SeasonIterator it = new SeasonIterator(petList);
		Iterator<List<Pair<Pet>>> iterator = it.iterator();
		int i = 1;
		while (iterator.hasNext()) {
			List<Pair<Pet>> roundBattles = iterator.next();
			System.out.println("Round " + i);
			int j = 1;

			// Loops through the round and sets up and executes the battles
			for (Pair<Pet> pair : roundBattles) {
				System.out.println("Battle " + j + ": " + pair.first().getPetName() + " - " + pair.second().getPetName());

				List<Pet> petsInRound = new ArrayList<>();
				petsInRound.add(pair.first());
				petsInRound.add(pair.second());

				Battle battle = new Battle(petsInRound, random, fightNumber);
				battleList.add(battle);

				Pet battleWinner = battle.executeBattle();
				Pet battleLoser = (battleWinner == pair.first()) ? pair.second() : pair.first();

				// Updates the fight wins map
				HashMap<Pet,Integer> fights = battle.getFightWins();
				for (Map.Entry<Pet,Integer> entry : fights.entrySet()) {
					Pet key = entry.getKey();
					fightResultMap.put(key,fightResultMap.get(key) + entry.getValue());
				}

				// Gets the current win/lose record for both pets
				Pair<Integer> winnerRecord = battleResultMap.get(battleWinner);
				Pair<Integer> loserRecord = battleResultMap.get(battleLoser);

				// Added the win/lose to its respective entry
				battleResultMap.put(battleWinner, new Pair<>(winnerRecord.first() + 1, winnerRecord.second()));
				battleResultMap.put(battleLoser, new Pair<>(loserRecord.first(), loserRecord.second() + 1));

				j++;
			}
			i++;
		}

		// Sorts the map by number of battles won descending then by number of fights won
		Map<Pet,Pair<Integer>> sortedBattleResultMap = battleResultMap.entrySet()
				.stream()
				.sorted(
						Comparator.comparing(
								(Map.Entry<Pet,Pair<Integer>> entry) -> entry.getValue().first(),Comparator.reverseOrder())
								.thenComparing(entry -> fightResultMap.get(entry.getKey()),Comparator.reverseOrder())
				)
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1,e2) -> e1,
						LinkedHashMap::new
				));

		System.out.println("Battle Results: (Wins/Losses - Fights Won)");
		sortedBattleResultMap.keySet().forEach(pet ->
				System.out.println(pet.getPetName() + ": " + sortedBattleResultMap.get(pet).first() + "/" + sortedBattleResultMap.get(pet).second() + " - " + fightResultMap.get(pet)));
	}
}
