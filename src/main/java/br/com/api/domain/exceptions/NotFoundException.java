package br.com.api.domain.exceptions;

import br.com.api.domain.exceptions.enums.ErrorMessageEnum;
import lombok.Getter;

@Getter
public class NotFoundException extends Exception{
    private String code;

    public NotFoundException(ErrorMessageEnum errorMessage) {
        super(errorMessage.getMessage());
        this.code = errorMessage.getCode();
    }
}