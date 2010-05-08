package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import static j2DbParser.cli.EOptions.VERSION;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class EOptionsExampleTest {
	@Test
	public void exampleEmpty() throws Exception {
		EOptions[] options = {};
		List<String> asList = Arrays.asList(EOptions.example(options));
		assertEquals("[]", asList.toString());
	}

	@Test
	public void exampleVersion() throws Exception {
		EOptions[] options = { VERSION };
		List<String> asList = Arrays.asList(EOptions.example(options));
		assertEquals("[-v]", asList.toString());
	}

	@Test
	public void exampleParser() throws Exception {
		EOptions[] options = { FILE, RULE_NAME };
		List<String> asList = Arrays.asList(EOptions.example(options));
		assertEquals("[-f, example.log, -r, log]", asList.toString());
	}
}

