package net.anzix.imprempta;

import org.junit.Test;

/**
 * Test to generate a whole site from the test-resource dir
 */
public class StartIntegrationTest {

    @Test
    public void main() {
        String source = "src/test/resources/jhacks";
        Start.main(new String[]{"-s", source, "extension"});
        Start.main(new String[]{"-s", source, "generate", "-d", "build/_site"});
    }
}
