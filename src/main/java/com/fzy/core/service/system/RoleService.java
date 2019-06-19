package com.fzy.core.service.system;

import com.fzy.core.base.BaseService;
import com.fzy.core.dao.system.RoleDao;
import com.fzy.core.dao.system.RolePermissionDao;
import com.fzy.core.entity.system.Role;
import com.fzy.core.entity.system.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色service
 *
 * @author Fucai
 * @date 2018/3/20
 */

@Service
public class RoleService extends BaseService<RoleDao, Role> {

    @Autowired
    private RolePermissionDao rolePermissionDao;


    /**
     * 获取用户角色
     * @param userId
     * @return
     */
    public List<Role> getRolesByUserId(Long userId){
        return  dao.findRolesByUser(userId);
    }


    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    public Set<String> getRolesNameByUserId(Long userId) {
        List<Role> list = getRolesByUserId(userId);
        return list.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    /**
     * 根据角色获取权限
     * @param roleId
     * @return
     */
    public List<Long> getPermissionListByRoleId(Long roleId){
        return rolePermissionDao.getPermissionListByRoleId(roleId);
    }

    /**
     * 更新角色的权限
     * @param roleId
     * @param permissionIdList
     */
    @Transactional(readOnly = false)
    public void updateRolePermission(final Long roleId, List<Long> permissionIdList){
        //先删除该角色的权限
        rolePermissionDao.deletePermissionByRole(roleId);
        //再插入新的权限
        List<RolePermission> insertList =permissionIdList.stream()
                .map(unit -> new RolePermission(roleId,unit))
                .collect(Collectors.toList());

        rolePermissionDao.batchInsert(insertList);

    }



}
