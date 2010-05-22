@ECHO OFF 
cd.. 
call hg add
call hg commit -m "%1"
call hg push
cd batch 
