package com.fzy.core.service.system;

import com.fzy.core.base.BaseService;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.dao.system.PermissionDao;
import com.fzy.core.entity.system.Permission;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限service
 *
 * @author Fucai
 * @date 2018/3/20
 */

@Service
public class PermissionService extends BaseService<PermissionDao, Permission> {

    private static Logger logger = LoggerFactory.getLogger(PermissionService.class);

    /**
     * 菜单类型
     */
    private enum TypeEnum {
        MENU(1),
        URL(2),
        BUTTON(3);

        private Integer type;

        TypeEnum(Integer type) {
            this.type = type;
        }

        public Integer getType() {
            return type;
        }
    }

    /**
     * 菜单根节点id
     */
    public final static Long MENU_ROOT_ID = 1L;

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    public Set<String> getPermissionsByUserId(Long userId) {
        List<Permission> list = dao.findPermissionByUser(userId);
        return list.stream()
                .map(Permission::getPermission)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }


    /**
     * 获取菜单
     *
     * @return
     */
    public List<Permission> getMenuTree() {
        List<Permission> list = dao.findList(new Permission());
        Permission menuTree = getTree(MENU_ROOT_ID, list);
        return menuTree.getChildNodes();
    }

    /**
     * 批量删除权限
     *
     * @param idList
     */
    public void batchDelete(List<Long> idList) {
        if (idList.size() > 0) {
            dao.batchDelete(idList);
        }
    }

    /**
     * 获取树形结构
     *
     * @param parentId
     * @param list
     * @return
     */
    private Permission getTree(final Long parentId, List<Permission> list) {
        //先找出当前节点
        Permission returnPermission = list.stream()
                .filter(unit -> unit.getId().equals(parentId))
                .findFirst()
                .orElse(null);

        if (returnPermission == null) {
            logger.error(">>>>>>>>>> 菜单根节点有误 <<<<<<<<<<<<");
            throw new ServiceException(ErrorsMsg.ERR_1);
        } else if (TypeEnum.BUTTON.getType() == returnPermission.getType()) {
            //如果是按钮权限了，则不再寻找子节点
            return returnPermission;
        }

        //寻找子节点
        list.stream().forEach(unit -> {
            if (unit.getParent().getId().equals(parentId)) {
                //递归获取该子节点的 子节点列表
                Permission childPermission = getTree(unit.getId(), list);
                //将子节点加入到当前的子节点列表中
                returnPermission.getChildNodes().add(childPermission);
            }
        });

        //对子节点列表进行排序(Permission类实现了Comparable接口)
        if (CollectionUtils.isNotEmpty(returnPermission.getChildNodes())) {
            Collections.sort(returnPermission.getChildNodes());
        }

        return returnPermission;
    }


}
