package com.yuntian.security.controller.sys;

import com.yuntian.security.common.Result;
import com.yuntian.security.common.ResultGenerator;
import com.yuntian.security.model.dto.SysMenuDTO;
import com.yuntian.security.service.SysMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 后台系统-菜单表(SysMenu)表控制层
 *
 * @author makejava
 * @since 2020-04-19 10:52:10
 */
@RestController
@RequestMapping("menu")
public class SysMenuController {
    /**
     * 服务对象
     */
    @Resource
    private SysMenuService sysMenuService;

    @PreAuthorize("hasAuthority(T(com.yuntian.security.config.security.AuthorityConstants).MENU_ADD)")
    @RequestMapping("add")
    public Result add(SysMenuDTO dto) {
        return ResultGenerator.genSuccessResult();
    }


}