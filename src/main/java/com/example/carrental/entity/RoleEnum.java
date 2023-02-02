package com.example.carrental.entity;

public enum RoleEnum {
    Admin,
    Client;

    public static String getLabel(RoleEnum role) {
        if (role == RoleEnum.Admin) {
            return "Admin";
        }

        return "Client";
    }
}
