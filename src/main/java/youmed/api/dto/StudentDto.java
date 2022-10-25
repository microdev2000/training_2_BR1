package youmed.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentDto {
	@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
	private String _id;
	
	@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
	private String firstName;
	
	@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
	private String lastName;
	
	@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
	private ClazzDto clazz;

	public ClazzDto getClazz() {
		return clazz;
	}

	public void setClazz(ClazzDto clazz) {
		this.clazz = clazz;
	}

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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
