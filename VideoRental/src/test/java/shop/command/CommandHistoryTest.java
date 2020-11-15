package shop.command;

import java.util.EmptyStackException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandHistoryTest {

  @Test
  public void testEmptyExceptions() {
    CommandHistoryObj h = new CommandHistoryObj();
    Assertions.assertSame(null, h.topUndoCommand());
    Assertions.assertSame(null, h.topRedoCommand());
    Assertions.assertFalse(h.getUndo().run());
    Assertions.assertFalse(h.getRedo().run());
    
    // Catch EmptyStackException when trying to get commands from an empty stack
    try {
    	h.getRedo();
    } catch (EmptyStackException e) {
    	Assertions.assertEquals(0, h.getRedoStackSize());
    	Assertions.assertSame(null, h.topRedoCommand());
    }
    
    try {
    	h.getUndo();
    } catch (EmptyStackException e) {
    	Assertions.assertEquals(0, h.getUndoStackSize());
    	Assertions.assertSame(null, h.topUndoCommand());
    }
  }
 
  // Class for testing UndoableCommands 
  class CmdSuccess implements UndoableCommand {
      public boolean run() { return true; }
      public void undo() { }
      public void redo() { }
  }
  
  // Test for checking that the size of the Undo and Redo stack will increase and decrease when undo and redo commands are added or run
  @Test
  public void testStackSize() {
	  CommandHistoryObj h = new CommandHistoryObj();
	  UndoableCommand u1 = new CmdSuccess();
	  UndoableCommand u2 = new CmdSuccess();
	  UndoableCommand u3 = new CmdSuccess();
	  
	  // Checking stack will increase when Undoable commands are added
	  h.add(u1);
	  Assertions.assertEquals(1, h.getUndoStackSize());
	  Assertions.assertEquals(0, h.getRedoStackSize());
	  
	  h.add(u2);
	  Assertions.assertEquals(2, h.getUndoStackSize());
	  Assertions.assertEquals(0, h.getRedoStackSize());
	  
	  h.add(u3);
	  Assertions.assertEquals(3, h.getUndoStackSize());
	  Assertions.assertEquals(0, h.getRedoStackSize());
	  
	  // Checking that Undo stack size decreases and Redo stack incereases when running undo command 
	  h.getUndo().run();
	  Assertions.assertEquals(2, h.getUndoStackSize());
	  Assertions.assertEquals(1, h.getRedoStackSize());
	  
	  h.getUndo().run();
	  Assertions.assertEquals(1, h.getUndoStackSize());
	  Assertions.assertEquals(2, h.getRedoStackSize());
	  
	  // Checking that Undo stack size increases and Redo stack decreases when running redo command
	  h.getRedo().run();
	  Assertions.assertEquals(2, h.getUndoStackSize());
	  Assertions.assertEquals(1, h.getRedoStackSize());
	  
	  h.getRedo().run();
	  Assertions.assertEquals(3, h.getUndoStackSize());
	  Assertions.assertEquals(0, h.getRedoStackSize());
	  
	  // Checking that Redo stack is cleared when adding new Undoable command
	  h.getUndo().run();
	  h.getUndo().run();
	  h.add(u3);
	  Assertions.assertEquals(2, h.getUndoStackSize());
	  Assertions.assertEquals(0, h.getRedoStackSize());
  }
  
  // Testing that the top commands will return the top command of their stack and not remove it
  @Test
  public void testTopCommands() {
	  CommandHistoryObj h = new CommandHistoryObj();
	  UndoableCommand u1 = new CmdSuccess();
	  UndoableCommand u2 = new CmdSuccess();
	  UndoableCommand u3 = new CmdSuccess();
	  
	  // Checking that topUndoCommand() will return the command on top of the Undo stack
	  h.add(u1);
	  Assertions.assertEquals(u1, h.topUndoCommand());
	  Assertions.assertEquals(1, h.getUndoStackSize());
	  h.add(u2);
	  Assertions.assertEquals(u2, h.topUndoCommand());
	  h.add(u3);
	  Assertions.assertEquals(3, h.getUndoStackSize());
	  Assertions.assertEquals(u3, h.topUndoCommand());
	  Assertions.assertEquals(null, h.topRedoCommand());
	  Assertions.assertEquals(3, h.getUndoStackSize());
	  
	  // Checking that topRedoCommand() will return the command on top of the Redo stack
	  h.getUndo().run();
	  Assertions.assertEquals(1, h.getRedoStackSize());
	  Assertions.assertEquals(u3, h.topRedoCommand());
	  Assertions.assertEquals(u2, h.topUndoCommand());
	  Assertions.assertEquals(1, h.getRedoStackSize());
	  h.getUndo().run();
	  Assertions.assertEquals(2, h.getRedoStackSize());
	  Assertions.assertEquals(u2, h.topRedoCommand());
	  Assertions.assertEquals(u1, h.topUndoCommand());
	  Assertions.assertEquals(2, h.getRedoStackSize());
  }
  
  @Test
  private void checkStacks(CommandHistoryObj h, UndoableCommand topUndo, UndoableCommand topRedo) {
    Assertions.assertSame(topUndo, h.topUndoCommand());
    Assertions.assertSame(topRedo, h.topRedoCommand());
  }

  @Test
  public void testThatTopIsSetByAddUndoAndRedo() {
    CommandHistoryObj h = new CommandHistoryObj();

    

    UndoableCommand x1 = new CmdSuccess();
    UndoableCommand x2 = new CmdSuccess();
    UndoableCommand x3 = new CmdSuccess();

    h.add(x1);          checkStacks(h, x1,   null);
    h.getUndo().run();  checkStacks(h, null, x1);
    h.getRedo().run();  checkStacks(h, x1,   null);

    h.add(x2);          checkStacks(h, x2,   null);
    h.getUndo().run();  checkStacks(h, x1,   x2);
    h.getUndo().run();  checkStacks(h, null, x1);
    h.getRedo().run();  checkStacks(h, x1,   x2);
    h.getRedo().run();  checkStacks(h, x2,   null);

    h.getUndo().run();  checkStacks(h, x1,   x2);
    h.add(x3);          checkStacks(h, x3,   null);
    h.getUndo().run();  checkStacks(h, x1,   x3);
    h.getUndo().run();  checkStacks(h, null, x1);
    h.getRedo().run();  checkStacks(h, x1,   x3);
    h.getRedo().run();  checkStacks(h, x3,   null);

    h = new CommandHistoryObj();
    h.add(x1);          checkStacks(h, x1,   null);  
    h.add(x2);          checkStacks(h, x2,   null);
    h.getUndo().run();  checkStacks(h, x1,   x2);  
    h.getRedo().run();  checkStacks(h, x2,   null);  
    h.add(x3);          checkStacks(h, x3,   null);  
    h.getUndo().run();  checkStacks(h, x2,   x3);
    h.getUndo().run();  checkStacks(h, x1,   x2);
  }

  // these must be fields so that they can be changed by the instances
  // of the inner class TestThatMethodsArePerformed.
  private boolean _didRun;
  private boolean _didUndo;
  private boolean _didRedo;

  @Test
  public void testThatMethodsArePerformed() {
    CommandHistoryObj h = new CommandHistoryObj();

    class MockCommand implements UndoableCommand {
      // Using "CommandHistoryTEST.this" to make references to
      // outer class instance explicit
      public boolean run() {
        CommandHistoryTest.this._didRun = true;
        return true;
      }
      public void undo() {
        CommandHistoryTest.this._didUndo = true;
      }
      public void redo() {
        CommandHistoryTest.this._didRedo = true;
      }
    }

    UndoableCommand x = new MockCommand();

    _didRun = _didUndo = _didRedo = false;
    h.add(x);
    Assertions.assertTrue(!_didRun && !_didUndo && !_didRedo);

    _didRun = _didUndo = _didRedo = false;
    h.getUndo().run();
    Assertions.assertTrue(!_didRun && _didUndo && !_didRedo);

    _didRun = _didUndo = _didRedo = false;
    h.getRedo().run();
    Assertions.assertTrue(!_didRun && !_didUndo && _didRedo);
  }
}
