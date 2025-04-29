package edu.team06.control;

import edu.team06.entity.Pair;
import edu.team06.entity.Playable;

import java.util.*;

public class SeasonIterator implements Iterable<List<Pair<Playable>>> {
    private final List<Playable> players;
    private final int totalRounds;
    private int currentRound = 0;

    public SeasonIterator(List<Playable> originalPlayers) {
        this.players = new ArrayList<>(originalPlayers);
        if (this.players.size() % 2 != 0)
        {
            this.players.add(null);
        }
        this.totalRounds = this.players.size() - 1;
    }


    @Override
    public Iterator<List<Pair<Playable>>> iterator() {
        return new ConcreteIterator();
    }



    private class ConcreteIterator implements Iterator<List<Pair<Playable>>>
    {
        @Override
        public boolean hasNext() {
            return currentRound < totalRounds;
        }

        @Override
        public List<Pair<Playable>> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more rounds");
            }

            List<Pair<Playable>> matchups = new ArrayList<>();

            int mid = players.size() / 2;
            for (int i = 0; i < mid; i++) {
                Playable p1 = players.get(i);
                Playable p2 = players.get(players.size() - 1 - i);
                matchups.add(new Pair<>(p1, p2));
            }

            rotate();
            currentRound++;
            return matchups;
        }

        private void rotate() {
            Playable anchor = players.get(0);
            List<Playable> rotated = new ArrayList<>(players.size());
            rotated.add(anchor);
            rotated.add(players.get(players.size() - 1)); // move last to second
            for (int i = 1; i < players.size() - 1; i++) {
                rotated.add(players.get(i));
            }

            for (int i = 0; i < players.size(); i++) {
                players.set(i, rotated.get(i));
            }
        }
    }
}
