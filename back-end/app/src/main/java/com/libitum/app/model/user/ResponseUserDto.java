package com.libitum.app.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ResponseUserDto {
    private String id;
    private String name;
    private String surname;
    private String nickname;
    private String description;
    private String avatarUrl;
    private String city;
    private String country;
}
