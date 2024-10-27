package com.nusiss.neighbourlysg.config;

public class ErrorMessagesConstants {
    public static final String PROFILE_NOT_FOUND = "Profile not found with id: ";
    public static final String ROLE_NOT_FOUND = "Role not found with id: ";
    public static final String USER_NOT_FOUND = "User not found with id: ";
    public static final String POST_NOT_FOUND = "Post not found with id: ";
    public static final String COMMENT_NOT_FOUND = "Comment not found with id: ";
    public static final String SURVEY_NOT_FOUND = "Survey not found with id: ";
    public static final String SURVEY_RESPONSE_NOT_FOUND = "Survey response not found with id: ";

    // Private constructor to prevent instantiation
    private ErrorMessagesConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
