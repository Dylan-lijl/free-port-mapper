package pub.carzy.free_port_mapper.server.modules.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicResponse;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadResponse extends BasicResponse<Void> {
    private String path;

    public UploadResponse() {
    }

    public UploadResponse(String path) {
        this.path = path;
    }
}
