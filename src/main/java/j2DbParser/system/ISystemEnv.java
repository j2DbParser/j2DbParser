package j2DbParser.system;

public interface ISystemEnv {
	final String JBOSS_LOG_DIR = System.getenv("JBOSS_LOG_DIR");
	final String SO_DUMP_DIR = System.getenv("SO_DUMP_DIR");
	final String START_DIR = System.getenv("START_DIR");
}
