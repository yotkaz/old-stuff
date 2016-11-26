package yotkaz.mas.mp.emanalea.model.three.java;

import java.math.BigDecimal;

public abstract class Meal {
    
    private String name;

    public Meal(String name) {
        this.name = name;
    }

    public abstract BigDecimal calcPrice();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}