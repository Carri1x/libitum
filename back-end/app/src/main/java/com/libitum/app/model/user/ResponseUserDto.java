package com.libitum.app.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
