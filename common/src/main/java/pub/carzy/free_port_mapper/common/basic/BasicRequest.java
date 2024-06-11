package pub.carzy.free_port_mapper.common.basic;

import lombok.Data;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;

import java.io.Serializable;

/**
 * @author admin
 */
@Data
public class BasicRequest implements Serializable {
    private ExportRequestParam _export;
    private String token;
}
