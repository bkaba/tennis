package org.kata.tennis.domain.model.tiebreak;

import java.util.*;
import java.util.stream.Collectors;

import org.kata.tennis.domain.model.exceptions.UnknownPlayerException;
import org.kata.tennis.domain.model.player.Player;

public class TieBreak {
    private Map<Player, List<Integer>> tieBreakScores;
    private Player winner;

    public TieBreak(Player firstPlayer, Player secondPlayer) {
        this.tieBreakScores = new HashMap<>();
        this.tieBreakScores.put(firstPlayer, new ArrayList<>(Collections.singletonList(0)));
        this.tieBreakScores.put(secondPlayer, new ArrayList<>(Collections.singletonList(0)));
    }

    public void scores(Player player) throws UnknownPlayerException {
        if (Objects.isNull(winner)) {
            final List<Integer> scores = Optional.ofNullable(this.tieBreakScores.get(player)).orElseThrow(UnknownPlayerException::new);
            incrementScore(player, scores);
            if (isTieBreakWinnable(player)) {
                endTieBreak(player);
            }
        }

    }

    private void incrementScore(Player player, List<Integer> scores) {
        scores.add(getLastScore(scores) + 1);
        this.tieBreakScores.replace(player, scores);
        this.tieBreakScores.entrySet().stream()
                .filter(o -> !o.getKey().equals(player))
                .forEach(o -> o.getValue().add(getLastScore(o.getValue())));
    }

    private int getLastScore(List<Integer> scores) {
        return scores.get(scores.size() - 1);
    }

    private boolean isTieBreakWinnable(Player player) {
        final int playerOneScore = getLastScore(this.tieBreakScores.get(player));
        final int playerTwoScore = getLastScore(
                this.tieBreakScores.entrySet()
                        .stream()
                        .filter(o -> !o.getKey().equals(player)).map(Map.Entry::getValue)
                        .flatMap(Collection::stream).collect(Collectors.toList())
        );
        return playerOneScore >= 7 && (playerOneScore - playerTwoScore) >= 2;
    }

    private void endTieBreak(Player player) {
        this.winner = player;
    }

    public List<Integer> getPlayerTieBreakScores(Player player) {
        return Optional.ofNullable(this.tieBreakScores.get(player)).orElse(new ArrayList<>());
    }

    public void setPlayerTieBreakScores(Player player, List<Integer> scores) {
        this.tieBreakScores.replace(player, scores);
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }
}
