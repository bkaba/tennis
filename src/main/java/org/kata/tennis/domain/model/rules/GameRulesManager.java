package org.kata.tennis.domain.model.rules;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kata.tennis.domain.model.game.Game;
import org.kata.tennis.domain.model.player.Player;
import org.kata.tennis.domain.model.scores.GameScore;

public class GameRulesManager {

	private Game game;

	public GameRulesManager(Game game) {
		super();
		this.game = game;
	}

	/**
	 * On sort du deuce
	 */
	public boolean shouldWeReturnFromDeuceScore() {
		return game.getGameScores().values().stream().filter(
				o -> getLastGameScore(o).equals(GameScore.ADVANTAGE) || getLastGameScore(o).equals(GameScore.DEUCE))
				.count() == 2;
	}

	public GameScore getLastGameScore(List<GameScore> gameScores) {
		return gameScores.get(gameScores.size() - 1);
	}

	/**
	 * Si les 2 joeurs ont chacun 40, on active le DEUCE
	 */
	public void activeDeuceIfNecessary() {
		final List<GameScore> gameScores = game.getGameScores().values().stream().map(this::getLastGameScore)
				.filter(o -> o.equals(GameScore.FORTY)).collect(Collectors.toList());
		if (gameScores.size() == 2) {
			game.getGameScores().forEach((key, value) -> getLastGameScore(value).activateDeuce());
		}
	}
	
	/**
	 * Si le joueur marquant le point à 40 et l'adversaire à AVANTAGE
	 */
	public boolean shouldWeResetScoreToDeuce(Player player) {
		final GameScore scorer = this.getLastGameScore(game.getPlayerScores(player));
		final GameScore secondPlayer = getLastGameScore(
				game.getGameScores().entrySet().stream().filter(o -> !o.getKey().equals(player))
						.map(Map.Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList()));
		return scorer.equals(GameScore.FORTY) && secondPlayer.equals(GameScore.ADVANTAGE);
	}

	public boolean isGameWinnable(GameScore score) {
		return (score.equals(GameScore.FORTY) && !score.isDeuceActivated()) || score.equals(GameScore.ADVANTAGE);
	}

	/**
	 * Le jeu est gagné par le joueur player
	 */
	public void endGame(Player player) {
		game.setWinnerPlayer(player);
		game.getGameScores().forEach((key, value) -> value.add(GameScore.ZERO));
	}

}
