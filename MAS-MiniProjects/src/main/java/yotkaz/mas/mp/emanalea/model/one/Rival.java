package yotkaz.mas.mp.emanalea.model.one;

import yotkaz.mas.mp.emanalea.base.Extent;

/**
 * Created on 03.04.16.
 */
public class Rival extends Extent {

    private String name;

    public Rival(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public Rival setName(String name) {
        this.name = name;
        return this;
    }

    public String fullName() {
        return getName();
    }

}
