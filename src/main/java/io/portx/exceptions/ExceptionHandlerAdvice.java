package io.portx.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler({ RuntimeException.class })
	@ResponseBody
	public ResponseEntity<HttpCustomException> handleServer(RuntimeException exception) {
		exception.printStackTrace();
		String message = exception.getMessage();
		return new ResponseEntity<>(new HttpCustomException("ERROR", message, message,
				HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
