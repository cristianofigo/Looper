package com.musicamovel.looper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdService;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;



import processing.core.PApplet;
import controlP5.*;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
//import android.view.MotionEvent;

public class LoopActivity extends PApplet {


	
	
	public int sketchWidth() {
		return displayWidth;
	}

	public int sketchHeight() {
		return displayHeight;
	}

	public String sketchRenderer() {
		return JAVA2D;
	}
	
	private PdService pdService = null;
	private PdUiDispatcher dispatcher;
	float pd = 0;
	String TAG="Processing Template";
	
	int bgCol = color(255);
	
	
	/**
	 * setting up libPd as a background service the initPdService() method binds
	 * the service to the background thread. call initPdService in onCreate() to
	 * start the service.
	 */

	protected final ServiceConnection pdConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pdService = ((PdService.PdBinder) service).getService();

			try {
				initPd();
				loadPatch();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
				finish();
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// Never called

		}
	};

	/* Bind pd service */

	private void initPdService() {

		new Thread() {
			@Override
			public void run() {
				bindService(new Intent(LoopActivity.this, PdService.class),
						pdConnection, BIND_AUTO_CREATE);
			}
		}.start();
	}

	/* initialise pd, also setup listeners here */
	protected void initPd() throws IOException {

		// Configure the audio glue
		int sampleRate = AudioParameters.suggestSampleRate();


		pdService.initAudio(sampleRate, 1, 2, 30.0f);
		
		pdService.startAudio(new Intent(this, LoopActivity.class),
				R.drawable.icon, "Sample", "Return to Processing");

		dispatcher = new PdUiDispatcher();
		PdBase.setReceiver(dispatcher);



	}

	protected void loadPatch() throws IOException {

		if (pd == 0) {
			File dir = getFilesDir();
			IoUtils.extractZipResource(
					getResources().openRawResource(
							com.musicamovel.looper.R.raw.synth), dir,
					true);
			File patchFile = new File(dir, "synth.pd");
			pd = PdBase.openPatch(patchFile.getAbsolutePath());


		}
	}
	
	public float[] getArrayFromPd(String array1) {
	int sizeArray = PdBase.arraySize(array1);
	//Log.i("LoopActivity", "sizeArray " + sizeArray);
	float[] frompd = new float[sizeArray];
	PdBase.readArray(frompd, 0, array1, 0, sizeArray);
	
	
	return frompd;
}
	
	

	ControlP5 cp5;
	
	boolean ligafx1 = false;
	
	boolean ligafx2 = false;
	
	
	public void setup() {	
	
	  cp5 = new ControlP5(this);
	  
	  
	  cp5.getTab("default")
	     .activateEvent(true)
	     .setLabel("Loops")
	     .setWidth((int)(width*.1f))
	     .setHeight((int)(height*.1f))
	     .setId(1)
	     ;
	  
	  cp5.getTab("Fx")
	     .activateEvent(true)
	     .setLabel("Fx")
	     .setWidth((int)(width*.1f))
	     .setHeight((int)(height*.1f))
	     .setId(2)
	     ;
	  
	  cp5.getTab("Rec")
	     .activateEvent(true)
	     .setLabel("Rec")
	     .setWidth((int)(width*.1f))
	     .setHeight((int)(height*.1f))
	     .setId(3)
	     ;
	  
	  cp5.getTab("ABOUT")
	     .activateEvent(true)
	     .setLabel("ABOUT")
	     .setWidth((int)(width*.1f))
	     .setHeight((int)(height*.1f))
	     .setId(4)
	     ;
	  
/// loops	  
	  
	  //rec1
	  cp5.addButton("Canal1")
	     .setValue(0)
	     .setPosition(width*0.1f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  //rec2
	  cp5.addButton("Canal2")
	     .setValue(0)
	     .setPosition(width*0.25f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  //rec3
	  cp5.addButton("Canal3")
	     .setValue(0)
	     .setPosition(width*0.4f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  //rec4
	  cp5.addButton("Canal4")
	     .setValue(0)
	     .setPosition(width*0.55f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  //vol1
	  cp5.addSlider("vol1")
	     .setPosition(width*.1f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.1f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("vol2")
	     .setPosition(width*.25f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.1f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("vol3")
	     .setPosition(width*.4f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.1f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;

	  cp5.addSlider("vol4")
	     .setPosition(width*.55f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.1f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("volume_global")
	     .setPosition(width*.7f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.15f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setColorActive(100)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("bpm_gui")
	     .setPosition(width*.7f,height*.15f)
	     .setRange(0,200)
	     .setSize(((int)(width*0.3f)),((int)(height*0.1f)))
	     .setValue(120)
	     .setColorActive(100)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  //// fx
	  
	  cp5.addToggle("ligafx1")
	     .setValue(0)
	     .setPosition(width*0.1f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  
	  cp5.addToggle("ligafx2")
	     .setValue(0)
	     .setPosition(width*0.25f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	     
	  
	  cp5.getController("ligafx1").moveTo("Fx");
	  cp5.getController("ligafx2").moveTo("Fx");
//	  cp5.getController("Canal2").moveTo("Loops");
//	  cp5.getController("Vol2").moveTo("Loops");
//	  cp5.getController("Canal3").moveTo("Loops");
//	  cp5.getController("Vol3").moveTo("Loops");
//	  cp5.getController("Canal4").moveTo("Loops");
//	  cp5.getController("Vol4").moveTo("Loops");
//	  cp5.getController("volume_global").moveTo("Loops");
//	  cp5.getController("bpm_gui").moveTo("Loops");
	  
	  
	  
	  
	}
	
	public void Canal1(int theValue) {
		 PdBase.sendBang("rec1");
		}
	
	public void Canal2(int theValue) {
		 PdBase.sendBang("rec2");
		}
	
	public void Canal3(int theValue) {
		 PdBase.sendBang("rec3");
		}
	
	public void Canal4(int theValue) {
		 PdBase.sendBang("rec4");
		}

	public void draw() {
		background(255);
	//loops	
		float vol1 = cp5.getController("vol1").getValue();
		PdBase.sendFloat("vol1", vol1);
		
		float vol2 = cp5.getController("vol2").getValue();
		PdBase.sendFloat("vol2", vol2);
		
		float vol3 = cp5.getController("vol3").getValue();
		PdBase.sendFloat("vol3", vol3);
		
		float vol4 = cp5.getController("vol4").getValue();
		PdBase.sendFloat("vol4", vol4);
		
		float volume_global = cp5.getController("volume_global").getValue();
		PdBase.sendFloat("vol_global", volume_global);
		
		float bpm_gui = cp5.getController("bpm_gui").getValue();
		PdBase.sendFloat("bpm_gui", bpm_gui);
		
		// fx
		
		
		PdBase.sendFloat("ligafx1", cp5.getValue("ligafx1"));
		PdBase.sendFloat("ligafx2", cp5.getValue("ligafx2"));
		
	}
	
public void comecaGrabacao() {
    prepareRecord();
    PdBase.sendFloat("pd_record", 1);
}

public void terminaGrabacao () {
    PdBase.sendFloat("pd_record", 0);
}





/*public void copiaarray(){
	pdpro = getArrayFromPd("array1");
	  strokeWeight(2);
	  stroke(0);
	  for (int i = 0; i < (pdpro.length - 1); i++) {
		  fill(0);
		  float plota = map(pdpro[i], 20f, 200f, height*0.6f, height*0.9f);
		  float plotax = map(i, 0, pdpro.length, width*0.1f, width*0.8f);
	    point(plotax, plota);
	  }
}*/

// prepare the file system to store the recordings
private void prepareRecord() {
    String root = Environment.getExternalStorageDirectory().toString();
    File myDir = new File(root + "/looper");
    myDir.mkdirs();
    SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
    Date now = new Date();
    String fileName = formatter.format(now);
    String fname = "recording_" + fileName;
    String saveFilePath = myDir.getAbsolutePath();
    String saveFileName = fname + ".wav";
    println ("IN:  " + myDir + "/" + fname);
  //  PdBase.sendSymbol("pd_path", myDir + "/" + fname);
    PdBase.sendSymbol("pd_path", myDir + "/");
}
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		initPdService();
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();

		// release all resources called by pdservice
		dispatcher.release();
		if (pd != 0) {
			PdBase.closePatch((int) pd);
			pd = 0;
		}
		pdService.stopAudio();
		unbindService(pdConnection);
	}



}

