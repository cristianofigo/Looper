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
		
		//definir icone e texto "sample"..etc..
		pdService.startAudio(new Intent(this, LoopActivity.class),
				R.drawable.ic_launcher, "Looper", "Continuar Loopando");

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
	  //texto
	  cp5.addTextlabel("clique")
      .setText("clique")
      .setPosition(width*0.0f,height*0.1f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.03f)))
      ;
	  cp5.addTextlabel("apenas")
      .setText("apenas")
      .setPosition(width*0.0f,height*0.15f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("uma")
      .setText("uma")
      .setPosition(width*0.0f,height*0.2f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.03f)))
      ;
	  cp5.addTextlabel("vez")
      .setText("vez ->")
      .setPosition(width*0.0f,height*0.25f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.03f)))
      ;
	  cp5.addTextlabel("para")
      .setText("para")
      .setPosition(width*0.0f,height*0.3f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.03f)))
      ;
	  cp5.addTextlabel("gravar")
      .setText("gravar")
      .setPosition(width*0.0f,height*0.35f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.03f)))
      ;
	  
	  cp5.addTextlabel("volume")
      .setText("volume")
      .setPosition(width*0.0f,height*0.7f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.025f)))
      ;
	  
	  cp5.addTextlabel("bpm")
      .setText("BPM")
      .setPosition(width*0.86f,height*0.28f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  
	  
	  cp5.addTextlabel("volumegeral")
      .setText("volume")
      .setPosition(width*0.86f,height*0.7f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("vol_geral")
      .setText("geral")
      .setPosition(width*0.86f,height*0.75f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
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
	  
//texto
	  
	  cp5.addTextlabel("cliquefx")
      .setText("clique")
      .setPosition(width*0.0f,height*0.15f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("parafx")
      .setText("para")
      .setPosition(width*0.0f,height*0.2f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("ligar")
      .setText("ligar ->")
      .setPosition(width*0.0f,height*0.25f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("efeitos")
      .setText("efeitos")
      .setPosition(width*0.0f,height*0.3f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("delay")
      .setText("delay")
      .setPosition(width*0.2f,height*0.15f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("delayspec")
      .setText("delay spectral")
      .setPosition(width*0.5f,height*0.15f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("vol_delay")
      .setText("vol")
      .setPosition(width*0.1f,height*0.93f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("tempo_delay")
      .setText("tempo")
      .setPosition(width*0.18f,height*0.93f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("feed_delay")
      .setText("feedback")
      .setPosition(width*0.29f,height*0.93f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("vol_delayspec")
      .setText("vol")
      .setPosition(width*0.42f,height*0.93f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("tempo_delayspec")
      .setText("tempo")
      .setPosition(width*0.5f,height*0.93f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("feed_delayspec")
      .setText("feedback")
      .setPosition(width*0.6f,height*0.93f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addToggle("ligafx1")
	     .setValue(0)
	     .setPosition(width*0.1f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  
	  cp5.addToggle("ligafx2")
	     .setValue(0)
	     .setPosition(width*0.4f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  
	  cp5.addSlider("volfx1")
	     .setPosition(width*.1f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.07f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("delayfx1")
	     .setPosition(width*.2f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.07f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("feedfx1")
	     .setPosition(width*.3f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.07f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("volfx2")
	     .setPosition(width*.4f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.07f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("delayfx2")
	     .setPosition(width*.5f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.07f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  cp5.addSlider("feedfx2")
	     .setPosition(width*.6f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.07f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
  
	  ///rec
	  
	  cp5.addTextlabel("cliquerec")
      .setText("clique")
      .setPosition(width*0.0f,height*0.15f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("pararec")
      .setText("para")
      .setPosition(width*0.0f,height*0.2f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("gravarrec")
      .setText("gravar->")
      .setPosition(width*0.0f,height*0.25f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.025f)))
      ;
	  
	  cp5.addTextlabel("cliqueplay")
      .setText("clique")
      .setPosition(width*0.27f,height*0.15f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("paraplay")
      .setText("para")
      .setPosition(width*0.27f,height*0.2f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  cp5.addTextlabel("tocar")
      .setText("tocar ->")
      .setPosition(width*0.27f,height*0.25f)
      .setColor(0)
      .setFont(createFont("Georgia",(int)(width*0.028f)))
      ;
	  
	  cp5.addToggle("recgeral1")
	     .setValue(0)
	     .setPosition(width*0.1f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  
	  cp5.addToggle("play1")
	     .setValue(0)
	     .setPosition(width*0.4f,height*0.15f)
	     .setSize(((int)(width*0.1f)), ((int)(width*0.1f)))
	     ;
	  
	  cp5.addSlider("playvolume1")
	     .setPosition(width*.4f,height*.4f)
	     .setRange(0,1)
	     .setSize(((int)(width*0.1f)),((int)(height*0.5f)))
	     .setValue(1)
	     .setScrollSensitivity(1.0f)
	     ;
	  
	  // about
	  
	  cp5.addTextlabel("label")
      .setText("Looper permite que você grave 4 canais de áudio e aplique,")
      .setPosition(width*0.1f,height*0.5f)
      .setColor(0)
      .setFont(createFont("Georgia",20))
      ;
cp5.addTextlabel("label2")
.setText("efeitos de delay e delay spectral em cada canal.")
.setPosition(width*0.1f,height*0.55f)
.setColor(0)
.setFont(createFont("Georgia",20))
;

cp5.addTextlabel("label3")
.setText("Grave 4 canais na aba LOOPS")
.setPosition(width*0.1f,height*0.65f)
.setColor(0)
.setFont(createFont("Georgia",20))
;
cp5.addTextlabel("label4")
.setText("Modifique os efeitos de cada canal na aba FX")
.setPosition(width*0.1f,height*0.75f)
.setColor(0)
.setFont(createFont("Georgia",20))
;
cp5.addTextlabel("label5")
.setText("Música Móvel - figocris@gmail.com")
.setPosition(width*0.1f,height*0.85f)
.setColor(0)
.setFont(createFont("Georgia",20))
;

	  
//	  cp5.addSlider("vol4")
//	     .setPosition(width*.55f,height*.4f)
//	     .setRange(0,1)
//	     .setSize(((int)(width*0.1f)),((int)(height*0.5f)))
//	     .setValue(1)
//	     .setScrollSensitivity(1.0f)
//	     ;
	     
	  //texto
cp5.getController("cliquefx").moveTo("Fx");
cp5.getController("parafx").moveTo("Fx");
cp5.getController("ligar").moveTo("Fx");
cp5.getController("efeitos").moveTo("Fx");
cp5.getController("delay").moveTo("Fx");
cp5.getController("delayspec").moveTo("Fx");
cp5.getController("vol_delay").moveTo("Fx");
cp5.getController("tempo_delay").moveTo("Fx");
cp5.getController("feed_delay").moveTo("Fx");
cp5.getController("vol_delayspec").moveTo("Fx");
cp5.getController("tempo_delayspec").moveTo("Fx");
cp5.getController("feed_delayspec").moveTo("Fx");
//
	  cp5.getController("ligafx1").moveTo("Fx");
	  cp5.getController("ligafx2").moveTo("Fx");
	  cp5.getController("volfx1").moveTo("Fx");
	  cp5.getController("delayfx1").moveTo("Fx");
	  cp5.getController("feedfx1").moveTo("Fx");
	  cp5.getController("volfx2").moveTo("Fx");
	  cp5.getController("delayfx2").moveTo("Fx");
	  cp5.getController("feedfx2").moveTo("Fx");
	  cp5.getController("recgeral1").moveTo("Rec");
	  cp5.getController("play1").moveTo("Rec");
	  cp5.getController("playvolume1").moveTo("Rec");
	  cp5.getController("cliquerec").moveTo("Rec");
	  cp5.getController("pararec").moveTo("Rec");
	  cp5.getController("gravarrec").moveTo("Rec");
	  cp5.getController("cliqueplay").moveTo("Rec");
	  cp5.getController("paraplay").moveTo("Rec");
	  cp5.getController("tocar").moveTo("Rec");
	  cp5.getController("label").moveTo("ABOUT");
	  cp5.getController("label2").moveTo("ABOUT");
	  cp5.getController("label3").moveTo("ABOUT");
	  cp5.getController("label4").moveTo("ABOUT");
	  cp5.getController("label5").moveTo("ABOUT");
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
		textSize(width*0.08f);
		
		  fill(0, 102, 153);
		 
		  text("Looper", width*.5f, height*0.1f); 
		  textSize(width*0.02f);
		  text("Música Móvel", width*0.75f, height*0.1f);
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
		PdBase.sendFloat("volfx1", cp5.getValue("volfx1"));
		PdBase.sendFloat("delayfx1", cp5.getValue("delayfx1"));
		PdBase.sendFloat("feedfx1", cp5.getValue("feedfx1"));
		PdBase.sendFloat("volfx2", cp5.getValue("volfx2"));
		PdBase.sendFloat("delayfx2", cp5.getValue("delayfx2"));
		PdBase.sendFloat("feedfx2", cp5.getValue("feedfx2"));
		PdBase.sendFloat("recgeral1", cp5.getValue("recgeral1"));
		PdBase.sendFloat("play1", cp5.getValue("play1"));
		PdBase.sendFloat("playvolume1", cp5.getValue("playvolume1"));
		
		comecaGrabacao();
		
	}
	
public void comecaGrabacao() {
    prepareRecord();
    
}

public void terminaGrabacao () {
    
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

