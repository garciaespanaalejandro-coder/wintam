package com.wintam.exception;

import com.wintam.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<MessageResponse> handleUserNotFound(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<MessageResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<MessageResponse> handleUserAlreadyExists(UserAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<MessageResponse> handleEmailNotVerified(EmailNotVerifiedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(AccountAlreadyVerifiedException.class)
    public ResponseEntity<MessageResponse> handleAccountAlredyVerified(AccountAlreadyVerifiedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<MessageResponse> handleInvalidCode(InvalidCodeException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Codigo de verificación incorrecto"));
    }

    @ExceptionHandler(CataNotFoundException.class)
    public ResponseEntity<MessageResponse> handleCataNotFound(CataNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCataStatusException.class)
    public ResponseEntity<MessageResponse> handleInvalidCataStatus(InvalidCataStatusException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UnuathorizedException.class)
    public ResponseEntity<MessageResponse> handleUnuathorized(UnuathorizedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyJoinedException.class)
    public ResponseEntity<MessageResponse> handleUserAlreadyJoined(UserAlreadyJoinedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserNotJoinedException.class)
    public ResponseEntity<MessageResponse> handleUserNotJoined(UserNotJoinedException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(CodeDontMatchException.class)
    public ResponseEntity<MessageResponse> handleCodeDontMatch(CodeDontMatchException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<MessageResponse> handleReportNotFound(ReportNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageResponse> handleBadCredentials(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<MessageResponse> handleDisabled(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MessageResponse("Cuenta deshabilitada"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Error interno del servidor"));
    }
}
