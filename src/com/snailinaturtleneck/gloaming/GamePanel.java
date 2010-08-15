package com.snailinaturtleneck.gloaming;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class GamePanel extends View implements Runnable {
	class DrawableThing {
		float x;
		float y;
		long started;
		String msg;
		public DrawableThing(float x, float y, long started, String msg) {
			this.x = x;
			this.y = y;
			this.started = started;
			this.msg = msg;
		}
	}

	void log(String... tolog) {
		StringBuilder b = new StringBuilder();
		for (String s : tolog) {
		  b.append(s);
		}
		android.util.Log.d("gamepanel", b.toString());
	}

	final Queue<DrawableThing> touches = new ConcurrentLinkedQueue<DrawableThing>();
	final long sleeptime = 300;
	
    public GamePanel(Context context) {
		super(context);
	}

    @Override
	public boolean onTouchEvent(MotionEvent event) {
		touches.add(
			new DrawableThing(event.getX(), event.getY(), event.getDownTime(), "Touch" + event.getAction()));
		log("I got bad-touched " + event);
    	return true;
	}

    @Override
    public synchronized void draw(Canvas canvas) {
    	super.onDraw(canvas);
    	canvas.save();
    	Set<DrawableThing> toRemove = new HashSet<DrawableThing>();
    	boolean shouldCull = false;
    	for (DrawableThing touch : touches) {
    		if (shouldCull && System.currentTimeMillis() - touch.started > 10000) {
    		  toRemove.add(touch);
    		  continue;
    		}
        	Paint paint = new Paint();
        	paint.setColor(Color.WHITE);
        	canvas.drawText(touch.msg,
        			touch.x, touch.y, paint);
    	}
    	touches.removeAll(toRemove);
    	canvas.restore();
    }

    /**
     * Invoked from a separate thread, this spams "draw" every so often.
     */
	public void run() {
		while(true) {
		  try {
			Thread.sleep(sleeptime);
  		  } catch (InterruptedException e) {
			// omnomnom
		  }
  		  log("Woke up; redrawing @ " + System.currentTimeMillis());
  		  this.touches.add(new DrawableThing(random(), random(), System.currentTimeMillis(), "draw!"));
  		  this.postInvalidate();
		}
	}
	float random() {
		return (float) (Math.random() * 150);
	}
}
