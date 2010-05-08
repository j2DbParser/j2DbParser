package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import j2DbParser.db.viewer.ResultSetSimpleViewer;
import j2DbParser.io.IDataReader;
import j2DbParser.junit.SystemOutWrapper;
import j2DbParser.system.MockStopperSingleton;

import org.junit.BeforeClass;
import org.junit.Test;

public class ParserLiveStartTest {

	@BeforeClass
	public static void setUp() {
		MockStopperSingleton.init();
	}

	@Test
	public void start() throws Exception {
		final String[] args = EOptions.example(FILE, RULE_NAME);
		final ParserLive parser = new ParserLive();

		// TODO: change to list
		IDataReader mockReader = createNiceMock(IDataReader.class);
		expect(mockReader.hasNext()).andReturn(true);
		expect(mockReader.next()).andReturn(
				"SELECT class,level,time,data FROM log");
		expect(mockReader.hasNext()).andReturn(false);
		replay(mockReader);

		parser.reader = mockReader;
		parser.viewer = new ResultSetSimpleViewer();

		SystemOutWrapper wrapper = wrapperWithStop(args, parser, false);

		String systemOut = wrapper.getSystemOut();
		System.out.println("systemOut=" + systemOut);

	}

	@Test
	public void startNoArgsRule() throws Exception {
		final String[] args = {};
		final ParserLive parser = new ParserLive();

		SystemOutWrapper wrapper = wrapperWithStop(args, parser, true);

		String systemOut = wrapper.getSystemOut();
		assertTrue("default option", systemOut.indexOf("usage:") != -1);
	}

	private SystemOutWrapper wrapperWithStop(final String[] args,
			final ParserLive parser, boolean stop) throws Exception {
		SystemOutWrapper wrapper = new SystemOutWrapper() {
			@Override
			public void run() throws Exception {
				parser.start(args);
			}
		};
		try {
			wrapper.start();
			if (stop) {
				fail("stop?");
			}
		} catch (IllegalStateException e) {
			assertEquals("stop", e.getMessage());
		}
		return wrapper;
	}
}
