package model;

import lombok.Data;

/**
 * Represents a team and the number of goals they scored
 * Created by MC on 4/26/2014.
 */
@Data
public class TeamGoals {
    private final String team;
    private final int goals;

    public TeamGoals(String team, int goals) {
        this.team = team;
        this.goals = goals;
    }
}
