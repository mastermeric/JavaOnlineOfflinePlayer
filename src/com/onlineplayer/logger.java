package com.onlineplayer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.*;


public class logger {

	public int logWrite(String icerik)
	{
		DateFormat myDateFormat = new SimpleDateFormat("ddMMyyy");
		DateFormat myDateFormat2 = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
		Date myDate = new Date();
		FileWriter myFile = null;
		String logFileName = PublicValues.LogFileAdress + myDateFormat.format(myDate) +"_LogFile.txt";
		try
		{
			myFile = new FileWriter(logFileName,true);
			myFile.write(myDateFormat2.format(myDate)+ "-->"+icerik );
			myFile.write("\r\n");
			myFile.flush();
			myFile.close();
		}
		catch(Exception ex)
		{
			System.out.println("EEROR at logger  ==>>>" + ex.getMessage());
		}

		return 0;
	}

	public int textYaz(String logFile, String data)
	{
		DateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
		Date myDate = new Date();
		FileWriter myFile = null;

		try
		{
			myFile = new FileWriter(logFile,true);
			myFile.write(myDateFormat.format(myDate));
			myFile.write("\r\n");
			myFile.write(data);
			myFile.flush();
			myFile.close();
		}
		catch(Exception ex)
		{
			System.out.println("textYaz icinde HATA ==>>>" + ex.getMessage());
		}

		return 0;
	}


	public int LisansYaz(String icerik)
	{
		FileWriter myFile = null;

		try
		{
			myFile = new FileWriter("LisansFile.txt",true);
			myFile.write(icerik);
			myFile.flush();
			myFile.close();
		}
		catch(Exception ex)
		{
			System.out.println("textYaz icinde HATA ==>>>" + ex.getMessage());
		}

		return 0;
	}

	// Tek satir okuma yapmak icin..
	public String TekSatirOku(String okunacakDosya)
	{
		try
		{
			FileReader myfileRdr = new FileReader(okunacakDosya);
			BufferedReader myReadBuffer = new BufferedReader(myfileRdr);
			StringBuffer myStringBuffer = new StringBuffer();
			String line;

			while((line = myReadBuffer.readLine()) != null )
			{
				myStringBuffer.append(line);
			}
			myfileRdr.close();
			return myStringBuffer.toString();
		}
		catch(Exception ex)
		{
			System.out.println("textOku icinde HATA ==>>>" + ex.getMessage());
		}
		return "";
	}

	// Satir satir okuma yapmak icin..
	public String textScan(String okunacakDosya)
	{
		try
		{
			String line = "";
			StringBuffer myStringBuffer = new StringBuffer();
			Scanner scanner = new Scanner(new File(okunacakDosya));
			scanner.useDelimiter("\r\n");
			while (scanner.hasNext()) {
			    line = scanner.next();
			    myStringBuffer.append(line);
			    myStringBuffer.append("\r\n");
			}
			scanner.close();
			return myStringBuffer.toString();
		}
		catch(Exception ex)
		{
			System.out.println("textScan icinde HATA ==>>>" + ex.getMessage());
		}
		return "";
	}


}
