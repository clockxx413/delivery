package com.express.delivery.entity;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户注册请求 DTO
 */
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String realName;

    private String phone;

    @NotBlank(message = "学号不能为空")
    private String studentId;

    // ========== Getters & Setters ==========

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}
