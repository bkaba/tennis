package org.kata.tennis.domain.model.game;

import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.player.Player;

public interface IGame {

	public void scores(Player player) throws UnknownPlayerException;

}