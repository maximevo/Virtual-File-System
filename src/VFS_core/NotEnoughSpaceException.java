package VFS_core;

public class NotEnoughSpaceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5898193073569328616L;

	public NotEnoughSpaceException(){
		super("Not enough space in the Virtual Disc");
	}

}
