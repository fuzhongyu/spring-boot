package com.fzy.core.entity.system;

import com.fzy.core.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色权限关联表
 * @author: fucai
 * @Date: 2019-02-19
 */
@Getter
@Setter
@NoArgsConstructor
public class RolePermission extends BaseEntity<RolePermission> {

    private Long roleId;

    private Long permissionId;

    public RolePermission(Long roleId, Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
