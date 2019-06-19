package com.fzy.core.controller.console.system;

import com.fzy.core.base.BaseController;
import com.fzy.core.base.ResponseResult;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.service.system.PermissionService;
import com.fzy.core.util.ParamUtil;
import com.fzy.core.validator.group.Group_1;
import com.fzy.core.vo.system.PermissionVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 权限
 *
 * @author Fucai
 * @date 2019/1/29
 */
@RestController
@RequestMapping(value = "${custom.console}/system/permission")
public class PermissionController extends BaseController {

    public final static Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取菜单列表
     *
     * @return
     */
    @GetMapping(value = "menu")
    @RequiresPermissions({"system:permission:view"})
    public ResponseResult getMenu() {
        return responseEntity(ErrorsMsg.SUCC_0, permissionService.getMenuTree());
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "{id}")
    @RequiresPermissions({"system:permission:view"})
    public ResponseResult getInfo(@PathVariable("id") String id) {
        if (ParamUtil.LongParam(id) == null) {
            throw new ServiceException(ErrorsMsg.ERR_2);
        }
        return responseEntity(ErrorsMsg.SUCC_0, permissionService.get(ParamUtil.LongParam(id)));
    }

    /**
     * 保存修改权限
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "")
    @RequiresPermissions({"system:permission:edit"})
    public ResponseResult save(@RequestBody @Validated({Group_1.class}) PermissionVo vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            doValidateHandler(bindingResult);
        }
        permissionService.save(PermissionVo.convert2Entity(vo));
        return responseEntity(ErrorsMsg.SUCC_0);
    }

    /**
     * 删除权限
     *
     * @return
     */
    @DeleteMapping(value = "")
    @RequiresPermissions({"system:permission:del"})
    public ResponseResult delete(@RequestParam(value = "idList[]", required = false) Long[] idList) {
        if (idList != null && idList.length > 0) {
            permissionService.batchDelete(Arrays.asList(idList));
        } else {
            throw new ServiceException(ErrorsMsg.ERR_1001, "请选择要删除的数据");
        }
        return responseEntity(ErrorsMsg.SUCC_0);
    }

}
