package org.kata.tennis.domain.model.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.player.Player;
import org.kata.tennis.domain.model.rules.GameRulesManager;
import org.kata.tennis.domain.model.scores.GameScore;


public class Game implements IGame {
	
    private Map<Player, List<GameScore>> gameScores;
    private Player winnerPlayer;
    GameRulesManager rulesManager = new GameRulesManager(this);
    public Game(Player firstPlayer, Player secondPlayer) {
        this.gameScores = new HashMap<>();
        this.gameScores.put(firstPlayer, new ArrayList<>(Collections.singletonList(GameScore.ZERO)));
        this.gameScores.put(secondPlayer, new ArrayList<>(Collections.singletonList(GameScore.ZERO)));
    }
    @Override
	public void scores(Player player) throws UnknownPlayerException {
	       if (Objects.isNull(this.winnerPlayer)) {
	            List<GameScore> gameScores = Optional.ofNullable(this.gameScores.get(player))
	                    .orElseThrow(UnknownPlayerException::new);

	            if (rulesManager.isGameWinnable(rulesManager.getLastGameScore(gameScores))) {
	            	rulesManager.endGame(player);

	            } else if (rulesManager.shouldWeResetScoreToDeuce(player)) {
	                this.gameScores.forEach((k, v) -> {
	                    GameScore gameScore = GameScore.DEUCE;
	                    gameScore.activateDeuce();
	                    v.add(gameScore);
	                });
	            } else {
	                gameScores.add(rulesManager.getLastGameScore(gameScores).next());
	                this.gameScores.replace(player, gameScores);
	                if (rulesManager.shouldWeReturnFromDeuceScore()) {
	                    this.gameScores.entrySet().stream()
	                            .filter(o -> !o.getKey().equals(player))
	                            .forEach(o -> o.getValue().add(rulesManager.getLastGameScore(o.getValue()).previous()));
	                } else {
	                    this.gameScores.entrySet().stream()
	                            .filter(o -> !o.getKey().equals(player))
	                            .forEach(o -> o.getValue().add(rulesManager.getLastGameScore(o.getValue())));
	                }
	            }
	            rulesManager.activeDeuceIfNecessary();
	        }
		
	}
	

    public Player getWinnerPlayer() {
		return winnerPlayer;
	}
	public void setWinnerPlayer(Player winnerPlayer) {
		this.winnerPlayer = winnerPlayer;
	}
	public Optional<Player> getWinner() {
        return Optional.ofNullable(winnerPlayer);
    }

    public void SetPlayerScore(Player player, List<GameScore> gameScores) {
        this.gameScores.replace(player, gameScores);
    }

    public List<GameScore> getPlayerScores(Player player) {
        return Optional.ofNullable(this.gameScores.get(player))
                .orElse(new ArrayList<>());
    }

    public Map<Player, List<GameScore>> getGameScores() {
		return gameScores;
	}
	public  void setPlayerGameScore(Player player, List<GameScore> gameScores) {
        this.gameScores.replace(player, gameScores);
    }
    

}
