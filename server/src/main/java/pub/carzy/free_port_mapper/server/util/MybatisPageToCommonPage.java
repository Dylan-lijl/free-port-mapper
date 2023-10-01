package pub.carzy.free_port_mapper.server.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import pub.carzy.free_port_mapper.common.api.CommonPage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
public class MybatisPageToCommonPage {
    @SuppressWarnings("unchecked")
    public static <T, O> CommonPage<T> convert(Page<O> page, Class<T> clazz) {
        CommonPage<T> result = new CommonPage<>();
        result.setPageNum(Convert.toInt(page.getCurrent()));
        // 一页多少条数据
        result.setPageSize(Convert.toInt(page.getSize()));
        // 总数据数量
        result.setTotal(page.getTotal());
        // 总页数
        result.setTotalPage(Convert.toInt(page.getTotal() / page.getSize() + 1));
        //处理当前页数据
        result.setList(new ArrayList<>(page.getRecords().size()));
        if (page.getRecords().isEmpty()) {
            return result;
        }
        //取出第一条对比对应的class
        List<O> records = page.getRecords();
        Class<?> c = null;
        for (O o : records) {
            if (o != null) {
                c = o.getClass();
                break;
            }
        }
        if (c == null) {
            return result;
        }
        //如果是同一个class就不拷贝
        if (c == clazz) {
            result.setList((List<T>) page.getRecords());
        } else {
            //遍历拷贝属性并将对象添加进去
            for (O o : records) {
                if (o == null) {
                    result.getList().add(null);
                } else {
                    result.getList().add(BeanUtil.copyProperties(o, clazz));
                }
            }
        }
        return result;
    }
}
