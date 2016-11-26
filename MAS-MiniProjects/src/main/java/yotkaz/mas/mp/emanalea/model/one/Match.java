package yotkaz.mas.mp.emanalea.model.one;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created on 03.04.16.
 */
public class Match extends Competition {

    private Map<Rival, Double> score = new HashMap<>();
    private Date startDate;

    public Match(String name, Date startDate) {
        this(name, State.PLANNED, startDate);
    }

    public Match(String name, State state, Date startDate) {
        super(name, state);
        setStartDate(startDate);
    }

    public static List<Match> lastMatches(int count) {
        return getExtent(Match.class)
                .stream()
                .filter(match -> match.getStartDate().toInstant().compareTo(Instant.now()) < 0)
                .sorted((match1, match2) -> match2.getStartDate().compareTo(match1.getStartDate())) //DESC ORDER
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Rival> getWinners() {
        double max = score
                .values()
                .stream()
                .max(Double::compareTo)
                .orElseGet(() -> 0.0);

        return score
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Map<Rival, Double> getScore() {
        return score;
    }

    public Match setScore(Map<Rival, Double> score) {
        this.score = score;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Match setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }
}