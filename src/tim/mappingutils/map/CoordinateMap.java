package tim.mappingutils.map;

import java.math.BigDecimal;

import tim.mappingutils.types.Coordinate;
import tim.mappingutils.types.MapRectangle;

/**
 * A CoordinateMap respresents a map of latitude/longitude coordinates.
 * 
 * It consists of a rectangle, with methods to help to scale/manipulate the map
 * without having to alter the underlying coordinate values.
 * 
 * This class can be initialized with a set of Coordinates, Centering on the coordinates 
 * and spanning 'radius' meters (minimum) out from the center.
 * surround them.
 * 
 * @author Tim
 *
 */
public class CoordinateMap {

	public static final double EARTH_RADIUS = 6378; 
	public static final String EARTH_R_STR = "6378";
	public static final String ONE_THOUSAND = "1000";
	public static final int ACCEPTABLE_LAT_LON_DECIMAL_PLACES = 15;
	
	private MapRectangle bounds;
	
	
	/**
	 * Initializes the map using an array of existing Coordinates.
	 *
	 * 
	 * @param coords The coordinates to build the map around
	 * @param radius The minimum radius to extends the map out from the center, in meters
	 */
	public CoordinateMap(Coordinate[] coords, double screenWidth, double screenHeight, double radius){
		//First get the bounds of the coordinates
		bounds = getMinimumBounds(coords);
		//now get the screen bounds (in lat/lon) from the relative bounds.
		bounds = getScreenBounds(bounds, screenWidth, screenHeight, 0, 0, radius);
	}
	
	/**
	 * Gets the bounding box of the screen, in Latitude/Longitude.
	 * @return
	 */
	public MapRectangle getBounds(){
		return bounds;
	}
	
	/**
	 * TODO: MOVE TO MathHandler
	 * 
	 * Finds the absolute difference between two BigDecimal values, with the decimal precision specified.
	 * 
	 * Rounds down if specified D.P is smaller than the precision of one or more of the BigDecimal values.
	 * 
	 * @param a
	 * @param b
	 * @param the number of decimal places to include in the calculation.  If dp == -1, it will default to the lowet precision BigDecimal given.
	 * @return
	 */
	public static double absSubtract(BigDecimal a, BigDecimal b, int dp){
		
		if(dp == -1){
			dp = Math.min(a.scale(), b.scale());
		}
		
		BigDecimal x = a.setScale(dp, BigDecimal.ROUND_DOWN);
		BigDecimal y = b.setScale(dp, BigDecimal.ROUND_DOWN);
		
		BigDecimal diff = x.subtract(y);
		return diff.abs().doubleValue();
	}
	
	/**
	 * TODO: MOVE TO MathHandler
	 * 
	 * @param latitude
	 * @param meters
	 * @return
	 */
	public static BigDecimal addToLatitude(BigDecimal latitude, BigDecimal meters){
		return latitude.add(
					(meters.divide
							(new BigDecimal(ONE_THOUSAND))
							.divide(new BigDecimal(EARTH_R_STR), 20,  BigDecimal.ROUND_HALF_UP))
					.multiply(BigDecimal.valueOf(180.0 / Math.PI))
			);
	}
	
