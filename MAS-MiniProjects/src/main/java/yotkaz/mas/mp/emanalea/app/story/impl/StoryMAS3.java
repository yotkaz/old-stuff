package yotkaz.mas.mp.emanalea.app.story.impl;

import yotkaz.mas.mp.emanalea.app.conf.Configuration;
import yotkaz.mas.mp.emanalea.app.story.AbstractStory;
import yotkaz.mas.mp.emanalea.model.three.java.Burger;
import yotkaz.mas.mp.emanalea.model.three.java.Meal;
import yotkaz.mas.mp.emanalea.model.three.java.Pizza;
import yotkaz.mas.mp.emanalea.service.log.Loggable;

public class StoryMAS3 extends AbstractStory {

    private final Loggable logger;

    public StoryMAS3(Configuration configuration) throws Exception {
        super(configuration);
        logger = configuration.getLogger();
    }

    @Override
    public void action() throws Exception {

        logger.info("MiniProject #3 started");

        logger.original("");
        logger.info("###### Creating objects");

        Meal cheeseBaconBurger = new Burger("CHEESE-BACON BURGER")
                .setBeefPieces(1)
                .setBaconSlices(2)
                .setCheeseSlices(2);

        Meal capriciosaXXL = new Pizza("CAPRICIOSA XXL", Pizza.PizzaSize.XXL)
                .setCheese(1)
                .setHam(1)
                .setMushrooms(1);

        logger.original("");
        logger.info("###### abstract calcPrice");
        logger.info(capriciosaXXL.getName() + ": " + capriciosaXXL.calcPrice());
        logger.info(cheeseBaconBurger.getName() + ": " + cheeseBaconBurger.calcPrice());

    }

}
