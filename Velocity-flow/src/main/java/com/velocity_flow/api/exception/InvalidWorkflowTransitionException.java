package com.velocity_flow.api.exception;

public class InvalidWorkflowTransitionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public InvalidWorkflowTransitionException(String message) {
        super(message);
    }

    public InvalidWorkflowTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
