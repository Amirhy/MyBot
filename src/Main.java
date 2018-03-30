import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import Dao.DataBase;
import Model.Person;

public class Main {

	public static void main(String[] args) throws TelegramApiRequestException {
		// TODO Auto-generated method stub
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		telegramBotsApi.registerBot(new MyBot());
		System.out.println("Bot Started");

	}

}
