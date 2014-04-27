package model;

import lombok.Data;

/**
 * Represents a top scorer object:
 * scorer rank, player, team, goals
 * Created by MC on 4/26/2014.
 */

@Data
public class TopScorer {
    private final int rank;
    private final String player;
    private final String team;
    private final int goals;

    public TopScorer(int rank, String player, String team, int goals) {
        this.rank = rank;
        this.player = player;
        this.team = team;
        this.goals = goals;
    }
}

