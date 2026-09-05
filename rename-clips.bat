@echo off
setlocal
chcp 65001 >nul
set JAVA_OPTS=-Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Dfile.encoding=UTF-8 %JAVA_OPTS%
kotlin "%~dp0script.main.kts" %*
endlocal