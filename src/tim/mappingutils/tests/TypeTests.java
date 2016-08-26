package tim.mappingutils.tests;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;

import tim.mappingutils.types.Coordinate;
import tim.mappingutils.types.MapRectangle;

public class TypeTests extends TestCase {

	@Override
	protected void setUp(){
		
	}
	
	@Test
	public void testCoordinateConstructor(){
		try {
		Coordinate c = new Coordinate(-500, 500, 0);
		} catch(IllegalArgumentException e){
			return;
		}
		fail("IllegalArgumentException was expected with bad input but was not thrown.");
	}
	
	@Test
	public void testMapRectangleConstructor(){
		try {
		MapRectangle m = new MapRectangle(BigDecimal.valueOf(43), BigDecimal.valueOf(150), BigDecimal.valueOf(50), BigDecimal.valueOf(80));
		} catch(IllegalArgumentException e){
			return;
		}
		fail("IllegalArgumentException was expected with bad input but was not thrown.");
	}
	
	@Test
	public void testMapRectangleContainsPostitive(){
		MapRectangle m = new MapRectangle(BigDecimal.valueOf(20), BigDecimal.valueOf(20), BigDecimal.valueOf(20), BigDecimal.valueOf(20));
		Coordinate c = new Coordinate(30, 30, 0);
		assertTrue(m.contains(c));
	}
	
	@Test
	public void testMapRectangleContainsNegative(){
		MapRectangle m = new MapRectangle(BigDecimal.valueOf(20), BigDecimal.valueOf(20), BigDecimal.valueOf(20), BigDecimal.valueOf(20));
		Coordinate c = new Coordinate(50, 50, 0);
		assertFalse(m.contains(c));
	}
	
}
