@echo on
CALL setup.cmd
cmd /C mvn failsafe:integration-test@default-test -Dmaven.test.additionalClasspath=out.jar
if %errorlevel% gtr 0 exit 1
cmd /C mvn failsafe:verify@default-cli
if %errorlevel% gtr 0 exit 2

exit 0

:: ========== FUNCTIONS ==========
EXIT /B

:NORMALIZEPATH
  SET RETVAL=%~dpfn1
  EXIT /B
