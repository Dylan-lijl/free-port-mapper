package pub.carzy.free_port_mapper.common.basic;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础返回对象
 *
 * @author admin
 */
@Data
public class BasicResponse<T> implements Serializable {
    private T id;

    public BasicResponse() {
    }

    public BasicResponse(T id) {
        this.id = id;
    }
}
