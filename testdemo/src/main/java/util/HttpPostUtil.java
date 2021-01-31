package util;

import com.google.protobuf.GeneratedMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

public class HttpPostUtil {
    public static HttpResponse doPost(GeneratedMessage message, String url) {
//        URI uri = UrlBuilder.geturl(url.trim());

        HttpPost post = new HttpPost(url);
        System.out.println(url);
        return httpPostInfo(message, post);
    }

    private static HttpResponse httpPostInfo(GeneratedMessage message, HttpPost post) {
        HttpClient client = HttpClients.createDefault();
        byte[] data = message == null ? new byte[0] : message.toByteArray();
        if( message != null ) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(message.toByteArray());
            InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream);
            post.setEntity(inputStreamEntity);
        }

        post.addHeader("Content-Type","application/json");

        try {
            return client.execute(post);
        } catch (IOException e) {
            throw new RuntimeException("response is null");
        }
    }
}
