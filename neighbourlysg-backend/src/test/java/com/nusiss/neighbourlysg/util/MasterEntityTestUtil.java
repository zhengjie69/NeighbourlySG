package com.nusiss.neighbourlysg.util;

import com.nusiss.neighbourlysg.entity.Profile;

public final class MasterEntityTestUtil {

    public static Profile createProfileEntity(){
        Profile profile = new Profile();
        profile.setConstituency("con");
        profile.setEmail("email");
        profile.setId(1L);
        profile.setIsAdmin(true);
        profile.setName("name");
        profile.setPassword("password");
        return profile;
    }
}
