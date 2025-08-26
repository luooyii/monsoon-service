package southwest.monsoon.module.common.web.result.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import southwest.monsoon.module.common.utils.FormatUtils;
import southwest.monsoon.module.common.web.result.msg.ReMsg;
import southwest.monsoon.module.common.web.result.msg.SimpleMsg;

@Getter
public class WebException extends RuntimeException {
    private HttpStatus httpStatus;
    private ReMsg reMsg;
    private Object data;

    private WebException() {
    }

    @Override
    public String getMessage() {
        if (reMsg != null) {
            return reMsg.getMsg();
        } else {
            return super.getMessage();
        }
    }

    protected WebException(HttpStatus status, ReMsg reMsg, Object data) {
        super(reMsg.getMsg());
        this.httpStatus = status;
        this.reMsg = reMsg;
        this.data = data;
    }

    private WebException(HttpStatus status, ReMsg reMsg, Object data, Throwable cause) {
        super(reMsg.getMsg(), cause);
        this.httpStatus = status;
        this.reMsg = reMsg;
        this.data = data;
    }

    public static WebException err(ReMsg reMsg) {
        return new WebException(HttpStatus.INTERNAL_SERVER_ERROR, reMsg, null);
    }

    public static WebException err(HttpStatus status, ReMsg reMsg) {
        return new WebException(status, reMsg, null);
    }

    public static WebException err(HttpStatus status, ReMsg reMsg, Object data) {
        return new WebException(status, reMsg, data);
    }

    public static RuntimeException cause(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        } else {
            return new WebException(HttpStatus.INTERNAL_SERVER_ERROR, SimpleMsg.text(cause.getMessage()), null, cause);
        }
    }

    public static WebException cause(ReMsg reMsg, Throwable cause) {
        return new WebException(HttpStatus.INTERNAL_SERVER_ERROR, reMsg, null, cause);
    }

    public static WebException cause(HttpStatus status, ReMsg reMsg, Throwable cause) {
        return new WebException(status, reMsg, null, cause);
    }

    public static WebException cause(HttpStatus status, ReMsg reMsg, Object data, Throwable cause) {
        return new WebException(status, reMsg, data, cause);
    }

    @Override
    public String toString() {
        return FormatUtils.formatStr("WebException: [{}] {}", httpStatus.value(), getMessage());
    }
}
