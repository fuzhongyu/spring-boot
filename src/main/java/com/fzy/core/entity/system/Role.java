package com.fzy.core.entity.system;

import com.fzy.core.base.BaseEntity;
import com.fzy.core.validator.group.Group_1;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 角色
 *
 * @author: fucai
 * @Date: 2019-01-21
 */
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity<Role> {

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空", groups = Group_1.class)
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色拥有的权限列表
     */
    private List<Permission> permissionList;

    public Role(Long id) {
        this.id = id;
    }

}
