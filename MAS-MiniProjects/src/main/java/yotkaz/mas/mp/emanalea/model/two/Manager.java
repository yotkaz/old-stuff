package yotkaz.mas.mp.emanalea.model.two;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Manager {

    private String firstName;
    private String lastName;
    private Set<Team> teams = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public Manager setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Manager setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Set<Team> getTeams() {
        HashSet<Team> result = new HashSet<>();
        result.addAll(teams);
        return result;
    }

    public Manager addTeam(Team team) {
        if (!teams.contains(team)) {
            teams.add(team);
            team.setManager(this);
        }
        return this;
    }

    public Manager removeTeam(Team team) {
        if (teams.contains(team)) {
            teams.remove(team);
            Optional<Manager> previousManager = team.getManager();
            if (previousManager.isPresent() && previousManager.get() == this) {
                team.setManager(null);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", teams=" + teams.stream()
                    .map(Team::getName)
                    .collect(Collectors.toSet())+
                '}';
    }
}