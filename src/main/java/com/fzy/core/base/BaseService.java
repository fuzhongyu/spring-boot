package com.fzy.core.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fzy.core.config.CustomConfigProperties;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * service 基类
 * @author Fucai
 * @date 2018/3/19
 */
@Transactional(readOnly = true)
public abstract class BaseService<D extends BaseDao<T>, T extends BaseEntity<T>>{

    private Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    protected D dao;

    /**
     * 条件查询方法
     *
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        return dao.findList(entity);
    }

    /**
     * 分页查询(当pageNo=null 或者pageSize=null的时候，会设置默认值)
     *
     * @param entity
     * @param pageNo   页码
     * @param pageSize 每页大小
     * @return
     */
    public PageEntity<T> findPage(T entity, Integer pageNo, Integer pageSize) {
        //设置默认页码和页数
        if (pageNo == null || pageSize == null) {
            pageNo = CustomConfigProperties.PAGE_NO;
            pageSize = CustomConfigProperties.PAGE_SIZE;
        }
        Page<T> page = PageHelper.startPage(pageNo, pageSize);
        List<T> list = dao.findList(entity);
        return new PageEntity<>(pageNo, pageSize, page.getTotal(), list);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public T get(Long id) {
        return dao.get(id);
    }

    /**
     * 保存数据（插入或更新）
     * @param entity
     */
    @Transactional(readOnly = false)
    public Integer save(T entity) {
        if (entity.getId()==null){
            entity.preInsert();
           return dao.insert(entity);
        }else{
            entity.preUpdate();
          return   dao.update(entity);
        }
    }

    /**
     * 保存并返回
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public T saveAndReturn(T entity){
        save(entity);
        return get(entity.getId());
    }


    /**
     * 批量插入数据
     *
     * @param entityList
     * @return
     */
    @Transactional(readOnly = false)
    public int batchInsert(List<T> entityList) {
        for (T entity : entityList) {
            entity.preInsert();
        }
        return dao.batchInsert(entityList);
    }

    /**
     * 逻辑删除
     *
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public int delete(T entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setDeleteFlag(BaseEntity.DEL_FLAG_DELETE);
        return dao.delete(entity);
    }

    /**
     * 删除（非逻辑删除）
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = false)
    public int del(Long id) {
        return dao.del(id);
    }
}
