package Global;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Start {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input source code location");

        Var.javaSource = scanner.nextLine();
        String temp = Var.javaSource.substring(0, Var.javaSource.length()-5);
        Var.javaDest = temp + "_Expanded.java";
        Var.tempFile = temp + "_temp.csv";

        if (!CheckLegalInput()) {
            System.out.println("Error input");
            return;
        }

        Step0.PreOperation.main(null);
        Step1.Main.main(null);
        Step2.HandleCSV.main(null);
    }

    public static boolean CheckLegalInput() {
        if (new File(Var.javaSource).exists()
                && (!new File(Var.javaDest).exists() || Var.javaDest.equals(Var.javaSource))) {
            return true;
        }
        return false;
    }

}
