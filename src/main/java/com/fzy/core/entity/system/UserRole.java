package com.fzy.core.entity.system;

import com.fzy.core.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户角色关联表
 * @author: fucai
 * @Date: 2019-02-19
 */
@Getter
@Setter
@NoArgsConstructor
public class UserRole extends BaseEntity<UserRole> {

    private Long userId;

    private Long roleId;


    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
