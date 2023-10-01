package pub.carzy.free_port_mapper.common.basic;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageRequest extends BasicRequest {
    private Integer pageNum = 1;
    private Integer pageSize = Integer.MAX_VALUE;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 是否独立的(单用户)
     */
    private Boolean single;

    /**
     * 过滤其他用户数据
     */
    private Boolean skipOtherUser;

    /**
     * 使用pageNum
     *
     * @return
     */
    @Deprecated
    public Integer getPageIndex() {
        return (pageNum - 1) * getPageSize();
    }
}
