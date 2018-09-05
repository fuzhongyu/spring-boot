package com.fzy.core.controller.common;

import com.google.common.collect.Maps;
import com.fzy.core.base.BaseController;
import com.fzy.core.base.ResponseResult;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.util.FileUploadUtil;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传Controller
 *
 * @author Fucai
 * @date 2018/3/24
 */
@RestController
@RequestMapping(value = "${custom.ihttp}/upload")
public class FileUploadController extends BaseController{

  /**
   * 上传头像
   */
  @PostMapping(value = "")
  public ResponseResult uploadVersionFile(@RequestParam("file") MultipartFile file) {
    String filePath = FileUploadUtil.uploadFile(file);

    Map<String, Object> returnMap = Maps.newHashMap();
    returnMap.put("filePath", filePath);
    return responseEntity(ErrorsMsg.SUCC_0, "", returnMap);
  }
}
