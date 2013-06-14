package net.anzix.imprempta.impl;

import com.github.rjeschke.txtmark.Processor;
import net.anzix.imprempta.api.TextContent;
import net.anzix.imprempta.api.Transformer;


public class TxtmarkTransformer implements Transformer {

    @Override
    public void transform(TextContent content) {
        content.setContent(Processor.process(content.getContent()));
    }
}
