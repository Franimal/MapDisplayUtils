package tim.mappingutils.types;

import java.math.BigDecimal;

/**
 * A Coordinate represents a location on the earths surface.
 * 
 * A coordinate must have the following properties:
 * 
 * Latitude must be between -90 and 90 inclusive
 * Longitude must be between -180 and 180 inclusive
 * 
 * @author Tim
 *
 */
public class Coordinate {

	public static final double EARTH_RADIUS = 6378; 
	
	private final BigDecimal latitude;
	private final BigDecimal longitude;
	private final BigDecimal altitude;
	
	public Coordinate(BigDecimal latitude, BigDecimal longitude, BigDecimal altitude){
		
		if(latitude.compareTo(BigDecimal.valueOf(-90)) < 0 || latitude.compareTo(BigDecimal.valueOf(90)) > 0){
			throw new IllegalArgumentException("Latitude must be between -90 and 90.");
		}
		
		if(longitude.compareTo(BigDecimal.valueOf(-180)) < 0 || latitude.compareTo(BigDecimal.valueOf(180)) > 0){
			throw new IllegalArgumentException("Longitude must be between -180 and 180.");
		}
		
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}
	
	public Coordinate(double latitude, double longitude, double altitude){
		this.latitude = BigDecimal.valueOf(latitude);
		this.longitude = BigDecimal.valueOf(longitude);
		this.altitude = BigDecimal.valueOf(altitude);
	}

	/**
	 * Returns a new Coordinate that is equal to this one, plus the specified amount of meters in latitude.
	 * @param latitude
	 * @param meters
	 * @return
	 */
	public Coordinate addToLatitude(double meters){
		return new Coordinate(latitude.add(BigDecimal.valueOf((meters/1000.0 /*put into KM*/ / EARTH_RADIUS) * (180.0 / Math.PI))), longitude, altitude);
	}
	
	
	/**
	 * Returns a new Coordinate that is equal to this one, plus the specified amount of meters in longitude.
	 * @param longitude
	 * @param latitude
	 * @param meters
	 * @return
	 */
	public Coordinate addToLongitude(double meters){
		return new Coordinate(latitude, longitude.add(BigDecimal.valueOf((meters/1000.0 /*put into KM*/ / EARTH_RADIUS) * (180.0 / Math.PI) / Math.cos(latitude.multiply(BigDecimal.valueOf(Math.PI/180.0)).doubleValue()))), altitude);
	} 

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public BigDecimal getAltitude() {
		return altitude;
	}
	
	@Override
	public String toString(){
		return "(" + latitude + " , " + longitude + " , " + altitude + ")";
	}
	
}
