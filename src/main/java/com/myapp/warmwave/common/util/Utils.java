package com.myapp.warmwave.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.warmwave.common.Role;
import com.myapp.warmwave.common.exception.CustomException;
import com.myapp.warmwave.common.exception.CustomExceptionCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static class CustomAuthority {
        public static List<GrantedAuthority> createAuthorities(Role role) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + role.toString()));
        }

        public static void verifyRole(Role role) {
            if (role.toString() == null) {
                throw new CustomException(CustomExceptionCode.USER_ROLE_NOT_EXIST);
            } else if (!role.toString().equals(Role.INDIVIDUAL.toString()) && !role.toString().equals(Role.INSTITUTION.toString()) && !role.toString().equals(Role.ADMIN.toString())) {
                throw new CustomException(CustomExceptionCode.USER_ROLE_INVALID);
            }
        }
    }

    public static class json {
        public static Object toStr(Map<String, Object> map) {
            try {
                return new ObjectMapper().writeValueAsString(map);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        public static Map<String, Object> toMap(String jsonStr) {
            try {
                return new ObjectMapper().readValue(jsonStr, LinkedHashMap.class);
            } catch (JsonProcessingException e) {
                return Collections.emptyMap();
            }
        }
    }
}
