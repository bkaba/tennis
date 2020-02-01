package org.kata.tennis.domain.model.match;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.game.Game;
import org.kata.tennis.domain.model.player.Player;
import org.kata.tennis.domain.model.scores.GameScore;
import org.kata.tennis.domain.model.scores.SetScore;
import org.kata.tennis.domain.model.set.Set;
import org.kata.tennis.domain.model.tiebreak.TieBreak;

import java.util.ArrayList;
import java.util.Collections;

public class MatchTest {

    private static final Player FIRST_PLAYER = new Player("firstPlayer");
    private static final Player SECOND_PLAYER = new Player("secondPlayer");
    private Match match;

    @Before
    public void init() {
        this.match = new Match(FIRST_PLAYER, SECOND_PLAYER);
    }
    /*
     * player should win the match if the set score before was 6 against 5
     */
    @Test
    public void playerShouldWinTheMatch() throws UnknownPlayerException {
        //GIVEN
        Game game = new Game(FIRST_PLAYER, SECOND_PLAYER);
        game.setPlayerGameScore(FIRST_PLAYER, new ArrayList<>(Collections.singletonList(GameScore.FORTY)));
        game.setPlayerGameScore(SECOND_PLAYER, new ArrayList<>(Collections.singletonList(GameScore.FIFTEEN)));
        Set set = new Set(FIRST_PLAYER, SECOND_PLAYER);
        set.setGames(new ArrayList<>(Collections.singletonList(game)));
        set.setPlayerSetScores(FIRST_PLAYER, new ArrayList<>(Collections.singletonList(SetScore.SIX)));
        set.setPlayerSetScores(SECOND_PLAYER, new ArrayList<>(Collections.singletonList(SetScore.FIVE)));
        this.match.setSet(set);

        //WHEN
        this.match.scores(FIRST_PLAYER);

        //THEN
        Player winner = this.match.getWinner().orElse(new Player("noOneYet"));
        Assert.assertEquals(FIRST_PLAYER, winner);
    }

    @Test
    public void playerShouldWinMatchIfHeWinTheTieBreak() throws UnknownPlayerException {
        //GIVEN
        TieBreak tieBreak = new TieBreak(FIRST_PLAYER, SECOND_PLAYER);
        tieBreak.setPlayerTieBreakScores(FIRST_PLAYER,new ArrayList<>(Collections.singletonList(6)));
        tieBreak.setPlayerTieBreakScores(SECOND_PLAYER,new ArrayList<>(Collections.singletonList(2)));
        Set set = new Set(FIRST_PLAYER, SECOND_PLAYER);
        set.setPlayerSetScores(FIRST_PLAYER, new ArrayList<>(Collections.singletonList(SetScore.SIX)));
        set.setPlayerSetScores(SECOND_PLAYER, new ArrayList<>(Collections.singletonList(SetScore.SIX)));
        set.setTieBreak(tieBreak);
        this.match.setSet(set);

        //WHEN
        this.match.scores(FIRST_PLAYER);

        //THEN
        Player setWinner = this.match.getWinner().orElse(new Player("noOneYet"));
        Player tieBreakWinner = tieBreak.getWinner().orElse(new Player("noOneYet"));
        Assert.assertEquals(FIRST_PLAYER,setWinner);
        Assert.assertEquals(FIRST_PLAYER,tieBreakWinner);
    }
}
