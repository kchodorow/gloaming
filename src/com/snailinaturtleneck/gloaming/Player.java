package com.snailinaturtleneck.gloaming;

import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.FloatMath;
import android.util.Log;

public class Player extends Drawable implements Movable {
	private final Queue<TaggedPoint<String>> touches;
	private final float speed;
	private final PointF curpos;
	private final Bitmap drawable;

	public Player(float speed, Bitmap drawable, PointF curpos) {
		this.drawable = drawable;
		this.speed = speed;
		this.touches = new LinkedList<TaggedPoint<String>>();
		this.curpos = curpos;
	}
	public void feed(TaggedPoint<String> touch) {
		touches.add(touch);
	}
	@Override
	public void animate(long ticks) {
		TaggedPoint<String> next = touches.peek();
        Log.d("fingerfollower", "Animate called w/ " + ticks + " ticks towards " + next);
		if (next != null) {
			float x_veloc = veloc(curpos.x, next.x, speed, ticks);
			float y_veloc = veloc(curpos.y, next.y, speed, ticks);
			if (FloatMath.floor(x_veloc) <= 1 && FloatMath.floor(y_veloc) <= 1) {
				touches.remove();
				Log.d("fingerfollower", "Reached " + next.tag);
			}
			this.curpos.offset(x_veloc, y_veloc);
		} else {
			this.setVisible(false, false);
		}
	}
	private float veloc (float start, float goal, float speed, long ticks) {
		float diff = goal - start;
		float sign = Math.signum(diff);
		float abs = Math.abs(diff);
		return sign * Math.max(speed * ticks / 1000, abs);
	}
	@Override
	public void draw(Canvas canvas) {
	    Paint paint = new Paint();
	    paint.setColor(Color.WHITE);
	    Log.d("fingerfollow", "Got a request to draw");
		canvas.drawBitmap(drawable, curpos.x, curpos.y, null);
	}
	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}
	@Override
	public void setAlpha(int alpha) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setColorFilter(ColorFilter cf) {
		throw new UnsupportedOperationException();
	}		
    public boolean isActive() {
        return true;
    }
}