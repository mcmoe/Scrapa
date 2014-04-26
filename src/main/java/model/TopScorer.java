package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a top scorer object:
 * scorer position, name, team, goals
 * Created by MC on 4/26/2014.
 */

@EqualsAndHashCode
public class TopScorer {
    @Getter private final int position;
    @Getter private final String name;
    @Getter private final String team;
    @Getter private final int goals;

    public TopScorer(int position, String name, String team, int goals) {
        this.position = position;
        this.name = name;
        this.team = team;
        this.goals = goals;
    }
}

