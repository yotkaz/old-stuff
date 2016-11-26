package yotkaz.mas.mp.emanalea.model.two;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Team {

    private String name;
    private String country;
    private Manager manager;
    private Set<TeamPlayer> teamPlayers = new HashSet<>();
    private Set<Tournament> tournaments = new HashSet<>();


    public String getName() {
        return name;
    }

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Team setCountry(String country) {
        this.country = country.toLowerCase();
        return this;
    }

    public Optional<Manager> getManager() {
        return Optional.ofNullable(manager);
    }

    public Team setManager(Manager manager) {
        if (this.manager != manager) {
            Optional<Manager> previousManager = getManager();
            this.manager = manager;
            previousManager.ifPresent(m -> {
                if (m.getTeams().contains(this)) {
                    m.removeTeam(this);
                }
            });
            getManager().ifPresent(m -> m.addTeam(this));
        }
        return this;
    }

    public Set<TeamPlayer> getTeamPlayers() {
        Set<TeamPlayer> result = new HashSet<>();
        result.addAll(teamPlayers);
        return result;
    }

    public Team addPlayer(Player player, TeamPlayer.Position position) {
        if (!teamPlayers.stream().anyMatch(tp -> tp.getPlayer() == player)) {
            teamPlayers.add(new TeamPlayer()
                    .setPlayer(player)
                    .setTeam(this)
                    .setPosition(position));
            player.addTeam(this, position);
        }
        return this;
    }

    public Team removePlayer(Player player) {
        Optional<TeamPlayer> teamPlayer = teamPlayers.stream()
                .filter(tp -> tp.getPlayer() == player)
                .findFirst();
        if (teamPlayer.isPresent()) {
            teamPlayers.remove(teamPlayer.get());
            player.removeTeam(this);
        }
        return this;
    }

    public Set<Tournament> getTournaments() {
        HashSet<Tournament> result = new HashSet<>();
        result.addAll(tournaments);
        return result;
    }

    public Team addTournament(Tournament tournament) {
        if (!tournaments.contains(tournament)) {
            tournaments.add(tournament);
            tournament.addTeam(this);
        }
        return this;
    }

    public Team removeTournament(Tournament tournament) {
        if (tournaments.contains(tournament)) {
            tournaments.add(tournament);
            tournament.removeTeam(this);
        }
        return this;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + getName() + '\'' +
                ", country='" + getCountry() + '\'' +
                ", manager='" + getManager().orElse(null) + '\'' +
                ", players=" + getTeamPlayers().stream()
                    .map(tp -> Pair.of(tp.getPlayer(), tp.getPosition()))
                    .collect(Collectors.toSet()) +
                '}';
    }
}
