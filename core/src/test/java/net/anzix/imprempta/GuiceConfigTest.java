package net.anzix.imprempta;

import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;
import org.junit.Test;

public class GuiceConfigTest {


    @Test
    public void testUse() throws Exception {
        GuiceConfig c = new GuiceConfig(null, null);
//        c.extensions.map(Transformer.class, new Extension(NoTransformer.class, Phase.PARSE));
//        c.use(MyTrafo.class).as(Transformer.class).after(Phase.PARSE);
//        Assert.assertEquals(1, c.extensions.size());
//        Assert.assertEquals(2, c.extensions.get(Transformer.class).size());
//        Assert.assertEquals(NoTransformer.class, c.extensions.get(Transformer.class).get(0).type);
//        Assert.assertEquals(MyTrafo.class, c.extensions.get(Transformer.class).get(1).type);


    }

    private static class MyTrafo implements Transformer {

        @Override
        public void transform(TextContent content) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}
