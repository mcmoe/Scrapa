package model;

import lombok.Data;

/**
 * Represents a team by its rank according to the number of goals they scored
 * Created by MC on 4/26/2014.
 */
@Data
public class TeamGoals {
    private final int rank;
    private final String team;
    private final int goals;

    public TeamGoals(int rank, String team, int goals) {
        this.rank = rank;
        this.team = team;
        this.goals = goals;
    }
}
