package Step1;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import Global.Var;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;

import expansion.AllExpansions;
import menuHandles.HandleOneFile;
import visitor.AssignVistor;
import visitor.ClassVisitor;
import visitor.CommentVisitor;
import visitor.MethodDeclarationVisitor;
import visitor.MethodInvocationVisitor;

public class Main
{

	public static void main(String[] args) throws IOException
	{

		handleCommand(Var.javaSource);
	}

	public static void handleCommand(String path)
	{
		byte[] input = null;
		try
		{
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
			input = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(input);
			bufferedInputStream.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.err.println("start to parse!");

		// System.err.println(compilationUnit.getElementName());
		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		astParser.setResolveBindings(true);
		astParser.setBindingsRecovery(true);
		astParser.setSource(new String(input).toCharArray());
		astParser.setUnitName("");
		astParser.setEnvironment(new String[] {}, new String[] {}, new String[] {}, true);
		CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
		Var.unit = unit;
		HandleOneFile resultOfOneFile = new HandleOneFile();
		unit.accept(new ClassVisitor(unit, resultOfOneFile));
		unit.accept(new MethodDeclarationVisitor(unit, resultOfOneFile));
		unit.accept(new MethodInvocationVisitor(unit, resultOfOneFile));
		unit.accept(new AssignVistor(unit, resultOfOneFile));
		// comment
		for (Object object : unit.getCommentList())
		{
			Comment comment = (Comment) object;
			String[] temp = {};
			try
			{
				temp = new String(input).split("\n");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			comment.accept(new CommentVisitor(unit, temp, resultOfOneFile));
		}
		resultOfOneFile.parse();

		AllExpansions.postprocess();
		System.err.println("Finished");
	}

}
