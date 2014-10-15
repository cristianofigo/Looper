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
	
	
	
	
	public void setup() {	
	
	  cp5 = new ControlP5(this);
	  
	  
	  cp5.getTab("default")
	     .activateEvent(true)
	     .setLabel("ABOUT")
	     .setWidth((int)(width*.15f))
	     .setHeight((int)(width*.3f))
	     .setId(1)
	     ;

	  cp5.getTab("Loops")
	     .activateEvent(true)
	     .setLabel("Loops")
	     .setWidth((int)(width*.15f))
	     .setHeight((int)(width*.2f))
	     .setId(1)
	     ;
	  
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
	  
//	  cp5.getController("Canal1").moveTo("Loops");
//	  cp5.getController("Vol1").moveTo("Loops");
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
		
		//guias design
		
//		for(int xt = 0; xt < width; xt += width/10){
//			fill(0);
//			line(xt, 0, xt, width);
//		}
//		for(int yt = 0; yt < height; yt += height/10){
//			fill(0);
//			line(0, yt, width, yt);
//		}
		// default
		
		
		
		//aba set_tempo
		
//		cp5.getController("bpm_gui").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
//		cp5.getController("bpm_gui").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
//		float bpm_slide = cp5.getController("bpm_gui").getValue();
//		int newbpm_slide = (int)bpm_slide;
//		PdBase.sendFloat("bpm_gui", newbpm_slide);
//		text("BPM " + newbpm_slide, width*.7f, height*.8f);
//		
//		cp5.getController("acento_gui").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
//		cp5.getController("acento_gui").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
//		float acento = cp5.getController("acento_gui").getValue();
//		int newacento = (int)acento;
//		PdBase.sendFloat("acento_gui", newacento);
//		text("ACENTO " + newacento, width*.7f, height*.85f);
//		
//		cp5.getController("compassos_gui").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
//		cp5.getController("compassos_gui").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
//		float compasso = cp5.getController("compassos_gui").getValue();
//		int newcompasso = (int)compasso;
//		PdBase.sendFloat("compassos_gui", newcompasso);
//		text("COMPASSOS " + newcompasso, width*.7f, height*.9f);
//		
//		if(atualiza_gui==true) {
//			  PdBase.sendBang("atualiza_gui");
//		    //fill(255,255,220);
//		  }
//		
//		 
//		
//		//float[] pdpro = new float[bla];
//		
//		
//		textSize(width*0.1f);
//		
//		  fill(0, 102, 153);
//		 
//		  text("Looper", width*.4f, height*0.15f); 
//		  textSize(width*0.02f);
//		  text("Mœsica M—vel", width*0.7f, height*0.2f);
//		//background(255);
//		
//		comecaGrabacao();
//		terminaGrabacao();
//	  //background(0);
//	  
//	  //inicializar o array frompd[] no processing
//	  
////	  if (!pegoArray) {
////		  pdpro = getArrayFromPd("arraypd");
////		  if (pdpro.length > 0)
////			  pegoArray = true;
////	  }
//	  
//		
//	  
//	  pushMatrix();
//	  
//	  if(calibra==true) {
//		  PdBase.sendFloat("calibra", 1);
//	    //fill(255,255,220);
//	  }else {
//		  PdBase.sendFloat("calibra", 0);
//		  }
//	  
//	  if(playcursor==true){
//		  PdBase.sendFloat("play_gui", 1);
//		 // float [] cursor = getArrayFromPd("cursor_gui");
//		  
//		  float [] cursor = new float[1];
//		  PdBase.readArray(cursor, 0, "cursor_gui", 0, 1);
//		  
//		  //pegar tamanho do array1 e mapear essevalor para valor de cursor[0] no x
//		  
//		 // int tamanholoop = PdBase.arraySize("array1");
//		  
//		  
//		  strokeWeight(3);
//		  stroke(0);
//		  fill(0);
//			 // float cursory = map(pdpro[i], -.5f, .5f, height*.9f, height);
//			//  float cursorx = map(i, 0, pdpro.length, width*0.7f, width);
//		  println(cursor[0]);
//		    line(map(cursor[0], 0, 1, width*.2f, width*.6f), height*.3f  ,map(cursor[0], 0, 1, width*.2f, width*.6f), height*.8f);
//		  
//	  }else {
//		  PdBase.sendFloat("play_gui", 0);
//		  strokeWeight(2);
//	  }
//	  
//	  if(canal_1==true) {
//		  PdBase.sendFloat("rec1", 1);
//	    fill(255,255,220);
//	  } else {
//		  PdBase.sendFloat("rec1", 0);
//	    fill(128,128,110);
//	  }
//	  if(metronomo==true) {
//		  PdBase.sendFloat("metronomo", 1);
//	    fill(255,255,220);
//	  } else {
//		  PdBase.sendFloat("metronomo", 0);
//	    fill(128,128,110);
//	  }
//	  PdBase.sendFloat("vol1", cp5.getValue("volcan1"));
//	  
//	  if(canal_2==true) {
//		  PdBase.sendFloat("toggle2", 1);
//	    fill(255,255,220);
//	  } else {
//		  PdBase.sendFloat("toggle2", 0);
//	    fill(128,128,110);
//	  }
//	  PdBase.sendFloat("volcan2", cp5.getValue("volcan2"));
//	  if(canal_3==true) {
//		  PdBase.sendFloat("toggle3", 1);
//	    fill(255,255,220);
//	  } else {
//		  PdBase.sendFloat("toggle3", 0);
//	    fill(128,128,110);
//	  }
//	  PdBase.sendFloat("volcan3", cp5.getValue("volcan3"));
//	  if(canal_4==true) {
//		  PdBase.sendFloat("toggle4", 1);
//	    fill(255,255,220);
//	  } else {
//		  PdBase.sendFloat("toggle4", 0);
//	    fill(128,128,110);
//	  }
//	  PdBase.sendFloat("volcan4", cp5.getValue("volcan4"));
//	  PdBase.sendFloat("reverb1", cp5.getValue("reverb1"));
//	  PdBase.sendFloat("feedback1", cp5.getValue("feedback1"));
//	  PdBase.sendFloat("reverb2", cp5.getValue("reverb2"));
//	  PdBase.sendFloat("feedback2", cp5.getValue("feedback2"));
//	  PdBase.sendFloat("reverb3", cp5.getValue("reverb3"));
//	  PdBase.sendFloat("feedback3", cp5.getValue("feedback3"));
//	  PdBase.sendFloat("reverb4", cp5.getValue("reverb4"));
//	  PdBase.sendFloat("feedback4", cp5.getValue("feedback4"));
//	  
//	  PdBase.sendFloat("recgeral", cp5.getValue("recgeral"));
//	  PdBase.sendFloat("volumegeral", cp5.getValue("volumegeral"));
//	  PdBase.sendFloat("play", cp5.getValue("play"));
//	  PdBase.sendFloat("playvolume", cp5.getValue("playvolume"));
//	  PdBase.sendFloat("latencia_gui", cp5.getValue("latencia_gui"));
//	  
//	  
//	  //copiaarray();
////	  
////	  float [] arraypd = pdManager.getArrayFromPd("audio");
////		 for (int i=0; i < arraypd.length; i++){
////		    	fill(255);
////		    	strokeWeight(2);
////				  stroke(255);
////		    	point(i+20, map(arraypd[i], -1, 1, height*.5f, height*.8f));
////		    }
////	  
//	  
//	  ////arrayssss
//	  
//	  if(ligaonda==true) {
//		 
//	  float [] pdpro = getArrayFromPd("metro");
//	  strokeWeight(2);
//	  stroke(0);
//	  for (int i = 0; i < (pdpro.length - 1); i+=50) {
//		  fill(0);
//		  float plota = map(pdpro[i], -.5f, .5f, height*.18f, height*.22f);
//		  float plotax = map(i, 0, pdpro.length, width*0.2f, width*0.6f);
//	    point(plotax, plota);
//	  }
//	  
//	  float [] canal1 = getArrayFromPd("array1");
//	  strokeWeight(2);
//	  stroke(0);
//	  for (int i = 0; i < (canal1.length - 1); i+=50) {
//		  fill(0);
//		  float canal1y = map(canal1[i], -.5f, .5f, height*.25f, height*.4f);
//		  float canal1x = map(i, 0, canal1.length, width*0.2f, width*0.6f);
//	    point(canal1x, canal1y);
//	  }
//	  
//	  float [] canal2 = getArrayFromPd("array2");
//	  strokeWeight(2);
//	  stroke(0);
//	  for (int i = 0; i < (canal2.length - 1); i+=50) {
//		  fill(0);
//		  float canal2y = map(canal2[i], -.5f, .5f, height*.4f, height*.5f);
//		  float canal2x = map(i, 0, canal2.length, width*0.2f, width*0.6f);
//	    point(canal2x, canal2y);
//	  }
//	  
//	  float [] canal3 = getArrayFromPd("array3");
//	  strokeWeight(2);
//	  stroke(0);
//	  for (int i = 0; i < (canal3.length - 1); i+=50) {
//		  fill(0);
//		  float canal3y = map(canal3[i], -.5f, .5f, height*.55f, height*.65f);
//		  float canal3x = map(i, 0, canal3.length, width*0.2f, width*0.6f);
//	    point(canal3x, canal3y);
//	  }
//	  
//	  float [] canal4 = getArrayFromPd("array4");
//	  strokeWeight(2);
//	  stroke(0);
//	  for (int i = 0; i < (canal4.length - 1); i+=50) {
//		  fill(0);
//		  float canal4y = map(canal4[i], -.5f, .5f, height*.7f, height*.8f);
//		  float canal4x = map(i, 0, canal4.length, width*0.2f, width*0.6f);
//	    point(canal4x, canal4y);
//	  }
//	  
//	  } 
//	  
//	  popMatrix();
	 
	}

	

//	public void mousePressed(){
//		//botao rec0
//	 if(mouseX > width*0.1f && mouseX < (width*0.1f + width*0.05f)
//	  && mouseY > height*0.25f && mouseY < (height*0.25f + height*0.05f)){
//	    i=0;
//	    rec = !rec;
//	}
//	}
	
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

