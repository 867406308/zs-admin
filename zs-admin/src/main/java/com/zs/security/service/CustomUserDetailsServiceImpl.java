package com.zs.security.service;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.model.LoginUserInfo;
import com.zs.framework.security.service.CustomUserDetailsService;
import  com.zs.common.model.SysUser;
import com.zs.modules.sys.user.domain.dto.SysUserDTO;
import com.zs.modules.sys.user.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;

@Service
public class CustomUserDetailsServiceImpl  implements CustomUserDetailsService, UserDetailsService {
    @Resource
    private ISysUserService iSysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUserDTO sysUserDTO = iSysUserService.selectByUserName(username);
        // 查询数据库用户是否存在
//        SysUserDTO sysUserDTO = BeanUtil.toBean(iSysUserService.selectByUserName(username), SysUserDTO.class);
        if(Objects.isNull(sysUserDTO)){
            throw new UsernameNotFoundException("用户不存在");
        }
        return new LoginUserInfo(BeanUtil.toBean(sysUserDTO, SysUser.class), new HashSet<>());
    }
}
