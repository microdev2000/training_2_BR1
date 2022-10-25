package youmed.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Student {
	@JsonProperty("_id")
	private String _id;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastname;

	@JsonProperty("class")
	private Clazz clazz;

	@JsonProperty("speciality")
	private Speciality speciality;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

}
