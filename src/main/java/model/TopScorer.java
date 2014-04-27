package model;

import lombok.Data;

/**
 * Represents a top scorer object:
 * player, team, and number of goals
 * Created by mcmoe on 4/26/2014.
 */

@Data
public class TopScorer {
    private final String player;
    private final String team;
    private final int goals;

    public TopScorer(String player, String team, int goals) {
        this.player = player;
        this.team = team;
        this.goals = goals;
    }
}

