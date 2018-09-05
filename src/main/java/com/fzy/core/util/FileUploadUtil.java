package com.fzy.core.util;

import com.fzy.core.base.ServiceException;
import com.fzy.core.config.CustomConfigProperties;
import com.fzy.core.config.ErrorsMsg;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 * @author Fucai
 * @date 2018/3/24
 */
public class FileUploadUtil {


  /**
   * 上传文件
   *
   * @return 返回文件路径
   */
  public static String uploadFile(MultipartFile file) {

    String filePath = createFilePath(file);

    File dest = new File(filePath);

    try {
      file.transferTo(dest);
    } catch (IOException e) {
      e.printStackTrace();
      throw new ServiceException(ErrorsMsg.ERR_1, "文件上传失败");
    }

    return CustomConfigProperties.PROJECT_URL + filePath;
  }


  /**
   * 创建文件路径
   */
  private static String createFilePath(MultipartFile file) {

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    String stringDate = sf.format(new Date());
    String filePath = CustomConfigProperties.UPLOAD_FILE_PATH + stringDate + "/";
    File dir = new File(filePath);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    String fileName = file.getOriginalFilename();
    String suffix = fileName.substring(fileName.indexOf("."));
    if (suffix == null) {
      suffix = "";
    }
    String defineFileName = file.hashCode() + suffix;

    return filePath + defineFileName;
  }
}
