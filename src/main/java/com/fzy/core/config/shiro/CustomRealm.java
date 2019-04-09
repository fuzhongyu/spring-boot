package com.fzy.core.config.shiro;

import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.system.User;
import com.fzy.core.service.system.PermissionService;
import com.fzy.core.service.system.RoleService;
import com.fzy.core.service.system.UserService;
import com.fzy.core.util.ParamUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义realm
 *
 * @author: fucai
 * @Date: 2019-01-18
 */
public class CustomRealm extends AuthorizingRealm {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomRealm.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取授权信息(设置用户角色)
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String name = ParamUtil.StringParam(principalCollection.getPrimaryPrincipal());
        //查询用户
        User user = userService.getByUserName(name);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //添加角色
        simpleAuthorizationInfo.addRoles(roleService.getRolesNameByUserId(user.getId()));
        //添加权限
        simpleAuthorizationInfo.addStringPermissions(permissionService.getPermissionsByUserId(user.getId()));

        return simpleAuthorizationInfo;
    }

    /**
     * 获取身份验证信息
     * shiro中，最终是通过realm来获取应用程序中的用户，角色及权限信息的
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.getByUserName(token.getUsername());
        if (user == null) {
            throw new ServiceException(ErrorsMsg.ERR_1002, "用户不存在");
        }
        //使用账号作为盐值
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUsername());
        // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), credentialsSalt, getName());

    }
}
