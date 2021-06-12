package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResDTO {
    private boolean isValid;
    private String jsonWebToken;
    private String errorMessage;
    private Integer errorCode;
    private String role;
    private int idAccount;


    public static LoginResDTO createErrorResponse(Error error){
        return new LoginResDTO(false, null, error.getMessage(), error.getCode(), null, 0);
    }

    public static LoginResDTO createSuccessResponse(String jsonWebToken, String role, int idAccount){
        return new LoginResDTO(true,jsonWebToken, null, null, role, idAccount);
    }

    public enum Error{
        USERNAME_NOT_FOUND(1,"Số điện thoại không tồn tại"),
        WRONG_PASSWORD(2,"Sai mật khẩu, vui lòng kiểm tra lại");

        private final int code;
        private final String message;

        Error(int code, String message){
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
