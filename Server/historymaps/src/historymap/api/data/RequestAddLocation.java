package historymap.api.data;

public class RequestAddLocation {
	private String locationName;
	private Float lon;
	private Float lat;
	private String description;
	
	public RequestAddLocation(String locationName, Float lon, Float lat, String description) {
		this.locationName = locationName;
		this.lon = lon;
		this.lat = lat;
		this.description = description;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}