package net.anzix.imprempta;

import org.junit.Test;

/**
 * Test to generate a whole site from the test-resource dir
 */
public class ExampleGenerationTest {

    @Test
    public void main() {
        String source = "../samples/wiki";
        Start.main(new String[]{"-v", "-p", "wiki", "-s", source, "extension"});
        Start.main(new String[]{"-v", "-p", "wiki", "-s", source, "generate", "-d", "build/wiki-sample"});
    }
}
