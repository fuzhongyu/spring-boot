package com.fzy.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 自定义配置文件
 *
 * @author Fucai
 * @date 2018/3/19
 */
@Component
@Lazy(false)
public class CustomConfigProperties {

  /**
   * 项目名称
   */
  public static String PROJECT_NAME;

  /**
   * 版本信息
   */
  public static String PROJECT_CONSOLE_URL;

  /**
   * 页码
   */
  public static Integer PAGE_NO;

  /**
   * 页面大小
   */
  public static Integer PAGE_SIZE;

  /**
   * token过期时间
   */
  public static Long TOKEN_EXPIRATION;

  /**
   * 请求参数最大长度
   */
  public static Integer MAX_CONTEXT_LENGTH;

  /**
   * 上传文件最大限制
   */
  public static Integer MAX_FILE_SIZE;

  /**
   * 服务器地址
   */
  public static String SERVER_URL;


  /**
   * 上传文件路径
   */
  public static String UPLOAD_FILE_PATH;

  /**
   * 项目全路径
   */
  public static String PROJECT_URL;


  @Value("${custom.project_name}")
  public void setProjectName(String projectName) {
    PROJECT_NAME = projectName;
  }

  @Value("${custom.console}")
  public void setConsole(String console) {
    PROJECT_CONSOLE_URL = console;
  }

  @Value("${custom.page_no}")
  public void setPageNo(Integer pageNo) {
    PAGE_NO = pageNo;
  }

  @Value("${custom.page_size}")
  public void setPageSize(Integer pageSize) {
    PAGE_SIZE = pageSize;
  }

  @Value("${custom.token_expiration}")
  public void setTokenExpiration(Long tokenExpiration) {
    TOKEN_EXPIRATION = tokenExpiration;
  }

  @Value("${custom.max_context_length}")
  public void setMaxContextLength(Integer maxContextLength) {
    MAX_CONTEXT_LENGTH = maxContextLength;
  }

  @Value("${custom.max_file_size}")
  public void setMaxFileSize(Integer maxFileSize) {
    MAX_FILE_SIZE = maxFileSize;
  }

  @Value("${custom.server_url}")
  public void setServerUrl(String serverUrl) {
    SERVER_URL = serverUrl;
  }

  @Value("${custom.upload_file_path}")
  public void setUploadFilePath(String uploadFilePath) {
    UPLOAD_FILE_PATH = uploadFilePath;
  }

  @Value("${custom.project_server_url}")
  public void setProjectUrl(String projectUrl) {
    PROJECT_URL = projectUrl;
  }
}
