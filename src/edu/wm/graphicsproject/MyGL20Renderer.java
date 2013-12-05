package edu.wm.graphicsproject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

public class MyGL20Renderer implements Renderer {
		
	Triangle mTriangle;
	Square mSquare;
	Mesh mMesh;
	Context context;
		
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjMatrix = new float[16];
	private final float[] mVMatrix = new float[16];
	private float[] xRotationMatrix = new float[16];
	private float[] yRotationMatrix = new float[16];
	private float[] mRotationMatrix = new float[16];
	//private float[] mRotationMatrix = new float[16];
	//private final float[] mRotationMatrix = new float[16];
	    
	// Declare as volatile because we are updating it from another thread
	public volatile float mAngle;
	public volatile float mAngle2;

	public MyGL20Renderer(Context context) {
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		//GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
	    // Set the camera position (View matrix)
	    Matrix.setLookAtM(mVMatrix, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

	    // Calculate the projection and view transformation
	    Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
	        
	    // Create a rotation transformation for the triangle
//	    long time = SystemClock.uptimeMillis() % 4000L;
//	    float angle = 0.090f * ((int) time);
	    //Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
	    Matrix.setRotateM(xRotationMatrix, 0, -mAngle2, 1, 0, 0f);
	    Matrix.setRotateM(yRotationMatrix, 0, -mAngle, 0, 1, 0f);
	    Matrix.multiplyMM(mRotationMatrix, 0, xRotationMatrix, 0, yRotationMatrix, 0);
	        
	    // Combine the rotation matrix with the projection and camera view
	    Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

	    // Draw shape

	    mMesh.draw(mMVPMatrix);
	    //mTriangle.draw(mMVPMatrix);
	       
		}

		@Override
		public void onSurfaceChanged(GL10 unused, int width, int height) {
			GLES20.glViewport(0, 0, width, height);
			
		    float ratio = (float) width / height;

		    // this projection matrix is applied to object coordinates
		    // in the onDrawFrame() method
		    Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

		}

		@Override
		public void onSurfaceCreated(GL10 unused, EGLConfig config) {
			
			// Set the background frame color
	        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	        
	        GLES20.glDepthFunc(GL10.GL_LEQUAL);
	        GLES20.glEnable(GL10.GL_DEPTH_TEST);
	        GLES20.glEnable(GL10.GL_CULL_FACE);
	        GLES20.glCullFace(GL10.GL_BACK);
	        
		    ObjImporter oi = new ObjImporter(context);
		    mMesh = oi.getMeshFromObj(R.raw.alduin);
	        
	        // initialize a triangle
	        //mTriangle = new Triangle();
	        // initialize a square
	        //mSquare = new Square();

		}

	}
