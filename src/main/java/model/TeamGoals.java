package model;

import lombok.Data;

/**
 * Represents a team by its position according to the number of goals they scored
 * Created by MC on 4/26/2014.
 */
@Data
public class TeamGoals {
    private final int position;
    private final String team;
    private final int goals;

    public TeamGoals(int position, String team, int goals) {
        this.position = position;
        this.team = team;
        this.goals = goals;
    }
}
