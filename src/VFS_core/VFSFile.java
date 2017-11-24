package VFS_core;

import java.io.Serializable;


public class VFSFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7150183845158320751L;
	private byte [] file;

	public VFSFile(byte [] array ) {
		super();
		file = array;
	}
	
	public byte [] getFile() {
		return file;
	}
	
	public int getSize() {
	return file.length;
	}

}
