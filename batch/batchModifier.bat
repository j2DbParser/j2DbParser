type ah > $1
echo @ECHO OFF > ah
echo cd.. >> ah
type $1 >> ah
echo. >> ah
echo cd batch >> ah