package com.fzy.core.vo.system;

import com.fzy.core.base.BaseVo;
import com.fzy.core.entity.system.Permission;
import com.fzy.core.service.system.PermissionService;
import com.fzy.core.validator.annotation.NotNullValid;
import com.fzy.core.validator.group.Group_1;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * 权限vo类
 *
 * @author Fucai
 * @date 2019/02/18
 */
@Getter
@Setter
@NotNullValid.List({@NotNullValid(fieldName = "type", message = "类型不能为空", groups = {Group_1.class}),
        @NotNullValid(fieldName = "name", message = "名称不能为空", groups = {Group_1.class})})
public class PermissionVo extends BaseVo<PermissionVo> {

    /**
     * 父级菜单id
     */
    private Long parentId;

    /**
     * 类型： 0-菜单 , 1-链接, 2- 按钮
     */
    private Integer type;

    /**
     * 地址
     */
    private String url;

    /**
     * 名称
     */
    private String name;

    /**
     * 权限
     */
    private String permission;


    /**
     * 排序
     */
    private Integer sort;


    public static Permission convert2Entity(PermissionVo vo){
        Permission entity = new Permission();
        BeanUtils.copyProperties(vo,entity,new String[]{"parent"});

        Permission parent = new Permission();
        //如果未传父节点，则设置为菜单根节点的子节点
        parent.setId(vo.getParentId()==null?PermissionService.MENU_ROOT_ID:vo.getParentId());

        entity.setParent(parent);
        return entity;
    }


}
