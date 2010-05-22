@ECHO OFF 
cd.. 
del testdb.lck
del testdb.log
del testdb.properties
del testdb.script
rd testdb.tmp
cd batch
pause
