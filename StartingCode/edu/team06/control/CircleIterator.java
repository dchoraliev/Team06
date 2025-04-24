package edu.team06.control;

import edu.team06.entity.Pair;
import edu.team06.entity.Playable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CircleIterator implements Iterator<List<Pair<Playable>>> {
    private final List<Playable> players;
    private final int totalRounds;
    private int currentRound = 0;

    public CircleIterator(List<Playable> originalPlayers) {
        this.players = new ArrayList<>(originalPlayers);
        if (this.players.size() % 2 != 0)
        {
            this.players.add(null);
        }
        this.totalRounds = this.players.size() - 1;
    }


    @Override
    public boolean hasNext() {
        return currentRound < totalRounds;
    }

    @Override
    public List<Pair<Playable>> next() {
        int numOfPlayers = players.size();
        List<Pair<Playable>> matchups = new ArrayList<>();
        for (int i = 0; i < numOfPlayers / 2; i++) {
            Playable p1 = players.get(i);
            Playable p2 = players.get(numOfPlayers - 1 - i);

            // Skip matches involving the dummy player
            if (p1 != null && p2 != null) {
                matchups.add(new Pair<>(p1, p2));
            }
        }
        // Rotate players (except index 0, which is fixed)
        List<Playable> rotated = new ArrayList<>(players);
        Playable anchor = rotated.remove(0);
        Collections.rotate(rotated, 1);
        rotated.add(0, anchor);

        for (int i = 0; i < players.size(); i++) {
            players.set(i, rotated.get(i));
        }

        currentRound++;
        return matchups;
    }
}
