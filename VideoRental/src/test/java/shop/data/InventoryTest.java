package shop.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import shop.command.UndoableCommand;
import shop.command.CommandHistory;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

// TODO:  complete the tests
public class InventoryTest {
//  public InventoryTEST(String name) {
//    super(name);
//  }

  // TODO  
  InventorySet s = new InventorySet();
  final Video v1 = new VideoObj( "A", 2000, "B" );
  final Video v1copy = new VideoObj( "A", 2000, "B" );
  final Video v2 = new VideoObj( "B", 2000, "B" );

  @Test
  public void testSize() {
    // TODO  
	  Assertions.assertEquals( 0, s.size() );
      s.addNumOwned(v1,  1); Assertions.assertEquals( 1, s.size() );
      s.addNumOwned(v1,  2); Assertions.assertEquals( 1, s.size() );
      s.addNumOwned(v2,  1); Assertions.assertEquals( 2, s.size() );
      s.addNumOwned(v2, -1); Assertions.assertEquals( 1, s.size() );
      s.addNumOwned(v1, -3); Assertions.assertEquals( 0, s.size() );
      try { s.addNumOwned(v1, -3); Assertions.fail(); } catch ( IllegalArgumentException e ) {}
  }

  @Test
  public void testAddNumOwned() {
    // TODO  
	  Assertions.assertEquals( null, s.get(v1) );
      s.addNumOwned(v1, 1);     Assertions.assertEquals( v1, s.get(v1).video() );
                                Assertions.assertEquals( 1, s.get(v1).numOwned());
      s.addNumOwned(v1, 2);     Assertions.assertEquals( 3, s.get(v1).numOwned());
      s.addNumOwned(v1, -1);    Assertions.assertEquals( 2, s.get(v1).numOwned());
      s.addNumOwned(v2, 1);     Assertions.assertEquals( 2, s.get(v1).numOwned());
      s.addNumOwned(v1copy, 1); Assertions.assertEquals( 3, s.get(v1).numOwned());
      s.addNumOwned(v1, -3);    Assertions.assertEquals( null, s.get(v1) );
      try { s.addNumOwned(null, 1);   Assertions.fail(); } catch ( IllegalArgumentException e ) {}
  }

  @Test
  public void testCheckOutCheckIn() {
    // TODO  
	  try { s.checkOut(null);     Assertions.fail(); } catch ( IllegalArgumentException e ) {}
	  try { s.checkIn(null);      Assertions.fail(); } catch ( IllegalArgumentException e ) {}
	  s.addNumOwned(v1, 2); Assertions.assertTrue( s.get(v1).numOut() == 0 && s.get(v1).numRentals() == 0 );
	  s.checkOut(v1);       Assertions.assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 1 );
	  try { s.addNumOwned(v1,-3); Assertions.fail(); } catch ( IllegalArgumentException e ) {}
	  try { s.addNumOwned(v1,-2); Assertions.fail(); } catch ( IllegalArgumentException e ) {}
	  s.addNumOwned(v1,-1); Assertions.assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 1 );
	  s.addNumOwned(v1, 1); Assertions.assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 1 );
	  s.checkOut(v1);       Assertions.assertTrue( s.get(v1).numOut() == 2 && s.get(v1).numRentals() == 2 );
	  try { s.checkOut(v1);       Assertions.fail(); } catch ( IllegalArgumentException e ) {}
	  s.checkIn(v1);        Assertions.assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 2 );
	  s.checkIn(v1copy);    Assertions.assertTrue( s.get(v1).numOut() == 0 && s.get(v1).numRentals() == 2 );
	  try { s.checkIn(v1);        Assertions.fail(); } catch ( IllegalArgumentException e ) {}
	  try { s.checkOut(v2);       Assertions.fail(); } catch ( IllegalArgumentException e ) {}
	  s.checkOut(v1);       Assertions.assertTrue( s.get(v1).numOut() == 1 && s.get(v1).numRentals() == 3 );
  }

  @Test
  public void testClear() {
    // TODO  
	  s.addNumOwned(v1, 2); Assertions.assertEquals( 1, s.size() );
      s.addNumOwned(v2, 2); Assertions.assertEquals( 2, s.size() );
      s.clear();            Assertions.assertEquals( 0, s.size() );
      try { s.checkOut(v2);       Assertions.fail(); } catch ( IllegalArgumentException e ) {}
  }

  @Test
  public void testGet() {
    // TODO  
	  s.addNumOwned(v1, 1);
	  Record r1 = s.get(v1);
	  Record r2 = s.get(v1);
	  Assertions.assertTrue( r1.equals(r2) );
	  Assertions.assertTrue( r1 == r2 );
  }

  @Test
  public void testIterator1() {
    // TODO  
	  Set<Video> expected = new HashSet<Video>();
	  InventorySet inv = new InventorySet();
	  Video v1 = new VideoObj("XX", 2004, "XX");
	  Video v2 = new VideoObj("XY", 2000, "XY");
	  inv.addNumOwned(v1,10);
	  inv.addNumOwned(v2,20);
	  expected.add(v1);
	  expected.add(v2);
	    
	  Iterator<Record> i = inv.iterator();
	  try { i.remove(); Assertions.fail(); }
	  catch (UnsupportedOperationException e) { }
	  while(i.hasNext()) {
		  Record r = i.next(); 
	      Assertions.assertTrue(expected.contains(r.video()));
	      expected.remove(r.video());
	  }
	  Assertions.assertTrue(expected.isEmpty());
  }

  @Test
  public void testIterator2() {
    // TODO  
	  List<Video> expected = new ArrayList<Video>();
	  InventorySet inv = new InventorySet();
	  Video v1 = new VideoObj("XX", 2004, "XX");
	  Video v2 = new VideoObj("XY", 2000, "XY");
	  expected.add(v2);
	  expected.add(v1);
	  inv.addNumOwned(v1,10);
	  inv.addNumOwned(v2,20);
	  
	  Comparator<Record> c = new Comparator<Record>() {
		  public int compare(Record r1, Record r2) {
			  return r1.video().year() - r2.video().year();
	        }	
	      };	
	  Iterator<Record> i = inv.iterator(c);
	  try { i.remove(); Assertions.fail(); }
	  catch (UnsupportedOperationException e) { }
	  Iterator j = expected.iterator();
	  while (i.hasNext()) {
		  Assertions.assertSame(j.next(), i.next().video());
		  j.remove();
	  }
	  Assertions.assertTrue(expected.isEmpty());
  }
}
