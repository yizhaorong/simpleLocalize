package org.yzr.poi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yizhaorong on 2017/3/28.
 */
public class CopyWriteContainer {
    // 语言
    private String language;
    // 文案Key
    private List<Localize> copyWrites = new ArrayList<>();
    // 安卓文案
    private List<Localize> androidCopyWrites = new ArrayList<>();
    // 缺失文案
    private List<String> lostCopyWrites = new ArrayList<>();

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public List<Localize> getCopyWrites() {
        return copyWrites;
    }

    public void setCopyWrites(List<Localize> copyWrites) {
        this.copyWrites = copyWrites;
    }

    public List<Localize> getAndroidCopyWrites() {
        return androidCopyWrites;
    }

    public void setAndroidCopyWrites(List<Localize> androidCopyWrites) {
        this.androidCopyWrites = androidCopyWrites;
    }

    public List<String> getLostCopyWrites() {
        return lostCopyWrites;
    }

    public void setLostCopyWrites(List<String> lostCopyWrites) {
        this.lostCopyWrites = lostCopyWrites;
    }
}
