package org.spoofax.jsglr.tests.haskell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods for calling external commands. Includes input and output
 * stream forwarding to standard in and out.
 * 
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class CommandExecution {

  /**
   * silences the main process
   */
  public static boolean SILENT_EXECUTION = false;

  /**
   * silences the sub processes
   */
  public static boolean SUB_SILENT_EXECUTION = true;

  /**
   * displays full command lines
   */
  public static boolean FULL_COMMAND_LINE = false;

  /**
   * Line-wraps command lines (if displayed at all)
   */
  public final static boolean WRAP_COMMAND_LINE = true;

  /**
   * displays caching information
   */
  public static boolean CACHE_INFO = true;

  /**
   * A thread that forwards the stream in to the stream out, prepending a prefix
   * to each line. See http://www.javaworld.com
   * /javaworld/jw-12-2000/jw-1229-traps.html to understand why we need this.
   */
  private static class StreamLogger extends Thread {
    private final InputStream in;
    private final OutputStream out;
    private String prefix;

    private List<String> msg = new ArrayList<String>();

    public StreamLogger(InputStream in, OutputStream out, String prefix) {
      this.in = in;
      this.out = out;
      this.prefix = prefix == null ? "" : prefix + " ";
    }

    @Override
    public void run() {
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while (!isInterrupted() && (line = reader.readLine()) != null)
          if (!SILENT_EXECUTION && !SUB_SILENT_EXECUTION)
            out.write((prefix + line + "\n").getBytes());
          else
            msg.add(prefix + " " + line);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    public String[] getUnloggedMsg() {
      return msg.toArray(new String[] {});
    }
  }

  public static class ExecutionError extends Error {
    private static final long serialVersionUID = -4924660269220590175L;
    private final String[] cmds;
    private final int exitValue;
    private final String[][] messages;

    public ExecutionError(String message, String[] cmds, int exitValue, String[][] messages) {
      super(message + ": " + commandLineAsString(cmds));
      this.cmds = cmds;
      this.exitValue = exitValue;
      this.messages = messages;
    }

    public ExecutionError(String message, String[] cmds, int exitValue, String[][] messages, Throwable cause) {
      super(message + ": " + commandLineAsString(cmds), cause);
      this.cmds = cmds;
      this.exitValue = exitValue;
      this.messages = messages;
    }

    public String[] getCmds() {
      return cmds;
    }
    
    public int getExitValue() {
      return exitValue;
    }
    
    public String[][] getMessages() {
      return messages;
    }
  }

  /**
   * Executes the given command.
   * <p>
   * All paths given to this function have to be treated by
   * {@link FileCommands#toCygwinPath} (if a cygwin program is to be executed)
   * or {@link FileCommands#toWindowsPath} (if a native Windows program is to be
   * executed) as appropriate.
   * <p>
   * The first argument of this method is used as a short name of the command
   * for logging purposes.
   * 
   * @param cmds
   *          the executable and its argument to execute
   * @throws IOException
   *           when something goes wrong
   */
  public static Object[] execute(OutputStream out, OutputStream err, String logPrefix, String... cmds) {
    return executeWithPrefix(out, err, logPrefix, cmds[0], cmds);
  }
  
  public static Object[] execute(String logPrefix, String... cmds) {
    return executeWithPrefix(System.out, System.err, logPrefix, cmds[0], cmds);
  }


  /**
   * Executes the given command.
   * <p>
   * All paths given to this function have to be treated by
   * {@link FileCommands#toCygwinPath} (if a cygwin program is to be executed)
   * or {@link FileCommands#toWindowsPath} (if a native Windows program is to be
   * executed) as appropriate.
   * 
   * @param prefix
   *          a short version of the command for logging purposes
   * @param cmds
   *          the executable and its argument to execute
   * @throws IOException
   *           when something goes wrong
   */
  public static Object[] executeWithPrefix(OutputStream out, OutputStream err, String logPrefix, String prefix, String... cmds) {
    int exitValue;
    StreamLogger errStreamLogger = null;
    StreamLogger outStreamLogger = null;
    long time = -1;

    try {
      Runtime rt = Runtime.getRuntime();

      // if (!SILENT_EXECUTION) {
      // log.beginExecution(prefix, cmds);
      // }

      long start = System.nanoTime();
      
      Process p = rt.exec(cmds);

      errStreamLogger = new StreamLogger(p.getErrorStream(), err, logPrefix);
      outStreamLogger = new StreamLogger(p.getInputStream(), out, logPrefix);

      // We need to start these threads even if we don't care for
      // the output, because the process will block if we don't
      // read from the streams

      errStreamLogger.start();
      outStreamLogger.start();

      // Wait for the process to finish
      exitValue = p.waitFor();
      
      long end = System.nanoTime();
      time = end - start;
      
      errStreamLogger.join();
      outStreamLogger.join();

      // log.endExecution(exitValue, errStreamLogger.getUnloggedMsg());
    } catch (Throwable t) {
      throw new ExecutionError("problems while executing " + prefix + ": "
          + t.getMessage(), cmds, -1, new String[][] {}, t);
    }
    
    if (exitValue != 0)
      throw new ExecutionError("problems while executing", cmds, exitValue, new String[][] {outStreamLogger.getUnloggedMsg(), errStreamLogger.getUnloggedMsg()});
    
    return new Object[] {time, outStreamLogger.getUnloggedMsg(), errStreamLogger.getUnloggedMsg()};

  }

  public static String commandLineAsString(String[] cmds) {
    StringBuffer buf = new StringBuffer();

    for (int i = 0; i < cmds.length; i++) {
      if (cmds[i] != null && cmds[i].startsWith("-") && i + 1 < cmds.length
          && (cmds[i + 1] == null || !cmds[i + 1].startsWith("-"))) {
        buf.append(i == 0 ? "" : " ").append(cmds[i]).append(" ").append(cmds[i + 1]);
        i++;
      } else if (cmds[i] != null)
        buf.append(i == 0 ? "" : " ").append(cmds[i]);
    }

    return buf.toString();
  }

}
