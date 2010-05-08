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

select count(data) as count_data, data from log group by data having count(data)>10 order by count_data desc
COUNT_DATA                                                            DATA
58                                                                    No xml files found
58                                                                    using deployer org.jboss.deployment.JARDeployer@100363
40                                                                    createLoaderRepository from config: LoaderRepositoryConfig(repositoryName: JMImplementation:service=LoaderRepository,name=Default, repositoryClassName: null, configParserClassName: null, repositoryConfig: null)
32                                                                    about to copy 0 local directories
29                                                                    using deployer org.jboss.deployment.SARDeployer@da3a1e
12                                                                    webContext: null
12                                                                    Multiple class loaders found for pkg:
11                                                                    considering <anonymous> with object name jboss:service=Naming

select data, count(data) as count_data from log group by data having count(data) = ( select max(cnt) from ( select data, count(data) cnt from log group by data))
DATA                                                                  COUNT_DATA
No xml files found                                                    58
using deployer org.jboss.deployment.JARDeployer@100363                58

select length(data) as length from log group by data having length(data)>300 order by length desc
LENGTH
700
462
381
379
359
353
348
338
335
326
315
304

select count(data) as count_data from log
COUNT_DATA
6741

select count(data) as count_data from log where data like '%jboss%'
COUNT_DATA
5901


