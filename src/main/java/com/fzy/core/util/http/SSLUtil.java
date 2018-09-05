package com.fzy.core.util.http;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ssl工具类
 *
 * @author Fucai
 * @date 2018/4/7
 */
public abstract class SSLUtil {

  private static Logger logger = LoggerFactory.getLogger(SSLUtil.class);

  /**
   * 创建https请求对象
   * @return
   * @version 1.0.0 2015年6月29日
   */
  public static CloseableHttpClient createSSLClientDefault() {
    try {
      SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
          null, new TrustStrategy() {
            // 信任所有
            @Override
            public boolean isTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
              return true;
            }
          }).build();
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
      return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    } catch (Exception e) {
      logger.error("请求失败", e);
    }
    return HttpClients.createDefault();
  }

}