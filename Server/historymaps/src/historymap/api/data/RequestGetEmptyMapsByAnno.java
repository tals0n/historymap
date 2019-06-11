package historymap.api.data;

public class RequestGetEmptyMapsByAnno {
	private Integer fromAnno;
	private Integer toAnno;
	
	public RequestGetEmptyMapsByAnno(Integer fromAnno, Integer toAnno) {
		this.fromAnno = fromAnno;
		this.toAnno = toAnno;
	}

	public Integer getFromAnno() {
		return fromAnno;
	}

	public void setFromAnno(Integer fromAnno) {
		this.fromAnno = fromAnno;
	}

	public Integer getToAnno() {
		return toAnno;
	}

	public void setToAnno(Integer toAnno) {
		this.toAnno = toAnno;
	}
}