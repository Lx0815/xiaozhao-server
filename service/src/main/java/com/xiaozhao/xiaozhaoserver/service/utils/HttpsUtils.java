package com.xiaozhao.xiaozhaoserver.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaozhao.xiaozhaoserver.service.exception.BadParameterException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class HttpsUtils {
    private static final String ENCODING = "UTF-8";

    private static ObjectMapper objectMapper = new ObjectMapper();


    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static <T> T httpGet(String httpUrl, Class<T> responseClass) {
        BufferedReader input = null;
        StringBuilder sb = null;
        URL url;
        HttpURLConnection con = null;
        try {
            url = new URL(httpUrl);
            try {
                // trust all hosts
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                if (url.getProtocol().equalsIgnoreCase("https")) {
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                    con = https;
                } else {
                    con = (HttpURLConnection) url.openConnection();
                }
                input = new BufferedReader(new InputStreamReader(con.getInputStream()));
                sb = new StringBuilder();
                String s;
                while ((s = input.readLine()) != null) {
                    sb.append(s).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } finally {
            // close buffered
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // disconnecting releases the resources held by a connection so they may be closed or reused
            if (con != null) {
                con.disconnect();
            }
        }
        String json = Objects.isNull(sb) ? null : sb.toString();
        try {
            return objectMapper.readValue(json, responseClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String wrapperGetParameter(@NotNull String url, @NotNull Map<String, String> map) {
        url = StringUtils.removeEnd(url, "/");
        StringBuilder stringBuilder = new StringBuilder(url).append("?");
        map.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String key, String value) {
                stringBuilder.append(key).append("=").append(value).append("&");
            }
        });
        return StringUtils.removeEnd(stringBuilder.toString(), "&");
    }

    public static String wrapperGetParameter(@NotNull String url, String... args) {
        if (ObjectUtils.isEmpty(args) || args.length % 2 == 1) {
            throw new BadParameterException("此处参数应为偶数个，而接收到奇数个参数");
        }
        Map<String, String> map = new HashMap<>();
        for (int i = args.length - 1; i >= 0; i -= 2) {
            map.put(args[i - 1], args[i]);
        }
        return wrapperGetParameter(url, map);
    }
}