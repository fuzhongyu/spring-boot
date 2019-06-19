package com.fzy.core.controller.console.system;

import javax.servlet.http.HttpServletRequest;

import com.fzy.core.base.BaseController;
import com.fzy.core.base.PageEntity;
import com.fzy.core.base.ResponseResult;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.system.Role;
import com.fzy.core.entity.system.User;
import com.fzy.core.service.system.PermissionService;
import com.fzy.core.service.system.RoleService;
import com.fzy.core.service.system.UserService;
import com.fzy.core.util.ParamUtil;
import com.fzy.core.validator.group.GroupIdNull;
import com.fzy.core.validator.group.Group_1;
import com.fzy.core.validator.group.Group_2;
import com.fzy.core.vo.system.PasswordVo;
import com.fzy.core.vo.system.UserVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

/**
 * 用户controller
 *
 * @author Fucai
 * @date 2018/3/20
 */
@RestController
@RequestMapping(value = "${custom.console}/system/user")
public class UserController extends BaseController {

    public final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;


    /**
     * 查询用户列表
     *
     * @param user
     * @return
     */
    @GetMapping(value = "")
    @RequiresPermissions(value = "system:user:view")
    public ResponseResult findPage(User user, Integer pageNo, Integer pageSize) {
        PageEntity<User> pageEntity = userService.findPage(user, pageNo, pageSize);
        return responseEntity(ErrorsMsg.SUCC_0, pageEntity);
    }

    /**
     * 根据id查询用户
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/{userId}")
    public ResponseResult get(@PathVariable("userId") String userId, HttpServletRequest request) {
        if (ParamUtil.LongParam(userId) == null) {
            throw new ServiceException(ErrorsMsg.ERR_2);
        }
        User user = userService.get(ParamUtil.LongParam(userId));
        List<Role> roleList = roleService.getRolesByUserId(ParamUtil.LongParam(userId));
        Set<String> permissionList = permissionService.getPermissionsByUserId(ParamUtil.LongParam(userId));
        return responseEntity(ErrorsMsg.SUCC_0, UserVo.convert(user, roleList, permissionList));
    }

    /**
     * 验证用户名
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/action/validate_username")
    public ResponseResult validateUserName(String username) {
        userService.validateUser(username);
        return responseEntity(ErrorsMsg.SUCC_0);
    }

    /**
     * 新增用户
     *
     * @param vo
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "")
    @RequiresPermissions(value = "system:user:edit")
    public ResponseResult add(@RequestBody @Validated({Group_1.class, GroupIdNull.class}) UserVo vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            doValidateHandler(bindingResult);
        }
        userService.saveUser(UserVo.convert2Entity(vo));
        return responseEntity(ErrorsMsg.SUCC_0);
    }

    /**
     * 修改用户信息
     *
     * @param vo
     * @param bindingResult
     * @return
     */
    @PutMapping(value = "{userId}")
    @RequiresPermissions(value = "system:user:edit")
    public ResponseResult update(@PathVariable("userId") String userId, @RequestBody @Validated({Group_2.class}) UserVo vo, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            super.doValidateHandler(bindingResult);
        }
        vo.setId(ParamUtil.LongParam(userId));
        userService.updateUser(UserVo.convert2Entity(vo));
        return responseEntity(ErrorsMsg.SUCC_0);
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param vo
     * @param bindingResult
     * @param request
     * @return
     */
    @PatchMapping(value = "/{userId}/pwd")
    public ResponseResult updatePassword(@PathVariable("userId") String userId, @RequestBody @Validated({Group_1.class})
            PasswordVo vo, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            super.doValidateHandler(bindingResult);
        }

        userService.updatePwd(ParamUtil.LongParam(userId), vo.getOldPassword(), vo.getNewPassword(), request.getHeader("token"));
        //修改密码后要重新登录
        return responseEntity(ErrorsMsg.ERR_1004);
    }

    /**
     * 删除用户
     *
     * @return
     */
    @DeleteMapping(value = "/{userId}")
    @RequiresPermissions(value = "system:user:del")
    public ResponseResult delete(@PathVariable("userId") String userId, HttpServletRequest request) {
        if (ParamUtil.LongParam(userId) == null) {
            throw new ServiceException(ErrorsMsg.ERR_2);
        }
        userService.deleteUser(ParamUtil.LongParam(userId));
        return responseEntity(ErrorsMsg.SUCC_0);
    }


}
