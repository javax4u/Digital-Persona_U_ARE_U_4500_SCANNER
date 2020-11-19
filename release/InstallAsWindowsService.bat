@echo off
set JAVAX4U_HOME=c:\VirtualDoxx
rem set your computer java dll path here
set javadll=C:\Program Files\Java\jdk1.8.0_201\jre\bin\server\jvm.dll
set outlog=%JAVAX4U_HOME%\stdout.log
set errlog=%JAVAX4U_HOME%\stderr.log

sc delete CamelSocketServer
rem del %JAVAX4U_HOME%\RFIDListener2\*.log
c:\VirtualDoxx\antcalls\JavaService_64_bit.exe -install CamelSocketServer "%javadll%" -Dlog4j.configurationFile=file:"log4j2.xml" -Djava.class.path=".;executable.jar" -start  com.javax4u.camel.StartSocketServerNetty -params future_use_1 future_use_2 -out "%outlog%" -err "%errlog%" -current "%JAVAX4U_HOME%\CamelService"

