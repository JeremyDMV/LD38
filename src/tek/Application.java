package tek;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import tek.game.Game;
import tek.game.Preferences;

public class Application {
	public static final boolean DEBUG = true;
	public static final boolean DEBUG_INPUT = true;
	
	public static final boolean WRITE_ERRORS = true;
	public static final String errorFile = "errors.txt";
	
	public static final String osName, osVersion, archName;
	
	public static final OS os;
	public static final Arch arch;
	
	public static final String userDir;
	public static final String userHome;
	public static final String userName;
	
	public static final String classPath;
	
	public static final String pathSep;
	public static final String fileSep;
	public static final String lineSep;
	
	public static final String jreVersion;
	
	/*  LOGGING */
	private static ArrayList<String> logs;
	private static ArrayList<PipeRule> rules;
	
	public static final int LOG_GENERAL = 0;
	public static final int LOG_WARNING = 1;
	public static final int LOG_ERROR   = 2;
	
	public static final char[] LOG_PRIORITY_CHARS = new char[]{'L','W','E'};
	
	private static InputTask inputTask;
	
	private static ArrayList<String> input;
	
	static {
		osName     = System.getProperty("os.name");
		osVersion  = System.getProperty("os.version");
		archName   = System.getProperty("os.arch");
		
		jreVersion = System.getProperty("java.version");
		
		classPath  = System.getProperty("java.class.path");
		
		pathSep    = System.getProperty("path.separator");
		fileSep    = System.getProperty("file.separator");
		lineSep    = System.getProperty("line.separator");
		
		os = OS.get(osName);
		arch = Arch.get(archName);
		
		userDir  = System.getProperty("user.dir");
		userHome = System.getProperty("user.home");
		userName = System.getProperty("user.name");
		
		logs = new ArrayList<String>();
		rules = new ArrayList<PipeRule>();
		
		if(DEBUG_INPUT)
			input = new ArrayList<String>();
	}
	
