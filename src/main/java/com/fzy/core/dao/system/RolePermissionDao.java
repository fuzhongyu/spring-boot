package com.fzy.core.dao.system;

import com.fzy.core.base.BaseDao;
import com.fzy.core.entity.system.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色权限dao
 *
 * @author Fucai
 * @date 2019/02/19
 */
@Mapper
public interface RolePermissionDao extends BaseDao<RolePermission> {

    /**
     * 根据角色id获取权限
     * @param roleId
     * @return
     */
    List<Long> getPermissionListByRoleId(Long roleId);

    /**
     * 根据角色删除权限
     * @param roleId
     * @return
     */
    Integer deletePermissionByRole(Long roleId);
}
