package com.fzy.core.entity.system;

import com.fzy.core.base.BaseEntity;
import com.fzy.core.validator.group.Group_1;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限，菜单 Entity
 *
 * @author: fucai
 * @Date: 2019/01/21
 */
@Getter
@Setter
@NoArgsConstructor
public class Permission extends BaseEntity<Permission> implements Comparable<Permission> {


    /**
     * 父类
     */
    private Permission parent;

    /**
     * 类型： 0-菜单 , 1-链接, 2- 按钮
     */
    @NotNull(message = "类型不能为空",groups = {Group_1.class})
    private Integer type;

    /**
     * 地址
     */
    private String url;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空", groups = {Group_1.class})
    private String name;

    /**
     * 权限
     */
    private String permission;


    /**
     * 排序
     */
    private Integer sort;

    /**
     * 子集菜单
     */
    private List<Permission> childNodes = new ArrayList<>();


    @Override
    public int compareTo(Permission s) {
        //自定义比较方法，如果认为此实体本身大则返回1，否则返回-1
        if (this.sort >= s.sort) {
            return 1;
        }
        return -1;
    }
}
