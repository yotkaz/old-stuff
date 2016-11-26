package yotkaz.mas.mp.emanalea.app.story.impl;

import com.google.common.collect.ListMultimap;
import yotkaz.mas.mp.emanalea.app.conf.Configuration;
import yotkaz.mas.mp.emanalea.app.story.AbstractStory;
import yotkaz.mas.mp.emanalea.base.Extent;
import yotkaz.mas.mp.emanalea.io.RWCombo;
import yotkaz.mas.mp.emanalea.model.one.*;
import yotkaz.mas.mp.emanalea.service.log.Loggable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 12.04.16.
 */
public class StoryMAS1 extends AbstractStory {

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private final Loggable logger;
    private final RWCombo<ListMultimap<Class, Object>> multiExtentRW;

    public StoryMAS1(Configuration configuration) throws Exception {
        super(configuration);
        logger = configuration.getLogger();
        multiExtentRW = configuration.getMultiExtentRW();
    }

    @Override
    public void action() throws Exception {

        logger.info("MiniProject #1 started");

        Player playerKamil = new Player("Kamil", "kamil@qw.com", parseDate("22/01/2016"));
        Player playerMarcin = new Player("Marcin", "marcin@qw.com", parseDate("10/01/2016"));
        Player playerAdam = new Player("Adam", "adam@qw.com", parseDate("23/03/2016"))
                .setInfo("Aaaaaa")
                .setPhone("111777888");
        Player playerTomek = new Player("Tomek", "tomek@qw.com", parseDate("23/02/2016"))
                .setInfo("Tttttt")
                .setPhone("222777888")
                .setPseudonyms("tt", "tom")
                .setAddress(new Address("Poland", "Warsaw", "Marszałkowska", "66")
                        .setApartmentNumber("77")
                        .setPostCode("66-666"));

        Set<Player> eaglesPlayers = new HashSet<>();
        eaglesPlayers.add(playerAdam);
        eaglesPlayers.add(playerKamil);
        Team eagles = new Team("Eagles", eaglesPlayers, playerKamil);

        Set<Player> lionsPlayers = new HashSet<>();
        lionsPlayers.add(playerMarcin);
        lionsPlayers.add(playerTomek);
        Team lions = new Team("Lions", lionsPlayers, playerTomek);

        League league = new League("ONE AND MAIN LEAGUE", Competition.State.STARTED);

        Set<Rival> rivals1 = new HashSet<>();
        rivals1.add(playerAdam);
        rivals1.add(playerTomek);
        Match m1 = new Match("match 1", parseDate("25/04/2016"));
        m1.setRivals(rivals1);

        Set<Rival> rivals2 = new HashSet<>();
        rivals2.add(playerMarcin);
        rivals2.add(playerAdam);
        Match m2 = new Match("match 2", Competition.State.FINISHED, parseDate("10/04/2016"));
        m2.setRivals(rivals2);

        Set<Rival> rivals3 = new HashSet<>();
        rivals3.add(playerMarcin);
        rivals3.add(playerTomek);
        Match m3 = new Match("match 3", Competition.State.FINISHED, parseDate("09/04/2016"));
        m3.setRivals(rivals3);

        Set<Rival> rivals4 = new HashSet<>();
        rivals4.add(playerMarcin);
        rivals4.add(playerTomek);
        Match m4 = new Match("match 4", Competition.State.FINISHED, parseDate("08/04/2016"));
        m4.setRivals(rivals4);

        Extent.saveExtents(multiExtentRW);
        Extent.clear();
        Extent.loadExtents(multiExtentRW);

        logger.original("");
        logger.original("PLAYERS EXTENT:");
        for (Player player : Extent.getExtent(Player.class)) {
            logger.original(player.toString());
        }

        logger.original("");
        logger.original("RIVALS (PLAYERS - fullName OVERRIDING):");
        for (Rival player : Extent.getExtent(Player.class)) {
            logger.original(player.fullName());
        }

        logger.original("");
        logger.original("RIVALS (TEAMS - fullName OVERRIDING):");
        for (Rival team : Extent.getExtent(Team.class)) {
            logger.original(team.fullName());
        }

        logger.original("");
        logger.original("LAST MATCHES (CLASS METHOD):");
        for (Match match : Match.lastMatches(2)) {
            logger.original(df.format(match.getStartDate()));
        }

        logger.original("");
        logger.info("MiniProject #1 finished");

    }

    private Date parseDate(String date) throws Exception {
        return df.parse(date);
    }

}
