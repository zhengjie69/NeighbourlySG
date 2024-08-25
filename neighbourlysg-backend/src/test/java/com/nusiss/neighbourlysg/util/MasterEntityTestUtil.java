package com.nusiss.neighbourlysg.util;

import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.entity.Role;

import java.util.Arrays;

public final class MasterEntityTestUtil {

    public static Profile createProfileEntity(){
        Profile profile = new Profile();
        profile.setConstituency("con");
        profile.setEmail("email");
        profile.setId(1L);
        profile.setRoles(Arrays.asList(createRoleEntity()));
        profile.setName("name");
        profile.setPassword("password");
        return profile;
    }

    public static Role createRoleEntity(){
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        return role;
    }
}
