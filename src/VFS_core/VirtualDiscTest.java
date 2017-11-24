package VFS_core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

public class VirtualDiscTest {

	/*public VirtualDisc instantiateTestVirtualDisk(){
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_1, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(vd.root, "f1_1", new VFSFile(array));
		System.out.println(fnodelevel1_1.getPath());
		// importer 3 fichiers dès que la methode import est implementee! Cela créera automatiquement les NodeFile associés 
		return vd;
	}
	*/
	
	// On commence par tester testPathToFile qui sont appelées par les autres méthodes
	
	@Test
	public void testFreeSpace() throws SameNameException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode1_1, "f1_1", new VFSFile(array));
		assertTrue(vd.freeSpace() == 7);
	}
	

	@Test 
	public void testPathToFile() throws FileNotFoundException, SameNameException  {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(vd.root, "f1_1", new VFSFile(array));
		FileNode result = vd.pathToFile(fnodelevel1_1.getPath());
		FileNode expected = fnodelevel1_1;
		assertTrue(result.equals(expected));
		
		
	}
	@Test(expected = FileNotFoundException.class)
	public void testPathToFilebis() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(vd.root, "f1_1", new VFSFile(array));
		
		// on met volontairement une mauvaise adresse: on doit lever une exception file not found!
		FileNode result = vd.pathToFile(fnodelevel1_1.getPath()+fnodelevel1_1.getPath());
		
	}
	
	@Test
	public void testPathToDirectory() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		DirectoryNode result = vd.pathToDirectory(dnode2_1_1.getPath());
		DirectoryNode expected = dnode2_1_1;
		assertTrue(result.equals(expected));
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testPathToDirectorybis() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		DirectoryNode result = vd.pathToDirectory(dnode2_1_1.getPath()+dnode2_1_1.getPath());
		
	}
	
	@Test
	public void TestSearchFile() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode1_1, "f1_1", new VFSFile(array));
		
		ArrayList<FileNode> a = vd.searchFile(vd.root.getPath(), "f1_1");
		assertTrue( a.size() == 1 && a.get(0).getName().equals("f1_1") );
	}
	
	@Test
	public void TestSearchFilebis() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		
		ArrayList<FileNode> a = vd.searchFile(dnode1_1.getPath(), "f1_1");
		assertTrue( a.size() == 1 && a.get(0).getName().equals("f1_1") );
	}
	
	@Test
	public void TestSearchFileter() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		
		ArrayList<FileNode> a = vd.searchFile(dnode1_2.getPath(), "f1_1");
		assertTrue( a.size() == 0 );
	}
	
	@Test(expected = FileNotFoundException.class)
	public void TestSearchFilefour() throws SameNameException, FileNotFoundException {
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
				
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode1_1, "f1_1", new VFSFile(array));
		// We give a wrong pathname: FileNotFoundException is raised! 
		vd.searchFile("vdlkk", "f1_1");
		
	}
	
	@Test
	public void TestSearchDirectory() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode1_1, "f1_1", new VFSFile(array));
		
		ArrayList<DirectoryNode> a = vd.searchDirectory(vd.root.getPath(), "2_1_2");
		assertTrue( a.size() == 1 && a.get(0).getName().equals("2_1_2") );
	}
	
	@Test
	public void TestSearchDirectorybis() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		
		ArrayList<DirectoryNode> a = vd.searchDirectory(dnode1_1.getPath(), "2_1_2");
		assertTrue( a.size() == 1 && a.get(0).getName().equals("2_1_2") );
	}
	
	@Test
	public void TestSearchDirectoryter() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		
		ArrayList<DirectoryNode> a = vd.searchDirectory(dnode1_2.getPath(), "2_1_2");
		assertTrue( a.size() == 0 );
	}
	
	@Test(expected = FileNotFoundException.class)
	public void TestSearchDirectoryfour() throws SameNameException, FileNotFoundException {
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
				
		
		
		// We give a wrong pathname: FileNotFoundException is raised! 
		vd.searchDirectory("vdlkk", "1_1");
		
	}
	
	@Test
	public void TestSearch() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode1_1, "f1_1", new VFSFile(array));
		
		ArrayList<Node> a1 = vd.search(vd.root.getPath(), "2_1_2");
		ArrayList<Node> a2 = vd.search(vd.root.getPath(), "f1_1");
		assertTrue( a1.size() == 1 && a1.get(0).getName().equals("2_1_2") &&
				a2.size() == 1 && a2.get(0).getName().equals("f1_1") );
	}
	
	@Test
	public void TestSearchbis() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		
		ArrayList<Node> a1 = vd.search(dnode1_1.getPath(), "2_1_2");
		ArrayList<Node> a2 = vd.search(dnode1_1.getPath(), "f1_1");
		assertTrue( a1.size() == 1 && a1.get(0).getName().equals("2_1_2") &&
				a2.size() == 1 && a2.get(0).getName().equals("f1_1") );
	}
	
	@Test
	public void TestSearchter() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files avec des mêmes noms
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_1_2");// level 2, in dnode of upper level (level1) number 2, 
		
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "2_1_2", new VFSFile(array));
		
		ArrayList<Node> a = vd.search(vd.root.getPath(), "2_1_2");
		assertTrue( a.size() == 3 );
	}
	
	@Test(expected = FileNotFoundException.class)
	public void TestSearchfour() throws SameNameException, FileNotFoundException {
		VirtualDisc vd = new VirtualDisc("vd",10);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
				
		
		
		// We give a wrong pathname: FileNotFoundException is raised! 
		vd.search("vdlkk", "1_1");
		
	}
	
	
	
	@Test
	public void testRenameFile() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_2, "f1_1", new VFSFile(array));
		
		// On applique la méthode à tester
		vd.renameFile(fnodelevel1_1.getPath(), "1_1renamed");
		String result = fnodelevel1_1.getName();
		String expected = "1_1renamed";
		assertTrue(result.equals(expected));
		
		
		
	}
		
	@Test (expected = SameNameException.class)
	public void testRenameFileter() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_2, "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(dnode2_1_2, "f1_2", new VFSFile(array));
		
		// On applique la méthode à tester: on veut lever l'exception SameName!
		vd.renameFile(fnodelevel1_1.getPath(), "f1_2");
		String result = fnodelevel1_1.getName();
	
	}
	@Test
	public void testRenameDirectory() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
				
		// On applique la méthode à tester
		vd.renameDirectory(dnode2_1_1.getPath(), "2_1_1RENAMED");
		String result = dnode2_1_1.getName();
		String expected = "2_1_1RENAMED";
		assertTrue(result.equals(expected));
		
	}
	@Test(expected = SameNameException.class)
	public void testRenameDirectorybis() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
				
		// On applique la méthode à tester: on veut lever l'exception SameName!
		vd.renameDirectory(dnode2_1_1.getPath(), "2_1_2");
		String result = dnode2_1_1.getName();
		String expected = "2_1_2";
		assertTrue(result.equals(expected));
	}
	@Test
	public void testMoveDirectory() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		//On applique méthode à tester: 2.1.1 est dans le dossier 1.1 : on veut le faire passer dans 1.2 
		vd.moveDirectory(dnode2_1_1.getPath(),vd.root.getPath() );
		String result = dnode2_1_1.getPath();
		String expected = vd.root.getPath() + File.separator +  dnode2_1_1.getName();
		assertTrue(result.equals(expected));
		
	}
	@Test (expected = FileNotFoundException.class)
	public void testMoveDirectorybis() throws FileNotFoundException, SameNameException {
		
			VirtualDisc vd = new VirtualDisc("vd",10000);
			//créer nodes: directory and files
			DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
			DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
			DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
			DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
			DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
			DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
			
			//On applique méthode à tester: on veut lever exception file not found! 
			vd.moveDirectory(dnode2_1_1.getPath(),dnode2_1_2.getPath()+ "z" );
			String result = dnode2_1_1.getPath();
			
			
		
	}
	@Test
	public void testMoveFile() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_2, "f1_1", new VFSFile(array));
		
		//On applique méthode à tester: 
		vd.moveFile(fnodelevel1_1.getPath(),vd.root.getPath());
		String result = fnodelevel1_1.getPath();
		String expected = vd.root.getPath() + File.separator +  fnodelevel1_1.getName();
		assertTrue(result.equals(expected));
		
	
	}
	@Test (expected = SameNameException.class)
	public void testMoveFilebis() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {};
		// deux fichiers avec le même nom dans deux dossier différents: on va essayer de les deplacer dans le meme dossier: samenameexception! 
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(dnode2_1_2, "f1_1", new VFSFile(array));
		
		//On applique méthode à tester: 
		vd.moveFile(fnodelevel1_1.getPath(),dnode2_1_2.getPath());
		String result = fnodelevel1_1.getPath();
		String expected = vd.root.getPath() + File.separator +  fnodelevel1_1.getName();
		assertTrue(result.equals(expected));
	}
	
	@Test (expected = FileNotFoundException.class)
	public void testMoveFileter() throws FileNotFoundException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {};
		
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(dnode2_1_2, "f1_2", new VFSFile(array));
		
		//On applique méthode à tester: on va donner en paramètre le path d'un fichier au lieu d'un dossier: FileNotFound Exception!
		vd.moveFile(fnodelevel1_1.getPath(),fnodelevel1_2.getPath());
		String result = fnodelevel1_1.getPath();
		
	}
	
	@Test 
	public void testCreateDirectory() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10000);
		vd.createDirectory(vd.root.getPath(), "d1");
		
		assertTrue(vd.root.sonNodes.size() == 1 && vd.root.sonNodes.get(0).getName().equals("d1"));
		
	}
	
	@Test (expected = FileNotFoundException.class)
	public void testCreateDirectorybis() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10000);
		// we give in a path that does not exist: FileNotFoundExcpetion is raised! 
		vd.createDirectory(vd.root.getPath()+ "koqsfj", "d1");
		// on ne doit pas avoir crée de son à la root (pour cela il faut modifier la méthode path to directory)
		assertTrue(vd.root.sonNodes.size() == 0);
		
		
		
	}
	
	@Test
	public void testCopyFile() throws FileNotFoundException, NotEnoughSpaceException, SameNameException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		
		vd.copyFile(fnodelevel1_1.getPath(), vd.root.getPath());
		assertTrue((vd.root.sonNodes.get(0).getName().equals("f1_1") | vd.root.sonNodes.get(1).getName().equals("f1_1") | vd.root.sonNodes.get(2).getName().equals("f1_1") ));
		
	}
	@Test (expected = FileNotFoundException.class)
	public void testCopyFilebis() throws SameNameException, FileNotFoundException, NotEnoughSpaceException {
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		byte[] array = new byte[] {};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		
		// On met un path vers un fichier inexistant! L'excpetion FileNotFoundException est levée! 
		vd.copyFile(fnodelevel1_1.getPath() + "z", vd.root.getPath());
		
	}
	
	@Test (expected = NotEnoughSpaceException.class )
	public void testCopyFileter() throws SameNameException, FileNotFoundException, NotEnoughSpaceException {
		// on crée un virtual disque qui n'a pas la place de stocker le fichier! 
		VirtualDisc vd = new VirtualDisc("vd",2);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		byte[] array = new byte[] {(byte)1, (byte)2, (byte)1};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		
		vd.copyFile(fnodelevel1_1.getPath(), vd.root.getPath());
		
	}
	
	@Test (expected = SameNameException.class )
	public void testCopyFilefour() throws SameNameException, FileNotFoundException, NotEnoughSpaceException {
		 
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(dnode2_2_1, "f1_1", new VFSFile(array));
		// on crée un file avec un nom déjà existant
		vd.copyFile(fnodelevel1_1.getPath(), dnode2_2_1.getPath());
		
	}
	
	@Test
	public void testCopyDirectory() throws SameNameException, FileNotFoundException, NotEnoughSpaceException {
		// on crée un virtual disque qui n'a pas la place de stocker le fichier! 
		VirtualDisc vd = new VirtualDisc("vd",1);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 

		

		vd.copyDirectory(dnode2_1_2.getPath(), dnode1_2.getPath());
		assertTrue(dnode1_1.sonNodes.size() == 3 && dnode1_2.sonNodes.size() == 2 && (vd.pathToDirectory(dnode1_2.getPath() + File.separator + dnode2_1_2.getName()).getName().equals("2_1_2")));
	}
	@Test (expected = SameNameException.class)
	public void testCopyDirectorybis() throws SameNameException, FileNotFoundException, NotEnoughSpaceException {
		// on crée un virtual disque qui n'a pas la place de stocker le fichier! 
		VirtualDisc vd = new VirtualDisc("vd",1);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 



		vd.copyDirectory(dnode2_1_2.getPath(), dnode1_1.getPath());
		
	}
	
	@Test
	public void testRemoveFile() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(dnode2_2_1, "f1_2", new VFSFile(array));
		vd.removeFile(fnodelevel1_1.getPath());
		assertTrue(dnode2_1_1.sonNodes.size() == 0);
	}
	@Test(expected = FileNotFoundException.class)
	public void testRemoveFilebis() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(dnode2_2_1, "f1_2", new VFSFile(array));
		vd.removeFile(fnodelevel1_1.getPath() + "poqsfjpo");
		
	}
	
	@Test
	public void testRemoveDirectory() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(dnode2_2_1, "f1_2", new VFSFile(array));
		vd.removeDirectory(dnode1_1.getPath());
		assertTrue(vd.root.sonNodes.size() == 1);
	}
	@Test(expected = FileNotFoundException.class)
	public void testRemoveDirectorybis() throws SameNameException, FileNotFoundException{
		VirtualDisc vd = new VirtualDisc("vd",10000);
		//créer nodes: directory and files
		DirectoryNode dnode1_1 = new DirectoryNode(vd.root, "1_1");//level1 (just under root), number1
		DirectoryNode dnode1_2 = new DirectoryNode(vd.root, "1_2");//level1 (just under root), number2
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_2, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_1, "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(dnode2_2_1, "f1_2", new VFSFile(array));
		vd.removeDirectory( "poqsfjpo");
		
	}
	

}
