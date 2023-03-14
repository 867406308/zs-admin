package com.zs.security.model;

import com.zs.modules.sys.user.domain.dto.SysUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class LoginUserInfo implements UserDetails {

    public SysUserDTO sysUserDTO;

    private Set<String> permissions;

    public LoginUserInfo(SysUserDTO sysUserDTO, Set<String> permissions){
        this.sysUserDTO = sysUserDTO;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return sysUserDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public SysUserDTO getSysUserDTO() {
        return sysUserDTO;
    }

    public void setSysUserDTO(SysUserDTO sysUserDTO) {
        this.sysUserDTO = sysUserDTO;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
