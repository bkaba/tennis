package org.kata.tennis.domain.model.game;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.tennis.domain.model.scores.GameScore;

import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class GameScoreTest {

	@Test
	@Parameters({ "ZERO,FIFTEEN,false" })
	public void scoreShouldIncrementToFifteen(GameScore given, GameScore expected, boolean deuce) {
		// GIVEN SCORE ZERO AND DEUCE DEACTIVATED
		if (deuce) {
			given.activateDeuce();
		}
		// WHEN SCORE INCREMENT
		GameScore result = given.next();
		// THEN RESULT SHOULD BE FIFTEEN
		Assert.assertThat(result, is(expected));
	}

	@Test
	@Parameters({ "THIRTY,FIFTEEN,false" })
	public void scoreShoulDecrementToFifteen(GameScore given, GameScore expected, boolean deuce) {
		// GIVEN SCORE THIRTY AND DEUCE DEACTIVATED
		if (deuce) {
			given.activateDeuce();
		}
		// WHEN SCORE DECREMENT
		GameScore result = given.previous();
		// THEN
		Assert.assertThat(result, is(expected));
	}

	@Test
	@Parameters({ 
		"FIFTEEN,THIRTY,false",
		"THIRTY,FORTY,false",
		"ADVANTAGE,ADVANTAGE,false",
		"DEUCE,ADVANTAGE,true" })
	public void scoreShouldIncrementToNext(GameScore given, GameScore expected, boolean deuce) {
		// GIVEN SCORE ZERO AND DEUCE DEACTIVATED
		if (deuce) {
			given.activateDeuce();
		}
		// WHEN SCORE INCREMENT
		GameScore result = given.next();
		// THEN RESULT SHOULD BE FIFTEEN
		Assert.assertThat(result, is(expected));
	}

	@Test
	@Parameters({ 
		"ZERO,ZERO,false",
		"FORTY,THIRTY,false",
		"ADVANTAGE,ADVANTAGE,false",
		"DEUCE,FORTY,true" })
	public void scoreSouldDecrementToPrevious(GameScore given, GameScore expected, boolean deuce) {
		// GIVEN
		if (deuce) {
			given.activateDeuce();
		}
		// WHEN
		GameScore result = given.previous();
		// THEN
		Assert.assertThat(result, is(expected));
	}

}
