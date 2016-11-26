package yotkaz.mas.mp.emanalea.model.one;

import java.util.Set;

/**
 * Created on 03.04.16.
 */
public class Team extends Rival {

    private Set<Player> players;
    private Player leader;

    public Team(String name, Set<Player> players, Player leader) {
        super(name);
        setPlayers(players);
        setLeader(leader);
    }

    public Player getLeader() {
        return leader;
    }

    public Team setLeader(Player player) {
        if (players.contains(player)) {
            leader = player;
        } else {
            throw new IllegalArgumentException("Player outside the team cannot be the leader!");
        }
        return this;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Team setPlayers(Set<Player> players) {
        this.players = players;
        return this;
    }

    @Override
    public String fullName() {
        return getName() + " led by " + getLeader().getName();
    }

}
