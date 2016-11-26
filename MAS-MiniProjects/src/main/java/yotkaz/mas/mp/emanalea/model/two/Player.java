package yotkaz.mas.mp.emanalea.model.two;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Player {

    private String firstName;
    private String lastName;
    private Set<TeamPlayer> teamPlayers = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public Player setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Player setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Set<TeamPlayer> getTeamPlayers() {
        HashSet<TeamPlayer> result = new HashSet<>();
        result.addAll(teamPlayers);
        return result;
    }

    public Player addTeam(Team team, TeamPlayer.Position position) {
        if (!teamPlayers.stream().anyMatch(tp -> tp.getTeam() == team)) {
            teamPlayers.add(new TeamPlayer()
                    .setPlayer(this)
                    .setTeam(team)
                    .setPosition(position));
            team.addPlayer(this, position);
        }
        return this;
    }

    public Player removeTeam(Team team) {
        Optional<TeamPlayer> teamPlayer = teamPlayers.stream()
                .filter(tp -> tp.getTeam() == team)
                .findFirst();
        if (teamPlayer.isPresent()) {
            teamPlayers.remove(teamPlayer.get());
            team.removePlayer(this);
        }
        return this;
    }

    @Override
    public String toString() {
        return "Player{" +
                "teams'" + teamPlayers.stream()
                    .map(tp -> tp.getTeam().getName())
                    .collect(Collectors.toSet()) + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                '}';
    }
}