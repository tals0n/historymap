package historymap.api.data;

public class RequestModifyMap {
	private String mapId;
	private String url;
	private Boolean copyright;
	private String img;
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
	private String[] locations;
	public RequestModifyMap(String mapId, String url, Boolean copyright, String img, Integer anno, Float scale,
			Float mapcoordNwLon, Float mapcoordNwLat, Float mapcoordNeLon, Float mapcoordNeLat, Float mapcoordSwLon,
			Float mapcoordSwLat, Float mapcoordSeLon, Float mapcoordSeLat, String description, String[] locations) {
		super();
		this.mapId = mapId;
		this.url = url;
		this.copyright = copyright;
		this.img = img;
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
		this.locations = locations;
	}
	public String getMapId() {
		return mapId;
	}
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getCopyright() {
		return copyright;
	}
	public void setCopyright(Boolean copyright) {
		this.copyright = copyright;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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
	public String[] getLocations() {
		return locations;
	}
	public void setLocations(String[] locations) {
		this.locations = locations;
	}
	
	
	}