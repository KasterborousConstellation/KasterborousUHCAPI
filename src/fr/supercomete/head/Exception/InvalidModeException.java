package fr.supercomete.head.Exception;

public class InvalidModeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3629547882651569328L;

	public InvalidModeException() {
		
	}

	public InvalidModeException(String arg0) {
		super(arg0);
	}

	public InvalidModeException(Throwable arg0) {
		super(arg0);
	}

	public InvalidModeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidModeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
