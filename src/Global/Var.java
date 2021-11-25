package Global;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.CompilationUnit;


public class Var {
    public static String javaSource;
    public static String tempFile;
    public static String javaDest;

    public static String rawSource;

    public static CompilationUnit unit;

    public static HashMap<String, Integer> IdLineInFile = new HashMap<String, Integer>();

}
