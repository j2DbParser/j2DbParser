14,5MB
added=21012

select level, max(time) as max_time, count(*) as how_many from log group by log.level order by how_many desc
LEVEL                                                                 MAX_TIME                                                              HOW_MANY
DEBUG                                                                 2010-03-19 15:43:28,654                                               19104
INFO                                                                  2010-03-19 15:30:42,122                                               1784
ERROR                                                                 2010-03-19 15:02:49,388                                               82
WARN                                                                  2010-03-19 15:30:42,435                                               42


select level, max(time) as max_time, min(time) as min_time, count(*) as how_many from log group by log.level order by how_many desc
LEVEL                                                                 MAX_TIME                                                              MIN_TIME                                                              HOW_MANY
DEBUG                                                                 2010-03-19 15:43:28,654                                               2010-03-19 14:30:16,841                                               19104
INFO                                                                  2010-03-19 15:30:42,122                                               2010-03-19 14:29:33,919                                               1784
ERROR                                                                 2010-03-19 15:02:49,388                                               2010-03-19 14:33:24,685                                               82
WARN                                                                  2010-03-19 15:30:42,435                                               2010-03-19 14:29:58,310                                               42
