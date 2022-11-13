package com.onlineplayer;


public class BashCommandExecuter {

	public BashCommandExecuter()
	{
	}

	public void ExecuteCommand(String komut)
	{
		logger myLog = new logger();

		try
		{
			Process myProc = Runtime.getRuntime().exec(komut);
		}
		catch (Exception ex)
		{
			myLog.logWrite("BashCommandExecuter() ERROR ==>>> komut :" + komut);
			myLog.logWrite("BashCommandExecuter() ERROR ==>>> Exception : " + ex.getMessage());
			System.exit(0);
		}
	}


}
