package southwest.monsoon.module.common.web.handler;

import org.springframework.http.HttpStatus;
import southwest.monsoon.module.common.web.result.msg.ReMsg;

public interface WebResultConverter<T> {
    T convert(HttpStatus status, ReMsg reMsg, Object data);
}
