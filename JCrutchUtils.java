package mainPack.Utility;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

public class JCrutchUtils {
	public static final BasicStroke stroke = new BasicStroke(4);
	public static final Color ALPHA = new Color(0, true);

	public static final Color ALPHA_2 = new Color(0, 0, 0, 100);

	public static final Color FILL_DRAG_COLOR = new Color(255, 255, 255, 130);
	public static final String[] Data_Size = { "B", "Kb", "Mb", "Gb" };

	static GraphicsDevice[] screens;
	static Toolkit tools = Toolkit.getDefaultToolkit();
	public static Random random = new Random();

	public static boolean getRandom(double chanceMul) {
		return ((random.nextDouble() * 100) < chanceMul);

	}

	public static String getTime(short minu) {
		String formatH = "";
		short min = (short) (minu % 60);
		short hors = (short) (minu / 60);
		if (hors > 0) {
			formatH = hors + "|";
		}
		return formatH + min;
	}

	public static Color getRandomColor(int minAlpha) {

		return new Color(random.nextInt(255), //
				random.nextInt(255), //
				random.nextInt(255), //
				random.nextInt(255 - minAlpha) + minAlpha);
	}

	public static float getProcentage(char[] rans, char[] in) {
		float corr = 0;

		for (int i = 0; i < rans.length; i++) {
			if (i < in.length) {
				if (rans[i] == in[i]) {
					corr++;
				}
			}
		}
		if (corr == 0) {
			return 0;
		}
		float per = 100f / (rans.length / corr);
		return per;

	}

	public static final void DoBeep() {
		tools.beep();
	}

	public static void updateScreens() {
		screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

	}

	public static Rectangle getRandomScreenRect() {
		if (screens != null) {
			return screens[random.nextInt(screens.length)].getDefaultConfiguration().getBounds();
		}
		return null;
	}

	public static void fadeToAlpha(DataBuffer imgData, byte sc) {

		for (int j = 0; j < imgData.getSize(); j++) {
			int pixel = imgData.getElem(j);

			int a = (pixel >> 24) & 0xff;
			imgData.setElem(j, 0);
			if (a != 0) {
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;
				if (a < 50) {
					imgData.setElem(j, 0);
				} else {
					imgData.setElem(j, ((a -= sc) << 24) | (r << 16) | (g << 8) | b);
				}

			}

		}
	}

