package shop.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CmdTest {

	// Test to make sure that CmdAdd will add video to inventory and undo() and redo() methods work
	@Test
	public void testCmdAdd() {
		InventorySet set = new InventorySet();
		Video v1 = new VideoObj( "A", 2000, "B" );
		Video v2 = new VideoObj( "B", 2000, "B" );
		
		// Checking that command would return false if given incorrect arguemnt
		CmdAdd ca = new CmdAdd(set,null,0);
		Assertions.assertFalse(ca.run());
		
		// Checking that 1 video is added to inventory
		CmdAdd ca1 = new CmdAdd(set,v1,1);
		Assertions.assertTrue(ca1.run());
		Assertions.assertEquals(1,set.size());
		Assertions.assertEquals(v1, set.get(v1).video());
		
		// Checking that second video is added to inventory
		CmdAdd ca2 = new CmdAdd(set,v2,2);
		Assertions.assertTrue(ca2.run());
		Assertions.assertEquals(2,set.size());
		Assertions.assertEquals(v2, set.get(v2).video());
		
		// Checking that undo method will remove the video from the inventory
		ca2.undo();
		Assertions.assertEquals(1,set.size());
		Assertions.assertEquals(null, set.get(v2));
		
		// Checking that redo method will add the video back to the inventory
		ca2.redo();
		Assertions.assertEquals(2,set.size());
		Assertions.assertEquals(v2, set.get(v2).video());
	}
	
	
	// Test to make sure that CmdClear will clear the inventory and undo() and redo() methods work
	@Test
	public void testCmdClear() {
		InventorySet set = new InventorySet();
		Video v1 = new VideoObj( "A", 2000, "B" );
		Video v2 = new VideoObj( "B", 2000, "B" );
		
		// Add videos to be cleared and check that the set size is not 0
		set.addNumOwned(v1, 1);
		set.addNumOwned(v2, 2);
		Assertions.assertEquals(2, set.size());
		Assertions.assertEquals(v1, set.get(v1).video());
		Assertions.assertEquals(v2, set.get(v2).video());
		
		// Check that clearing the set will make the set empty
		CmdClear cc = new CmdClear(set);
		Assertions.assertTrue(cc.run());
		Assertions.assertEquals(0, set.size());
		Assertions.assertEquals(null, set.get(v1));
		Assertions.assertEquals(null, set.get(v2));
		
		// Check that the undo method will make the set have the same videos before the clear
		cc.undo();
		Assertions.assertEquals(2, set.size());
		Assertions.assertEquals(v1, set.get(v1).video());
		Assertions.assertEquals(v2, set.get(v2).video());
		
		// Check that the redo method will clear the set again
		cc.redo();
		Assertions.assertEquals(0, set.size());
		Assertions.assertEquals(null, set.get(v1));
		Assertions.assertEquals(null, set.get(v2));
	}
	
	
	// Test to make sure that videos will be checked out when CmdOut is run and that undo() and redo() methods work
	@Test
	public void testCmdOut() {
		InventorySet set = new InventorySet();
		Video v1 = new VideoObj( "A", 2000, "B" );
		Video v2 = new VideoObj( "B", 2000, "B" );
		Video v3 = new VideoObj( "C", 2000, "B" );
		CmdOut co;
		
		set.addNumOwned(v1, 1);
		set.addNumOwned(v2, 2);
		
		// Check that null video will return false when command is run
		co = new CmdOut(set,null);
		Assertions.assertFalse(co.run());
		
		// Check that video not in set will return false when command is run
		co = new CmdOut(set,v3);
		Assertions.assertFalse(co.run());
		
		// Check that video is checked out when command is run and number of copies that are out and times the video is rented increases
		co = new CmdOut(set,v1);
		Assertions.assertTrue(co.run());
		Assertions.assertEquals(1, set.get(v1).numOut());
		Assertions.assertEquals(1, set.get(v1).numRentals());
		
		co = new CmdOut(set,v2);
		Assertions.assertTrue(co.run());
		Assertions.assertEquals(1, set.get(v2).numOut());
		Assertions.assertEquals(1, set.get(v2).numRentals());
		
		co = new CmdOut(set,v2);
		Assertions.assertTrue(co.run());
		Assertions.assertEquals(2, set.get(v2).numOut());
		Assertions.assertEquals(2, set.get(v2).numRentals());
		
		// Check that undo command will decrease the number of copies that are out and number of times the video has been rented
		co.undo();
		Assertions.assertEquals(1, set.get(v2).numOut());
		Assertions.assertEquals(1, set.get(v2).numRentals());
		
		// Check that redo command will check the video out and increase the copies out and times it has been rented.
		co.redo();
		Assertions.assertEquals(2, set.get(v2).numOut());
		Assertions.assertEquals(2, set.get(v2).numRentals());
	}
	
	
	// Test to make sure videos are checked in after being checked out and undo() and redo() methods work
	@Test
	public void testCmdIn() {
		InventorySet set = new InventorySet();
		Video v1 = new VideoObj( "A", 2000, "B" );
		Video v2 = new VideoObj( "B", 2000, "B" );
		Video v3 = new VideoObj( "C", 2000, "B" );
		CmdIn ci;
		
		set.addNumOwned(v1, 1);
		set.addNumOwned(v2, 2);
		
		// Check that null video will return false when command is run
		ci = new CmdIn(set,null);
		Assertions.assertFalse(ci.run());
		
		// Check that video not in set will return false when command is run
		ci = new CmdIn(set,v3);
		Assertions.assertFalse(ci.run());
		
		// Check that video that is not checked out will return false when check in is run
		ci = new CmdIn(set,v1);
		Assertions.assertFalse(ci.run());
		
		set.checkOut(v1);
		Assertions.assertEquals(1, set.get(v1).numOut());
		Assertions.assertEquals(1, set.get(v1).numRentals());
		
		// Check that the number of copies out will decrease when video is checked in
		ci = new CmdIn(set,v1);
		Assertions.assertTrue(ci.run());
		Assertions.assertEquals(0, set.get(v1).numOut());
		
		set.checkOut(v2);
		set.checkOut(v2);
		Assertions.assertEquals(2, set.get(v2).numOut());
		Assertions.assertEquals(2, set.get(v2).numRentals());
		
		ci = new CmdIn(set,v2);
		Assertions.assertTrue(ci.run());
		Assertions.assertEquals(1, set.get(v2).numOut());
		
		// Check that undo command will increase the number of copies that are out
		ci.undo();
		Assertions.assertEquals(2, set.get(v2).numOut());
		
		// Check that redo command will decrease the number of copies that are out
		ci.redo();
		Assertions.assertEquals(1, set.get(v2).numOut());
	}
}
