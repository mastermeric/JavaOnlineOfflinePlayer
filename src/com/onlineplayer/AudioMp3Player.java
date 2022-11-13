package com.onlineplayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import com.sun.org.apache.bcel.internal.generic.StackInstruction;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class AudioMp3Player extends Application implements Runnable
{
	int playerCounter=0;
	int maximumSarkiNo;
	List<String> urls;
	final MediaView view = new MediaView();
    Iterator<String> itr ;
    @Override
    public void start(Stage stage) throws Exception
    {
			logger myLog = new logger();

			try
			{
				File myFodler = new File(PublicValues.targetDirectory);
				File[] myFiles = myFodler.listFiles();
				PublicValues.UsedDays = 0;
				myLog.logWrite("App Started..");

				PublicValues.LicenceTimeLeftStr = myLog.TekSatirOku(PublicValues.LisansDosyasi); // Muzik klasor adresini dinamik al..
				PublicValues.LicenceTimeLeft = Integer.valueOf(PublicValues.LicenceTimeLeftStr);

				myLog.logWrite("Licence Situation  :");
				myLog.logWrite("Remaining time : " + String.valueOf(PublicValues.LicenceTimeLeft));

				// Lisans kontrolu 30 sn de bir yapiliyor..
				if(PublicValues.LicenceTimeLeft < 60)
				{
					myLog.logWrite("APP CANNOT BE STARTED ! Licence Expired ..");
					System.exit(0);
				}



			//***************    1sn lik Timer ve Lisanslama       ***********************************************
			TimerTask myTimerTask = new TimerTask()
			{
				@Override
				public void run()
				{
					PublicValues.UsedDays++;
					PublicValues.ReaminingTime = PublicValues.LicenceTimeLeft - PublicValues.UsedDays;

					//Lisans dosyasini guncelle
					if((PublicValues.UsedDays %10) == 0)  // 30 sn de bir lisans guncelle..
					{
						PrintWriter pw;
						try
						{
							pw = new PrintWriter(PublicValues.LisansDosyasi);
							pw.print("");
							pw.print(PublicValues.ReaminingTime);
							pw.close();
						}
						catch (Exception ex)
						{
							myLog.logWrite("Timer() ERROR ==>>> " + ex.getMessage());
							System.exit(0);
						}
					}

					//Lisans kontrolu
					try
					{
					  if((PublicValues.UsedDays %10 ) == 0)  // 60 sn de bir Lisans kontrolu..
						{
							myLog.logWrite("Lisans guncellendi .... ");
							myLog.logWrite("Used Days : " + String.valueOf(PublicValues.UsedDays));
							myLog.logWrite("Remaining Days : " + String.valueOf(PublicValues.ReaminingTime));

							// Lisans kontrolu 60 sn de bir yapiliyor..
							if(PublicValues.ReaminingTime < 60)
							{
								myLog.logWrite("APP CLOSING! Licence Expired !");
								System.exit(0);
							}
						}
					}
					catch(Exception ex)
					{
						myLog.logWrite("Lisans Kontrolu  ERROR ==>>> " + ex.getMessage());
						System.exit(0);
					}
				}
			};
			Timer myTimer = new Timer();
			myTimer.schedule(myTimerTask,0, 60000);
			//****************************************************************************************************

			maximumSarkiNo = myFiles.length;

			myLog.logWrite("Album Icerigi:");
			for(int i=0;i<maximumSarkiNo;i++)
			{
				myLog.logWrite(Integer.toString(i) +" nolu sarki : " + myFiles[i].getName());
			}
			myLog.logWrite(".....................................");

			ArrayList<Integer> myList = new ArrayList<Integer>(maximumSarkiNo);
	        for (int i=0; i<maximumSarkiNo; i++)
	        {
	        	myList.add(new Integer(i));
	        }

			myLog.logWrite("Player baslatildi..");

			Collections.shuffle(myList);

			PublicValues.myJFX = new JFXPanel(); // mp3 calarken Application.Launch()  icin gerek

			urls = new ArrayList<String>();

			for (playerCounter=0; playerCounter<maximumSarkiNo; playerCounter++)
			{
				int sayac =  myList.get(playerCounter); //  Shuffle + Random
	        	String dosyaStr =  myFiles[sayac].getName();

	        	//Dinamik sure hesaplama
		    	String tempDuration = dosyaStr.substring(dosyaStr.indexOf("TIME-")+5, dosyaStr.indexOf("TIME-")+11);
	        	long testZaman = ZamanHesapla(tempDuration);

	        	urls.add(Paths.get(PublicValues.targetDirectory +dosyaStr).toUri().toString());
			}


			while(true)
			{
				Collections.shuffle(urls);
				//Plays the first file
		        itr = urls.iterator();
				for (playerCounter=0; playerCounter<maximumSarkiNo; playerCounter++)
				{
			        String str  = itr.next();
			        PublicValues.EncFileName = str.substring(8, str.length()); //  Path To Str  yapmis olduk

			        //****************    Sifresiz Cal       ***********************
			        Thread.sleep(500);
			        play(PublicValues.EncFileName);
			        //***********************************************************

			        //****************    Sifreli Cal       ***********************
			        //sifrele(PublicValues.EncFileName);
			        //Thread.sleep(500);
			        //play(PublicValues.TempClearFileName);
			        //***********************************************************
				}
			}

        }
		catch(Exception ex)
		{
			myLog.logWrite("start() ERROR ==>>> " + ex.getMessage());
			System.exit(0);
		}
   }




	    public void play(String mediaFile)
	    {
	    	logger myLog = new logger();

	    	//sifreli cal..
	    	//Media media = new Media(Paths.get(PublicValues.TempClearFileName).toUri().toString());

	    	//sifresiz cal..
	    	Media media = new Media(Paths.get(PublicValues.EncFileName).toUri().toString());
	        MediaPlayer player = new MediaPlayer(media);
	        view.setMediaPlayer(player);
	        player.play();
	        try {Thread.sleep(PublicValues.SongDuration*1000);} catch (InterruptedException e) {e.printStackTrace();}
	        player.stop();
	        /*
	        player.setOnEndOfMedia(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	            	player.stop();
	            	try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
					player.dispose();
					System.gc();


	            	while(true)
	            	{
	    				Collections.shuffle(urls);
	    				for (playerCounter=0; playerCounter<maximumSarkiNo; playerCounter++)
	    				{
		            	try
		            	{
		            		player.stop();
							Thread.sleep(100);
							player.dispose();
							System.gc();
							//Files.deleteIfExists(Paths.get(PublicValues.TempClearFileName));
							if (itr.hasNext())
							{
								String str  = itr.next();
			        	        PublicValues.EncFileName = str.substring(8, str.length()); //  Path To Str  yapmis olduk

			                    //Plays the subsequent files

			        	        //****************    Sifresiz Cal       *********************
			        	        Thread.sleep(100);
			                	play(PublicValues.EncFileName);
			                	//************************************************************

			                	//****************    Sifreli Cal       **********************
			                	//sifrele(PublicValues.EncFileName);
			                	//Thread.sleep(500);
			                	//play(PublicValues.TempClearFileName);
			                	//************************************************************
							}
		            	 }
		            	catch (Exception ex)
		            	{
		            		myLog.logYaz("play() ERROR ==>>> " + ex.getMessage());
		        			System.exit(0);
						}
		            	}
	            	}

	            }
	        });
	        */
	    }

		@Override
		public void run()
		{
			logger myLog = new logger();
			try
			{
				start(null);
			}
			catch (Exception ex)
			{
				myLog.logWrite("run() ERROR ==>>> " + ex.getMessage());
				System.exit(0);
			}
		}


		public void sifrele(String encFile)
		{
			logger myLog = new logger();

			try
			{
				byte maskForMusicFile = (byte)0xA1;
	        	Path EncryptedDosya = Paths.get(encFile); // LINUX ?

	        	myLog.logWrite("Path bilgisi : " + EncryptedDosya);

	        	byte[] binaryData = Files.readAllBytes(EncryptedDosya);
	        	for (int len = 0; len < binaryData.length; len++)
	            {
	                binaryData[len] ^= maskForMusicFile;
	            }

	        	myLog.logWrite("Decryption Ok...  binaryData.length =" + String.valueOf(binaryData.length) );
	        	myLog.logWrite("PublicValues.TempClearFileName =" + PublicValues.TempClearFileName);

	        	FileOutputStream tempClearFile = new FileOutputStream(PublicValues.TempClearFileName);
	        	myLog.logWrite("Stage-1  ok..");

	        	tempClearFile.write(binaryData);
	        	myLog.logWrite("Stage-2  ok..");
	        	tempClearFile.close();
	        	myLog.logWrite("Stage-3  ok..");
			}
			catch(Exception ex)
			{
				myLog.logWrite("sifrele() ERROR ==>>> " + ex.getMessage());
				System.exit(0);
			}
		}

		 public long ZamanHesapla (String sure)
		 {
			 logger myLog = new logger();
			 try
			 {
		    	PublicValues.SongDuration = 0;
		    	String saatStr, dakikaStr, saniyeStr;
		    	int saat, dakika, saniye, toplamSure;

		    	if(sure.length() == 6)
		    	{
		    		saatStr = sure.substring(0, 2);
		    		dakikaStr = sure.substring(2, 4);
		    		saniyeStr = sure.substring(4, 6);

		    		saat = Integer.valueOf(saatStr);
		    		dakika = Integer.valueOf(dakikaStr);
		    		saniye = Integer.valueOf(saniyeStr);

		    		toplamSure = saat*60*60 + dakika*60 + saniye;
		    		PublicValues.SongDuration = toplamSure;

		    	}
		    	return PublicValues.SongDuration ;
			 }
			 catch(Exception ex)
			 {
				 myLog.logWrite("ZamanHesapla() ERROR ==>>> " + ex.getMessage());
				 return 0;
			 }

		    }
}
