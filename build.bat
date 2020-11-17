rem comment with rem

call mvn clean install

rem deleting earlier file and copy newer one
del release\executable.jar
del release\lib\*.jar
xcopy /s/Y target\executable.jar release
xcopy /s/Y target\lib\*.jar release\lib\