package com.fitshare.backend.common.model;

import lombok.Data;
import lombok.Getter;

@Getter
public class NaverProfile {
    private String resultcode;
    private String message;
    private Profile response;

    @Getter
    public class Profile {
        public String id;
        public String name;
        public String email;
        public String profile_image;
        public String mobile;
    }
}
