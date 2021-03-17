package com.upgrad.FoodOrderingApp.api.exception;

/* spring imports */
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/* java imports */
import java.util.Map;
import java.util.HashMap;

/* project imports */
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;

@ControllerAdvice
public class AppException {
  /* signup */
  @ExceptionHandler(SignUpRestrictedException.class)
  public ResponseEntity<Map<String, Object>> signup(SignUpRestrictedException exception, WebRequest request) {
    Map errorMap = new HashMap();
    errorMap.put("code", exception.getCode());
    errorMap.put("message", exception.getErrorMessage());
    return new ResponseEntity(errorMap, HttpStatus.UNAUTHORIZED);
  }
}
