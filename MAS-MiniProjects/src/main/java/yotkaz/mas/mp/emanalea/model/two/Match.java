package yotkaz.mas.mp.emanalea.model.two;

public class Match {

    private Tournament tournament;
    private Team team1;
    private Team team2;

    private Match(Tournament tournament, Team team1, Team team2) {
        this.tournament = tournament;
        this.team1 = team1;
        this.team2 = team2;
    }


    public static Match create(Tournament tournament, Team team1, Team team2) {
        if (tournament == null) {
            throw new RuntimeException("Match cannot be added to non-existent tournament");
        }

        Match match = new Match(tournament, team1, team2);
        tournament.addMatch(match);

        return match;
    }

    @Override
    public String toString() {
        return "Match{" +
                "tournament=" + tournament.getName() +
                ", team1=" + team1.getName() +
                ", team2=" + team2.getName() +
                '}';
    }

}
