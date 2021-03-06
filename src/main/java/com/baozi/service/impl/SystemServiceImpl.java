package com.baozi.service.impl;

import com.baozi.exception.CustomException;
import com.baozi.mappers.*;
import com.baozi.po.*;
import com.baozi.realm.UserRealm;
import com.baozi.service.SystemService;
import com.baozi.statics.Constant;
import com.baozi.util.GenerateLogFactory;
import com.baozi.util.LogUtils;
import com.baozi.util.MD5;
import com.baozi.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 老笼包
 * @create 2017-06-16 10:49
 * @description 一句话说明此类的作用
 **/
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermissionMapperCustom sysPermissionMapperCustom;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public ActiveUser authenticat(String userCode, String password) throws Exception {
        SysUser user=this.findSysUserByUserCode(userCode);
        if(user==null){
            throw new CustomException("账号不存在");
        }
        //数据库的密码
        String pwdDb=user.getPassword();
        //将用户输入的密码加密进行比对
        String pwdUser=new MD5().getMD5ofStr(password);
        if(!pwdDb.equalsIgnoreCase(pwdUser)){
            throw new CustomException("账号或者密码错误");
        }
        //认证通过，可以将用户信息返回
        ActiveUser activeUser=new ActiveUser();
        activeUser.setUserid(user.getId());
        activeUser.setUsername(user.getUsername());
        activeUser.setUsercode(user.getUsercode());
        return activeUser;
    }

    @Override
    public SysUser findSysUserByUserCode(String userCode){
        SysUserExample sysUserExample=new SysUserExample();
        SysUserExample.Criteria criteria= sysUserExample.createCriteria();
        criteria.andUsercodeEqualTo(userCode);
        List<SysUser> user=sysUserMapper.selectByExample(sysUserExample);
        if(user.size()>0&&user!=null){
            return user.get(0);
        }
        return null;
    }

    @Override
    public List<SysPermission> findMenuListByUserId(int userId){
        return sysPermissionMapperCustom.findMenuListByUserId(userId);
    }

    @Override
    public List<SysPermission> findPermissionListByUserId(int userId){
        return sysPermissionMapperCustom.findPermissionListByUserId(userId);
    }

    @Override
    public Set<String> findRolesListByUserId(int userId) {
        return sysPermissionMapperCustom.findRolesListByUserId(userId);
    }

    @Override
    public List<SysRole> findNowAllPermission(int userId) {
        return sysRoleMapper.findNowAllPermission(userId);
    }

    @Override
    public PageInfo<SysPermissionVo> findSysPermissionPage(Map<String, Object> paramMap) {
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()),Integer.valueOf(paramMap.get("limit").toString()),true);
        List<SysPermissionVo> dataList = sysPermissionMapper.findSysPermissionPage(paramMap);
        return new PageInfo<SysPermissionVo>(dataList);
    }

    @Override
    public int deleteSysPermissionSingleOrBatch(List idList,ActiveUser activeUser,Session session) {
        sysLogMapper.insertSelective(GenerateLogFactory.buildSysLogCurrency(activeUser,"删除权限",(short) 0,activeUser.getUsername()+"删除权限",session.getHost()));
        return sysPermissionMapper.deleteSysPermissionSingleOrBatch(idList);
    }

    @Override
    public void insert(SysPermission sysPermission,ActiveUser activeUser,Session session) {
        sysLogMapper.insertSelective(GenerateLogFactory.buildSysLogCurrency(activeUser,"新增权限",(short) 0,activeUser.getUsername()+"新增权限",session.getHost()));
        sysPermissionMapper.insertSelective(sysPermission);
        executePermission(String.valueOf(Constant.SUPER_MANAGER_USERID),String.valueOf(sysPermission.getId()));
    }

    @Override
    public PageInfo<SysRoleVo> findSysRolePage(Map<String, Object> paramMap) {
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()),Integer.valueOf(paramMap.get("limit").toString()),true);
        List<SysRoleVo> dataList = sysRoleMapper.findSysRolePage(paramMap);
        return new PageInfo<SysRoleVo>(dataList);
    }

    @Override
    public void updateSysRole(SysRole sysRole, ActiveUser activeUser, Session session) {
        sysLogMapper.insertSelective(GenerateLogFactory.buildSysLogCurrency(activeUser,"修改角色",(short) 0,activeUser.getUsername()+"修改角色",session.getHost()));
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public void insert(SysRole sysRole, ActiveUser activeUser, Session session) {
        sysLogMapper.insertSelective(GenerateLogFactory.buildSysLogCurrency(activeUser,"新增角色",(short) 0,activeUser.getUsername()+"新增角色",session.getHost()));
        sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    public boolean deleteSysRole(int id, ActiveUser activeUser, Session session) {
        boolean flag = false;
        try {
            if ( id == Constant.SUPER_MANAGER_USERID) {
                flag = false;
            } else {
                sysLogMapper.insertSelective(GenerateLogFactory.buildSysLogCurrency(activeUser,"删除角色",(short) 0,activeUser.getUsername()+"删除角色",session.getHost()));
                sysRoleMapper.deleteByPrimaryKey(id);
                flag = true;
            }
        } catch ( Exception e ) { }
        return flag;
    }

    @Override
    public PageInfo<UserRoleAllocationVo> findUserRoleAllocationPage(Map<String, Object> paramMap) {
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()),Integer.valueOf(paramMap.get("limit").toString()),true);
        List<UserRoleAllocationVo> dataList = sysRoleMapper.findUserRoleAllocationPage(paramMap);
        return new PageInfo<UserRoleAllocationVo>(dataList);
    }

    @Override
    public List<UserRoleVo> selectRoleByUserId(int userId) {
        return sysRoleMapper.selectRoleByUserId(userId);
    }

    @Override
    public void addRoleToUser(int userId, String ids,ActiveUser activeUser,Session session) {
        try {
            sysRoleMapper.deleteByUserId(userId);

            if(StringUtils.isNotBlank(ids)){
                String[] idArray = null;

                if(StringUtils.contains(ids, ",")){
                    idArray = ids.split(",");
                }else{
                    idArray = new String[]{ids};
                }

                for (String rid : idArray) {

                    if(StringUtils.isNotBlank(rid)){
                        SysUserRole entity = new SysUserRole(userId,Integer.parseInt(rid));
                        sysUserRoleMapper.insertSelective(entity);
                    }

                }
            }
            sysLogMapper.insertSelective(GenerateLogFactory.buildSysLogCurrency(activeUser,"用户【"+userId+"】赋予新角色成功",(short) 0,"用户【"+userId+"】赋予新角色成功，操作人"+activeUser.getUsername(),session.getHost()));
            LogUtils.logInfo("用户【"+userId+"】赋予新角色成功");
        } catch (Exception e) {
            LogUtils.logError("为用户【"+userId+"】赋予新角色失败",e);
        }
    }

    @Override
    public PageInfo<RolePermissionAllocationVo> findRolePermissionAllocationPage(Map<String, Object> paramMap) {
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()),Integer.valueOf(paramMap.get("limit").toString()),true);
        List<RolePermissionAllocationVo> dataList = sysRolePermissionMapper.findRolePermissionAllocationPage(paramMap);
        return new PageInfo<RolePermissionAllocationVo>(dataList);
    }

    @Override
    public List<RolePermissionVo> selectPermissionById(int permissionId) {
        return sysRolePermissionMapper.selectPermissionById(permissionId);
    }

    @Override
    public void addPermissionToRole(int roleId, String ids,ActiveUser activeUser,Session session) {
        try {
            sysRolePermissionMapper.deleteByRoleId(roleId);
            executePermission(String.valueOf(roleId),ids);
            sysLogMapper.insertSelective(GenerateLogFactory.buildSysLogCurrency(activeUser,"赋予角色ID【"+roleId+"】权限成功",(short) 0,"赋予角色ID【"+roleId+"】权限成功，操作人"+activeUser.getUsername(),session.getHost()));
        } catch (Exception e) {
            LogUtils.logError("为角色【"+roleId+"】赋予新角色失败",e);
        }
    }

    /**
     * 每次新增权限，都默认给超管添加一项，保证超管为最大权限
     * @param roleId
     * @param ids
     */
    private void executePermission(String roleId, String ids){
        try {
            if(StringUtils.isNotBlank(ids)){
                String[] idArray = null;

                if(StringUtils.contains(ids, ",")){
                    idArray = ids.split(",");
                }else{
                    idArray = new String[]{ids};
                }

                for (String pid : idArray) {

                    if(StringUtils.isNotBlank(pid)){
                        SysRolePermission entity = new SysRolePermission(String.valueOf(roleId),pid);
                        sysRolePermissionMapper.insertSelective(entity);
                    }

                }
            }
            UserRealm userRealm = new UserRealm();
            userRealm.clearCachedAuthorizationInfo();
            userRealm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
            LogUtils.logInfo("角色【"+roleId+"】更新权限成功");
        } catch (Exception e) {
            LogUtils.logError("赋予新权限出错",e);
        }
    }

}
