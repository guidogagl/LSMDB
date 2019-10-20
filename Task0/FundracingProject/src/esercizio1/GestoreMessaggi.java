package esercizio1;



public class GestoreMessaggi {
	private DepositoDati d;
	private TableMessage tm;
	private String agencyName;
	public GestoreMessaggi( DepositoDati d,TableMessage tm,String agencyName)
	{
		this.d=d;
		this.tm=tm;
		this.agencyName=agencyName;
		
	}
	public void startAggiornamentoTabella() {
		//while(true) {
			ThreadGestioneMessaggi tgm=new ThreadGestioneMessaggi( d,tm,agencyName);
			tgm.start();
			
			/*try {
				tgm.join(2000);
			}catch(Exception e) {e.printStackTrace();}*/
		//}
							
	}
}

