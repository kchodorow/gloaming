package com.snailinaturtleneck.gloaming;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

	final long sleeptime = 100;
	private final Resources resources;
	private final Queue<Drawable> drawables;
	private final Queue<Movable> movables;
    private final Queue<Physics> physics;
	private Player fingerFollower;
	private SurfaceHolder holder;
	private Timer timer;
	
	public GamePanel(Context context) {
		super(context);
		resources = context.getResources();
		drawables = new LinkedList<Drawable>();
		movables = new LinkedList<Movable>();
		physics = new LinkedList<Physics>();

		fingerFollower = new Player(3.0f, happyFunBall(),
                new PointF(0, 0));
		movables.add(fingerFollower);
		timer = new Timer(true);
		getHolder().addCallback(this);
	}

	public synchronized void surfaceCreated(SurfaceHolder holder) {
	    this.holder = holder;
	    log("Created; ignoring");
	}
	public synchronized void surfaceChanged(SurfaceHolder holder2, int format, int width,
			int height) {
	    log("Got the kickoff; starting the game");
	    timer.scheduleAtFixedRate(new TimerTask() {
	        private long lastRunTime = System.currentTimeMillis();
            @Override
            public void run() {
                long currTime = System.currentTimeMillis();
                long diffTicks = currTime - lastRunTime;
                lastRunTime = currTime;
                Set<Movable> toRemove = new HashSet<Movable>();
                for (Movable m : movables) {
                    m.animate(diffTicks);
                    if (!m.isActive()) {
                        toRemove.add(m);
                    }
                }
                movables.removeAll(toRemove);
                Canvas c = null;
                  try {
                      c = holder.lockCanvas(null);
                      // synchronized (mSurfaceHolder) {
                      draw(c);
                      // }
                  } finally {
                      // do this in a finally so that if an exception is thrown
                      // during the above, we don't leave the Surface in an
                      // inconsistent state
                      if (c != null) {
                          holder.unlockCanvasAndPost(c);
                      }
                  }// end finally block
            }               
        }, 0, sleeptime);
	}
	public synchronized void surfaceDestroyed(SurfaceHolder holder) {
		this.holder = null;
	}
	
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	int action = event.getAction();
        switch(action) {
    	case MotionEvent.ACTION_DOWN:
    	    fingerFollower.setVisible(true, false);
    	    drawables.add(fingerFollower);
    	    // fall through
    	case MotionEvent.ACTION_MOVE:
    	    // fall through
    	case MotionEvent.ACTION_UP:
    		appendFingerFollower(event, "" + action);
    		return true;
    	default:
    		log("I got bad-touched " + event);
    		return false;
    	}
	}

	private void appendFingerFollower(MotionEvent event, String eventType) {
		log ("Adding a goal for finger follower");
		fingerFollower.feed(TaggedPoint.make(event.getX(), event.getY(), eventType));
	}

	private Bitmap happyFunBall() {
		return BitmapFactory.decodeResource(resources, R.drawable.happyfunball);
	}

	@Override
    public synchronized void draw(Canvas canvas) {
    	super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
    	canvas.save();
    	Set<Drawable> toRemove = new HashSet<Drawable>();
    	for (Drawable drawable : drawables) {
            drawable.draw(canvas);
    		if (!drawable.isVisible()) {
    		  toRemove.add(drawable);
    		}
    	}
    	drawables.removeAll(toRemove);
    	canvas.restore();
    }

	void log(String... tolog) {
		StringBuilder b = new StringBuilder();
		for (String s : tolog) {
		  b.append(s);
		}
		android.util.Log.d("gamepanel", b.toString());
	}

	float random() {
		return (float) (Math.random() * 150);
	}
}
