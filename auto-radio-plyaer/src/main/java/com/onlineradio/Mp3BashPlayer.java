package com.onlineradio;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class Mp3BashPlayer
{

	public Mp3BashPlayer()
	{

	}

	public void PlayMp3FromBash()
	{
		int playerCounter=0;

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

			// Program acilis Lisans kontrolu ..
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
				if((PublicDegerler.UsedDays %20) == 0)  // 10 dk da bir sn de bir lisans guncelle..
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

				//Lisans kontrolu  SIMDILIK SUREKLI LISANS KONTROLUNE GEREK YOK..
				try
				{
				  if((PublicDegerler.UsedDays %10) == 0)  // 10 dk da bir sn de bir Lisans kontrolu..
					{
						myLog.logYaz("Lisans guncellendi.. ");
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
		myTimer.schedule(myTimerTask,0, 60000);//60 sn lik timer
		//****************************************************************************************************

		int maximumSarkiNo = myFiles.length;

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

		while(true)
		{
			Collections.shuffle(myList);

			for (playerCounter=0; playerCounter<maximumSarkiNo; playerCounter++)
			{
				int sayac =  myList.get(playerCounter); //  Shuffle + Random
		    	String dosyaStr =  myFiles[sayac].getName();

		    	//Dinamik sure hesaplama
		    	String tempDuration = dosyaStr.substring(dosyaStr.indexOf("TIME-")+5, dosyaStr.indexOf("TIME-")+11);
	        	ZamanHesapla(tempDuration);

		    	PublicDegerler.EncFileName = PublicDegerler.targetDirectory + dosyaStr;

				//*********************************************************
				//sifrele(PublicDegerler.EncFileName);  // Sifreli Calmak icin bunu ac..
				Thread.sleep(500);
				BashCommandExecuter myExecuter = new BashCommandExecuter();
				myExecuter.ExecuteCommand("mpg123 " + PublicDegerler.EncFileName);
				Thread.sleep(PublicDegerler.SongDuration*1000);
				myExecuter.ExecuteCommand("pkill -f mpg123");
				Thread.sleep(500);
				//***********************************************************
			}
		}
	}
	catch(Exception ex)
	{
		myLog.logYaz("PlayMp3FromBash() ERROR ==>>> " + ex.getMessage());
		System.exit(0);
	}
  }


	// iptal
	/*
	public void sifrele(String encFile)
	{
		loglama myLog = new loglama();
		try
		{
			myLog.logYaz("sifrele() Started...");
			byte maskForMusicFile = (byte)0xA1;
        	Path EncryptedDosya = Paths.get(encFile); // LINUX ?
        	byte[] binaryData = Files.readAllBytes(EncryptedDosya);
        	for (int len = 0; len < binaryData.length; len++)
            {
        		if (len % 500 == 0)
        		{
        			binaryData[len] ^= maskForMusicFile;
        		}
            }
        	//FileOutputStream tempClearFile = new FileOutputStream(PublicDegerler.TempClearFileName);
        	myLog.logYaz(EncryptedDosya +" -->> Decryption Ok.. length =" + String.valueOf(binaryData.length) + "Tempfile created. Ok." );
        	tempClearFile.write(binaryData);
        	tempClearFile.close();
		}
		catch(Exception ex)
		{
			myLog.logYaz("sifrele() ERROR ==>>> " + ex.getMessage());
			System.exit(0);
		}
	}
	*/

	 public int ZamanHesapla (String sure)
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
	    	return 0;
	    }


}
