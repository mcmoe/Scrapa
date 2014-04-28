package model;

import lombok.Data;

/**
 * Represents the rank of a team in a league
 * Created by mcmoe on 4/28/2014.
 */
@Data
public class LeagueStanding {
    private final int standing;
    private final String team;

    public LeagueStanding(String team, int standing) {
        this.team = team;
        this.standing = standing;
    }
}
