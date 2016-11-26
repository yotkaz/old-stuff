package yotkaz.mas.mp.emanalea.app.story.impl;

import yotkaz.mas.mp.emanalea.app.conf.Configuration;
import yotkaz.mas.mp.emanalea.app.story.AbstractStory;
import yotkaz.mas.mp.emanalea.model.two.*;
import yotkaz.mas.mp.emanalea.service.log.Loggable;

import java.util.Optional;

import static yotkaz.mas.mp.emanalea.model.two.TeamPlayer.Position.DEFENDER;
import static yotkaz.mas.mp.emanalea.model.two.TeamPlayer.Position.FORWARD;

public class StoryMAS2 extends AbstractStory {

    private final Loggable logger;

    public StoryMAS2(Configuration configuration) throws Exception {
        super(configuration);
        logger = configuration.getLogger();
    }

    @Override
    public void action() throws Exception {

        logger.info("MiniProject #2 started");

        logger.original("");
        logger.info("###### Creating objects");

        Team teamABC = new Team()
                .setName("ABC")
                .setCountry("Poland");
        Team teamQWE = new Team()
                .setName("QWE")
                .setCountry("Hungary");

        Tournament tournamentEURO2016 = new Tournament()
                .setName("EURO2016")
                .addTeam(teamABC)
                .addTeam(teamQWE);

        Tournament tournamentEURO2020 = new Tournament()
                .setName("EURO2020")
                .addTeam(teamABC)
                .addTeam(teamQWE);

        Match.create(tournamentEURO2016, teamABC, teamQWE);
        Match.create(tournamentEURO2016, teamQWE, teamABC);
        Match.create(tournamentEURO2020, teamABC, teamQWE);

        Manager mZenekZloty = new Manager()
                .setFirstName("Zenek")
                .setLastName("Zloty")
                .addTeam(teamABC);

        Manager mStaszekSrebrny = new Manager()
                .setFirstName("Staszek")
                .setLastName("Srebrny");
        teamQWE.setManager(mStaszekSrebrny);

        Player pAdamZet = new Player()
                .setFirstName("Adam")
                .setLastName("Zet")
                .addTeam(teamABC, DEFENDER);

        Player pKamilBet = new Player()
                .setFirstName("Kamil")
                .setLastName("Bet");
        teamABC.addPlayer(pKamilBet, FORWARD);

        Player pLucasKot = new Player()
                .setFirstName("Lucas")
                .setLastName("Kot")
                .addTeam(teamQWE, DEFENDER);

        Player pFooBar = new Player()
                .setFirstName("Foo")
                .setLastName("Bar");
        teamQWE.addPlayer(pFooBar, FORWARD);

        logger.info(teamABC.toString());
        logger.info(teamQWE.toString());

        logger.original("");
        logger.info("###### Removing player Lucas Kot from QWE");

        teamQWE.removePlayer(pLucasKot);
        logger.info(teamQWE.toString());
        logger.info(pLucasKot.toString());

        logger.original("");
        logger.info("###### Player Foo Bar goes to ABC");

        pFooBar.addTeam(teamABC, FORWARD);
        logger.info(teamABC.toString());
        logger.info(teamQWE.toString());

        logger.original("");
        logger.info("###### Manager Staszek Srebrny goes to ABC");
        mStaszekSrebrny.addTeam(teamABC);
        logger.info(teamABC.toString());
        logger.info(teamQWE.toString());
        logger.info(mZenekZloty.toString());

        logger.original("");
        logger.info("###### Manager Zenek Zloty takes QWE");
        mZenekZloty.addTeam(teamQWE);
        logger.info(teamABC.toString());
        logger.info(teamQWE.toString());

        logger.original("");
        logger.info("###### Tournaments");
        Tournament.getExtent(Tournament.class).stream().forEach(tournament -> {
            logger.info(tournament.toString());
        });

        logger.original("");
        logger.info("###### Team from Poland");
        Optional<Team> teamFromPoland = tournamentEURO2016.getTeamByCountry("Poland");
        logger.info(teamFromPoland.isPresent() ? teamFromPoland.get().toString() : "There's no team from Poland.");

        logger.original("");
        logger.info("###### Matches");
        logger.info(tournamentEURO2016.getMatches().toString());
        logger.info(tournamentEURO2020.getMatches().toString());

    }

}
