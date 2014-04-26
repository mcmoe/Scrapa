package model;

import lombok.Data;

/**
 * Represents a top scorer object:
 * scorer position, name, team, goals
 * Created by MC on 4/26/2014.
 */

@Data
public class TopScorer {
    private final int position;
    private final String name;
    private final String team;
    private final int goals;

    public TopScorer(int position, String name, String team, int goals) {
        this.position = position;
        this.name = name;
        this.team = team;
        this.goals = goals;
    }
}

