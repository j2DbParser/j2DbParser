@ECHO OFF
copy config.ini target
copy rules.ini target
cd target
call parser -f "%SO_DUMP_DIR%\votes.xml" -r SO
call parser -f "%SO_DUMP_DIR%\users.xml" -r SO
call parser -f "%SO_DUMP_DIR%\badges.xml" -r SO
call parser -f "%SO_DUMP_DIR%\comments.xml" -r SO
call parser -f "%SO_DUMP_DIR%\posts.xml" -r SO
cd..