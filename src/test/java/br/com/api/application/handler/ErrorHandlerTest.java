package br.com.api.application.handler;

import br.com.api.application.dto.ErrorDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.exceptions.UnauthorizedException;
import br.com.api.domain.exceptions.enums.ErrorMessageEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    void testHandleWhenIsNotFoundExceptionThenErrorDTO() {
        NotFoundException notFoundException = new NotFoundException(ErrorMessageEnum.CATEGORY_NOT_FOUND);
        ErrorDTO errorDTO = errorHandler.handleException(notFoundException).getBody();

        assertEquals(notFoundException.getCode(), errorDTO.getCode());
        assertEquals(notFoundException.getMessage(), errorDTO.getMessage());
    }

    @Test
    void testHandleWhenIsBadRequestExceptionThenErrorDTO() {
        BadRequestException badRequestException = new BadRequestException(ErrorMessageEnum.SUBCATEGORY_DELETE_CONSTRAIN);
        ErrorDTO errorDTO = errorHandler.handleException(badRequestException).getBody();

        assertEquals(badRequestException.getCode(), errorDTO.getCode());
        assertEquals(badRequestException.getMessage(), errorDTO.getMessage());
    }

    @Test
    void testHandleWhenIsGenericExceptionThenErrorDTO() {
        Exception exception = new Exception("Test Generic Error");
        ErrorDTO errorDTO = errorHandler.handleException(exception).getBody();

        assertEquals(ErrorMessageEnum.GENERIC_ERROR.getCode(), errorDTO.getCode());
        assertNotNull(errorDTO.getMessage());
    }

    @Test
    void testHandleWhenIsUnauthorizedExceptionThenErrorDTO() {
        UnauthorizedException unauthorizedException = new UnauthorizedException(ErrorMessageEnum.UNAUTHORIZED);
        ErrorDTO errorDTO = errorHandler.handleException(unauthorizedException);

        assertEquals(unauthorizedException.getCode(), errorDTO.getCode());
        assertEquals(unauthorizedException.getMessage(), errorDTO.getMessage());
    }
}