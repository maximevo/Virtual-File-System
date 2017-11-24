package VFS_core;

import java.io.File;
import java.io.FileNotFoundException;


public class Brouillon {

	public static void main (String args []) throws SameNameException, FileNotFoundException, NotEnoughSpaceException {
		String s = "a;b";
		String [] t = s.split(";");
		for (String c : t) {
			System.out.println(c);
		}
			
	}		
}
