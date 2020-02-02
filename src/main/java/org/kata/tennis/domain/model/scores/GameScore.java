package org.kata.tennis.domain.model.scores;

public enum GameScore {

	ZERO("0"), FIFTEEN("15"), THIRTY("30"), FORTY("40"), ADVANTAGE("ADVANTAGE"), DEUCE("DEUCE");

	private boolean deuce;

	GameScore(String value) {
	}

	public GameScore next() {
		if (isDeuceActivated()) {
			if (this.equals(FORTY) || this.equals(DEUCE)) {
				GameScore gameScore = ADVANTAGE;
				gameScore.activateDeuce();
				return gameScore;
			}
			return this;
		} else {
			if (ordinal() == values().length - 1 || this.equals(GameScore.ADVANTAGE) || this.equals(GameScore.DEUCE))
				return this;
			return values()[ordinal() + 1];
		}
	}

	public GameScore previous() {
		if (isDeuceActivated()) {
			if (this.equals(DEUCE)) {
				GameScore gameScore = FORTY;
				gameScore.activateDeuce();
				return gameScore;
			}
			return this;
		} else {
			if (ordinal() == 0 || this.equals(GameScore.ADVANTAGE) || this.equals(GameScore.DEUCE))
				return this;
			return values()[ordinal() - 1];
		}
	}

	public void activateDeuce() {
		this.deuce = true;
	}

	public void deactivateDeuce() {
		this.deuce = false;
	}

	public boolean isDeuceActivated() {
		return this.deuce;
	}

}
