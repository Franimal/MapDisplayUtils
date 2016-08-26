package tim.mappingutils.types;

import java.math.BigDecimal;

public class MapRectangle {

	private BigDecimal latitude;
	private BigDecimal longitude;

	private BigDecimal width;
	private BigDecimal height;

	public MapRectangle(BigDecimal latitude, BigDecimal longitude, BigDecimal width,
			BigDecimal height) {

		if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 || latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
			throw new IllegalArgumentException(
					"Latitude must be between -90 and 90.");
		}

		if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 || longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
			throw new IllegalArgumentException(
					"Longitude must be between -180 and 180.");
		}

		if ((latitude.add(height)).compareTo(BigDecimal.valueOf(-90)) < 0 || (latitude.add(height).compareTo(BigDecimal.valueOf(90))) > 0
				|| (longitude.add(width).compareTo(BigDecimal.valueOf(-180))) < 0 || (longitude.add(width).compareTo(BigDecimal.valueOf(180))) > 0) {
			throw new IllegalArgumentException(
					"MapRectangle bounds must be between"
							+ " -90 and 90 latitude, and -180 and 180 longitude.");
		}

		this.latitude = latitude;
		this.longitude = longitude;
		this.width = width;
		this.height = height;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public BigDecimal getMinLat() {
		return latitude;
	}

	public BigDecimal getMinLon() {
		return longitude;
	}

	public BigDecimal getMaxLat() {
		return latitude.add(height);
	}

	public BigDecimal getMaxLon() {
		return longitude.add(width);
	}

	public Coordinate getCenterCoordinate() {
		return new Coordinate(latitude.add((height.divide(BigDecimal.valueOf(2.0)))), longitude.add((width.divide(BigDecimal.valueOf(2.0)))), BigDecimal.ZERO);
	}

	public boolean contains(Coordinate coord) {
		return coord.getLatitude().compareTo(getLatitude()) > 0
				&& coord.getLatitude().compareTo(getLatitude().add(getHeight())) < 0
				&& coord.getLongitude().compareTo(getLongitude()) > 0
				&& coord.getLongitude().compareTo(getLongitude().add(getWidth())) < 0;
	}
	
	@Override
	public String toString(){
		return "(MinLat: " + getMinLat() + " MinLon: " + getMinLon() + " MaxLat: " + getMaxLat() + " MaxLon: " + getMaxLon();
	}

}
