package org.antlr.md;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MDTests {
	@Test
	public void testHeader() {
		check("# header\n", "(file (elem (header #   h e a d e r \\n)) <EOF>)");
	}

	@Test
	public void testP() throws IOException {
		runStandardTest("p");
	}

	@Test
	public void testH() throws IOException {
		runStandardTest("h");
	}

	@Test
	public void testH2() throws IOException {
		runStandardTest("h2");
	}

	@Test
	public void testList() throws IOException {
		runStandardTest("list");
	}

	@Test
	public void testT() throws IOException {
		runStandardTest("t");
	}

	public void runStandardTest(String baseName) throws IOException {
		String input = loadInputFile("input/" + baseName + ".md");
		String expected = loadInputFile("input/" + baseName + ".md.tree");
		check(input, expected);
	}

	public String loadInputFile(String name) throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(name)));
		try {
			reader.mark(1024 * 1024);
			long fileLength = reader.skip(1024 * 1024);
			reader.reset();
			char[] buffer = new char[(int)fileLength];
			int read = reader.read(buffer);
			return new String(buffer, 0, read).replace("\r\n", "\n");
		}
		finally {
			reader.close();
		}
	}

	public void check(String input, String expected) {
		ParserRuleContext t = getParseTree(input);
		assertEquals(expected, asString(t));
	}

	public String asString(ParserRuleContext t) {
		return t.toStringTree(Arrays.asList(MarkdownParser.ruleNames));
	}

	public ParserRuleContext getParseTree(String input) {
		ANTLRInputStream chars = new ANTLRInputStream(input);
		CharsAsTokens charTokens = new CharsAsTokens(chars, MarkdownParser.tokenNames);
		CommonTokenStream tokens = new CommonTokenStream(charTokens);
		MarkdownParser parser = new MarkdownParser(tokens);
		ParserRuleContext t = parser.file();
//		t.inspect(Arrays.asList(MarkdownParser.ruleNames));
		return t;
	}
}
