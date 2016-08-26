package tim.mappingutils.tests;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;

import tim.mappingutils.map.CoordinateMap;
import tim.mappingutils.types.Coordinate;
import tim.mappingutils.types.MapRectangle;

public class MapTests extends TestCase {

	@Override
	protected void setUp(){
		
	}
	
	@Test
	public void testMapBoundCreation(){
		
		int radius = 20;
		
		Coordinate center = new Coordinate(-41.294300, 174.769764, 0);
		Coordinate up = center.addToLatitude(radius);
		Coordinate down = center.addToLatitude(-radius);
		Coordinate right = center.addToLongitude(radius);
		Coordinate left = center.addToLongitude(-radius);
		
		CoordinateMap map = new CoordinateMap(new Coordinate[]{center, up, down, right, left}, 640, 480, radius);
		//System.out.println(map.getBounds());
		//System.out.println(map.getBounds().getCenterCoordinate().getLatitude() + " , " + map.getBounds().getCenterCoordinate().getLongitude());
		double ratio = 640.0/480.0;
		Coordinate desiredLeft = center.addToLongitude(-radius * ratio);
		Coordinate desiredRight = center.addToLongitude(radius * ratio);
		
		if(CoordinateMap.absSubtract(up.getLatitude(), map.getBounds().getMaxLat(), CoordinateMap.ACCEPTABLE_LAT_LON_DECIMAL_PLACES) != 0){
			fail("MaxLat should be " + up.getLatitude() + " , but is " + map.getBounds().getMaxLat());
		}
		if(CoordinateMap.absSubtract(down.getLatitude(), map.getBounds().getMinLat(), CoordinateMap.ACCEPTABLE_LAT_LON_DECIMAL_PLACES) != 0){
			fail("MinLat should be " + down.getLatitude() + " , but is " + map.getBounds().getMinLat());
		}
		if(CoordinateMap.absSubtract(desiredLeft.getLongitude(), map.getBounds().getMinLon(), CoordinateMap.ACCEPTABLE_LAT_LON_DECIMAL_PLACES) != 0){
			fail("MinLon should be " + left.getLongitude() + " , but is " + map.getBounds().getMinLon());
		}
		if(CoordinateMap.absSubtract(desiredRight.getLongitude(), map.getBounds().getMaxLon(), CoordinateMap.ACCEPTABLE_LAT_LON_DECIMAL_PLACES) != 0){
			fail("MaxLon should be " + right.getLongitude() + " , but is " + map.getBounds().getMaxLon());
		}
	}
	
	@Test
	public void testMinimumBounds(){
		
		Coordinate c1 = new Coordinate(20, 20, 0);
		Coordinate c2 = new Coordinate(20, 30, 0);
		Coordinate c3 = new Coordinate(40, 20, 0);
		
		Coordinate[] coords = {c1, c2, c3};
		
		//Max latitude is 40, min Latitude is 20, max Long = 30, min = 20
		
		MapRectangle map = CoordinateMap.getMinimumBounds(coords);
		assertTrue("Bounds should be (20, 20, 10, 20) but are instead " +
				map.getLatitude() + " , " + map.getLongitude() + " , " + map.getWidth() + " , " + map.getHeight() 
				,
				map.getLatitude().equals(BigDecimal.valueOf(20.0))
				&& map.getLongitude().equals(BigDecimal.valueOf(20.0))
				&& map.getWidth().equals(BigDecimal.valueOf(10.0))
				&& map.getHeight().equals(BigDecimal.valueOf(20.0)));
	}
	
}
