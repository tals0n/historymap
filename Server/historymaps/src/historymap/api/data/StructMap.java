package historymap.api.data;

public class StructMap {
	private String id;
	private String url;
	private Integer anno;
	private Float scale;
	private String description;
	
	public StructMap(String id, String url, Integer anno, Float scale, String description) {
		this.id = id;
		this.url = url;
		this.anno = anno;
		this.scale = scale;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}