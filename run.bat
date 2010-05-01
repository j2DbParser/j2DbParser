@ECHO OFF
cls
mvn -o exec:java -Dexec.mainClass="j2DbParser.cli.ParserLive"
rem -Dexec.args="-X myproject:dist"