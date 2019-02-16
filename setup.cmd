for /D %%f in (target\jdk-*) do (
    CALL :NORMALIZEPATH %%f
)
set JAVA_HOME=%RETVAL%
set PATH=%RETVAL%\bin;%PATH%

for /D %%f in (target\apache-maven-*) do (
    CALL :NORMALIZEPATH %%f
)
set MAVEN_HOME=%RETVAL%
set PATH=%RETVAL%\bin;%PATH%

for %%i in (%MAVEN_HOME%\boot\plexus-classworlds-*) do set CLASSWORLDS_JAR="%%i"

:: ========== FUNCTIONS ==========
EXIT /B

:NORMALIZEPATH
  SET RETVAL=%~dpfn1
  EXIT /B
