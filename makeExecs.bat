@ECHO OFF
cls
call mvn -o package
call "C:\Program Files\Launch4j\launch4j.exe" J:\workspace\j2DbParse\configParserLive.xml
call "C:\Program Files\Launch4j\launch4j.exe" J:\workspace\j2DbParse\configParser.xml
call parser-0.0.1-SNAPSHOT.exe
call parserLive-0.0.1-SNAPSHOT.exe

pause