package com.example.customview.flow;

/**
 * Created by Administrator on 2017/7/14.
 */

/** 流程节点 */
public class FlowPoint {
    private String title;
    private String content;

    public FlowPoint(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static class Builder {
        private String title;
        private String content;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public FlowPoint build() {
            return new FlowPoint(title, content);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
