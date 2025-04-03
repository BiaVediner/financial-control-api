package br.com.api.domain.exceptions;

import br.com.api.domain.exceptions.enums.ErrorMessageEnum;
import lombok.Getter;

@Getter
public class BadRequestException extends Exception {
    private String code;

    public BadRequestException(ErrorMessageEnum errorMessage) {
        super(errorMessage.getMessage());
        this.code = errorMessage.getCode();
    }
}