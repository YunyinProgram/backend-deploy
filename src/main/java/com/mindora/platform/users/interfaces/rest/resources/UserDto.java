package com.mindora.platform.users.interfaces.rest.resources;

public record UserDto(
        Long id,
        String name,
        String email
) {}

