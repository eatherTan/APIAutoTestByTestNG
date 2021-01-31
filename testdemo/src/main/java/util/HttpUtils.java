package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils{
    /**
     * * HTTP请求处理
     *
     * @author 绵知了
     *
     */
        private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
        private static final Charset UTF_8 = Charset.forName("UTF-8");
        public static final String POST = "POST";
        public static final String GET = "GET";
        public static final int TIMEOUT = 30000;
        public static final int FILE_TIMEOUT = 300000;

        // boundary就是request头和上传文件内容的分隔符
        public static final String BOUNDARY = "---------------------------123821742118716";
        public static final String RESULT_FILE = "attachment";

        // Http Get请求
        //////////////////////////////////////////////////////////////////
        /**
         * * 发送HTTP请求
         *
         * @param serverUrl
         * @param params
         * @return
         */
        public static String getHttp(String serverUrl, Map<String, String> params) {
            return getHttp(serverUrl, params, null, TIMEOUT);
        }

        /**
         * * 发送HTTP请求
         *
         * @param serverUrl
         * @param params
         * @return
         */
        public static String getHttp(String serverUrl, Map<String, String> params, Map<String, String> headers) {
            return getHttp(serverUrl, params, headers, TIMEOUT);
        }

        /**
         * 发送HTTP请求
         *
         * @param serverUrl
         * @param params
         * @param headers
         * @param time
         * @return
         */
        public static String getHttp(String serverUrl, Map<String, String> params, Map<String, String> headers,
                                     Integer time) {
            logger.info("\n【请求地址】: {} \n【请求参数】: {} \n【请求头部】: {}", serverUrl, params, headers);
            HttpURLConnection conn = null;
            BufferedReader br = null;
            try {
                // 构建请求参数
                StringBuilder sbParam = new StringBuilder(serverUrl);
                if (mapIsNotEmpty(params)) {
                    sbParam.append("?");
                    if (mapIsNotEmpty(params)) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            sbParam.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                            sbParam.append("=");
                            sbParam.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                            sbParam.append("&");
                        }
                    }

                }
                // 发送请求
                URL url = new URL(sbParam.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                conn.setRequestMethod(GET);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setReadTimeout(time);
                conn.setConnectTimeout(time);
                if (mapIsNotEmpty(headers)) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                logger.info("[HttpsUtls] [getHttp] connection success");

                // 获取状态码
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    logger.error("[HttpsUtls] [getHttp] response status = {}", responseCode);
                    return "";
                }

                // 取得返回信息
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                logger.info("\n【响应数据】: {} ", result);
                // JSON处理
                return result.toString();
            } catch (Exception e) {
                logger.error("[HttpsUtls] [getHttp Exception", e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls] [getHttp] br.close is exception. {}", e);
                    }
                }
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls] [getHttp] conn.disconnect() is exception. {}", e);
                    }
                }
            }

            return "";
        }

        // HTTP POST请求
        ///////////////////////////////////////////////////////////////////////////////////
        /**
         * POST 请求
         *
         * @param serverUrl
         * @param params
         * @return
         */
        public static String postHttp(String serverUrl, Map<String, String> params) {
            return postHttp(serverUrl, params, null);
        }

        /**
         * POST 请求
         *
         * @param serverUrl
         * @param params
         * @param headers
         * @return
         */
        public static String postHttp(String serverUrl, Map<String, String> params, Map<String, String> headers) {
            return postHttp(serverUrl, params, headers, POST, TIMEOUT);
        }

        /**
         * 发送HTTP请求
         *
         * @param serverUrl
         * @param params
         * @param headers
         * @param timeout
         * @return
         */
        public static String postHttp(String serverUrl, Map<String, String> params, Map<String, String> headers,
                                      String type, int timeout) {
            logger.info("\n【请求地址】: {} \n【请求参数】: {} \n【请求头部】: {}", serverUrl, params, headers);
            HttpURLConnection conn = null;
            OutputStreamWriter osw = null;
            BufferedReader br = null;
            try {
                // 发送请求
                URL url = new URL(serverUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                conn.setRequestMethod(type);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setReadTimeout(timeout);
                conn.setConnectTimeout(timeout);
                if (mapIsNotEmpty(headers)) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }

                // 构建请求参数
                StringBuilder sbParam = new StringBuilder();
                if (mapIsNotEmpty(params)) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        sbParam.append(entry.getKey());
                        sbParam.append("=");
                        sbParam.append(entry.getValue());
                        sbParam.append("&");
                    }
                }

                // 获得输出流
                osw = new OutputStreamWriter(conn.getOutputStream(), UTF_8);
                osw.write(sbParam.toString());
                osw.flush();
                osw.close();

                logger.info("[HttpsUtls][postHttp] connection success.");
                // 获取状态码
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    logger.error("[HttpsUtls][postHttp] response status = {}", responseCode);
                    return "";
                }

                // 取得返回信息
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                logger.info("\n【响应数据】: {} ", result);
                // JSON处理
                return result.toString();
            } catch (Exception e) {
                logger.error("[HttpsUtls][postHttp] Exception", e);
            } finally {
                if (osw != null) {
                    try {
                        osw.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postHttp] osw.close() is Exception. {}", e);
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postHttp] br.close() is Exception. {}", e);
                    }
                }
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postHttp] conn.disconnect() is Exception. {}", e);
                    }
                }
            }

            return "";
        }

        // HTTP 推送JSON格式
        ///////////////////////////////////////////
        /**
         * Post 请求 格式为 application/json
         *
         * @param serverUrl
         * @param urlParams
         * @param headers
         * @param jsonParam
         * @return
         */
        public static String postJson(String serverUrl, Map<String, String> urlParams, Map<String, String> headers,
                                      String jsonParam) {
            logger.info("\n【请求地址】: {} \n【请求参数】: {} \n【请求头部】: {}\n【请求JSON】: {}", serverUrl, urlParams, headers, jsonParam);
            HttpURLConnection conn = null;
            OutputStreamWriter osw = null;
            BufferedReader br = null;
            try {
                // 构建请求参数
                StringBuilder sbParam = new StringBuilder(serverUrl);
                if (mapIsNotEmpty(urlParams)) {
                    sbParam.append("?");
                    if (mapIsNotEmpty(urlParams)) {
                        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                            sbParam.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                            sbParam.append("=");
                            sbParam.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                            sbParam.append("&");
                        }
                    }

                }
                // 发送请求
                URL url = new URL(sbParam.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                if (mapIsNotEmpty(headers)) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                conn.setRequestMethod(POST);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setReadTimeout(TIMEOUT);
                conn.setConnectTimeout(TIMEOUT);
                // 设置文件类型:
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                // 设置接收类型否则返回415错误
                conn.setRequestProperty("accept", "application/json");
                // 往服务器里面发送数据
                if (jsonParam != null) {
                    byte[] writebytes = jsonParam.getBytes();
                    // 设置文件长度
                    conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                }

                // 获得输出流
                osw = new OutputStreamWriter(conn.getOutputStream(), UTF_8);
                osw.write(jsonParam);
                osw.flush();
                osw.close();
                logger.info("[HttpsUtls][postJson] connection success.");

                // 构建请求参数
                sbParam = new StringBuilder();

                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    logger.error("[HttpsUtls][postJson] responseCode：{}", responseCode);
                    return "";
                }
                // 取得返回信息
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                logger.info("\n【响应数据】: {} ", result);
                // JSON处理
                return result.toString();
            } catch (Exception e) {
                logger.error("[HttpsUtls][postJson] Exception", e);
            } finally {
                if (osw != null) {
                    try {
                        osw.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postJson] osw.close() is Exception. {}", e);
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postJson] br.close() is Exception. {}", e);
                    }
                }
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postJson] conn.disconnect() is Exception. {}", e);
                    }
                }
            }

            return "";
        }

        /**
         * post 请求 格式为 text/plain
         *
         * @param serverUrl
         * @param urlParams
         * @param headers
         * @param jsonParam
         * @return
         */
        public static String postText(String serverUrl, Map<String, String> urlParams, Map<String, String> headers,
                                      String jsonParam) {
            logger.info("\n【请求地址】: {} \n【请求参数】: {} \n【请求头部】: {}\n【请求JSON】: {}", serverUrl, urlParams, headers, jsonParam);
            HttpURLConnection conn = null;
            OutputStreamWriter osw = null;
            BufferedReader br = null;
            try {
                // 构建请求参数
                StringBuilder sbParam = new StringBuilder(serverUrl);
                if (mapIsNotEmpty(urlParams)) {
                    sbParam.append("?");
                    if (mapIsNotEmpty(urlParams)) {
                        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                            sbParam.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                            sbParam.append("=");
                            sbParam.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                            sbParam.append("&");
                        }
                    }

                }
                // 发送请求
                URL url = new URL(sbParam.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                if (mapIsNotEmpty(headers)) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                conn.setRequestMethod(POST);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setReadTimeout(TIMEOUT);
                conn.setConnectTimeout(TIMEOUT);
                // 设置文件类型:
                conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
                // 设置接收类型否则返回415错误
                conn.setRequestProperty("accept", "application/json");
                // 往服务器里面发送数据
                if (jsonParam != null) {
                    byte[] writebytes = jsonParam.getBytes();
                    // 设置文件长度
                    conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                }

                // 获得输出流
                osw = new OutputStreamWriter(conn.getOutputStream(), UTF_8);
                osw.write(jsonParam);
                osw.flush();
                osw.close();
                logger.info("[HttpsUtls][postText] connection success.");

                // 构建请求参数
                sbParam = new StringBuilder();

                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    logger.error("[HttpsUtls][postText] responseCode：{}", responseCode);
                    return "";
                }
                // 取得返回信息
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                logger.info("\n【响应数据】: {} ", result);
                // JSON处理
                return result.toString();
            } catch (Exception e) {
                logger.error("[HttpsUtls][postText] Exception", e);
            } finally {
                if (osw != null) {
                    try {
                        osw.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postText] osw.close() is Exception. {}", e);
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postText] br.close() is Exception. {}", e);
                    }
                }
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][postText] conn.disconnect() is Exception. {}", e);
                    }
                }
            }

            return "";
        }

        /**
         * * 上传多个文件
         *
         * @param urlStr
         * @param urlParams
         * @param fileMap
         * @return
         */
        public static String formUpload(String urlStr, Map<String, String> urlParams, Map<String, String> fileMap) {
            if (mapIsEmpty(fileMap)) {
                logger.info("\n【请求地址】: {} \n【请求参数】: {} ", urlStr, urlParams);
            } else {
                logger.info("\n【请求地址】: {} \n【请求参数】: {} \n【请求文件】: {}", urlStr, urlParams, fileMap);
            }

            String res = "";
            HttpURLConnection conn = null;
            DataInputStream in = null;
            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(FILE_TIMEOUT);
                conn.setReadTimeout(FILE_TIMEOUT);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

                OutputStream out = new DataOutputStream(conn.getOutputStream());
                // urlParams
                if (urlParams != null) {
                    StringBuilder strBuf = new StringBuilder();
                    Iterator<Map.Entry<String, String>> iter = urlParams.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, String> entry = iter.next();
                        String inputName = entry.getKey();
                        String inputValue = entry.getValue();
                        if (inputValue == null) {
                            continue;
                        }
                        strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue);
                    }
                    out.write(strBuf.toString().getBytes());
                }

                // file
                if (fileMap != null) {
                    Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, String> entry = iter.next();
                        String inputName = entry.getKey();
                        String inputValue = entry.getValue();
                        if (inputValue == null) {
                            continue;
                        }
                        File file = new File(inputValue);
                        if (!file.exists()) {
                            logger.error("[HttpsUtls][formUpload] file is not exist. name = {}", inputValue);
                            return res;
                        }
                        String filename = file.getName();

                        StringBuilder strBuf = new StringBuilder();
                        strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
                                + "\"\r\n");
                        strBuf.append("Content-Type:" + "multipart/form-data;boundary=" + "*****" + "\r\n\r\n");

                        out.write(strBuf.toString().getBytes());

                        in = new DataInputStream(new FileInputStream(file));
                        int bytes = 0;
                        byte[] bufferOut = new byte[1024];
                        while ((bytes = in.read(bufferOut)) != -1) {
                            out.write(bufferOut, 0, bytes);
                        }
                        in.close();
                    }
                }

                byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                out.write(endData);
                out.flush();
                out.close();

                // 读取返回数据
                StringBuilder strBuf = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line);
                }
                res = strBuf.toString();
                reader.close();
                reader = null;
            } catch (Exception e) {
                logger.error("[HttpsUtls][formUpload] is Exception. {}", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls][formUpload] in.close is Exception. {}", e);
                    }
                }
            }
            logger.info("\n【响应数据】: {} ", res);
            return res;
        }

        /**
         * * 通过url下载单个文件
         *
         * @param serverUrl
         * @param params
         * @param folder
         * @param filePath
         * @return
         */
        public static String downloadFile(String serverUrl, Map<String, String> params, String folder, String filePath) {
            logger.info("\n【请求地址】: {} \n【请求参数】: {} ", serverUrl, params);
            HttpURLConnection conn = null;
            InputStream in = null;
            FileOutputStream os = null;
            try {
                // 构建请求参数
                StringBuilder sbParam = new StringBuilder(serverUrl);
                if (mapIsNotEmpty(params)) {
                    sbParam.append("?");
                    if (mapIsNotEmpty(params)) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            sbParam.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                            sbParam.append("=");
                            sbParam.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                            sbParam.append("&");
                        }
                    }

                }
                // 发送请求
                URL url = new URL(sbParam.toString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                conn.setRequestMethod(GET);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setReadTimeout(TIMEOUT);
                conn.setConnectTimeout(TIMEOUT);

                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    logger.error("[HttpsUtls][downloadFile] responseCode：{}", responseCode);
                    return "";
                }

                boolean resultIsFile = parseIsFile(conn);
                // 返回不是文件流，则返回字符串
                if (!resultIsFile) {
                    // 读取返回数据
                    StringBuilder strBuf = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        strBuf.append(line);
                    }
                    reader.close();
                    reader = null;
                    logger.info("\n【响应数据】: {} ", strBuf);
                    return strBuf.toString();
                }

                // 返回文件流
                File targetFile = new File(folder, filePath);
                in = conn.getInputStream();
                os = new FileOutputStream(targetFile);
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = in.read(buffer)) > -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
                return filePath;
            } catch (Exception e) {
                logger.error("[HttpsUtls][downloadFile] Exception", e);
                return StringUtils.EMPTY;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls] [downloadFile] in.close is exception. {}", e);
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls] [downloadFile] os.close is exception. {}", e);
                    }
                }
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                        logger.error("[HttpsUtls] [downloadFile] conn.disconnect is exception. {}", e);
                    }
                }
            }
        }

        // 通用
        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * * 是否是文件
         *
         * @param conn
         * @return
         */
        private static boolean parseIsFile(HttpURLConnection conn) {
            String isFile = conn.getHeaderField("Content-Disposition");
            if (StringUtils.isEmpty(isFile)) {
                return false;
            }
//            StringUtils.con/s
            return StringUtils.contains(isFile, RESULT_FILE);
        }

        /**
         * 判断Map是否不为空
         *
         * @param m
         * @return
         */
        public static boolean mapIsNotEmpty(Map<?, ?> m) {
            return !mapIsEmpty(m);
        }

        /**
         * 判断Map是否为空
         *
         * @param m
         * @return
         */
        public static boolean mapIsEmpty(Map<?, ?> m) {
            return m == null || m.isEmpty();
        }



}