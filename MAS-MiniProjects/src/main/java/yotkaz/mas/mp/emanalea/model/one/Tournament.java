package yotkaz.mas.mp.emanalea.model.one;

import java.util.Set;

/**
 * Created on 03.04.16.
 */
public class Tournament extends Competition {

    public Tournament(String name) {
        this(name, State.PLANNED);
    }

    public Tournament(String name, State state) {
        super(name, state);
    }

    @Override
    public Set<Rival> getWinners() {
        throw new RuntimeException("Tournaments aren't implemented yet!");
    }
}