	/**
	 * TODO: MOVE TO MathHandler
	 * 
	 * @param longitude
	 * @param latitude
	 * @param meters
	 * @return
	 */
	public static BigDecimal addToLongitude(BigDecimal longitude, BigDecimal latitude, BigDecimal meters){
		return longitude.add(
						(meters.divide
								(new BigDecimal(ONE_THOUSAND)
								).divide
								(new BigDecimal(EARTH_R_STR), 20,  BigDecimal.ROUND_HALF_UP)
								.multiply(
										(BigDecimal.valueOf(180.0 / Math.PI))
								).divide
								(BigDecimal.valueOf
										(Math.cos
												(latitude.multiply
														(BigDecimal.valueOf(Math.PI/180.0)).doubleValue()
												)
										), 20, BigDecimal.ROUND_HALF_UP
								)
						)
				);
	} 
	
	
	/**
	 * Find the appropriate screen bounds in latitude and longitude, given the MapRectangle to enclose, as well as
	 * the screen width and height.
	 * 
	 * Map may be offset by offsetX/offsetY (in meters).  
	 * 
	 * Zoom is the minimum radius in meters that needs to be shown on the map.
	 *  
	 * @param coord
	 * @param screenWidth
	 * @param screenHeight
	 * @param zoom
	 */
	public MapRectangle getScreenBounds(MapRectangle bb, 
			double screenWidth, double screenHeight, 
			double offsetX, double offsetY, 
			double zoom){ 
	
		MapRectangle mapRect = bb;
		//MapRectangle screenRect = mapRect.toScreenSpace(screenWidth, screenHeight, zoom);
		
		Coordinate center = mapRect.getCenterCoordinate();
		
		BigDecimal heightMeters = BigDecimal.ZERO;
		BigDecimal widthMeters = BigDecimal.ZERO;
		
		if(screenWidth > screenHeight){
			heightMeters = BigDecimal.valueOf(zoom);
			widthMeters = BigDecimal.valueOf(zoom).multiply(BigDecimal.valueOf(screenWidth/screenHeight));
		} else {
			widthMeters = BigDecimal.valueOf(zoom);
			heightMeters = BigDecimal.valueOf(zoom).multiply(BigDecimal.valueOf(screenHeight/screenWidth));
		}
		
		BigDecimal minLat = addToLatitude(center.getLatitude(), heightMeters.negate());
		BigDecimal maxLat = addToLatitude(center.getLatitude(), heightMeters);
		
		BigDecimal minLon = addToLongitude(center.getLongitude(), center.getLatitude(), widthMeters.negate());
		BigDecimal maxLon = addToLongitude(center.getLongitude(), center.getLatitude(), widthMeters);
		
		//ScreenCoordinate screenCoord = mapCoordinateToScreen(coord, mapRect, screenRect);
		return new MapRectangle(minLat, minLon, maxLon.subtract(minLon), maxLat.subtract(minLat));
	}
	
	/**
	 * Find the appropriate screen bounds in latitude and longitude, given the MapRectangle to enclose, as well as
	 * the screen width and height.
	 * 
	 * Zoom is the minimum radius in meters that needs to be shown on the map.
	 *  
	 * @param coord
	 * @param screenWidth
	 * @param screenHeight
	 * @param zoom
	 */
	public MapRectangle getScreenBounds(MapRectangle bb, double screenWidth, double screenHeight, double zoom){
		return getScreenBounds(bb, screenWidth, screenHeight, 0.0, 0.0, zoom);
	}
	
	/**
	 * Find the appropriate screen bounds in latitude and longitude, given the MapRectangle to enclose, as well as
	 * the screen width and height.
	 * 
	 * This method will always show a screen that is either 100 meters high or 100 meters wide (depending on ratio of screen).
	 * 
	 * @param coord
	 * @param screenWidth
	 * @param screenHeight
	 */
	public MapRectangle getScreenBounds(MapRectangle bb, double screenWidth, double screenHeight){
		return getScreenBounds(bb, screenWidth, screenHeight, 0.0, 0.0, 50);
	}

	/**
	 * 
	 * @param coords The coordinates to build the MapRectangle around.
	 * @return
	 */
	public static MapRectangle getMinimumBounds(Coordinate[] coords) {
		
		BigDecimal minLat = BigDecimal.valueOf(1000);
		BigDecimal minLon = BigDecimal.valueOf(1000);
		
		BigDecimal maxLat = BigDecimal.valueOf(-1000);
		BigDecimal maxLon = BigDecimal.valueOf(-1000);
		
		for(Coordinate c : coords){
			if(c.getLatitude().compareTo(minLat) < 0){
				minLat = c.getLatitude();
			}
			if(c.getLongitude().compareTo(minLon) < 0){
				minLon = c.getLongitude();
			}
			if(c.getLatitude().compareTo(maxLat) > 0){
				maxLat = c.getLatitude();
			}
			if(c.getLongitude().compareTo(maxLon) > 0){
				maxLon = c.getLongitude();
			}
		}
		
		//System.out.println(minLat + " , " + minLon + " , " + maxLat + " , " + maxLon);
		
		return new MapRectangle(minLat, minLon, maxLon.subtract(minLon) , maxLat.subtract(minLat));
	}

	@Override
	public String toString(){
		return "CoordinateMap: " + bounds;
	}
	
}
