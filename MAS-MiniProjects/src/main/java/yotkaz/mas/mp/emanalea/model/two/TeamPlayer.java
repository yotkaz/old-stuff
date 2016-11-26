package yotkaz.mas.mp.emanalea.model.two;

public class TeamPlayer {

    public enum Position {DEFENDER, FORWARD};

    private Team team;
    private Player player;
    private Position position;

    Team getTeam() {
        return team;
    }

    TeamPlayer setTeam(Team team) {
        this.team = team;
        return this;
    }

    Player getPlayer() {
        return player;
    }

    TeamPlayer setPlayer(Player player) {
        this.player = player;
        return this;
    }

    Position getPosition() {
        return position;
    }

    TeamPlayer setPosition(Position position) {
        this.position = position;
        return this;
    }
}
