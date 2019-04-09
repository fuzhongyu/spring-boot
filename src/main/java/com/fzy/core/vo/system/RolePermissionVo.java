package com.fzy.core.vo.system;

import com.fzy.core.validator.group.Group_1;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 角色权限列表vo类
 *
 * @author Fucai
 * @date 2019/02/19
 */
@Getter
@Setter
@NoArgsConstructor
public class RolePermissionVo {

    @NotEmpty(message = "权限列表不能为空",groups = {Group_1.class})
    private List<Long> permissionList;

}
