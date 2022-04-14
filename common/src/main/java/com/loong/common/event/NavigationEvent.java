package com.loong.common.event;

/*******************************************************************
 * NavigationEvent.java  2019-09-17
 * <P>
 * <br/>
 * <br/>
 * </p>
 *
 * @author:chengzm
 *
 ******************************************************************/
public class NavigationEvent {
    public String type;
    public String content;

    public NavigationEvent() {
    }

    public NavigationEvent(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
