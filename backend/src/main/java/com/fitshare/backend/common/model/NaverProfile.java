package com.fitshare.backend.common.model;

import lombok.Data;

@Data
public class NaverProfile {
    public String resultcode;
    public String message;
    public Profile response;

    @Data
    public class Profile {
        public String id;
        public String name;
        public String email;
        public String mobile;
    }
}
