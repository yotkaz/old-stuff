package yotkaz.mas.mp.emanalea.model.one;

import yotkaz.mas.mp.emanalea.base.Extent;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 03.04.16.
 */
public abstract class Competition extends Extent {

    private String name;
    private Set<Rival> rivals = new HashSet<>();
    private State state;

    public Competition(String name) {
        this(name, State.PLANNED);
    }

    public Competition(String name, State state) {
        setName(name);
        setState(state);
    }

    abstract public Set<Rival> getWinners();

    public boolean isWinner() {
        return state == State.FINISHED;
    }

    public String getName() {
        return name;
    }

    public Competition setName(String name) {
        this.name = name;
        return this;
    }

    public Set<Rival> getRivals() {
        return rivals;
    }

    public Competition setRivals(Set<Rival> rivals) {
        this.rivals = rivals;
        return this;
    }

    public State getState() {
        return state;
    }

    public Competition setState(State state) {
        this.state = state;
        return this;
    }

    public enum State {PLANNED, STARTED, FINISHED, CANCELED}
}