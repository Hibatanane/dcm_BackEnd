package prjt.dcm.Exceptions.Handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import prjt.dcm.Exceptions.MinioException;
import prjt.dcm.Shared.ErrorMessage;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(value = {MinioException.class})
    public ResponseEntity<Object> minioException(MinioException e) {

        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(e.getMessage())
                .code(500)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
