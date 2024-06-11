package pub.carzy.client;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.common.response.UserClientInfoResponse;

// @SpringBootTest
class ClientApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testJson(){
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"成功\",\n" +
                "    \"data\": {\n" +
                "        \"id\": 1,\n" +
                "        \"mappings\": []\n" +
                "    }\n" +
                "}";
        CommonResult<UserClientInfoResponse> object = JSONObject.parseObject(json, new TypeReference<CommonResult<UserClientInfoResponse>>(){});
        System.out.println(object);
    }

}
