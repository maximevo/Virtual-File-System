package VFS_core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class DirectoryNodeTest {

	@Test
	public void testGetSize() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1

		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_1, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_3, "f1_1", new VFSFile(array));
		assertTrue(dnode1_1.getSize() == 2 );
	}

	@Test
	public void testAddNodeNodeArray() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1
		
		DirectoryNode dnode2_1_1 = new DirectoryNode(null, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(null, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(null, "2_1_3");
		Node[] n = new Node[]{dnode2_1_1, dnode2_1_2, dnode2_1_3};
		
		dnode1_1.addNode(n);
		assertTrue(dnode1_1.sonNodes.size() == 3 && dnode2_1_1.getParentDirectory().getName().equals ("1_1")
				&& dnode2_1_2.getParentDirectory().getName().equals ("1_1")
				&& dnode2_1_3.getParentDirectory().getName().equals ("1_1"));
	}
	
	@Test (expected = SameNameException.class)
	public void testAddNodeNodeArraybis() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1
		
		DirectoryNode dnode2_1_1 = new DirectoryNode(null, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(null, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(null, "2_1_2");
		Node[] n = new Node[]{dnode2_1_1, dnode2_1_2, dnode2_1_3};
		
		dnode1_1.addNode(n);
		
	}

	@Test
	public void testAddNodeNode() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1
		DirectoryNode dnode2_1_1 = new DirectoryNode(null, "2_1_1");
		dnode1_1.addNode(dnode2_1_1);
		assertTrue(dnode1_1.sonNodes.size() == 1 && dnode2_1_1.getParentDirectory().getName().equals ("1_1"));
	}
	
	@Test(expected = SameNameException.class)
	public void testAddNodeNodebis() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1
		DirectoryNode dnode2_1_1 = new DirectoryNode(null, "2_1_1");
		DirectoryNode dnode2_1_2 = new DirectoryNode(null, "2_1_1");
		dnode1_1.addNode(dnode2_1_1);
		dnode1_1.addNode(dnode2_1_2);
		
	}

	@Test
	public void testRemoveNodeNodeArray() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1

		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		Node[] n = new Node[]{dnode2_1_1, dnode2_1_2, dnode2_1_3};
		dnode1_1.removeNode(n);
		
		assertTrue(dnode1_1.sonNodes.size() == 0);
	}

	@Test
	public void testRemoveNodeNode() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1
		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");
		dnode1_1.removeNode(dnode2_1_1);
		assertTrue(dnode1_1.sonNodes.size() == 0);
		
	}

	@Test
	public void testSearchFile() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1

		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_1, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_3, "f1_1", new VFSFile(array));
		
		ArrayList<FileNode> f = dnode1_1.searchFile("f1_1");
		assertTrue(f.size() == 1 && f.get(0).getName().equals("f1_1"));
		
		
	}

	@Test
	public void testSearchDirectory() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1

		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_1, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_3, "f1_1", new VFSFile(array));
		
		ArrayList<DirectoryNode> d = dnode1_1.searchDirectory("2_1_2");
		assertTrue(d.size() == 1 && d.get(0).getName().equals("2_1_2"));
	}

	@Test
	public void testSearch() throws SameNameException {
		DirectoryNode dnode1_1 = new DirectoryNode(null, "1_1");//level1 (just under root), number1

		DirectoryNode dnode2_1_1 = new DirectoryNode(dnode1_1, "2_1_1");// level 2, in dnode of upper level (level1) number 1, number 1 
		DirectoryNode dnode2_1_2 = new DirectoryNode(dnode1_1, "2_1_2");//level 2, in dnode of upper level (level1) number 1, number 2 
		DirectoryNode dnode2_1_3 = new DirectoryNode(dnode1_1, "2_1_3");
		DirectoryNode dnode2_2_1 = new DirectoryNode(dnode1_1, "2_2_1");// level 2, in dnode of upper level (level1) number 2, 
		byte[] array = new byte[] {(byte)1, (byte)2};
		FileNode fnodelevel1_1 = new FileNode(dnode2_1_3, "f1_1", new VFSFile(array));
		
		ArrayList<Node> d = dnode1_1.search("2_1_2");
		ArrayList<Node> f = dnode1_1.search("f1_1");
		assertTrue( d.size() == 1 && d.get(0).getName().equals("2_1_2") &&
				f.size() == 1 && f.get(0).getName().equals("f1_1") );
	}

}
