package org.kata.tennis.domain.model.set;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.game.Game;
import org.kata.tennis.domain.model.player.Player;
import org.kata.tennis.domain.model.scores.GameScore;
import org.kata.tennis.domain.model.scores.SetScore;
import org.kata.tennis.domain.model.tiebreak.TieBreak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SetTest {

    private static final Player FIRST_PLAYER =  new Player("firstPlayer");
    private static final Player SECOND_PLAYER = new Player("secondPlayer");
    private Set set;

    @Before
    public void init() {
        this.set = new Set(FIRST_PLAYER, SECOND_PLAYER);
    }


    /*
     * player should win the set  if he got score 6 against 4 or lower
     */
    @Test
    public void firstPlayerShouldWinSet() throws UnknownPlayerException {
        //GIVEN
        Game game = new Game(FIRST_PLAYER, SECOND_PLAYER);
        game.setPlayerGameScore(FIRST_PLAYER, new ArrayList<>(Collections.singletonList(GameScore.FORTY)));
        game.setPlayerGameScore(SECOND_PLAYER, new ArrayList<>(Collections.singletonList(GameScore.FIFTEEN)));
        List<Game> games = new ArrayList<>(Collections.singletonList(game));
        this.set.setGames(games);
        this.set.setPlayerSetScores(FIRST_PLAYER, new ArrayList<>(Collections.singletonList(SetScore.FIVE)));
        this.set.setPlayerSetScores(SECOND_PLAYER, new ArrayList<>(Collections.singletonList(SetScore.FOUR)));

        //WHEN
        this.set.scores(FIRST_PLAYER);

        //THEN
        Assert.assertEquals(FIRST_PLAYER, this.set.getWinner().orElse(new Player("NoOneYet")));
    }
    /*
     * player should play a new game if the score is 6 against 5
     */
    @Test
    public void playersShouldPlayNewGameAndNoOneWinTheSet() throws UnknownPlayerException {
        //GIVEN
        Game game = new Game(FIRST_PLAYER, SECOND_PLAYER);
        game.setPlayerGameScore(FIRST_PLAYER, new ArrayList<>(Collections.singletonList(GameScore.FORTY)));
        game.setPlayerGameScore(SECOND_PLAYER, new ArrayList<>(Collections.singletonList(GameScore.FIFTEEN)));
        List<Game> games = new ArrayList<>(Collections.singletonList(game));
        this.set.setGames(games);
        this.set.setPlayerSetScores(FIRST_PLAYER, new ArrayList<>(Collections.singletonList(SetScore.FIVE)));
        this.set.setPlayerSetScores(SECOND_PLAYER, new ArrayList<>(Collections.singletonList(SetScore.FIVE)));

        //WHEN
        this.set.scores(FIRST_PLAYER);

        //THEN
        final Player winner = this.set.getWinner().orElse(null);
        Assert.assertNull(winner);
        Assert.assertEquals(2, this.set.getGames().size());
    }


    @Test
    public void playerShouldWinSetIfHeWinTheTieBreak() throws UnknownPlayerException {
        //GIVEN
        TieBreak tieBreak = new TieBreak(FIRST_PLAYER, SECOND_PLAYER);
        tieBreak.setPlayerTieBreakScores(FIRST_PLAYER,new ArrayList<>(Collections.singletonList(6)));
        tieBreak.setPlayerTieBreakScores(SECOND_PLAYER,new ArrayList<>(Collections.singletonList(2)));
        this.set.setTieBreak(tieBreak);

        //WHEN
        this.set.scores(FIRST_PLAYER);

        //THEN
        Player setWinner = this.set.getWinner().orElse(new Player("noOneYet"));
        Player tieBreakWinner = tieBreak.getWinner().orElse(new Player("noOneYet"));
        Assert.assertEquals(FIRST_PLAYER,setWinner);
        Assert.assertEquals(FIRST_PLAYER,tieBreakWinner);
    }
}
