package util;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.GeneratedMessage;
import com.googlecode.protobuf.format.JsonFormat;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

public class ResponseResult {
    /*public static <T extends GeneratedMessage> T getResponse(ByteArrayOutputStream buf, Class<? extends GeneratedMessage> clazz) {
        T message = null;
        try {
            message = (T) clazz.getMethod("parseFrom", byte[].class).invoke(null, buf.toByteArray());
            System.out.print("\nresponse:" + + JSON.toJSONString(JsonFormat(message)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/
}
