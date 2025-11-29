package com.mindora.platform.users.interfaces.rest.resources;

public record AuthResponseDto(
        String token,
        UserDto user
) {}

