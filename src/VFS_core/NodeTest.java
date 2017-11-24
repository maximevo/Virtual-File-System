package VFS_core;

import static org.junit.Assert.*;

import java.io.File;

import javax.swing.JPopupMenu.Separator;

import org.junit.Test;


public class NodeTest {
	
	/*public DirectoryNode instantiateTestFile(){
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnodelevel1_1 = new DirectoryNode(vd.root, "1_1");
		DirectoryNode dnodelevel1_2 = new DirectoryNode(vd.root, "1_2");
		DirectoryNode dnodelevel2_1 = new DirectoryNode(dnodelevel1_1, "2_1");
		DirectoryNode dnodelevel2_2 = new DirectoryNode(dnodelevel1_1, "2_2");
		DirectoryNode dnodelevel2_3 = new DirectoryNode(dnodelevel1_1, "2_3");
		// importer 3 fichiers dès que la methode import est implementee! Cela créera automatiquement les NodeFile associés 
		return dnodelevel1_1;
	}
	*/

	@Test
	public void testGetPath() throws SameNameException {
		//DirectoryNode node = instantiateTestVirtualDisk();
		DirectoryNode dnodelevel1_1 = new DirectoryNode(null, "1_1");
		DirectoryNode dnodelevel2_1 = new DirectoryNode(dnodelevel1_1, "2_1");
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(dnodelevel1_1, "f1_1", new VFSFile(array));
		// expected vs result 
		String result1 = dnodelevel2_1.getPath();
		String result2 = fnodelevel1_1.getPath();
		String expected1 = "1_1"+File.separator+"2_1";
		String expected2 ="1_1"+File.separator+"f1_1";
		assertTrue(result1.equals(expected1) && result2.equals(expected2));
		}

}
