package org.kata.tennis.domain.model.exceptions;

public class UnknownPlayerException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownPlayerException() {
        super("UnknownPlayerException");
    }


}
