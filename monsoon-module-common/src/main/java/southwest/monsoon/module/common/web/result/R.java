package southwest.monsoon.module.common.web.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import southwest.monsoon.module.common.web.result.exception.WebException;
import southwest.monsoon.module.common.web.result.msg.MultiLangMsg;
import southwest.monsoon.module.common.web.result.msg.ReMsg;

import java.io.Serializable;

@Getter
@ToString
public class R<T> implements Serializable {

    @Schema(description = "Response code", requiredMode = Schema.RequiredMode.REQUIRED)
    private int code;
    @Schema(description = "Response message", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
    @JsonIgnore
    private ReMsg reMsg;
    @Schema(description = "Response data")
    private T data;

    private static final ReMsg okMsg = MultiLangMsg.key("monsoon.web.msg.Success");

    public static <T> R<T> success() {
        return new R(HttpStatus.OK, okMsg, null);
    }

    public static <T> R<T> success(ReMsg reMsg) {
        return new R(HttpStatus.OK, reMsg, null);
    }

    public static <T> R<T> success(T data) {
        return new R(HttpStatus.OK, okMsg, data);
    }

    public static <T> R<T> success(ReMsg reMsg, T data) {
        return new R(HttpStatus.OK, reMsg, data);
    }

    public static R error(ReMsg reMsg) {
        return new R(HttpStatus.INTERNAL_SERVER_ERROR, reMsg, null);
    }

    public static R error(HttpStatus status, ReMsg reMsg) {
        return error(status, reMsg, null);
    }

    public static R error(HttpStatus status, ReMsg reMsg, Object data) {
        if (status == null) {
            throw WebException.err(MultiLangMsg.key("monsoon.web.msg.HttpStatusNotNull"));
        }
        return new R(status, reMsg, data);
    }

    private R(HttpStatus status, ReMsg reMsg, T data) {
        if (reMsg == null) {
            throw WebException.err(MultiLangMsg.key("monsoon.web.msg.RespMsgNotNull"));
        }
        this.code = status.value();
        this.reMsg = reMsg;
        this.message = reMsg.getMsg();
        this.data = data;
    }

    private R() {
    }
}
