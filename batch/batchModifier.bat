type %1 > $1
echo @ECHO OFF > %1
echo cd.. >> %1
type $1 >> %1
echo. >> %1
echo cd batch >> %1