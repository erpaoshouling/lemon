package com.mossle.auth.web.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mossle.api.LocalScopeDTO;
import com.mossle.api.ScopeConnector;
import com.mossle.api.UserConnector;
import com.mossle.api.UserDTO;

import com.mossle.auth.component.UserStatusChecker;
import com.mossle.auth.component.UserStatusConverter;
import com.mossle.auth.domain.Role;
import com.mossle.auth.domain.UserStatus;
import com.mossle.auth.manager.RoleManager;
import com.mossle.auth.manager.UserStatusManager;
import com.mossle.auth.service.AuthService;
import com.mossle.auth.support.CheckUserStatusException;
import com.mossle.auth.support.CheckUserStatusException;
import com.mossle.auth.support.RoleDTO;
import com.mossle.auth.support.UserStatusDTO;

import com.mossle.core.hibernate.PropertyFilter;
import com.mossle.core.page.Page;
import com.mossle.core.scope.ScopeHolder;
import com.mossle.core.struts2.BaseAction;
import com.mossle.core.util.ServletUtils;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

@Results({ @Result(name = UserConnectorBatchAction.RELOAD, location = "user-connector.do?operationMode=RETRIEVE", type = "redirect") })
public class UserConnectorBatchAction extends BaseAction {
    private static Logger logger = LoggerFactory
            .getLogger(UserConnectorBatchAction.class);
    public static final String RELOAD = "reload";
    private UserStatusManager userStatusManager;
    private MessageSourceAccessor messages;
    private UserStatusConverter userStatusConverter;
    private String userText;
    private List<Long> userIds = new ArrayList<Long>();
    private List<Long> roleIds = new ArrayList<Long>();
    private List<UserStatus> userStatuses = new ArrayList<UserStatus>();
    private UserStatusChecker userStatusChecker;
    private UserConnector userConnector;
    private RoleManager roleManager;
    private ScopeConnector scopeConnector;
    private List<Role> roles;
    private AuthService authService;
    private List<RoleDTO> roleDtos = new ArrayList<RoleDTO>();

    public String execute() {
        return SUCCESS;
    }

    public String input() {
        Long globalId = scopeConnector
                .findGlobalId(ScopeHolder.getGlobalCode());
        Long localId = scopeConnector.findLocalId(ScopeHolder.getGlobalCode(),
                ScopeHolder.getLocalCode());

        if (userText != null) {
            for (String str : userText.split("\n")) {
                str = str.trim();

                if (str.length() == 0) {
                    continue;
                }

                // UserStatus userStatus = userStatusManager.findUniqueBy(
                // "username", str);
                String username = str;
                UserDTO userDto = userConnector.findByUsername(username,
                        globalId);

                if (userDto == null) {
                    addActionMessage(str + " is not exists.");
                } else {
                    UserStatus userStatus = authService.createOrGetUserStatus(
                            username, userDto.getId(), globalId, localId);

                    try {
                        userStatusChecker.check(userStatus);
                        userStatuses.add(userStatus);
                    } catch (CheckUserStatusException ex) {
                        addActionMessage(ex.getMessage());
                    }
                }
            }
        }

        roles = roleManager.find("from Role where globalId=? and localId=?",
                globalId, localId);
        roleDtos.addAll(convertRoleDtos(roles, false));

        List<LocalScopeDTO> sharedLocalScopes = scopeConnector
                .findSharedLocalScopes();

        System.out.println(sharedLocalScopes);

        for (LocalScopeDTO localScopeDto : sharedLocalScopes) {
            List<Role> sharedRoles = authService.findRoles(localScopeDto
                    .getId());
            roleDtos.addAll(convertRoleDtos(sharedRoles, true));
        }

        return INPUT;
    }

    public String save() {
        Long globalId = scopeConnector
                .findGlobalId(ScopeHolder.getGlobalCode());
        Long localId = scopeConnector.findLocalId(ScopeHolder.getGlobalCode(),
                ScopeHolder.getLocalCode());
        logger.debug("userIds: {}, roleIds: {}", userIds, roleIds);

        for (Long userId : userIds) {
            authService.configUserRole(userId, roleIds, globalId, localId,
                    false);
        }

        return RELOAD;
    }

    public List<RoleDTO> convertRoleDtos(List<Role> roles, boolean useScope) {
        List<RoleDTO> roleDtos = new ArrayList<RoleDTO>();

        for (Role role : roles) {
            roleDtos.add(convertRoleDto(role, useScope));
        }

        return roleDtos;
    }

    public RoleDTO convertRoleDto(Role role, boolean useScope) {
        RoleDTO roleDto = new RoleDTO();
        roleDto.setId(role.getId());

        if (useScope) {
            roleDto.setName(role.getName() + "("
                    + scopeConnector.getLocalScope(role.getLocalId()).getName()
                    + ")");
        } else {
            roleDto.setName(role.getName());
        }

        roleDto.setLocalId(role.getLocalId());

        return roleDto;
    }

    // ~ ======================================================================
    public void setUserStatusManager(UserStatusManager userStatusManager) {
        this.userStatusManager = userStatusManager;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setUserStatusConverter(UserStatusConverter userStatusConverter) {
        this.userStatusConverter = userStatusConverter;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setUserStatusChecker(UserStatusChecker userStatusChecker) {
        this.userStatusChecker = userStatusChecker;
    }

    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    // ~ ======================================================================
    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<UserStatus> getUserStatuses() {
        return userStatuses;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setScopeConnector(ScopeConnector scopeConnector) {
        this.scopeConnector = scopeConnector;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public List<RoleDTO> getRoleDtos() {
        return roleDtos;
    }
}
