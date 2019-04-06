package com.danielthedev.config;

public class ConfigValueValidatorResult {

    public static final ConfigValueValidatorResult success = new ConfigValueValidatorResult(true);

    private final boolean succces;
    private String error_message;

    public ConfigValueValidatorResult(boolean succces) {
        this.succces = succces;
    }

    public static ConfigValueValidatorResult error(String msg) {
        return new ConfigValueValidatorResult(msg);
    }

    public ConfigValueValidatorResult(String error_message) {
        this.succces = false;
        this.error_message = error_message;
    }

    public boolean isSuccces() {
        return succces;
    }

    public String getError_message() {
        return error_message;
    }
}
