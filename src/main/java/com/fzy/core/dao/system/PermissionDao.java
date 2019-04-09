package com.fzy.core.dao.system;

import com.fzy.core.base.BaseDao;
import com.fzy.core.entity.system.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 权限dao
 *
 * @author Fucai
 * @date 2018/3/20
 */
@Mapper
public interface PermissionDao extends BaseDao<Permission> {

    /**
     * 根据用户获取权限
     * @param userId
     * @return
     */
    List<Permission> findPermissionByUser(Long userId);

    /**
     * 批量删除数据
     * @param list
     * @return
     */
    Integer batchDelete(List<Long> list);
}
