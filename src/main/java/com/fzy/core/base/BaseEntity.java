package com.fzy.core.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fzy.core.util.IdWorker;
import com.fzy.core.validator.group.GroupIdNotNull;
import com.fzy.core.validator.group.GroupIdNull;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础实体类
 *
 * @author Fucai
 * @date 2018/3/19
 */
@Getter
@Setter
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 删除标记 0-正常，1-删除
     */
    public static final Integer DEL_FLAG_NORMAL = 0;
    public static final Integer DEL_FLAG_DELETE = 1;

    @NotNull(message = "id不能为空", groups = GroupIdNotNull.class)
    @Null(message = "不可传id",groups = GroupIdNull.class)
    protected Long id;

    /**
     * 标识是否需要生成id，false代表数据库自己生成id(自增id)
     */
    @JsonIgnore
    protected Boolean isCreateId = true;

    /**
     *创建时间
     */
    protected Long createTime;

    /**
     * 更新时间
     */
    protected Long updateTime;

    /**
     * 删除标记：0-正常，1-删除
     */
    @JsonIgnore
    protected Integer deleteFlag;

    /**
     * 备注
     */
    protected String remarks;


    public BaseEntity() {
        super();
        this.deleteFlag = DEL_FLAG_NORMAL;
    }

    public BaseEntity(Long id) {
        super();
        this.id = id;
        this.deleteFlag = DEL_FLAG_NORMAL;
    }


    /**
     * 使用自定义id的时候需要手动调用
     */
    public void preInsert() {
        if (this.isCreateId) {
            this.id=IdWorker.createId();
        }
        this.createTime = System.currentTimeMillis();
        this.updateTime=System.currentTimeMillis();
    }

    public void preUpdate() {
        this.updateTime = System.currentTimeMillis();
    }

}
