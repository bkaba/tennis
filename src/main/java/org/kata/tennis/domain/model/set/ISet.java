package org.kata.tennis.domain.model.set;

import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.player.Player;

public interface ISet {
	
	public void scores(Player player) throws UnknownPlayerException; 
	
}