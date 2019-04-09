package com.fzy.core.controller.console.system;

import com.fzy.core.base.BaseController;
import com.fzy.core.base.PageEntity;
import com.fzy.core.base.ResponseResult;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.system.Role;
import com.fzy.core.service.system.RoleService;
import com.fzy.core.util.ParamUtil;
import com.fzy.core.validator.group.Group_1;
import com.fzy.core.vo.system.RolePermissionVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 角色controller
 *
 * @author Fucai
 * @date 2019/2/16
 */
@RestController
@RequestMapping(value = "${custom.console}/system/role")
public class RoleController extends BaseController {

    public final static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    /**
     * 查询角色列表
     * @param role
     * @return
     */
    @GetMapping(value = "")
    public ResponseResult findList(Role role){
        return responseEntity(ErrorsMsg.SUCC_0, roleService.findList(role));
    }

    /**
     * 查询角色列表（分页）
     *
     * @param role
     * @return
     */
    @GetMapping(value = "page")
    @RequiresPermissions(value = "system:role:view")
    public ResponseResult findPage(Role role, Integer pageNo, Integer pageSize) {
        PageEntity<Role> pageEntity = roleService.findPage(role, pageNo, pageSize);
        return responseEntity(ErrorsMsg.SUCC_0, pageEntity);
    }

    /**
     * 根据id查询角色
     *
     * @param roleId
     * @return
     */
    @GetMapping(value = "/{roleId}")
    @RequiresPermissions(value = "system:role:view")
    public ResponseResult get(@PathVariable("roleId") String roleId, HttpServletRequest request) {

        if (ParamUtil.LongParam(roleId) == null) {
            throw new ServiceException(ErrorsMsg.ERR_2);
        }
        return responseEntity(ErrorsMsg.SUCC_0, roleService.get(ParamUtil.LongParam(roleId)));
    }

    /**
     * 新增角色
     *
     * @param role
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "")
    @RequiresPermissions(value = "system:role:edit")
    public ResponseResult add(@RequestBody @Validated({Group_1.class}) Role role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            doValidateHandler(bindingResult);
        }
        roleService.save(role);
        return responseEntity(ErrorsMsg.SUCC_0, role);
    }

    /**
     * 获取角色权限
     * @param roleId
     * @return
     */
    @GetMapping(value = "/{roleId}/permission")
    public ResponseResult getRolePermission(@PathVariable("roleId") String roleId){
        if (ParamUtil.LongParam(roleId) == null){
            throw new ServiceException(ErrorsMsg.ERR_2);
        }
        return responseEntity(ErrorsMsg.SUCC_0, roleService.getPermissionListByRoleId(ParamUtil.LongParam(roleId)));
    }

    /**
     * 给角色分配权限
     * @param vo
     * @return
     */
    @PostMapping(value = "/{roleId}/permission")
    @RequiresPermissions(value = "system:role:edit")
    public ResponseResult updateRolePermission(@PathVariable("roleId") String roleId, @RequestBody @Validated({Group_1.class}) RolePermissionVo vo, BindingResult bindingResult){
        if (ParamUtil.LongParam(roleId) == null) {
            throw new ServiceException(ErrorsMsg.ERR_2);
        }
        if (bindingResult.hasErrors()){
            doValidateHandler(bindingResult);
        }
        roleService.updateRolePermission(ParamUtil.LongParam(roleId),vo.getPermissionList());
        return responseEntity(ErrorsMsg.SUCC_0);
    }


    /**
     * 删除角色
     *
     * @return
     */
    @DeleteMapping(value = "{roleId}")
    @RequiresPermissions(value = "system:role:del")
    public ResponseResult delete(@PathVariable( value = "roleId") String roleId, HttpServletRequest request) {
        if (ParamUtil.LongParam(roleId) == null) {
            throw new ServiceException(ErrorsMsg.ERR_2);
        }
        roleService.delete(ParamUtil.LongParam(roleId));
        return responseEntity(ErrorsMsg.SUCC_0);
    }

}
