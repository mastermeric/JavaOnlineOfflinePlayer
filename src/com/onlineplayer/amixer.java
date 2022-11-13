package com.onlineplayer;

public class amixer {


	public amixer ()
	{

	}


	public void setVolumeMax()
	{
		logger myLog = new logger();

		try
		{
			BashCommandExecuter myExecuter1 = new BashCommandExecuter();
			myExecuter1.ExecuteCommand("amixer sset PCM 100%");
			Thread.sleep(500);
		}
		catch(Exception ex)
		{
			myLog.logWrite("ERROR -->  Ses AYARINDA HATA oldu");
			myLog.logWrite(ex.getMessage());
		}
	}

}
