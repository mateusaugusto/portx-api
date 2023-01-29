package io.portx.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "stackTrace", "cause", "suppressed" })
public class HttpCustomException extends RuntimeException {

	private String type;

	private HttpStatus code;

	private String title;

	private String message;

	public HttpCustomException(HttpCustomException exception) {
		super(exception.message);
		this.type = exception.type;
		this.title = exception.title;
		this.message = exception.message;
		this.code = exception.code;
	}

	public HttpCustomException(String type, String title, String message, HttpStatus code) {
		super(message, null, false, false);
		this.type = type;
		this.title = title;
		this.message = message;
		this.code = code;
	}

}
