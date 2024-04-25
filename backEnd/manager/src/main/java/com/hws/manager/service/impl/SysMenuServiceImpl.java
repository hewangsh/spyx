package com.hws.manager.service.impl;

import com.hws.manager.mapper.SysMenuMapper;
import com.hws.manager.mapper.SysRoleMenuMapper;
import com.hws.manager.service.SysMenuService;
import com.hws.manager.utils.MenuHelper;
import com.hws.model.entity.system.SysMenu;
import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.system.SysMenuVo;
import com.hws.service.exception.MyException;
import com.hws.utils.AuthContextUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public Result findNodes() {
        //查询所有菜单，返回list
        List<SysMenu> sysMenuList=sysMenuMapper.findAll();
        if(sysMenuList.isEmpty()){
            return null;
        }
        //调用工具类方法，封装成要求的格式
        List<SysMenu> treeMenuList= MenuHelper.buildTree(sysMenuList);
        return Result.build(treeMenuList, ResultCodeEnum.SUCCESS);
    }

    //菜单添加
    @Override
    public Result save(SysMenu sysMenu){
        sysMenuMapper.save(sysMenu);
        //TODO 当新添加子菜单，把父菜单isHalf转为半开状态1
        updateSysRoleMenu(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
    //当新添加子菜单，把父菜单isHalf转为半开状态1
    private void updateSysRoleMenu(SysMenu sysMenu){
        //通过id得到父菜单
        SysMenu parentMenu=sysMenuMapper.selectParentMenu(sysMenu.getParentId());
        if(parentMenu !=null){
            //把父菜单isHalf转为半开状态1
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId()) ;
            //递归调用，可能这个父节点是另一个节点的子节点，需要把它的父节点也改成半开状态
            updateSysRoleMenu(parentMenu);
        }
    }

    @Override
    public Result updateById(SysMenu sysMenu) {
        sysMenuMapper.updateById(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result removeById(Long id) {
        //先判断当前菜单是否包含子菜单，包含不删，反之删除
        //根据当前id查询是否有子菜单，只需要查有没有（多少），不需要查有哪些--count()函数实现
        int count=sysMenuMapper.selectCount(id);
        if(count>0){
            throw new MyException(ResultCodeEnum.NODE_ERROR);
        }
        sysMenuMapper.removeById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //查找用户的菜单
    @Override
    public Result findMenusByUserId() {
        //获取用户id
        Long userId = AuthContextUtil.getAuthContext().getId();

        //根据用户id得到菜单列表
        List<SysMenu> sysMenuList=sysMenuMapper.findMenusByUserId(userId);
        List<SysMenu> menuTree = MenuHelper.buildTree(sysMenuList);

        //封装要求数据格式返回
        List<SysMenuVo> sysMenuVos = buildMenus(menuTree);
        return Result.build(sysMenuVos, ResultCodeEnum.SUCCESS);
    }
    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
