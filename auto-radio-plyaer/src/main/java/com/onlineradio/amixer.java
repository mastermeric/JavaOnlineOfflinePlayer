package com.onlineradio;

public class amixer {


	public amixer ()
	{

	}


	public void setVolumeMax()
	{
		loglama myLog = new loglama();

		try
		{
			BashCommandExecuter myExecuter1 = new BashCommandExecuter();
			myExecuter1.ExecuteCommand("amixer sset PCM 100%");
			Thread.sleep(500);
		}
		catch(Exception ex)
		{
			myLog.logYaz("ERROR -->  Ses AYARINDA HATA oldu");
			myLog.logYaz(ex.getMessage());
		}
	}

}
