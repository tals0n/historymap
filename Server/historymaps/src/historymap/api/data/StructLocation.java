package historymap.api.data;

public class StructLocation {
	private String id;
	private String name;
	private Float lon;
	private Float lat;
	private String description;
	
	public StructLocation(String id, String name, Float lon, Float lat, String description) {
		this.id = id;
		this.name = name;
		this.lon = lon;
		this.lat = lat;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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