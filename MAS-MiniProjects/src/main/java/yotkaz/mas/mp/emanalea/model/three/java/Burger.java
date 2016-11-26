package yotkaz.mas.mp.emanalea.model.three.java;

import java.math.BigDecimal;

public class Burger extends Meal {

    private static final BigDecimal BUN_PRICE = new BigDecimal(2.00);
    private static final BigDecimal BEEF_PIECE_PRICE = new BigDecimal(4.00);
    private static final BigDecimal BACON_SLICE_PRICE = new BigDecimal(2.00);
    private static final BigDecimal CHEESE_SLICE_PRICE = new BigDecimal(1.00);
    private static final BigDecimal TOMATO_PIECE_SLICE = new BigDecimal(1.00);
    private static final BigDecimal LETTUCE_PIECE_PRICE = new BigDecimal(0.50);
    private static final BigDecimal SPICY_SAUCE_PRICE = new BigDecimal(0.50);
    private static final BigDecimal MILD_SAUCE_PRICE = new BigDecimal(0.50);

    private int beefPieces = 0;
    private int baconSlices = 0;
    private int cheeseSlices = 0;
    private int tomatoPieces = 0;
    private int lettucePieces = 0;
    private boolean spicySauce = false;
    private boolean mildSauce = false;

    public Burger() {
        this("Custom Burger");
    }

    public Burger(String name) {
        super(name);
    }

    @Override
    public BigDecimal calcPrice() {
        return new BigDecimal(0)
                .add(BUN_PRICE)
                .add(calcSimpleIngredientCost(getBeefPieces(), BEEF_PIECE_PRICE))
                .add(calcSimpleIngredientCost(getBaconSlices(), BACON_SLICE_PRICE))
                .add(calcSimpleIngredientCost(getCheeseSlices(), CHEESE_SLICE_PRICE))
                .add(calcSimpleIngredientCost(getTomatoPieces(), TOMATO_PIECE_SLICE))
                .add(calcSimpleIngredientCost(getLettucePieces(), LETTUCE_PIECE_PRICE))
                .add(calcSimpleIngredientCost(isSpicySauce() ? 1 : 0, SPICY_SAUCE_PRICE))
                .add(calcSimpleIngredientCost(isMildSauce() ? 1: 0, MILD_SAUCE_PRICE));
    }

    private BigDecimal calcSimpleIngredientCost(int amount, BigDecimal price) {
        return new BigDecimal(amount).multiply(price);
    }

    public int getBeefPieces() {
        return beefPieces;
    }

    public Burger setBeefPieces(int beefPieces) {
        this.beefPieces = beefPieces;
        return this;
    }

    public int getBaconSlices() {
        return baconSlices;
    }

    public Burger setBaconSlices(int baconSlices) {
        this.baconSlices = baconSlices;
        return this;
    }

    public int getCheeseSlices() {
        return cheeseSlices;
    }

    public Burger setCheeseSlices(int cheeseSlices) {
        this.cheeseSlices = cheeseSlices;
        return this;
    }

    public int getTomatoPieces() {
        return tomatoPieces;
    }

    public Burger setTomatoPieces(int tomatoPieces) {
        this.tomatoPieces = tomatoPieces;
        return this;
    }

    public int getLettucePieces() {
        return lettucePieces;
    }

    public Burger setLettucePieces(int lettucePieces) {
        this.lettucePieces = lettucePieces;
        return this;
    }

    public boolean isSpicySauce() {
        return spicySauce;
    }

    public Burger setSpicySauce(boolean spicySauce) {
        this.spicySauce = spicySauce;
        return this;
    }

    public boolean isMildSauce() {
        return mildSauce;
    }

    public Burger setMildSauce(boolean mildSauce) {
        this.mildSauce = mildSauce;
        return this;
    }

}