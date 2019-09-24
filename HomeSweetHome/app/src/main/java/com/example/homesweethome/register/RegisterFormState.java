package com.example.homesweethome.register;
import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer cfnPasswordError;

    private boolean isDataValid;

    RegisterFormState(@Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer cfnPasswordError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.cfnPasswordError = cfnPasswordError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.passwordError = null;
        this.cfnPasswordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return passwordError;
    }

    @Nullable
    Integer getPasswordError() {
        return cfnPasswordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
