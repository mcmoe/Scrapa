package model;

import lombok.Data;

/**
 * Represents the rank of a team in a league
 * Created by mcmoe on 4/28/2014.
 */
@Data
public class LeagueStanding {
    private final String team;
    private final int standing;

    public LeagueStanding(int standing, String team) {
        this.team = team;
        this.standing = standing;
    }
}
