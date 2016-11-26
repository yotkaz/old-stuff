package yotkaz.mas.mp.emanalea.model.one;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 03.04.16.
 */
public class League extends Competition {

    public static final int FIRST_PLACE = 1;

    private SetMultimap<Integer, Rival> ranking = HashMultimap.create();

    public League(String name) {
        this(name, State.PLANNED);
    }

    public League(String name, State state) {
        super(name, state);
    }

    @Override
    public Set<Rival> getWinners() {
        return new HashSet<>(ranking.get(FIRST_PLACE));
    }

    public SetMultimap<Integer, Rival> getRanking() {
        return ranking;
    }

    public League setRanking(SetMultimap<Integer, Rival> ranking) {
        this.ranking = ranking;
        return this;
    }
}
