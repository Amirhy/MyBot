import java.awt.List;
import java.util.ArrayList;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Location;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import Dao.DataBase;
import Model.Person;

public class MyBot extends TelegramLongPollingBot {
	Person person = new Person();
	DataBase dao = new DataBase();

	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		boolean isValid = false;
		boolean hasUsername = false;
		long chat_id = update.getMessage().getChatId();
		String UpdateMess = update.getMessage().getText();
		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
		SendMessage message = new SendMessage();
		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton("👨"));
		row.add(new KeyboardButton("👩"));
		ArrayList<KeyboardRow> list = new ArrayList<>();
		list.add(row);
		keyboard.setKeyboard(list);
		person = dao.UserExist(chat_id);
		System.out.println("" + update.getMessage().getFrom().getId());
		if (person == null) {
			person = new Person();
			person.setChat_id(chat_id);
			person.setState(0);
			person.setQn(1);
			dao.InsertUser(person);
			message.setText("نام خود را وارد کنید");
			isValid = true;

		}
		if (update.getMessage().hasText() && update.hasMessage()) {
			message.setChatId(chat_id);
			if (person.getState() == 1) {

				person.setName(UpdateMess);
				message.setText("سن خود را به عدد وارد کنید");
				dao.InsertUser(person);
				isValid = true;
			}
			if (person.getState() == 2) {
				try {
					person.setAge(Integer.valueOf(UpdateMess));
					message.setReplyMarkup(keyboard);
					message.setText("جنسیت خود را وارد کنید");
					dao.InsertUser(person);
					isValid = true;
				} catch (Exception e) {
					// TODO: handle exception
					person.setState(2);
					dao.InsertUser(person);
					message.setText("لطفا سن خود را درست و به عدد وارد کنید");
				}
			}
			if (person.getState() == 3) {
				if (UpdateMess.equals("👨"))
					person.setGender(1);
				else
					person.setGender(0);
				if (update.getMessage().getFrom().getUserName() == null) {
					message.setText(
							"شما فاقد نام کاربری(usrename) هستید.ما برای ارتباط دادن شما به دیگران نیاز به یوزرنیم داریم لطفا وارد بخش تنظیمات تلگرام خود شده و برای خود یک یوزنیم ایجاد کنید وسپس کلید تایید را بزنید ");
					KeyboardRow krow = new KeyboardRow();
					krow.add(new KeyboardButton("تایید"));
					ArrayList<KeyboardRow> l = new ArrayList();
					l.add(krow);
					keyboard.setKeyboard(l);
					message.setReplyMarkup(keyboard);
				} else {
					message.setReplyMarkup(new ReplyKeyboardRemove());
					person.setUsername(update.getMessage().getFrom().getUserName());
					message.setText(
							"سوالات مربوط به بخش  اطلاعات شخصی به پایان رسید لطفا به 10 سوال تست شخصیت پیش رو پاسخ داده تا فرد مورد نظر برای شما انتخاب شود");
					hasUsername = true;
				}
				dao.InsertUser(person);
				isValid = true;
			}
			if (person.getState() == 4) {
				if (UpdateMess.equals("تایید")) {
					if (update.getMessage().getFrom().getUserName() == null) {
						message.setText(
								"شما فاقد نام کاربری(usrename) هستید.ما برای ارتباط دادن شما به دیگران نیاز به یوزرنیم داریم لطفا وارد بخش تنظیمات تلگرام خود شده و برای خود یک یوزرنیم ایجاد کنید وسپس کلید تایید را بزنید ");

					} else {
						person.setUsername(update.getMessage().getFrom().getUserName());
						dao.InsertUser(person);
						message.setReplyMarkup(new ReplyKeyboardRemove());
						message.setText(
								"سوالات مربوط به بخش  اطلاعات شخصی به پایان رسید لطفا به 10 سوال تست شخصیت پیش رو پاسخ داده تا فرد مورد نظر برای شما انتخاب شود");
						isValid = true;

					}
				} else {
					message.setText("دستوراشتباه ؟؟؟لطفا پس از ایجاد یوزرنیم دکمه تایید را بزنید");
				}
			}
			if (person.getState() == 5) {
				KeyboardRow row0 = new KeyboardRow();
				KeyboardRow row1 = new KeyboardRow();
				ArrayList<KeyboardRow> l = new ArrayList();
				message.setText("کلمه یا عبارتی که به بهترین شکل شما را توصیف می نماید انتخاب نمایید");
				switch (person.getQn()) {
				case 1:
					row0.add(new KeyboardButton("خود‌ رأي"));
					row0.add(new KeyboardButton("حمايت‌گر"));
					row1.add(new KeyboardButton("خلاق"));
					row1.add(new KeyboardButton("خونگرم"));
					break;
				case 2:
					if(UpdateMess.equals("خود‌ رأي"))
						person.setQ1(1);
					if(UpdateMess.equals("حمايت‌گر"))
						person.setQ1(2);
					if(UpdateMess.equals("خلاق"))
						person.setQ1(3);
					if(UpdateMess.equals("خونگرم"))
						person.setQ1(4);
					row0.add(new KeyboardButton("قدرت‌طلب"));
					row0.add(new KeyboardButton("کمال‌گرا"));
					row1.add(new KeyboardButton("مردد"));
					row1.add(new KeyboardButton("خودمحور"));
					break;
				case 3:
					if(UpdateMess.equals("قدرت‌طلب"))
						person.setQ2(1);
					if(UpdateMess.equals("حمايت‌گر"))
						person.setQ2(2);
					if(UpdateMess.equals("خلاق"))
						person.setQ2(3);
					if(UpdateMess.equals("خونگرم"))
						person.setQ2(4);
					row0.add(new KeyboardButton("قدرت‌طلب"));
					row0.add(new KeyboardButton("کمال‌گرا"));
					row1.add(new KeyboardButton("مردد"));
					row1.add(new KeyboardButton("خودمحور"));
					break;
				}
				person.setQn(person.getQn() + 1);
				dao.InsertUser(person);
				l.add(row0);
				l.add(row1);
				keyboard.setKeyboard(l);
				message.setReplyMarkup(keyboard);
			}

			try {
				execute(message);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isValid) {
				person.setState(person.getState() + 1);
				if (hasUsername)
					person.setState(person.getState() + 1);
				dao.InsertUser(person);
			}
		}

		if (update.hasMessage() && update.getMessage().hasPhoto())

		{

			java.util.List<PhotoSize> photo = update.getMessage().getPhoto();
			SendPhoto sendPhoto = new SendPhoto();
			sendPhoto.setChatId(chat_id);
			sendPhoto.setPhoto(photo.get(0).getFileId());
			try {
				sendPhoto(sendPhoto);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "Shakhsiat_test_bot";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "598618073:AAGGwC_Ho57p8j4E3lMvv74lA8W5YrfeIdk";
	}

}
