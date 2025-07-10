package com.shinhan.pda_midterm_project.common.response;

public record Response<T>(Boolean status, String code, String message, T data) {
    public static <T> Response<T> success(String code, String message, T data) {
        return new Response<>(true, code, message, data);
    }

    public static <T> Response<T> success(String code, String message) {
        return new Response<>(true, code, message, null);
    }

    public static <T> Response<T> failure(String code, String message) {
        return new Response<>(false, code, message, null);
    }

    public static <T> Response<T> failure(String code, String message, T data) {
        return new Response<>(false, code, message, data);
    }
}
