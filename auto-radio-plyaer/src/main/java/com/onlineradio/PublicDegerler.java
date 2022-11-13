package com.onlineradio;

import javafx.embed.swing.JFXPanel;

import javafx.scene.media.MediaPlayer;

public class PublicValues {


	//PublicValues.LisansDosyasi = "/mnt/myusb/data_binx/data.binx"; // LINUX
	//PublicValues.LisansDosyasi = "/home/exeler/data.binx"; // LINUX

	public static int i;
	public static int UsedDays;
	public static int LicenceTimeLeft;
	public static int ReaminingTime;
	public static String  LicenceTimeLeftStr;
	public static String  EncFileName;
	public static boolean StopRequested;
	public static JFXPanel myJFX;
	public static long SongDuration;

	//WINDOWS
	public static String targetDirectory ="C:\\muzikler\\";
	public static String TempClearFileName = "C:\\Windows\\temp_bin\\tempfile.bin";
	public static String LisansDosyasi = "C:\\lokal1\\data.binx";
	public static String LogFileAdress ="C:\\Windows\\temp_bin\\";


	//LINUX USB

	//public static String targetDirectory ="/mnt/myusb/temp_bin/sysFile/fat32/bin_files/databin/";  //gecici cozum
	//public static String TempClearFileName = "/mnt/myusb/temp_bin/tempfile.bin";
	//public static String LisansDosyasi = "/mnt/myusb/lokal1/data.binx";
	//public static String LogFileAdress ="/mnt/myusb/temp_bin/";


	// LINUX HOME
	//public static String targetDirectory ="/home/muzikler/";
	//public static String  TempClearFileName = "/home/temp_bin/tempfile.bin";
	//public static String  LisansDosyasi = "/home/lokal1/data.binx";
	//public static String LogFileAdress ="/home/temp_bin/";

}
