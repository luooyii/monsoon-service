package southwest.monsoon.module.common.web.handler;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import southwest.monsoon.module.common.web.result.exception.WebException;
import southwest.monsoon.module.common.web.result.msg.MultiLangMsg;
import southwest.monsoon.module.common.web.result.msg.ReMsg;
import southwest.monsoon.module.common.web.result.msg.SimpleMsg;

import java.util.List;
import java.util.Set;

/**
 * @version 7/2/2021
 */
@Slf4j
@ControllerAdvice
public class WebExceptionHandler {
    private static final ReMsg errMsg = MultiLangMsg.key("monsoon.web.msg.InternalError");

    @Autowired
    WebResultConverter webResultConverter;

    /**
     * Handle custom exceptions
     */
    @ExceptionHandler(WebException.class)
    @ResponseBody
    public Object handleWebException(HttpServletResponse resp, WebException e) {
        log.error("Handle WebException.", e);
        resp.setStatus(e.getHttpStatus().value());
        return webResultConverter.convert(e.getHttpStatus(), e.getReMsg(), e.getData());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(HttpServletResponse resp, Exception e) {
        log.error("Handle Exception.", e);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        resp.setStatus(httpStatus.value());
        return webResultConverter.convert(httpStatus, errMsg, e.getMessage());
    }

    /**
     * Validation Error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(HttpServletResponse resp, MethodArgumentNotValidException e) {
        log.error("Handle MethodArgumentNotValidException.", e);
        HttpStatus httpStatus = HttpStatus.PRECONDITION_FAILED;
        resp.setStatus(httpStatus.value());
        return generateValidationErrorResult(httpStatus, e.getAllErrors());
    }


    /**
     * Validation Error
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(HttpServletResponse resp, ConstraintViolationException e) {
        log.error("Handle MethodArgumentNotValidException.", e);
        HttpStatus httpStatus = HttpStatus.PRECONDITION_FAILED;
        resp.setStatus(httpStatus.value());
        String msg = getExceptionMsg(e.getConstraintViolations());
        return webResultConverter.convert(httpStatus, SimpleMsg.text(msg), null);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Object handleBeanPropertyBindingResult(HttpServletResponse resp, BindException e) {
        log.error("Handle BindException", e);
        HttpStatus httpStatus = HttpStatus.PRECONDITION_FAILED;
        resp.setStatus(httpStatus.value());
        return generateValidationErrorResult(httpStatus, e.getAllErrors());
    }

    private Object generateValidationErrorResult(HttpStatus httpStatus, List<ObjectError> objectErrors) {
        StringBuilder errorMessage = new StringBuilder();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            objectErrors.forEach(error -> {
                errorMessage.append(error.getDefaultMessage());
                errorMessage.append("   ");
            });
        } else {
            errorMessage.append("Validation Error");
        }
        return webResultConverter.convert(httpStatus, SimpleMsg.text(errorMessage.toString()), null);
    }

    public static <T extends ConstraintViolation> String getExceptionMsg(Set<T> validationResults) {
        if (!CollectionUtils.isEmpty(validationResults)) {
            StringBuilder stringBuilder = new StringBuilder();
            String padding = "   ";
            validationResults.forEach(result -> {
                stringBuilder.append(result.getMessage());
                stringBuilder.append(padding);
            });
            stringBuilder.delete(stringBuilder.length() - padding.length(), stringBuilder.length());
            return stringBuilder.toString();
        }
        return "ConstraintViolation is empty!";
    }
}
