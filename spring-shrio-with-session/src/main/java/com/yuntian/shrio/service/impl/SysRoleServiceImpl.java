package com.yuntian.shrio.service.impl;

import com.yuntian.shrio.model.entity.SysRole;
import com.yuntian.shrio.dao.SysRoleDao;
import com.yuntian.shrio.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 后台系统-角色表(SysRole)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 10:52:15
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleDao sysRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysRole queryById(Long id) {
        return this.sysRoleDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<SysRole> queryAllByLimit(int offset, int limit) {
        return this.sysRoleDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    @Override
    public SysRole insert(SysRole sysRole) {
        this.sysRoleDao.insert(sysRole);
        return sysRole;
    }

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    @Override
    public SysRole update(SysRole sysRole) {
        this.sysRoleDao.update(sysRole);
        return this.queryById(sysRole.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.sysRoleDao.deleteById(id) > 0;
    }

    @Override
    public List<String> getListByUserName(String userName) {
        if (userName.equals("admin")){
            return Stream.of("admin").collect(Collectors.toList());
        }else if (userName.equals("yuntian")){
            return Stream.of("test").collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}