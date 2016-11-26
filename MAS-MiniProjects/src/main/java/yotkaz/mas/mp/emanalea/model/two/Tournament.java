package yotkaz.mas.mp.emanalea.model.two;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import yotkaz.mas.mp.emanalea.base.Extent;

import java.util.*;
import java.util.stream.Collectors;

public class Tournament extends Extent {

    private String name;
    private Map<String, Team> teams = new HashMap<>();
    private Set<Match> matches = new HashSet<>();
    private static SetMultimap<Tournament, Match> allMatches = HashMultimap.create();

    public String getName() {
        return name;
    }

    public Tournament setName(String name) {
        this.name = name;
        return this;
    }

    public Set<Team> getTeams() {
        HashSet<Team> result = new HashSet<>();
        result.addAll(teams.values());
        return result;
    }

    public Optional<Team> getTeamByCountry(String country) {
        return Optional.ofNullable(teams.get(country.toLowerCase()));
    }

    public Tournament addTeam(Team team) {
        if (!teams.containsKey(team.getCountry())) {
            teams.put(team.getCountry(), team);
            team.addTournament(this);
        }
        return this;
    }

    public Tournament removeTeam(Team team) {
        if (teams.containsKey(team.getCountry())) {
            teams.remove(team.getCountry());
            team.removeTournament(this);
        }
        return this;
    }

    public Set<Match> getMatches() {
        HashSet<Match> result = new HashSet<>();
        result.addAll(matches);
        return result;
    }

    public Tournament addMatch(Match match) {
        if (allMatches.values().contains(match)) {
            throw new RuntimeException("Cannot add match to more than one tournament");
        }
        matches.add(match);
        allMatches.put(this, match);
        return this;
    }

    public void remove() {
        getExtent(this.getClass()).remove(this);
        allMatches.removeAll(this);
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "name='" + name + '\'' +
                ", teams=" + teams.values().stream()
                    .map(Team::getName)
                    .collect(Collectors.toSet()) +
                '}';
    }

}
