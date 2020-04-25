package com.yuntian.ssoserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 * @date 2020-04-16 23:33
 * @description
 */
@Controller
public class PageController {


    @RequestMapping("/")
    public String index() {
        return "sys/index";
    }

    @RequestMapping("/index")
    public String indexPage() {
        return "sys/index";
    }


    @PreAuthorize("hasAuthority(T(com.yuntian.ssoserver.config.security.AuthorityConstants).MENU_VIEW)")
    @RequestMapping("/menu")
    public String menuPage() {
        return "sys/menu";
    }

    @PreAuthorize("hasAuthority(T(com.yuntian.ssoserver.config.security.AuthorityConstants).USER_VIEW)")
    @RequestMapping("/user")
    public String userPage() {
        return "sys/user";
    }


}
