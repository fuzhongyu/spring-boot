package com.fzy.core.vo.system;

import com.fzy.core.base.BaseVo;
import com.fzy.core.entity.system.Role;
import com.fzy.core.entity.system.User;
import com.fzy.core.validator.annotation.NotNullValid;
import com.fzy.core.validator.group.Group_1;
import com.fzy.core.validator.group.Group_2;
import com.fzy.core.validator.group.Group_3;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户详情视图类
 *
 * @author Fucai
 * @date 2018/3/26
 */
@Getter
@Setter
@NoArgsConstructor
@NotNullValid.List({@NotNullValid(fieldName = "username", message = "用户名不能为空", groups = {Group_1.class,
        Group_3.class}),
        @NotNullValid(fieldName = "password", message = "密码不能为空", groups = {Group_1.class})})
public class UserVo extends BaseVo<UserVo> {

    /**
     * 用户名
     */
    @Size(max = 32, message = "用户名过长", groups = {Group_1.class})
    @Null(message = "用户名不可更改", groups = {Group_2.class})
    private String username;

    /**
     * 密码
     */
    private String password;


    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否是超级管理员，0-不是，1-是
     */
    private Integer type;


    /**
     * 用户状态：0-正常，1-冻结
     */
    private Integer status;

    /**
     * 用户角色id列表
     */
    private List<Long> roleList;

    /**
     * 用户权限列表
     */
    private Set<String> permissionList;


    /**
     * entity转vo
     *
     * @param entity
     * @return
     */
    public static UserVo convert(User entity) {
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    /**
     * entity转vo
     *
     * @param entity
     * @return
     */
    public static UserVo convert(User entity, List<Role> roleList, Set<String> permissionList) {
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(entity, vo, new String[]{"password", "roleList", "permissionList"});
        List<Long> roleIdList = new ArrayList<>();
        for (Role unit: roleList){
            roleIdList.add(unit.getId());
        }
        vo.setRoleList(roleIdList);
        vo.setPermissionList(permissionList);
        return vo;
    }

    /**
     * vo转entity
     * @param vo
     * @return
     */
    public static User convert2Entity(UserVo vo){
        User entity = new User();
        BeanUtils.copyProperties(vo, entity, new String[]{"roleList", "permissionList"});
        if (CollectionUtils.isNotEmpty(vo.getRoleList())){
            List<Role> roleList = new ArrayList<>();
            for (Long unit: vo.getRoleList()){
                roleList.add(new Role(unit));
            }
            entity.setRoleList(roleList);
        }
        return entity;
    }

}
