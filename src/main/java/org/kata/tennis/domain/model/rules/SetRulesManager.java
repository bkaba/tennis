package org.kata.tennis.domain.model.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.game.Game;
import org.kata.tennis.domain.model.player.Player;
import org.kata.tennis.domain.model.scores.SetScore;
import org.kata.tennis.domain.model.set.Set;
import org.kata.tennis.domain.model.tiebreak.TieBreak;

/**
 * @author bkaba
 *
 */
public class SetRulesManager {

	private Set set;

	public SetRulesManager(Set set) {
		super();
		this.set = set;
	}
	
	/**
	 * Incremente le nombre de sets d'unjoueur
	 */
	public void incrementSetScore(Player player) {
		set.getSetScores().get(player).add(getLastSetScore(set.getSetScores().get(player)).next());
		set.getSetScores().entrySet().stream().filter(o -> !o.getKey().equals(player))
				.forEach(o -> o.getValue().add(getLastSetScore(o.getValue())));
	}

	/**
	 * Renvoit le dernier set de la liste
	 */
	public SetScore getLastSetScore(List<SetScore> setScores) {
		return setScores.get(setScores.size() - 1);
	}

	/**
	 * Active le tiebreak si necessaire (condition: les 2 joueurs ont atteint le score de 6 en sets)
	 */
	public void enableTieBreakRuleIfNecessary() {
		final boolean bothPlayerReachedSix = set.getSetScores().values().stream().map(this::getLastSetScore)
				.filter(o -> o.equals(SetScore.SIX)).count() == 2;
		if (bothPlayerReachedSix) {
			List<Player> players = new ArrayList<>(set.getSetScores().keySet());
			set.setTieBreak(new TieBreak(players.get(0), players.get(1)));
		}
	}

	/**
	 * Un nouveau jeu est à faire
	 */
	public boolean shouldPlayNewGame(Player scorer) throws UnknownPlayerException {
		final SetScore firstPlayerSetScore = getLastSetScore(
				Optional.ofNullable(set.getSetScores().get(scorer)).orElseThrow(UnknownPlayerException::new));
		final SetScore secondPlayerSetScore = getLastSetScore(
				set.getSetScores().entrySet().stream().filter(o -> !o.getKey().equals(scorer)).map(Map.Entry::getValue)
						.flatMap(Collection::stream).collect(Collectors.toList()));
		return firstPlayerSetScore.equals(SetScore.SIX) && secondPlayerSetScore.equals(SetScore.FIVE);
	}

	/**
	 * Gère le set score d'un joueur quand le tiebreak est activé  
	 */
	public void manageSetScoreWhenTieBreakRuleEnabled(Player player) throws UnknownPlayerException {
		set.getTieBreak().get().scores(player);
		Player tieBreakWinner = set.getTieBreak().get().getWinner().orElse(null);
		if (Objects.nonNull(tieBreakWinner)) {
			incrementSetScore(player);
			endSet(player);
		}
	}

	/**
	 * Gère le set score d'un joueur quand le tiebreak n'est pas activé 
	 */
	public void manageSetWhenTieBreakRuleDisabled(Player player) throws UnknownPlayerException {
		getLastGame().scores(player);
		boolean isSetScoreIncremented = isSetScoreIncremented(player);
		if (isSetWinnable(player)) {
			endSet(player);
		} else if (shouldPlayNewGame(player) || isSetScoreIncremented) {
			createNewGame();
		}
	}
	
	/**
	 * Crée un nouveau jeu entre 2 joueurs
	 */
	private void createNewGame() {
		List<Player> players = new ArrayList<>(set.getSetScores().keySet());
		set.getGames().add(new Game(players.get(0), players.get(1)));
	}

	
	private boolean isSetScoreIncremented(Player player) {
		AtomicBoolean isSetScoreIncremented = new AtomicBoolean(false);
		final Optional<Player> winner = this.getLastGame().getWinner();
		winner.ifPresent(w -> {
			incrementSetScore(player);
			isSetScoreIncremented.set(true);
		});
		return isSetScoreIncremented.get();
	}

	/**
	 * Renvoie le dernier jeu 
	 */
	public Game getLastGame() {
		return set.getGames().get(set.getGames().size() - 1);
	}

	/**
	 * Le joueur player gagne le set 
	 */
	
	private void endSet(Player player) {
		set.setWinner(player);
	}

	/**
	 * Renvoit true si le set est gagnant par le joeur qui score 
	 * (Condition: il doit avoir 6 sets contre au max 4 sets pour son adversaire)
	 */
	private boolean isSetWinnable(Player scorer) throws UnknownPlayerException {
		final SetScore firstPlayerSetScore = getLastSetScore(
				Optional.ofNullable(set.getSetScores().get(scorer)).orElseThrow(UnknownPlayerException::new));
		final SetScore secondPlayerSetScore = getLastSetScore(
				set.getSetScores().entrySet().stream().filter(o -> !o.getKey().equals(scorer)).map(Map.Entry::getValue)
						.flatMap(Collection::stream).collect(Collectors.toList()));

		return firstPlayerSetScore.equals(SetScore.SIX) && secondPlayerSetScore.getValue() <= 4;
	}

}
