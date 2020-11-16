package shop.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import shop.command.RerunnableCommand;
import shop.command.UndoableCommand;
import shop.data.Data;
import shop.data.Video;
import shop.data.Inventory;

public class Integration2Test {

  @Test
  public void test1() {
    final Inventory inventory = Data.newInventory();
    final Video v1 = Data.newVideo("K", 2003, "S");
    final Video v2 = Data.newVideo("S", 2002, "K");
    final RerunnableCommand UNDO = Data.newUndoCmd(inventory);
    final RerunnableCommand REDO = Data.newRedoCmd(inventory);
	
    UndoableCommand c = Data.newAddCmd(inventory, v1, 2);
    Assertions.assertTrue  ( c.run() );
    Assertions.assertEquals( 1, inventory.size() );
    Assertions.assertTrue  (!c.run() );     // cannot run an undoable command twice
    Assertions.assertTrue  (!Data.newAddCmd(inventory, null, 3).run()); // can't add null!
    Assertions.assertTrue  (!Data.newAddCmd(inventory, v2, 0).run());   // can't add zero copies!
    Assertions.assertEquals( 1, inventory.size() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( 0, inventory.size() );
    Assertions.assertTrue  (!UNDO.run() );  // nothing to undo!
    Assertions.assertEquals( 0, inventory.size() );
    Assertions.assertTrue  ( REDO.run() );
    Assertions.assertEquals( 1, inventory.size() );
    Assertions.assertTrue  (!REDO.run() );  // nothing to redo!
    Assertions.assertEquals( 1, inventory.size() );
    Assertions.assertTrue  ( Data.newAddCmd(inventory, v1, -2).run());   // delete!
    Assertions.assertEquals( 0, inventory.size() );
    Assertions.assertTrue  (!Data.newOutCmd(inventory, v1).run());       // can't check out
    Assertions.assertEquals( 0, inventory.size() );
    Assertions.assertTrue  ( UNDO.run() );  // should undo the AddCmd, not the OutCmd
    Assertions.assertEquals( 1, inventory.size() ); 
    Assertions.assertTrue  (!Data.newAddCmd(inventory, v1, -3).run());   // can't delete 3
    Assertions.assertTrue  ( Data.newAddCmd(inventory, v1, -2).run());   // delete 2
    Assertions.assertEquals( 0, inventory.size() );
    Assertions.assertTrue  ( UNDO.run() ); 
    Assertions.assertEquals( 1, inventory.size() ); 

    Assertions.assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );	
    Assertions.assertTrue  ( Data.newAddCmd(inventory, v1, 2).run());
    Assertions.assertEquals( "K (2003) : S [4,0,0]", inventory.get(v1).toString() );	
    Assertions.assertTrue  ( Data.newAddCmd(inventory, v1, 2).run());
    Assertions.assertEquals( "K (2003) : S [6,0,0]", inventory.get(v1).toString() );	
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [4,0,0]", inventory.get(v1).toString() );	
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );	

    Assertions.assertTrue  ( Data.newOutCmd(inventory, v1).run());
    Assertions.assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( Data.newOutCmd(inventory, v1).run());
    Assertions.assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
    Assertions.assertTrue  (!Data.newOutCmd(inventory, v1).run());
    Assertions.assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( REDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( REDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
    
    Assertions.assertTrue  ( Data.newInCmd(inventory, v1).run() );
    Assertions.assertEquals( "K (2003) : S [2,1,2]", inventory.get(v1).toString() );	
    Assertions.assertTrue  ( Data.newInCmd(inventory, v1).run() );
    Assertions.assertEquals( "K (2003) : S [2,0,2]", inventory.get(v1).toString() );
    Assertions.assertTrue  (!Data.newInCmd(inventory, v1).run() );
    Assertions.assertEquals( "K (2003) : S [2,0,2]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,1,2]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( REDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,1,2]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( REDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,0,2]", inventory.get(v1).toString() );

    Assertions.assertTrue  ( Data.newAddCmd(inventory, v2, 4).run());
    Assertions.assertEquals( 2, inventory.size() );
    Assertions.assertTrue  ( Data.newClearCmd(inventory).run());
    Assertions.assertEquals( 0, inventory.size() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( 2, inventory.size() );
    Assertions.assertTrue  ( REDO.run() );
    Assertions.assertEquals( 0, inventory.size() );
  }

  @Test
  public void test2() {
    final Inventory inventory = Data.newInventory();
    final Video v1 = Data.newVideo("K", 2003, "S");
    final RerunnableCommand UNDO = Data.newUndoCmd(inventory);
    final RerunnableCommand REDO = Data.newRedoCmd(inventory);
    Assertions.assertTrue  ( Data.newAddCmd(inventory, v1,2).run());
    Assertions.assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( Data.newOutCmd(inventory, v1).run());
    Assertions.assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( REDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( Data.newOutCmd(inventory, v1).run());
    Assertions.assertEquals( "K (2003) : S [2,2,2]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,1,1]", inventory.get(v1).toString() );
    Assertions.assertTrue  ( UNDO.run() );
    Assertions.assertEquals( "K (2003) : S [2,0,0]", inventory.get(v1).toString() );
  }
}
