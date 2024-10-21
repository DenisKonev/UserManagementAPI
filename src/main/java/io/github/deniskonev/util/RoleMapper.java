package io.github.deniskonev.util;

import io.github.deniskonev.dto.UserResponseDto;
import io.github.deniskonev.dto.UserRoleRequestDto;
import io.github.deniskonev.dto.UserRoleResponseDto;
import io.github.deniskonev.model.UserRole;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class RoleMapper {

    public static void mapUserRoles(List<UserResponseDto> usersDto, List<UserRoleRequestDto> userRolesDto, List<UserRoleResponseDto> rolesDto) {
        Map<Long, UserRoleResponseDto> roleMap = rolesDto.stream()
                .collect(Collectors.toMap(UserRoleResponseDto::getId, role -> role));

        Map<Long, List<Long>> userRolesMap = userRolesDto.stream()
                .collect(Collectors.groupingBy(UserRoleRequestDto::getUserId,
                        Collectors.mapping(UserRoleRequestDto::getRoleId, Collectors.toList())));

        List<UserRoleResponseDto> userRoleResponseDtos = new ArrayList<>();
        for (UserResponseDto userDto : usersDto) {
            List<Long> roleIds = userRolesMap.get(userDto.getId());
            if (roleIds != null) {
                userRoleResponseDtos = roleIds.stream()
                        .map(roleMap::get)
                        .filter(Objects::nonNull)
                        .map(role -> {
                            UserRoleResponseDto dto = new UserRoleResponseDto();
                            dto.setId(userDto.getId());
                            dto.setRoleId(role.getId());
                            dto.setName(role.getName());
                            dto.setDescription(role.getDescription());
                            return dto;
                        }).toList();
            } else {
                userDto.setUserRoles(Collections.emptyList());
            }
            userDto.setUserRoles(userRoleResponseDtos);
        }
    }

    public static List<UserRoleRequestDto> mapUserRequestDto(List<UserRole> userRoles) {
        List<UserRoleRequestDto> userRoleRequestDtos = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            UserRoleRequestDto userRoleRequestDto = new UserRoleRequestDto();
            userRoleRequestDto.setUserId(userRole.getUser().getId());
            userRoleRequestDto.setRoleId(userRole.getRoleId());
            userRoleRequestDtos.add(userRoleRequestDto);
        }
        return userRoleRequestDtos;
    }
}
