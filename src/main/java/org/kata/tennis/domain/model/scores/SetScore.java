package org.kata.tennis.domain.model.scores;

public enum SetScore {
	ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);

	private int value;

	SetScore(int value) {
		this.value = value;
	}

	public SetScore next() {
		if (ordinal() == values().length - 1) {
			return this;
		}
		return values()[ordinal() + 1];
	}

	public SetScore previous() {
		if (ordinal() == 0) {
			return this;
		}
		return values()[ordinal() - 1];
	}

	public int getValue() {
		return value;
	}
}
