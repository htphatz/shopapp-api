package com.example.shopapp.controllers;

import com.example.shopapp.models.Role;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseCustom<?> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all roles successfully", roles);
    }
}
