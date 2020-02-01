package org.kata.tennis.domain.model.tiebreak;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class TieBreakTest {

    private static final Player FIRST_PLAYER = new Player("firstPlayer");
    private static final Player SECOND_PLAYER = new Player("secondPlayer");
    private TieBreak tieBreak;

    @Before
    public void init() {
        this.tieBreak = new TieBreak(FIRST_PLAYER, SECOND_PLAYER);
    }

    @Test
    public void tie_break_score_should_increment_after_scoring() throws UnknownPlayerException {
        //WHEN
        this.tieBreak.scores(FIRST_PLAYER);
        //
        int score = getLastScore(this.tieBreak.getPlayerTieBreakScores(FIRST_PLAYER));
        Assert.assertEquals(1, score);
    }


    /*
     * player should win tie break if he gets at least 7 points and 2 points more than his opponent
     */
    @Test
    @Parameters({
            "6,2,true",
            "8,3,true",
            "6,6,false"
    })
    public void player_should_win_if_he_respect_criteria(int playerOneScore, int playerTwoScore, boolean shouldWin) throws UnknownPlayerException {

        //GIVEN
        this.tieBreak.setPlayerTieBreakScores(FIRST_PLAYER, new ArrayList<>(Collections.singletonList(playerOneScore)));
        this.tieBreak.setPlayerTieBreakScores(SECOND_PLAYER, new ArrayList<>(Collections.singletonList(playerTwoScore)));

        //WHEN
        this.tieBreak.scores(FIRST_PLAYER);

        //THEN
        Player winner = this.tieBreak.getWinner().orElse(new Player("noOneYet"));
        boolean heIsTheWinner = winner.equals(FIRST_PLAYER);
        Assert.assertEquals(shouldWin, heIsTheWinner);

    }

    private int getLastScore(List<Integer> scores) {
        return scores.get(scores.size() - 1);
    }
}
