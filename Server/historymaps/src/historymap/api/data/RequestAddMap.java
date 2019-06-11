package historymap.api.data;

public class RequestAddMap {
	private String[] locations;
	private String url;
	private String img;
	private Boolean copyright;
	private Integer anno;
	private Float scale;
	private Float mapcoordNwLon;
	private Float mapcoordNwLat;
	private Float mapcoordNeLon;
	private Float mapcoordNeLat;
	private Float mapcoordSwLon;
	private Float mapcoordSwLat;
	private Float mapcoordSeLon;
	private Float mapcoordSeLat;
	private String description;
	public RequestAddMap(String[] locations, String url, String img, Boolean copyright, Integer anno, Float scale,
			Float mapcoordNwLon, Float mapcoordNwLat, Float mapcoordNeLon, Float mapcoordNeLat, Float mapcoordSwLon,
			Float mapcoordSwLat, Float mapcoordSeLon, Float mapcoordSeLat, String description) {
		super();
		this.locations = locations;
		this.url = url;
		this.img = img;
		this.copyright = copyright;
		this.anno = anno;
		this.scale = scale;
		this.mapcoordNwLon = mapcoordNwLon;
		this.mapcoordNwLat = mapcoordNwLat;
		this.mapcoordNeLon = mapcoordNeLon;
		this.mapcoordNeLat = mapcoordNeLat;
		this.mapcoordSwLon = mapcoordSwLon;
		this.mapcoordSwLat = mapcoordSwLat;
		this.mapcoordSeLon = mapcoordSeLon;
		this.mapcoordSeLat = mapcoordSeLat;
		this.description = description;
	}
	public String[] getLocations() {
		return locations;
	}
	public void setLocations(String[] locations) {
		this.locations = locations;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Boolean getCopyright() {
		return copyright;
	}
	public void setCopyright(Boolean copyright) {
		this.copyright = copyright;
	}
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public Float getScale() {
		return scale;
	}
	public void setScale(Float scale) {
		this.scale = scale;
	}
	public Float getMapcoordNwLon() {
		return mapcoordNwLon;
	}
	public void setMapcoordNwLon(Float mapcoordNwLon) {
		this.mapcoordNwLon = mapcoordNwLon;
	}
	public Float getMapcoordNwLat() {
		return mapcoordNwLat;
	}
	public void setMapcoordNwLat(Float mapcoordNwLat) {
		this.mapcoordNwLat = mapcoordNwLat;
	}
	public Float getMapcoordNeLon() {
		return mapcoordNeLon;
	}
	public void setMapcoordNeLon(Float mapcoordNeLon) {
		this.mapcoordNeLon = mapcoordNeLon;
	}
	public Float getMapcoordNeLat() {
		return mapcoordNeLat;
	}
	public void setMapcoordNeLat(Float mapcoordNeLat) {
		this.mapcoordNeLat = mapcoordNeLat;
	}
	public Float getMapcoordSwLon() {
		return mapcoordSwLon;
	}
	public void setMapcoordSwLon(Float mapcoordSwLon) {
		this.mapcoordSwLon = mapcoordSwLon;
	}
	public Float getMapcoordSwLat() {
		return mapcoordSwLat;
	}
	public void setMapcoordSwLat(Float mapcoordSwLat) {
		this.mapcoordSwLat = mapcoordSwLat;
	}
	public Float getMapcoordSeLon() {
		return mapcoordSeLon;
	}
	public void setMapcoordSeLon(Float mapcoordSeLon) {
		this.mapcoordSeLon = mapcoordSeLon;
	}
	public Float getMapcoordSeLat() {
		return mapcoordSeLat;
	}
	public void setMapcoordSeLat(Float mapcoordSeLat) {
		this.mapcoordSeLat = mapcoordSeLat;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	


}