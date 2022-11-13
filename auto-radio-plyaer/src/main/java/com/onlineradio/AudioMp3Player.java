package com.onlineradio;

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
			loglama myLog = new loglama();

			try
			{
				File myFodler = new File(PublicDegerler.targetDirectory);
				File[] myFiles = myFodler.listFiles();
				PublicDegerler.UsedDays = 0;
				myLog.logYaz("App Started..");

				PublicDegerler.LicenceTimeLeftStr = myLog.TekSatirOku(PublicDegerler.LisansDosyasi); // Muzik klasor adresini dinamik al..
				PublicDegerler.LicenceTimeLeft = Integer.valueOf(PublicDegerler.LicenceTimeLeftStr);

				myLog.logYaz("Startup Licence Situation :");
				myLog.logYaz("Remaining Days : " + String.valueOf(PublicDegerler.LicenceTimeLeft));

				// Lisans kontrolu 30 sn de bir yapiliyor..
				if(PublicDegerler.LicenceTimeLeft < 60)
				{
					myLog.logYaz("APP CANNOT BE STARTED ! Licence Expired ..");
					System.exit(0);
				}



			//***************    1sn lik Timer ve Lisanslama       ***********************************************
			TimerTask myTimerTask = new TimerTask()
			{
				@Override
				public void run()
				{
					PublicDegerler.UsedDays++;
					PublicDegerler.ReaminingTime = PublicDegerler.LicenceTimeLeft - PublicDegerler.UsedDays;

					//Lisans dosyasini guncelle
					if((PublicDegerler.UsedDays %10) == 0)  // 30 sn de bir lisans guncelle..
					{
						PrintWriter pw;
						try
						{
							pw = new PrintWriter(PublicDegerler.LisansDosyasi);
							pw.print("");
							pw.print(PublicDegerler.ReaminingTime);
							pw.close();
						}
						catch (Exception ex)
						{
							myLog.logYaz("Timer() ERROR ==>>> " + ex.getMessage());
							System.exit(0);
						}
					}

					//Lisans kontrolu
					try
					{
					  if((PublicDegerler.UsedDays %10 ) == 0)  // 60 sn de bir Lisans kontrolu..
						{
							myLog.logYaz("Lisans guncellendi .... ");
							myLog.logYaz("Used Days : " + String.valueOf(PublicDegerler.UsedDays));
							myLog.logYaz("Remaining Days : " + String.valueOf(PublicDegerler.ReaminingTime));

							// Lisans kontrolu 60 sn de bir yapiliyor..
							if(PublicDegerler.ReaminingTime < 60)
							{
								myLog.logYaz("APP CLOSING! Licence Expired !");
								System.exit(0);
							}
						}
					}
					catch(Exception ex)
					{
						myLog.logYaz("Lisans Kontrolu  ERROR ==>>> " + ex.getMessage());
						System.exit(0);
					}
				}
			};
			Timer myTimer = new Timer();
			myTimer.schedule(myTimerTask,0, 60000);
			//****************************************************************************************************

			maximumSarkiNo = myFiles.length;

			myLog.logYaz("Album Icerigi:");
			for(int i=0;i<maximumSarkiNo;i++)
			{
				myLog.logYaz(Integer.toString(i) +" nolu sarki : " + myFiles[i].getName());
			}
			myLog.logYaz(".....................................");

			ArrayList<Integer> myList = new ArrayList<Integer>(maximumSarkiNo);
	        for (int i=0; i<maximumSarkiNo; i++)
	        {
	        	myList.add(new Integer(i));
	        }

			myLog.logYaz("Player baslatildi..");

			Collections.shuffle(myList);

			PublicDegerler.myJFX = new JFXPanel(); // mp3 calarken Application.Launch()  icin gerek

			urls = new ArrayList<String>();

			for (playerCounter=0; playerCounter<maximumSarkiNo; playerCounter++)
			{
				int sayac =  myList.get(playerCounter); //  Shuffle + Random
	        	String dosyaStr =  myFiles[sayac].getName();

	        	//Dinamik sure hesaplama
		    	String tempDuration = dosyaStr.substring(dosyaStr.indexOf("TIME-")+5, dosyaStr.indexOf("TIME-")+11);
	        	long testZaman = ZamanHesapla(tempDuration);

	        	urls.add(Paths.get(PublicDegerler.targetDirectory +dosyaStr).toUri().toString());
			}


			while(true)
			{
				Collections.shuffle(urls);
				//Plays the first file
		        itr = urls.iterator();
				for (playerCounter=0; playerCounter<maximumSarkiNo; playerCounter++)
				{
			        String str  = itr.next();
			        PublicDegerler.EncFileName = str.substring(8, str.length()); //  Path To Str  yapmis olduk

			        //****************    Sifresiz Cal       ***********************
			        Thread.sleep(500);
			        play(PublicDegerler.EncFileName);
			        //***********************************************************

			        //****************    Sifreli Cal       ***********************
			        //sifrele(PublicDegerler.EncFileName);
			        //Thread.sleep(500);
			        //play(PublicDegerler.TempClearFileName);
			        //***********************************************************
				}
			}

        }
		catch(Exception ex)
		{
			myLog.logYaz("start() ERROR ==>>> " + ex.getMessage());
			System.exit(0);
		}
   }




	    public void play(String mediaFile)
	    {
	    	loglama myLog = new loglama();

	    	//sifreli cal..
	    	//Media media = new Media(Paths.get(PublicDegerler.TempClearFileName).toUri().toString());

	    	//sifresiz cal..
	    	Media media = new Media(Paths.get(PublicDegerler.EncFileName).toUri().toString());
	        MediaPlayer player = new MediaPlayer(media);
	        view.setMediaPlayer(player);
	        player.play();
	        try {Thread.sleep(PublicDegerler.SongDuration*1000);} catch (InterruptedException e) {e.printStackTrace();}
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
							//Files.deleteIfExists(Paths.get(PublicDegerler.TempClearFileName));
							if (itr.hasNext())
							{
								String str  = itr.next();
			        	        PublicDegerler.EncFileName = str.substring(8, str.length()); //  Path To Str  yapmis olduk

			                    //Plays the subsequent files

			        	        //****************    Sifresiz Cal       *********************
			        	        Thread.sleep(100);
			                	play(PublicDegerler.EncFileName);
			                	//************************************************************

			                	//****************    Sifreli Cal       **********************
			                	//sifrele(PublicDegerler.EncFileName);
			                	//Thread.sleep(500);
			                	//play(PublicDegerler.TempClearFileName);
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
			loglama myLog = new loglama();
			try
			{
				start(null);
			}
			catch (Exception ex)
			{
				myLog.logYaz("run() ERROR ==>>> " + ex.getMessage());
				System.exit(0);
			}
		}


		public void sifrele(String encFile)
		{
			loglama myLog = new loglama();

			try
			{
				byte maskForMusicFile = (byte)0xA1;
	        	Path EncryptedDosya = Paths.get(encFile); // LINUX ?

	        	myLog.logYaz("Path bilgisi : " + EncryptedDosya);

	        	byte[] binaryData = Files.readAllBytes(EncryptedDosya);
	        	for (int len = 0; len < binaryData.length; len++)
	            {
	                binaryData[len] ^= maskForMusicFile;
	            }

	        	myLog.logYaz("Decryption Ok...  binaryData.length =" + String.valueOf(binaryData.length) );
	        	myLog.logYaz("PublicDegerler.TempClearFileName =" + PublicDegerler.TempClearFileName);

	        	FileOutputStream tempClearFile = new FileOutputStream(PublicDegerler.TempClearFileName);
	        	myLog.logYaz("Stage-1  ok..");

	        	tempClearFile.write(binaryData);
	        	myLog.logYaz("Stage-2  ok..");
	        	tempClearFile.close();
	        	myLog.logYaz("Stage-3  ok..");
			}
			catch(Exception ex)
			{
				myLog.logYaz("sifrele() ERROR ==>>> " + ex.getMessage());
				System.exit(0);
			}
		}

		 public long ZamanHesapla (String sure)
		 {
			 loglama myLog = new loglama();
			 try
			 {
		    	PublicDegerler.SongDuration = 0;
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
		    		PublicDegerler.SongDuration = toplamSure;

		    	}
		    	return PublicDegerler.SongDuration ;
			 }
			 catch(Exception ex)
			 {
				 myLog.logYaz("ZamanHesapla() ERROR ==>>> " + ex.getMessage());
				 return 0;
			 }

		    }
}
