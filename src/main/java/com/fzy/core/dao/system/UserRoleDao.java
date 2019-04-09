package com.fzy.core.dao.system;

import com.fzy.core.base.BaseDao;
import com.fzy.core.entity.system.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色dao
 *
 * @author Fucai
 * @date 2019/02/19
 */
@Mapper
public interface UserRoleDao extends BaseDao<UserRole> {


    /**
     * 根据用户删除角色
     * @param userId
     * @return
     */
    Integer deleteRoleByUser(Long userId);
}
