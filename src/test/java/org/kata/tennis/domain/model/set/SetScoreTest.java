package org.kata.tennis.domain.model.set;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.tennis.domain.model.scores.SetScore;

import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class SetScoreTest {

    @Test
    @Parameters({
            "ZERO,ONE",
            "TWO,THREE"})
    public void scoreShouldIncrementToNext(SetScore given, SetScore expected) {
        //WHEN
        SetScore result = given.next();
        //THEN
        Assert.assertThat(result, is(expected));
    }

    @Test
    @Parameters({
            "ZERO,ZERO",
            "THREE,TWO"})
    public void scoreShouldDecrementToPrevious(SetScore given, SetScore expected) {
        //WHEN
        SetScore result = given.previous();
        //THEN
        Assert.assertThat(result, is(expected));
    }
}

