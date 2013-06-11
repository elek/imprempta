package net.anzix.imprempta;

import junit.framework.Assert;
import net.anzix.imprempta.api.Phase;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;
import net.anzix.imprempta.impl.NoTransformer;
import org.junit.Test;

public class GuiceConfigTest {

    @Test
    public void testUse() throws Exception {
        GuiceConfig c = new GuiceConfig(null);
        c.transformations.add(new GuiceConfig.PhasedClass(NoTransformer.class, Phase.PARSE));
        c.use(MyTrafo.class).after(Phase.PARSE);
        Assert.assertEquals(2, c.transformations.size());
        Assert.assertEquals(NoTransformer.class, c.transformations.get(0).type);
        Assert.assertEquals(MyTrafo.class, c.transformations.get(1).type);


    }

    private static class MyTrafo implements Transformer {

        @Override
        public void transform(TextContent content) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
