package com.onlineplayer;

import java.util.Random;

import javafx.embed.swing.JFXPanel;


//NOTLAR :
/*
 Sampled Audio: Sampled audio is represented as a sequence of time-sampled data of the amplitude of sound wave.
 It is supported in package javax.sound.sampled. The supported file formats are: "wav", "au" and "aiff".
 The samples can be either 8-bit or 16-bit, with sampling rate from 8 kHz to 48 kHz.
 */

//1 gun = 86400 sn ,  30 gun = 2592000 sn

public class konsolv1
{
	public static void main(String[] args) throws Exception
	{
		JFXPanel myFFX = new JFXPanel(); // mp3 calarken Application.Launch()  icin gerek
		logger myLog = new logger();
		try
		{
			//Files.deleteIfExists(Paths.get(PublicDegerler.TempClearFileName)); // gerek yok..

			//WINDOWS PLAYER...			
			Thread.sleep(500);
			AudioMp3Player myPalyer = new AudioMp3Player();
			myPalyer.start(null);
			

			//LINUX PLAYER
			/*
			Thread.sleep(500);
			amixer myMixer = new amixer();
			myMixer.setVolumeMax();			

			Mp3BashPlayer myPlayer = new Mp3BashPlayer();
			myPlayer.PlayMp3FromBash();
			*/

			
		}
		catch(Exception ex)
		{
			myLog.logWrite("main() ERROR ==>>> " + ex.getMessage());
			System.exit(0);
		}
	}

	public static int anyRandomIntRange(Random myrandom, int low, int high)
	{
		int randomInt = myrandom.nextInt(high) + low;
		return randomInt;
	}


	public static void  Mp3Start()
	{
		logger myLog = new logger();
		try
		{
			Thread kanal1;
			Runnable myTask = new AudioMp3Player();
			kanal1 = new Thread(myTask);
			kanal1.start();
		}
		catch(Exception ex)
		{
			myLog.logWrite("APP CANNOT BE STARTED ! Licence Expired ..");
			System.exit(0);
		}
	}


	/*
	public static void playWav()
	{
		byte maskForMusicFile = (byte)0xA1;
		loglama myLog = new loglama();

		//Hardcoded adres..
		//String targetDirectory ="/mnt/myusb/muzikler"; // LINUX
		//String targetDirectory ="/home/muzikler"; // LINUX
		String targetDirectory ="C:\\muzikler"; // WINDOWS

		PublicDegerler.LisansDosyasi = "data.binx"; // WINDOWS
		//PublicDegerler.LisansDosyasi = "/mnt/myusb/data_binx/data.binx"; // LINUX
		//PublicDegerler.LisansDosyasi = "/home/exeler/data.binx"; // LINUX

		try
		{
			File myFodler = new File(targetDirectory);
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


		//Long StartTime = System.currentTimeMillis();
		//Long EndTime = System.currentTimeMillis() - StartTime;

		//****************************************************************************************************
		//***************    1sn lik Timer ve Lisanslama       ***********************************************
		//****************************************************************************************************
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
						myLog.logYaz("TimerTask icinde HATA  ==>>" + ex.getMessage());
					}
				}

				//Lisans kontrolu
				try
				{
				  if((PublicDegerler.UsedDays %20 ) == 0)  // 60 sn de bir Lisans kontrolu..
					{
						myLog.logYaz("Lisans guncellendi .... ");
						myLog.logYaz("Used Days : " + String.valueOf(PublicDegerler.UsedDays));
						myLog.logYaz("Remaining Days : " + String.valueOf(PublicDegerler.ReaminingTime));

						// Lisans kontrolu 60 sn de bir yapiliyor..
						if(PublicDegerler.ReaminingTime < 60)
						{
							myLog.logYaz("APP CLOSING! Licence Expired !");
							Files.deleteIfExists(Paths.get(PublicDegerler.TempClearFileName));
							System.exit(0);
						}
					}
				}
				catch(Exception ex)
				{
					myLog.logYaz("Lisans kontrolu icinde HATA  ==>>" + ex.getMessage());
				}
			}
		};
		Timer myTimer = new Timer();
		myTimer.schedule(myTimerTask,0, 1000);

		//****************************************************************************************************
		//****************************************************************************************************


		final AudioWavPlayer player = new AudioWavPlayer();
		int sayac = 0;
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
	        for (int i=0; i<maximumSarkiNo; i++) {
	        	sayac = myList.get(i); //  Shuffle + Random
	        	Path dosya = Paths.get(myFiles[sayac].getName());
	        	myLog.logYaz(Integer.toString(sayac) +" nolu sarki baslatildi :" + dosya);

	        	//*****************    clear cal    ***************************************************
	        	//player.play(targetDirectory +"/"+ dosya ); //    windows icin:\\    Linux icin:  /
	        	//player.play(targetDirectory +"\\"+ dosya); //    windows icin:\\    Linux icin:  /
	        	//*************************************************************************************

	        	//****************     sifreli cal  ***************************************************
	        	try
	        	{
		        	String clearFileName = "";

		        	//WINDOWS
		        	String clearFilesLocation = "C:\\Windows\\temp_bin"; // Windows
		        	clearFileName = clearFilesLocation+"\\tempfile.wav"; // windows
		        	Path EncryptedDosya = Paths.get(targetDirectory +"\\"+myFiles[sayac].getName());//Windows

		        	// LINUX
		        	//String clearFilesLocation = "/home/temp_bin"; // Linux
		        	//clearFileName = clearFilesLocation+"/tempfile.wav";// Linux
		        	//Path EncryptedDosya = Paths.get(targetDirectory +"/"+myFiles[sayac].getName());//Linux

		        	PublicDegerler.TempClearFileName = clearFileName;

		        	byte[] binaryData = Files.readAllBytes(EncryptedDosya);
		        	for (int len = 0; len < binaryData.length; len++)
                    {
                        binaryData[len] ^= maskForMusicFile;
                    }

		        	FileOutputStream tempClearFile = new FileOutputStream(clearFileName); // windows icin:\\    Linux icin:  /
		        	tempClearFile.write(binaryData);
		        	tempClearFile.close();

		        	player.play(clearFileName);
		        	Thread.sleep(500);
		        	Files.deleteIfExists(Paths.get(clearFileName));
	        	}
	        	catch(Exception ex)
	        	{
	        		myLog.logYaz("Play icinde HATA  ==>>" + ex.getMessage());
	        	}
	        	//**************************************************************************************
	        }
		}
		}
		catch(Exception ex)
		{
			myLog.logYaz("Main icinde HATA  ==>>" + ex.getMessage());
		}
	}
	*/
}
