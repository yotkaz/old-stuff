package yotkaz.mas.mp.emanalea.model.three.java;

import java.math.BigDecimal;

public class Pizza extends Meal {

    private static final BigDecimal BASE_PRICE = new BigDecimal(12.00);
    private static final BigDecimal CHEESE_PRICE = new BigDecimal(3.00);
    private static final BigDecimal HAM_PRICE = new BigDecimal(4.00);
    private static final BigDecimal TOMATOES_PRICE = new BigDecimal(2.00);
    private static final BigDecimal MUSHROOMS_PRICE = new BigDecimal(2.00);

    private PizzaSize size;
    private int cheese = 1;
    private int ham = 0;
    private int tomatoes = 0;
    private int mushrooms = 0;

    public Pizza(PizzaSize size) {
        this("Custom Pizza", size);
    }

    public Pizza(String name, PizzaSize size) {
        super(name);
        setSize(size);
    }

    @Override
    public BigDecimal calcPrice() {
        return new BigDecimal(0)
                .add(BASE_PRICE.multiply(getSize().getBigDecimalRatio()))
                .add(calcSimpleIngredientCost(getHam(), HAM_PRICE))
                .add(calcSimpleIngredientCost(getTomatoes(), TOMATOES_PRICE))
                .add(calcSimpleIngredientCost(getMushrooms(), MUSHROOMS_PRICE))
                .add(
                    getHam() > 0 && getTomatoes() > 0 && getMushrooms() > 0
                        ? new BigDecimal(0)
                        : calcSimpleIngredientCost(getCheese(), CHEESE_PRICE)
                );

    }

    private BigDecimal calcSimpleIngredientCost(int layers, BigDecimal price) {
        return new BigDecimal(layers).multiply(price).multiply(getSize().getBigDecimalRatio());
    }

    public enum PizzaSize {
        NORMAL(1.0), BIG(1.5), XXL(2.0);

        PizzaSize(double ratio) {
            this.ratio = ratio;
        }

        private double ratio;

        public double getRatio() {
            return ratio;
        }

        public BigDecimal getBigDecimalRatio() {
            return new BigDecimal(getRatio());
        }
    }

    public PizzaSize getSize() {
        return size;
    }

    public Pizza setSize(PizzaSize size) {
        this.size = size;
        return this;
    }

    public int getCheese() {
        return cheese;
    }

    public Pizza setCheese(int cheese) {
        this.cheese = cheese;
        return this;
    }

    public int getHam() {
        return ham;
    }

    public Pizza setHam(int ham) {
        this.ham = ham;
        return this;
    }

    public int getTomatoes() {
        return tomatoes;
    }

    public Pizza setTomatoes(int tomatoes) {
        this.tomatoes = tomatoes;
        return this;
    }

    public int getMushrooms() {
        return mushrooms;
    }

    public Pizza setMushrooms(int mushrooms) {
        this.mushrooms = mushrooms;
        return this;
    }
}
