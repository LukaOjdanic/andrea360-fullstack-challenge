package com.andrea.fitness.model;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    EMPLOYEE("ROLE_EMPLOYEE"),
    MEMBER("ROLE_MEMBER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }

    /**
     * Accepts either the bare name ("EMPLOYEE") or the prefixed form ("ROLE_EMPLOYEE")
     * Example: Role.valueOf("EMPLOYEE") works; Role.from("ROLE_EMPLOYEE") 
     */
    public static Role from(String nameOrPrefixed) {
        if (nameOrPrefixed == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        String normalized = nameOrPrefixed.startsWith("ROLE_") ? nameOrPrefixed.substring(5) : nameOrPrefixed;
        return Role.valueOf(normalized);
    }
}
