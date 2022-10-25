package youmed.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Limit {
	@JsonProperty("_id")
	private String _id;

	@JsonProperty("maximum")
	private int maximum;

	@JsonProperty("total")
	private int total;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