	public static void main(String[] args){
		//safe case exit to prevent unwanted experiences
		if(os == OS.NULL && arch == Arch.NULL){
			System.err.println("UNKNOWN ARCHITECTURE:" + archName + " AND OPERATING SYSTEM: " + osName);
			System.err.println("PLEASE CONTACT DEVELOPER FOR SUPPORT OR MODIFY SOURCE");
		}
		
		if(DEBUG){
			//inclusive of all priorities except Erros
			addPipe(new BlacklistPipeRule(System.out, LOG_ERROR));
			
			//only directs errors to System.err
			addPipe(new WhitelistPipeRule(System.err, LOG_ERROR));
		}
		
		if(WRITE_ERRORS){
			try {
				addPipe(new WhitelistPipeRule(new PrintStream(new FileOutputStream(errorFile)), LOG_ERROR));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if(DEBUG_INPUT){
			inputTask = new InputTask(System.in, input);
			inputTask.start();
		}
		
		/* INITIALIZE PROPER GAME ENGINE */
		
		new Engine(new Game(), Preferences.getPrefs());
		
		/* SYSTEM EXITING */
		
		if(DEBUG_INPUT)
			inputTask.interrupt();
		
		if(hasLogged(LOG_ERROR)){
			String[] sys = systemInfo();
			try {
				PrintWriter f = new PrintWriter(errorFile);
				
				String[] errors = getErrors();
				
				for(String i : sys){
					f.print(i+"\n");
				}
				
				//re write the errors logged
				for(String l : errors){
					f.print(l.trim() + "\n");
				}
				
				f.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		rules.forEach((rule)->rule.destroy());
		
		rules.clear();
		logs.clear();
		
		System.exit(0);
	}
	
	public static boolean startsWithLogPrefix(String str){
		return isLogPrefix(str.charAt(0));
	}
	
	public static boolean isLogPrefix(char c){
		char t = Character.toUpperCase(c);
		for(int i=0;i<LOG_PRIORITY_CHARS.length;i++){
			if(LOG_PRIORITY_CHARS[i] == t)
				return true;
		}
		return false;
	}
	
	public static int getLogPrefixType(String in){
		return getLogPrefixType(in.charAt(0));
	}
	
	public static int getLogPrefixType(char c){
		char t = Character.toUpperCase(c);
		for(int i=0;i<LOG_PRIORITY_CHARS.length; i++){
			char l = Character.toUpperCase(LOG_PRIORITY_CHARS[i]);
			if(l == t){
				return i;
			}
		}
		return -4;
	}
	
	public static String[] systemInfo(){
		return new String[]{
			"OS Name: " + osName,
			"OS Version: " + osVersion,
			"Arch: " + archName,
			"JRE: " + jreVersion
		};
	}
		
	
	public static String[] gather(int logPriority){
		ArrayList<String> list = new ArrayList<String>();
		String prefix = "" + getPriorityChar(logPriority);
		prefix = prefix.toLowerCase(); //remove any ambiguity
		
		for(String log : logs){
			if(log.toLowerCase().startsWith(prefix)){
				list.add(log);
			}
		}
		
		String[] array = new String[list.size()];
		for(int i=0;i<array.length;i++)
			array[i] = list.get(i);
		
		list.clear();
		return array;
	}
	
	public static String[] getErrors(){
		return gather(LOG_ERROR);
	}
	
	public static String[] getWarnings(){
		return gather(LOG_WARNING);
	}
	
	public static String[] getGeneral(){
		return gather(LOG_GENERAL);
	}
	
	public static boolean hasLogged(int logPriority){
		char c = Character.toLowerCase(getPriorityChar(logPriority));
		for(String line : logs)
			if(Character.toLowerCase(line.charAt(0)) == c)
				return true;
		return false;
	}
	
	public static boolean hasLoggedError(){
		return hasLogged(LOG_ERROR);
	}
	
	public static boolean hasLoggedWarning(){
		return hasLogged(LOG_WARNING);
	}
	
	public static boolean hasLoggedGeneral(){
		return hasLogged(LOG_GENERAL);
	}
	
	public static boolean hasHadLog(){
		return logs.size() != 0;
	}
	
	public static void log(String log){
		log(LOG_GENERAL, log);
	}
	
	public static void logi(String log){
		log(LOG_GENERAL, log, true);
	}
	
	public static void warning(String log){
		log(LOG_WARNING, log);
	}
	
	public static void warningi(String log){
		log(LOG_WARNING, log, true);
	}
	
	public static void error(String log){
		log(LOG_ERROR, log);
	}
	
	public static void errori(String log){
		log(LOG_ERROR, log, true);
	}
	
	public static void log(int priority, String log, boolean ignoreText){
		if(!ignoreText){
			log(priority, log);
			return;
		}

		if(log.length() > 2){
			if(log.substring(0, 2).contains(":"))
				if(startsWithLogPrefix(log)){
					logs.add(log);
					log = log.substring(2, log.length());
				}
		}
		
		if(!log.endsWith("\n"))
			log = log + "\n";
		
		if(rules.size() == 0)
			return;
		
		for(PipeRule rule : rules)
			rule.add(priority, log);
	}
	
	public static void log(int priority, String log){
		if(log.length() > 3){
			if(log.substring(0, 3).contains(":"))
				if(startsWithLogPrefix(log)){
					logs.add(log);
					priority = getLogPrefixType(log.charAt(0));
					log = log.substring(2, log.length());
				}else{
					char priorityChar = getPriorityChar(priority);
					logs.add(priorityChar + ": " + log);
				}
		}

		if(!log.endsWith("\n"))
			log = log + "\n";
		
		if(rules.size() == 0)
			return;
		
		logs.size();
				
		for(PipeRule rule : rules)
			rule.add(priority, log);
	}
	
	public static char getPriorityChar(int priority){
		if(priority > LOG_ERROR || priority < 0)
			priority = 0;
		return LOG_PRIORITY_CHARS[priority];
	}
	
	public static void addPipe(PipeRule rule){
		rules.add(rule);
	}
	
	public static boolean hasInput(){
		if(!DEBUG_INPUT)
			return false;
		synchronized(input){
			if(input.size() != 0)
				return true;
		}
		return false;
	}
	
	public static String[] getInput(){
		if(!DEBUG_INPUT)
			return null;
		
		synchronized(input){
			if(input.size() != 0){
				String[] array = new String[input.size()];
				for(int i=0;i<array.length;i++)
					array[i] = input.get(i);
				input.clear();
				return array;
			}
		}
		return null;
	}
	
	private Application(){
	}
	
	public static interface PipeRule {
		public void add(int priority, String log);
		public void destroy();
		
		public static boolean contains(int v, int[] array){
			if(array.length == 0)
				return false;
			
			for(int i=0;i<array.length;i++){
				if(array[i] == v)
					return true;
			}
			return false;
		}
		
		public static boolean containsToLength(int v, int[] array, int l){
			for(int i=0;i<l;i++){
				if(array[i] == v)
					return true;
			}
			return false;
		}
		
		public static void clean(int[] array){
			int[] cleaned = new int[array.length];
			int index = 0;
			for(int i=0;i<array.length;i++){
				if(i == 0){
					cleaned[i] = array[i];
					index++;
				}else{
					if(!containsToLength(array[i], cleaned, index)){
						cleaned[index] = array[i];
						index++;
					}
				}
			}
			
			array = Arrays.copyOf(cleaned, index);
		}
	}
	
	public static class WhitelistPipeRule implements PipeRule {
		private final PrintStream pipe;
		public final int[] inclusions;
		private boolean dead = false;
		
		public WhitelistPipeRule(PrintStream pipe, int... inclusions){
			this.pipe = pipe;
			this.inclusions = inclusions;
		}
		
		public void destroy(){
			pipe.close();
			dead = true;
		}

		@Override
		public void add(int priority, String log) {
			if(dead)
				return;
			
			if(!PipeRule.contains(priority, inclusions)){
				return;
			}
			pipe.println(log);
		}
		
		public boolean isDead(){
			return dead;
		}
	}
	
	public static class BlacklistPipeRule implements PipeRule {
		private final PrintStream pipe;
		public final int[] exclusions;
		private boolean dead = false;
		
		public BlacklistPipeRule(PrintStream pipe, int... exclusions){
			this.pipe = pipe;
			this.exclusions = exclusions;
		}
		
		@Override
		public void add(int priority, String log) {
			if(dead)
				return;
			
			if(PipeRule.contains(priority, exclusions))
				return;
			
			pipe.print(log);
		}

		@Override
		public void destroy() {
			pipe.close();
			dead = true;
		}

		public boolean isDead(){
			return dead;
		}
	}
	
	public static enum OS {
		WINDOWS,
		MAC,
		LINUX,
		NULL;
		
		public static OS get(String osStr){
			String os = osStr.toLowerCase();
			
			if(os.contains("windows")){
				return WINDOWS;
			}else if(os.contains("mac") || os.contains("osx")){
				return MAC;
			}else if(os.contains("linux") || os.contains("ubuntu")){
				return LINUX;
			}
			return NULL;
		}
	}
	
	public static enum Arch {
		x64,
		x32,
		NULL;
		
		public static Arch get(String archStr){
			switch(archStr.toLowerCase()){
			case "amd64":
			case "x86_64":
			case "x64":
				return x64;
			case "x86":
			case "i386":
				return x32;
			default:
				return NULL;
			}
		}
	}
	
	public static class InputTask extends Thread {
		private InputStream in;
		private BufferedReader reader;
		
		private ArrayList<String> queue;
		private ArrayList<String> out;
		
		public InputTask(InputStream in, ArrayList<String> out){
			this.in = in;
			reader = new BufferedReader(new InputStreamReader(in));
			
			queue = new ArrayList<String>();
			this.out = out;
		}
		
		@Override
		public void run() {
			while(true){
				if(this.isInterrupted())
					break;
				
				try {
					String ln = reader.readLine();
					if(ln != null){
						queue.add(ln);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(queue.size() != 0){
					synchronized(out){
						out.addAll(queue);
						out.notifyAll();
					}
					queue.clear();
				}
			}
			
			System.out.println("Interrupted, closing");
			
			try{
				in.close();
				reader.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
