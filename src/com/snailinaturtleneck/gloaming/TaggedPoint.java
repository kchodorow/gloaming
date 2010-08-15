package com.snailinaturtleneck.gloaming;

import android.graphics.PointF;

public class TaggedPoint<T> extends PointF {
    public final T tag;
    public TaggedPoint(T tag) {
        super();
        this.tag = tag;
    }
    public TaggedPoint(PointF pt, T tag) {
        super(pt.x, pt.y);
        this.tag = tag;
    }
    public TaggedPoint(float x, float y, T tag) {
        super(x, y);
        this.tag = tag;
    }
    public TaggedPoint(TaggedPoint<T> tagpt) {
        super(tagpt.x, tagpt.y);
        this.tag = tagpt.tag;
    }
    @Override
    public String toString() {
        return super.toString() + ":" + tag;
    }
    public static<T> TaggedPoint<T> make(float x, float y, T t) {
        return new TaggedPoint<T>(x, y, t);
    }
}