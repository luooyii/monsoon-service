package southwest.monsoon.module.common.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import southwest.monsoon.module.common.web.result.R;
import southwest.monsoon.module.common.web.result.msg.ReMsg;

@Component
public class DefaultResultConverter implements WebResultConverter<R> {
    @Override
    public R convert(HttpStatus status, ReMsg reMsg, Object data) {
        return R.error(status, reMsg, data);
    }
}
