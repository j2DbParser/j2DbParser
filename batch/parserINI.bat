@ECHO OFF 
cd.. 
cd target
parser -f src/test/resources/example.log -r log
cd ..
cd batch 
