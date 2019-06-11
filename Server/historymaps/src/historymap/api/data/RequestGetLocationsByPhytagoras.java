package historymap.api.data;

public class RequestGetLocationsByPhytagoras {
	private Float lon;
	private Float lat;
	private Float distance;
	
	public RequestGetLocationsByPhytagoras(Float lon, Float lat, Float distance) {
		this.lon = lon;
		this.lat = lat;
		this.distance = distance;
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

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}
}