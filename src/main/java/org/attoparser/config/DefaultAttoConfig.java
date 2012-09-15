package org.attoparser.config;




public class DefaultAttoConfig implements IAttoConfig {

    
    public DefaultAttoConfig() {
        super();
    }

    
    public boolean allowUnbalancedTags() {
        return true;
    }

    public boolean returnUnfinishedTagsAsText() {
        return true;
    }

    
}
