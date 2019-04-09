package com.fzy.core.dao.system;

import com.fzy.core.base.BaseDao;
import com.fzy.core.entity.system.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色dao
 *
 * @author Fucai
 * @date 2018/3/20
 */
@Mapper
public interface RoleDao extends BaseDao<Role> {

    /**
     * 根据用户获取角色
     *
     * @param userId
     * @return
     */
    List<Role> findRolesByUser(Long userId);
}
