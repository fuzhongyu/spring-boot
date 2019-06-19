package com.fzy;

import com.alibaba.fastjson.JSON;
import com.fzy.core.entity.system.Permission;
import com.fzy.core.service.system.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

/**
 * @author: fucai
 * @Date: 2019-01-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PermissionTest {

    @Autowired
    private PermissionService permissionService;

    @Test
    public void getTreeTest() {
        List<Permission> permission = permissionService.getMenuTree();
        System.out.println(JSON.toJSON(permission));
    }
}
