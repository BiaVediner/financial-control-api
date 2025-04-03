package br.com.api.application.handler;

import br.com.api.application.dto.ErrorDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.GENERIC_ERROR;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(NotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(ex.getCode());
        errorDTO.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleException(BadRequestException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(ex.getCode());
        errorDTO.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ErrorDTO handleException(UnauthorizedException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(ex.getCode());
        errorDTO.setMessage(ex.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(GENERIC_ERROR.getCode());
        errorDTO.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

}
