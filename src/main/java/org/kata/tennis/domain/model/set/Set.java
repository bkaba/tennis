package org.kata.tennis.domain.model.set;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.game.Game;
import org.kata.tennis.domain.model.game.IGame;
import org.kata.tennis.domain.model.player.Player;
import org.kata.tennis.domain.model.rules.SetRulesManager;
import org.kata.tennis.domain.model.scores.SetScore;
import org.kata.tennis.domain.model.tiebreak.TieBreak;

public class Set implements IGame {
    private Map<Player, List<SetScore>> setScores;
    private List<Game> games;
    private Player winner;
    private TieBreak tieBreak;
    private SetRulesManager rulesManager = new SetRulesManager(this);

    public Map<Player, List<SetScore>> getSetScores() {
		return setScores;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Set(Player firstPlayer, Player secondPlayer) {
        this.setScores = new HashMap<>();
        this.setScores.put(firstPlayer, new ArrayList<>(Collections.singletonList(SetScore.ZERO)));
        this.setScores.put(secondPlayer, new ArrayList<>(Collections.singletonList(SetScore.ZERO)));
        this.games = new ArrayList<>(Collections.singletonList(new Game(firstPlayer, secondPlayer)));
    }
    
    @Override
    public void scores(Player player) throws UnknownPlayerException {
        if (Objects.nonNull(tieBreak)) {
        	rulesManager.manageSetScoreWhenTieBreakRuleEnabled(player);
        } else {
        	rulesManager.manageSetWhenTieBreakRuleDisabled(player);
            rulesManager.enableTieBreakRuleIfNecessary();
        }
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public void setPlayerSetScores(Player player, List<SetScore> setScores) {
        this.setScores.replace(player, setScores);
    }

    public List<SetScore> getPlayerSetScores(Player player) {
        return Optional.ofNullable(this.setScores.get(player)).orElse(new ArrayList<>());
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public Optional<TieBreak> getTieBreak() {
        return Optional.ofNullable(tieBreak);
    }

    public void setTieBreak(TieBreak tieBreak) {
        this.tieBreak = tieBreak;
    }
}
