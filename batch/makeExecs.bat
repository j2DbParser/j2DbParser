@ECHO OFF 
cd.. 
cls
call mvn -o package
call "C:\Program Files\Launch4j\launch4j.exe" J:\workspace\j2DbParse\configParser.xml
cd target
call parser.exe
cd ..
cd batch 
pause 