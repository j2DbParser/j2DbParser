package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULES_FILE;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertTrue;
import j2DbParser.db.viewer.ResultSetSimpleViewer;
import j2DbParser.io.IDataReader;
import j2DbParser.junit.SystemOutWrapper;

import org.junit.Test;

public class ParserLiveStartTest {


	// @Test
	public void start() throws Exception {
		String[] args = EOptions.example(FILE, RULES_FILE);
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

		SystemOutWrapper wrapper = wrap(parser, args);

		String systemOut = wrapper.getSystemOut();
		System.out.println("systemOut=" + systemOut);

	}

	@Test
	public void startNoArgsRule() throws Exception {
		final String[] args = {};
		final ParserLive parser = new ParserLive();

		SystemOutWrapper wrapper = wrap(parser, args);

		String systemOut = wrapper.getSystemOut();
		assertTrue("default option", systemOut.indexOf("usage:") != -1);
	}

	private SystemOutWrapper wrap(final ParserLive parser, final String[] args)
			throws Exception {
		SystemOutWrapper wrapper = new SystemOutWrapper() {
			@Override
			public void run() throws Exception {
				parser.start(args);
			}
		}.start();
		return wrapper;
	}
}
