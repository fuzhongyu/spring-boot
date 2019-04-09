package com.fzy.core.dao.system;

import com.fzy.core.base.BaseDao;
import com.fzy.core.entity.system.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户dao
 *
 * @author Fucai
 * @date 2018/3/20
 */

@Mapper
public interface UserDao extends BaseDao<User> {


  User getByUserName(String username);
}
