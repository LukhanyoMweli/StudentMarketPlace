package za.ac.cput.studentmarketplace.dto;

public class AuthResponse {

    private boolean success;
    private String message;
    private String fullName;
    private String role;

    public AuthResponse(boolean success, String message, String fullName, String role) {
        this.success = success;
        this.message = message;
        this.fullName = fullName;
        this.role = role;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }
}
