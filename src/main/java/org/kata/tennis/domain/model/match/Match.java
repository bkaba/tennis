package org.kata.tennis.domain.model.match;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.player.Player;
import org.kata.tennis.domain.model.scores.SetScore;
import org.kata.tennis.domain.model.set.Set;
import org.kata.tennis.domain.model.tiebreak.TieBreak;

public class Match {

	private Set set;
	private Player winner;

	public Match(Player firstPlayer, Player secondPlayer) {
		this.set = new Set(firstPlayer, secondPlayer);
	}
	
	/**
	 * Le joeur player marque
	 */
	public void scores(Player player) throws UnknownPlayerException {
		if (Objects.isNull(winner)) {
			this.set.scores(player);
			if (isMatchWinnable(player)) {
				endMatch(player);
			}
		}
	}

	public void setSet(Set set) {
		this.set = set;
	}

	public Optional<Player> getWinner() {
		return Optional.ofNullable(winner);
	}
	
	/**
	 * Le match est il gagné
	 */
	private boolean isMatchWinnable(Player player) {
		SetScore lastSetScore = getLastSetScore(player);
		Player setWinner = this.set.getWinner().orElse(null);
		AtomicReference<Player> tieBreakWinner = new AtomicReference<>();
		this.set.getTieBreak().flatMap(TieBreak::getWinner).ifPresent(tieBreakWinner::set);

		return (lastSetScore.equals(SetScore.SEVEN) && Objects.isNull(setWinner))
				|| tieBreakWinner.get().equals(player);
	}

	private SetScore getLastSetScore(Player player) {
		return this.set.getPlayerSetScores(player).get(this.set.getPlayerSetScores(player).size() - 1);
	}

	/**
	 * Finit le match. Le joeur player remporte le match
	 */
	private void endMatch(Player player) {
		this.winner = player;
	}
}