	public static void closer(Closeable cl) {
		try {
			cl.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void popUP(Window w) {
		w.setAlwaysOnTop(true);
		w.setAlwaysOnTop(false);
	}

	public static final int getRandomInBounds(int dest) {
		return random.nextInt(dest) - random.nextInt(dest);
	}

	public static void unlock(Object lock) {
		synchronized (lock) {
			lock.notify();
		}
	}

	public static void locker(Object lock, long delay) {

		synchronized (lock) {
			try {
				lock.wait(delay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void locker(Object lock) {

		synchronized (lock) {
			try {
				lock.wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static final double getAngleBetP(float x1, float y1, float x2, float y2) {
		return Math.atan2(y1 - y2, x1 - x2);

	}

	public static int calculateTime(long fileSize, long readed, int speed) {
		if (speed > 0) {
			return (int) ((fileSize - readed) / speed);
		}
		return 0;
	}

	public static Icon getImg(File f) {
		Icon i = FileSystemView.getFileSystemView().getSystemIcon(f);
		return i;
	}

	public static BufferedImage getBufferedImageFileIcon(File f) {
		Icon i = getImg(f);

		BufferedImage out = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
		i.paintIcon(null, out.getGraphics(), 0, 0);
		return out;

	}

	public static byte[] getImgArrey(File f) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Icon i = getImg(f);
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		i.paintIcon(null, g, 0, 0);
		g.dispose();

		try {
			ImageIO.write(img, "png", bos);
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bos.toByteArray();

	}

	public static final BufferedImage ImageSetDue(BufferedImage image, Color color) {

		// fix this
		// L = 0.2126*R + 0.7152*G + 0.0722*B
		// so something like
		// pictureR.set(col, row, new Color(255 * L/255, 153 * L/255, 51 * L/255));

		Graphics2D g = image.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.setComposite(AlphaComposite.SrcAtop);
		g.setColor(color);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.dispose();
		return image;
	}

	public static String getFileSizeString(float b) {
		byte tims = 0;
		while (b > 1024f) {
			b /= 1024f;
			tims++;
			if (tims == 3) {
				break;
			}

		}
		return b + "" + Data_Size[tims];

	}

	public static final void clearBG(Graphics g, Component com) {
		Graphics2D gd = (Graphics2D) g;
		gd.setBackground(ALPHA);
		gd.clearRect(0, 0, com.getWidth(), com.getHeight());
	}

	public static final void clearBGN(Graphics g, Component com) {
		Graphics2D gd = (Graphics2D) g;
		gd.setBackground(ALPHA_2);
		gd.clearRect(0, 0, com.getWidth(), com.getHeight());
	}

	public static final void clearBG(Graphics g, int x, int y, int w, int h) {
		Graphics2D gd = (Graphics2D) g;
		gd.setBackground(ALPHA);
		gd.clearRect(x, y, w, h);
	}

	public static boolean t_fildChec(JTextField t) {
		if (t.getText().trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public static boolean isSubPath(String par, String ch) {
		if (ch.length() < par.length()) {
			return false;
		}
		if (ch.substring(0, par.length()).equals(par)) {
			return true;
		}

		return false;

	}

	public static int scaleTrans(int cord, float scaler, int trans) {
		return (int) (cord * scaler) - trans;
	}

	public static double map(double n, double start1, double stop1, double start2, double stop2) {
		return ((n - start1) / (stop1 - start1)) * (stop2 - start2) + start2;
	}

	public static double lerp(double point1, double point2, double alpha) {
		return point1 + alpha * (point2 - point1);
	}

	public static double badLerp(double tempV, double tempT, double step) {
		if (Math.abs(tempT - tempV) >= step) {
			return tempT > tempV ? tempV + step : tempV - step;
		} else {
			return tempT;
		}
	}

	public static int compSX(JComponent comp) {
		return comp.getX() + comp.getWidth();
	}

	public static int compSY(JComponent comp) {
		return comp.getY() + comp.getHeight();
	}

	// Curves but unfinished
	public static double doElasticShit(double t, double b, double c, double d) {
		// i cur cycle
		// b start
		// c path
		// d cycles
		double ts = (t /= d) * t;
		double tc = ts * t;
		return b + c * (33 * tc * ts + -106 * ts * ts + 126 * tc + -67 * ts + 15 * t);
	}

	// Some Math

	public static final double shortAngleDist(double a0, double a1) {
		double max = Math.PI * 2;
		double da = (a1 - a0) % max;
		return 2 * da % max - da;
	}

	public static final double angleLerp(double a0, double a1, double t) {
		return a0 + shortAngleDist(a0, a1) * t;
	}

	public static final double getAngleBetPoints(int x1, int y1, int x2, int y2) {
		return Math.atan2(y1 - y2, x1 - x2);

	}

	public static final double getDistBetPoints(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static int angleToX(double theta, int x) {
		return (int) (x * Math.cos(theta));
	}

	public static int angleToY(double theta, int y) {
		return (int) (y * Math.sin(theta));
	}

	public static float getProcentage(int Òa, int wa) {
		if (Òa == 0) {
			return 0;
		}
		float summary = wa + Òa;
		float s = summary / Òa;
		return 100f / s;

	}

	public static Color changeColorAlpha(Color t, int nAlpha) {
		return new Color(t.getRed(), t.getGreen(), t.getBlue(), nAlpha);
	}

	public static String getTime(long ms) {

		String formatM = "";
		String formatS = "";

		double mili = (ms / 1000);
		double sec = (mili / 1000) % 60;
		double min = (mili / 1000) / 60;

		if (sec < 10) {
			formatS = "0" + (int) sec;
		} else {
			formatS = (int) sec + "";
		}
		if (min < 10) {
			formatM = "0" + (int) min;
		} else {
			formatM = (int) min + "";
		}
		return formatM + ":" + formatS;
	}

	public static <T> void println(T t) {
		System.out.println("{Utils:} - " + t.toString());
	}

	public static final void BeePBoop() {
		Toolkit.getDefaultToolkit().beep();

	}

	public static final double getAngleBetPoints(float x1, float y1, float x2, float y2) {
		return Math.atan2(y1 - y2, x1 - x2);

	}

	public static final double getDistBetPoints(float x1, float y1, float x2, float y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sleep(long milis, int nanos) {
		try {
			Thread.sleep(milis, nanos);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static final int getRandom(int dest) {
		return random.nextInt(dest) - random.nextInt(dest);
	}

	public static int random(int b) {
		return random.nextInt(b);
	}

	public static final Color getRandomColor() {
		return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 140);
	}

	public static final int[] GetfilledArrey(int how, int leg) {
		int[] ar = new int[leg];
		Arrays.fill(ar, how);
		return ar;

	}

	public static void popUpWindow(Window comp) {
		comp.setAlwaysOnTop(true);
		comp.setAlwaysOnTop(false);

	}

	public static final void AmaZingFixSize(Window trg, int w, int h) {

		// AMAZING FUN FIX ON WINDOW SIZE WTF LOL //
		///////////////////////////////////////////
		JPanel fuk = new JPanel();
		fuk.setPreferredSize(new Dimension(w, h));
		trg.add(fuk);
		trg.pack();
		trg.remove(fuk);
		/////////////////////////////////////////

	}

	public static final void doPressAinmation(JButton comp, int delay) {

		new Thread() {
			@Override
			public void run() {
				setName("Button Animator");
				comp.setSelected(true);
				comp.getModel().setArmed(true);
				comp.getModel().setPressed(true);

				JCrutchUtils.sleep(delay);

				comp.setSelected(false);
				comp.getModel().setArmed(false);
				comp.getModel().setPressed(false);

			}
		}.start();

	}

	public static void Fuck_FrameSzie(Window t, int w, int h) {
		AmaZingFixSize(t, w, h);
		AmaZingFixSize(t, w, h);
		AmaZingFixSize(t, w, h);
		t.setLayout(null);
	}

	public static boolean checkFile(String path) {
		if (path == null || path.trim().isEmpty()) {
			return false;
		}
		return checkFile(new File(path));

	}

	public static boolean checkFile(File f) {
		if (f.exists() && f.canRead() && !f.isDirectory() && f.canWrite()) {
			return true;
		}
		return false;

	}

	public static Color getRgbMappedColor(short stepCount, int alpha) {

		// 1530

		short cycle = (short) (stepCount / 255 % 6);
		short a = (short) (stepCount % 255);
		short prev = (short) (255 - a);

		switch (cycle) {

		case 0:
			return new Color(255, 0, a, alpha);
		case 1:
			return new Color(prev, 0, 255, alpha);
		case 2:
			return new Color(0, a, 255, alpha);
		case 3:
			return new Color(0, 255, prev, alpha);
		case 4:
			return new Color(a, 255, 0, alpha);
		case 5:
			return new Color(255, prev, 0, alpha);

		}
		return new Color(0, 0, 0);
	}

	public static int getActualWidth(Window trg) {
		return trg.getWidth() - (trg.getInsets().left + trg.getInsets().right);
	}

	public static int getActualHeight(Window trg) {
		return trg.getHeight() - (trg.getInsets().bottom + trg.getInsets().top);
	}

	public static String getExecuteName() {
		return new File(JCrutchUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();

	}

	public static File getExecuteFile() {
		return new File(JCrutchUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());

	}

	public static final void setSettings() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("sun.java2d.opengl", "true");
		UIManager.put("FileChooser.readOnly", true);

	}

	public static int CalculateSolidPixels(BufferedImage img) {
		int solidPixels = 0;
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		for (int i : pixels) {
			int a = (i >> 24) & 0xff;
			if (a > 50) {
				solidPixels++;
			}
		}
		pixels = null;
		return solidPixels;
	}

	public static Icon getMiniFileIcon(File f) {
		Icon i = FileSystemView.getFileSystemView().getSystemIcon(f);
		BufferedImage out = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
		i.paintIcon(null, out.getGraphics(), 0, 0);

		return i;
	}

	public static final boolean checkMask(int modifiers, int mask) {
		return ((modifiers & mask) == mask);
	}

	public static void printTBinaryString(long humIn, int countOfBits) {
		StringBuffer buff = new StringBuffer();
		long temp = 0;
		for (int i = countOfBits - 1; i > -1; i--) {
			temp = humIn >> i;
			if ((temp & 0x1) != 0) {
				buff.append(1);
			} else {
				buff.append(0);
			}

			if (i % 8 == 0 && i != 0) {
				buff.append("_");
			}

		}
		System.out.println(buff.toString());

	}

	public static void displayMixerInfo() {
		Mixer.Info[] mixersInfo = AudioSystem.getMixerInfo();

		for (Mixer.Info mixerInfo : mixersInfo) {
			System.out.println("Mixer: " + mixerInfo.getName());

			Mixer mixer = AudioSystem.getMixer(mixerInfo);

			Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();
			for (Line.Info info : sourceLineInfo)
				showLineInfo(info);

			Line.Info[] targetLineInfo = mixer.getTargetLineInfo();
			for (Line.Info info : targetLineInfo)
				showLineInfo(info);
		}
	}

	private static void showLineInfo(final Line.Info lineInfo) {
		System.out.println("  " + lineInfo.toString());

		if (lineInfo instanceof DataLine.Info) {
			DataLine.Info dataLineInfo = (DataLine.Info) lineInfo;

			AudioFormat[] formats = dataLineInfo.getFormats();
			for (final AudioFormat format : formats)
				System.out.println("  " + format.toString());
		}
	}

	public static final float PreampDbMapping(float value) {
		/* -12dB .. 12dB mapping */
		return (float) (9.9999946497217584440165E-01 * Math.exp(6.9314738656671842642609E-02 * value)
				+ 3.7119444716771825623636E-07);
	}

	public final static float DbMapping(float db) {
		// -0.2f - 1.0

		// For bands
		return (float) (2.5220207857061455181125E-01 * Math.exp(8.0178361802353992349168E-02 * db)
				- 2.5220207852836562523180E-01);
	}

	public static byte[] adjustVolume(byte[] audioSamples, float volume) {
		// Big indian
		// Wock
		byte[] array = new byte[audioSamples.length];
		for (int i = 0; i < array.length; i += 2) {
			// convert byte pair to int
			short buf1 = audioSamples[i + 1];
			short buf2 = audioSamples[i];
			buf1 = (short) ((buf1 & 0xff) << 8);
			buf2 = (short) (buf2 & 0xff);
			short res = (short) (buf1 | buf2);

			res = (short) (res * volume);
			// convert back
			array[i] = (byte) res;
			array[i + 1] = (byte) (res >> 8);
		}
		return array;
	}

	public static boolean inRect(double px, double py, double x, double y, double w, double h) {

		if (px >= x && px <= (x + w)) {
			if (py >= y && py <= (y + h)) {
				return true;

			}
		}
		return false;

	}

	public static void dueImageVerySlow(BufferedImage in, BufferedImage out, Color tcolor) {

		int targ = tcolor.getRGB();
		int width = in.getWidth();
		int height = in.getHeight();

		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {

				int color = in.getRGB(col, row);

				int a = (color >> 24) & 0xff;
				int r = (color >> 16) & 0xff;
				int g = (color >> 8) & 0xff;
				int b = color & 0xff;
				// Lums somsing magic shit and somsing else
				int L = (int) (0.2126f * r + 0.7152f * g + 0.0722f * b);

				r = L * (targ >> 16 & 0xff) / 255;
				g = L * (targ >> 8 & 0xff) / 255;
				b = L * (targ & 0xff) / 255;

				int c = (a << 24) | (r << 16) | (g << 8) | b;

				out.setRGB(col, row, c);

			}
		}
	}

	public static double cliplerp(double point1, double point2, double alpha, int delta) {
		if (Math.abs(point1 - point2) < delta) {
			return point2;
		}

		return point1 + alpha * (point2 - point1);
	}

	public static boolean checkArrays(byte[] a1, byte[] a2) {

		if (a1.length != a2.length) {
			return false;
		}
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i]) {
				return false;
			}

		}
		return true;

	}

	// File Text Working
	public static byte getUTFCharLeng(int charHeader) {
		int s = ((charHeader & 0xF0) >> 4);
		if (s == 0xC) {
			return 1;
		}
		if (s == 0xE) {
			return 2;
		}
		if (s == 0xF) {
			return 3;
		}
		return 0;
	}

	public static int findNLPrevs(long from, RandomAccessFile f) throws IOException {
		int offset = 1;
		while (from - offset > 0) {
			f.seek(from - offset);
			byte r = f.readByte();
			if (r != 0x0D && r != 0x0A) {
				break;
			}
			offset++;
		}
		return offset - 1;
	}

	public static byte[] readUTFChar(RandomAccessFile f) throws IOException {
		byte charEnt = f.readByte();
		byte extraCharLeng = getUTFCharLeng(charEnt);
		byte[] out = new byte[extraCharLeng + 1];
		out[0] = charEnt;
		for (int i = 1; i < extraCharLeng + 1; i++) {
			out[i] = f.readByte();
		}
		return out;

	}

	// Some byte things
	public static byte[] toByteArray(long value, int byteleng) {
		byte[] data = new byte[byteleng];
		for (int i = 0; i < data.length; i++) {
			data[(data.length - 1) - i] = (byte) (value >> i * 8 & 0xFF);
		}
		return data;
	}

	public static long fromByteArray(byte[] data, int bytesLeng) {
		long val = 0;
		for (int i = 0; i < data.length; i++) {
			val <<= 8;
			val |= data[i] & 0xFF;
		}
		return val;
	}

	public static long fromByteArray(byte[] data, int offset, int bytesLeng) {
		long val = 0;
		for (int i = 0; i < data.length; i++) {
			val <<= 8;
			val |= data[i] & 0xFF;
		}
		return val;
	}

	public static String printBinary(long val, int bitsCount) {
		StringBuffer outBuffer = new StringBuffer();
		for (int i = 0; i < bitsCount; i++) {
			if (i != 0 && i % 8 == 0) {
				outBuffer.append("_");
			}
			outBuffer.append(val >> (bitsCount - 1) - i & 1);
		}
		return outBuffer.toString();
	}

	public class TimeTracer {
		long time;
		String nam;

		public TimeTracer(String name) {
			time = System.currentTimeMillis();
			nam = name;
		}

		public TimeTracer() {
			time = System.currentTimeMillis();
			nam = "No Name";
		}

		public final void finish() {
			System.out.println("TimeTracer: " + nam);
			System.out.println(System.currentTimeMillis() - time + ":Ms\n");
		}

	}

	public static boolean getRandomChace(double chanceMul) {
		return ((random.nextDouble() * 100) < chanceMul);

	}

	public static float getProcentageS(float val, float max) {
		return 100f / (max / val);
	}

}
//int ind = 1;
//for (int i = fuker.length - 1; i >= ind; i--) {
//	if (i + 1 <= fuker.length - 1) {
//		fuker[i + 1] = fuker[i];
//	}
//
//}

//
//UserDefinedFileAttributeView userDefView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
//
//String name = "attrName";
//String value = "attrValue";
//userDefView.write(name, Charset.defaultCharset().encode(value));
//
//List<String> attribList = userDefView.list();
//for (String string : attribList) {
//	System.out.println(string);
//}

//
//final UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
//
//try {
//	Files.setAttribute(path, "basic:lastModifiedTime", fileTime, LinkOption.NOFOLLOW_LINKS);
//	Files.setAttribute(path, "basic:creationTime", fileTime, LinkOption.NOFOLLOW_LINKS);
//	Files.setAttribute(path, "basic:lastAccessTime", fileTime, LinkOption.NOFOLLOW_LINKS);
//	// Files.setAttribute(path, attribute, "¿¿¿¿¿", LinkOption.NOFOLLOW_LINKS);
//
//} catch (IOException e) {
//	System.err.println(e);
//}
