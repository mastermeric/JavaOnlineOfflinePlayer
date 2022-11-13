package com.onlineradio;


public class BashCommandExecuter {

	public BashCommandExecuter()
	{
	}

	public void ExecuteCommand(String komut)
	{
		loglama myLog = new loglama();

		try
		{
			Process myProc = Runtime.getRuntime().exec(komut);
		}
		catch (Exception ex)
		{
			myLog.logYaz("BashCommandExecuter() ERROR ==>>> komut :" + komut);
			myLog.logYaz("BashCommandExecuter() ERROR ==>>> Exception : " + ex.getMessage());
			System.exit(0);
		}
	}


}
