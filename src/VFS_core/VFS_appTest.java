package VFS_core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class VFS_appTest {


	
	@Test
	public void testSaveVD() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VirtualDisc vd = VFS_app.getVD("VDTest");
		vd.createDirectory(vd.root.getPath(), "d1");
		VFS_app.saveVD(vd);
		VirtualDisc vd2 = VFS_app.getVD("VDTest");
		assertTrue(vd2.root.sonNodes.size() == 1 && vd2.root.sonNodes.get(0).getName().equals("d1"));
		
		
	}
	
	@Test
	public void testSaveVDbis() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VirtualDisc vd = VFS_app.getVD("VDTest");
		vd.createDirectory(vd.root.getPath(), "d1");
		// we do not save the vd: the creation of a directory should not be taken into account when we will use the "getVD" method
		VirtualDisc vd2 = VFS_app.getVD("VDTest");
		assertTrue(vd2.root.sonNodes.size() == 0);
		
		
	}
	@Test
	public void testSaveVFSmap() {
		VFS_app.resetVFSmap();
		HashMap<String,String> VFSmap = VFS_app.getVFSmap();
		VFSmap.put("name1", "path1");
		VFSmap.put("name2", "path2");
		VFSmap.put("name3", "path3");
		VFS_app.saveVFSmap(VFSmap);
		HashMap<String,String> VFSmap2 = VFS_app.getVFSmap();
		assertTrue(VFSmap2.get("name1").equals("path1") && VFSmap2.get("name2").equals("path2") && VFSmap.get("name3").equals("path3"));
	}
	
	

	@Test // this test depend on the precedent, as we deserialize the VFSmap 
	public void testResetVFSmap() {
		VFS_app.resetVFSmap();
		HashMap<String,String> VFSmap = VFS_app.getVFSmap();
		assertTrue(VFSmap.isEmpty());
	}
	
		
	
	@Test(expected = VDNotFoundException.class)
	public void testgetVD() throws VDNotFoundException{
		VFS_app.resetVFSmap();
		VFS_app.getVD("sdqdfsd");
	}
	
	@Test 
	public void testCreateVD(){
		VFS_app.resetVFSmap();
		
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createVD("VDTest2",10000, "");
		HashMap<String,String> VFSmap = VFS_app.getVFSmap();
		assertTrue(VFSmap.size() == 2 && VFSmap.containsKey("VDTest") && VFSmap.containsKey("VDTest2"));
	}
	
	
	
	@Test 
	public void testDeleteVD(){
		VFS_app.resetVFSmap();

		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createVD("VDTest2",10000, "");
		VFS_app.deleteVD("VDTest");
		HashMap<String,String> VFSmap = VFS_app.getVFSmap();
		assertTrue(!(VFSmap.containsKey("VDTest")) && VFSmap.containsKey("VDTest2"));

	}
	
	
	@Test
	public void testCreateDirectory() throws VDNotFoundException {
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.size() == 1 && VFS_app.getVD("VDTest").root.sonNodes.get(0).getName().equals("d1") );
	}
	
	@Test
	public void testRemoveDirectory() throws VDNotFoundException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.removeDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1" );
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.size() == 0);
	}
	
	@Test
	public void testRemoveFile() throws FileNotFoundException, SameNameException, VDNotFoundException {
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		
		
		byte[] array = new byte[] {(byte)1};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath()), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		VFS_app.removeFile("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "f1_1");
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.size() == 0);
	}
	
	@Test
	public void testMoveDirectory() throws VDNotFoundException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d2");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		VFS_app.moveDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d2" );
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.size() == 1);
	}
	
	@Test
	public void testMoveFile() throws VDNotFoundException, FileNotFoundException, SameNameException {
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		
		byte[] array = new byte[] {(byte)1};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		VFS_app.moveFile("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1"+ File.separator + "f1_1", VFS_app.getVD("VDTest").root.getPath());
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.size() == 2);
	}
	
	@Test
	public void testCopyDirectory() throws VDNotFoundException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1

		
		VFS_app.copyDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1" + File.separator + "d1_1", VFS_app.getVD("VDTest").root.getPath()  );
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.size() == 2 );
	}
	
	@Test
	public void testCopyFile() throws VDNotFoundException, FileNotFoundException, SameNameException {
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		
		byte[] array = new byte[] {(byte)1};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		VFS_app.copyFile("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1"+ File.separator + "f1_1", VFS_app.getVD("VDTest").root.getPath());
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.size() == 2);
	}
	
	
	@Test
	public void testRenameDirectory() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.renameDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath()+  File.separator + "d1", "d1renamed");
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.get(0).getName().equals("d1renamed"));
	}
	
	@Test
	public void testRenameFile() throws FileNotFoundException, SameNameException, VDNotFoundException {
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		
		
		byte[] array = new byte[] {(byte)1};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath()), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		VFS_app.renameFile("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "f1_1", "f1_1renamed");
		assertTrue(VFS_app.getVD("VDTest").root.sonNodes.get(0).getName().equals("f1_1renamed")) ;
	}
	
	@Test
	public void testFreeSpace() throws VDNotFoundException, FileNotFoundException, SameNameException {
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		int result = VFS_app.freespace("VDTest");
		int expected = 10000 - 2;
		assertTrue(result == expected);
	}
	
	@Test
	public void testSearchDirectory() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<DirectoryNode> a = VFS_app.searchDirectory("VDTest", "d1_1");
		assertTrue(a.size() == 1 && a.get(0).getName().equals("d1_1") && a.get(0).sonNodes.size() == 0 );
		
	}
	
	@Test
	public void testSearchDirectorybis() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_2");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<DirectoryNode> a = VFS_app.searchDirectory("VDTest", "d1_1");
		assertTrue(a.size() == 0);
		
	}
	
	@Test
	public void testSearchDirectoryter() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath()+ File.separator + "d1", "d1");// inside d1, name d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<DirectoryNode> a = VFS_app.searchDirectory("VDTest", "d1");
		assertTrue(a.size() == 2);
		
	}
	
	@Test
	public void testSearchDirectoryfour() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1" + File.separator + "d1_1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<DirectoryNode> a = VFS_app.searchDirectory("VDTest",vd.root.getPath() + File.separator + "d1", "d1_1");
		assertTrue(a.size() == 1 && a.get(0).getName().equals("d1_1") );
		
	}
	
	@Test
	public void testSearchDirectoryfive() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() ), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<FileNode> a = VFS_app.searchFile("VDTest",vd.root.getPath() + File.separator + "d1" + File.separator + "d1_1", "d1");
		assertTrue(a.size() == 0 );
		
	}
	
	@Test
	public void testSearchFile() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<FileNode> a = VFS_app.searchFile("VDTest", "f1_1");
		assertTrue(a.size() == 1 && a.get(0).getName().equals("f1_1") );
		
	}
	
	@Test
	public void testSearchFilebis() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_2", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<FileNode> a = VFS_app.searchFile("VDTest", "f1_1");
		assertTrue(a.size() == 0);
		
	}
	
	@Test
	public void testSearchFileter() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1"), "f1_1", new VFSFile(array));
		FileNode fnodelevel1_2 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1" +  File.separator + "d1_1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<FileNode> a = VFS_app.searchFile("VDTest", "f1_1");
		assertTrue(a.size() == 2);
		
	}
	
	@Test
	public void testSearchFilefour() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() + File.separator + "d1" + File.separator + "d1_1"), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<FileNode> a = VFS_app.searchFile("VDTest",vd.root.getPath() + File.separator + "d1", "f1_1");
		assertTrue(a.size() == 1 && a.get(0).getName().equals("f1_1") );
		
	}
	
	@Test
	public void testSearchFilefive() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1_1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath() ), "f1_1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<FileNode> a = VFS_app.searchFile("VDTest",vd.root.getPath() + File.separator + "d1", "f1_1");
		assertTrue(a.size() == 0 );
		
	}
	
	@Test
	public void testSearch() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath()+ File.separator + "d1" +  File.separator + "d1"), "d1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<Node> a = VFS_app.search("VDTest","d1");
		assertTrue(a.size() == 3 );
	}
	
	@Test
	public void testSearchbis() throws VDNotFoundException, FileNotFoundException, SameNameException{
		VFS_app.resetVFSmap();
		VFS_app.createVD("VDTest",10000, "");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath(), "d1");
		VFS_app.createDirectory("VDTest", VFS_app.getVD("VDTest").root.getPath() + File.separator + "d1", "d1");//inside d1
		
		byte[] array = new byte[] {(byte)1, (byte)2};
		VirtualDisc vd = VFS_app.getVD("VDTest");
		FileNode fnodelevel1_1 = new FileNode(vd.pathToDirectory(vd.root.getPath()+ File.separator + "d1" +  File.separator + "d1"), "d1", new VFSFile(array));
		VFS_app.saveVD(vd);
		
		ArrayList<Node> a = VFS_app.search("VDTest",vd.root.getPath()+ File.separator + "d1","d1");
		assertTrue(a.size() == 2 );
	}
	
}