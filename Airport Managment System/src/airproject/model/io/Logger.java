package airproject.model.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import airproject.view.LoggerPanel;

public abstract class Logger {

	public static final String LOGGING_DIR = System.getenv("TEMP") + "\\airproject\\logs\\";
	private static boolean dirExists = false;
	private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY");
	private static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static FileWriter fw;
	private static Calendar currentDate;

	private static FileWriter getWriter() throws IOException {
		Calendar newDate = Calendar.getInstance();
		if (fw == null || currentDate == null || newDate.get(Calendar.DAY_OF_YEAR) != currentDate.get(Calendar.DAY_OF_YEAR)
				|| newDate.get(Calendar.YEAR) != currentDate.get(Calendar.YEAR)) {
			currentDate = newDate;
			fw = new FileWriter(getLog(currentDate.getTime()), true);
		}
		return fw;
	}

	public static File getLog(Date date) {
		if (!dirExists) {
			new File(LOGGING_DIR).mkdirs();
			dirExists = true;
		}
		return new File(LOGGING_DIR + "log_" + dateFormat.format(date));
	}

	public static void log(String data, MessageType type) {
		try {
			FileWriter writer = getWriter();
			synchronized (Logger.class) {
				String text = String.format("{%d}[%s] - %s%n", type != null ? type.toInt() : -1, timeFormat.format(new Date()), data);
				writer.write(text);
				writer.flush();
				if (LoggerPanel.isCurrent)
					LoggerPanel.addLog(text);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isCurrentDate(File file) {
		String[] split = file.getName().split("log_|-");
		if (split.length < 4) {
			throw new IllegalArgumentException();
		}
		int[] dateInfo = new int[3];
		for (int i = 0; i < 3; i++) {
			dateInfo[i] = Integer.parseInt(split[i + 1]);
		}
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_MONTH);
		int month = now.get(Calendar.MONTH) + 1;
		int year = now.get(Calendar.YEAR) % 100;
		if (dateInfo[0] == day && dateInfo[1] == month && dateInfo[2] == year)
			return true;
		return false;
	}

	public enum MessageType {
		START_STOP,
		RUNWAY,
		OBSTACLE,
		CALCULATION,
		RUNWAY_SWITCH,
		VIEW,
		IMPORT_EXPORT;

		private static final MessageType[] TYPES = MessageType.values();

		public static MessageType getType(int i) {
			return TYPES[i];
		}

		public int toInt() {
			return ordinal();
		}
	}

}
