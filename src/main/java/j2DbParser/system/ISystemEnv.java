package j2DbParser.system;

public interface ISystemEnv {
	public static final String JBOSS_LOG_DIR = System.getenv("JBOSS_LOG_DIR");
	public static final String SO_DUMP_DIR = System.getenv("SO_DUMP_DIR");
}
