@echo off
cd /d C:\Users\yiyanwuxiao\Desktop\campus-express\delivery

echo ============================================
echo  校园快递代取 - Git 提交推送脚本
echo ============================================
echo.

echo [1/3] 暂存所有文件...
git add -A
if %errorlevel% neq 0 (
    echo 错误: git add 失败!
    pause
    exit /b 1
)
echo 完成!
echo.

echo [2/3] 提交代码...
git commit -m "feat(runner): 跑腿员申请模块 - 实体+Mapper+Service+Controller

- 新增 Runner 实体类 (entity/Runner.java)
- 新增 RunnerApplyRequest DTO (entity/RunnerApplyRequest.java)
- 新增 RunnerMapper (mapper/RunnerMapper.java)
- 新增 RunnerService 接口及实现 (service/RunnerService, service/impl/RunnerServiceImpl)
- 新增 RunnerController - POST /api/runner/apply (controller/RunnerController.java)
- 新增统一响应类 Result (common/Result.java)
- 新增全局异常处理 (common/GlobalExceptionHandler.java)
- 新增 MyBatis-Plus 配置 (config/MybatisPlusConfig.java)
- 更新 pom.xml 添加 MyBatis-Plus/Lombok/Validation 依赖
- 新增 runner.sql 建表脚本
- 新增 Postman 测试集合"
if %errorlevel% neq 0 (
    echo 错误: git commit 失败!
    pause
    exit /b 1
)
echo 完成!
echo.

echo [3/3] 推送到 GitHub...
git push origin main
if %errorlevel% neq 0 (
    echo 错误: git push 失败! 请检查网络和 GitHub 登录状态。
    pause
    exit /b 1
)
echo 完成!
echo.

echo ============================================
echo  全部完成! 代码已推送到:
echo  https://github.com/clockxx413/delivery.git
echo ============================================
pause
