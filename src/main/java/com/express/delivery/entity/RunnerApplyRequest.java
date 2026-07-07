package com.express.delivery.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 跑腿员申请请求 DTO
 */
public class RunnerApplyRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "学号不能为空")
    private String studentId;

    // ========== Getters & Setters ==========

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}
