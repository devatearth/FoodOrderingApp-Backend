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
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

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
  /* authentication failed */
  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<Map<String, Object>> authFailExc(AuthenticationFailedException exception, WebRequest request) {
    Map errorMap = new HashMap();
    errorMap.put("code", exception.getCode());
    errorMap.put("message", exception.getErrorMessage());
    return new ResponseEntity(errorMap, HttpStatus.UNAUTHORIZED);
  }
  /* authorization failed */
  @ExceptionHandler(AuthorizationFailedException.class)
  public ResponseEntity<Map<String, Object>> authFail(AuthorizationFailedException exception, WebRequest request) {
    Map errorMap = new HashMap();
    errorMap.put("code", exception.getCode());
    errorMap.put("message", exception.getErrorMessage());
    return new ResponseEntity(errorMap, HttpStatus.UNAUTHORIZED);
  }
  /* update failed */
  @ExceptionHandler(UpdateCustomerException.class)
  public ResponseEntity<Map<String, Object>> customerUpdateFail(UpdateCustomerException exception, WebRequest request) {
    Map errorMap = new HashMap();
    errorMap.put("code", exception.getCode());
    errorMap.put("message", exception.getErrorMessage());
    return new ResponseEntity(errorMap, HttpStatus.UNAUTHORIZED);
  }
}
