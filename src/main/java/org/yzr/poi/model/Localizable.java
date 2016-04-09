package org.yzr.poi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaorongyi on 16/3/26.
 */
public class Localizable {

    private static final String COMMENT_KEY = "[COMMENT]";

    private String key;
    private List<String > values = new ArrayList<String>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void putValue(String value) {
        this.values.add(value);
    }

    public int getComment() {
        return this.key != null && this.key.equalsIgnoreCase(COMMENT_KEY) ? 1 : 0;
    }

    public String getValue() {
        if(this.getValues().size() > 0) {
            return this.getValues().get(0);
        }
        return null;
    }

    @Override
    public String toString() {
//        if(this.isComment()) {
//            return "// " + this.values +"\n";
//        }
        return "\""+key +"\" = \"" +values + "\";\n";
    }
}