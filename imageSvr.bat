set path=%~dp0
echo %path%
cd nginx-1.12.2
start nginx.exe
echo "当前nginx启动，静态资源地址：" %path%
pause 