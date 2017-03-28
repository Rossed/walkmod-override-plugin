package org.walkmod.override.tests;

//TESSSTTING

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.walkmod.javalang.ast.CompilationUnit;
import org.walkmod.javalang.ast.body.BodyDeclaration;
import org.walkmod.javalang.ast.body.ClassOrInterfaceDeclaration;
import org.walkmod.javalang.ast.body.MethodDeclaration;
import org.walkmod.javalang.ast.expr.AnnotationExpr;
import org.walkmod.javalang.ast.expr.Expression;
import org.walkmod.javalang.ast.expr.ObjectCreationExpr;
import org.walkmod.javalang.ast.expr.VariableDeclarationExpr;
import org.walkmod.javalang.ast.stmt.ExpressionStmt;
import org.walkmod.javalang.ast.stmt.TypeDeclarationStmt;
import org.walkmod.javalang.test.SemanticTest;
import org.walkmod.override.visitors.OverrideVisitor;

public class OperationVisitorTest extends SemanticTest {

	@Test
	public void testOverrideOnSimpleClass() throws Exception {
		CompilationUnit cu = compile("public class Foo{ "
				+ "public String toString(){ return \"\"; }" + " }");
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);

		Assert.assertEquals("Override", ann.getName().getName());
	}

	@Test
	public void testOverrideNotRequired() throws Exception {
		CompilationUnit cu = compile("public class Foo{ "
				+ "public boolean equalsTo(Object o){ return false; }" + " }");
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNull(annotations);
	}

	@Test
	public void testOverrideWithSimpleParameter() throws Exception {
		CompilationUnit cu = compile("public class Foo{ "
				+ "public boolean equals(Object o){ return false; }" + " }");
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);

		Assert.assertEquals("Override", ann.getName().getName());
	}

	@Test
	public void testOverrideWithInheritance() throws Exception {

		String fooCode = "public class Foo extends Bar{ "
				+ "public void doSomething(){}" + " }";

		String barCode = "public class Bar{ " + "public void doSomething(){}"
				+ " }";

		CompilationUnit cu = compile(fooCode, barCode);
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);

		Assert.assertEquals("Override", ann.getName().getName());
	}

	@Test
	public void testOverrideWithInterfaces() throws Exception {

		String fooCode = "public class Foo implements Bar{ "
				+ "public void doSomething(){}" + " }";

		String barCode = "public interface Bar{ "
				+ "public void doSomething();" + " }";

		CompilationUnit cu = compile(fooCode, barCode);
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);

		Assert.assertEquals("Override", ann.getName().getName());
	}

	@Test
	public void testOverrideWithGenerics() throws Exception {

		String fooCode = "import java.util.List; public class Foo implements Bar<List>{ "
				+ "public void doSomething(List l){}" + " }";

		String barCode = "import java.util.Collection; public interface Bar<T extends Collection>{ "
				+ "public void doSomething(T c);" + " }";

		CompilationUnit cu = compile(fooCode, barCode);
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);
		Assert.assertEquals("Override", ann.getName().getName());

	}

	@Test
	public void testOverrideWithArrays() throws Exception {

		String fooCode = "import java.util.List; public class Foo implements Bar<List>{ "
				+ "public void doSomething(List[] l){}" + " }";

		String barCode = "import java.util.Collection; public interface Bar<T extends Collection>{ "
				+ "public void doSomething(T[] c);" + " }";

		CompilationUnit cu = compile(fooCode, barCode);
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);
		Assert.assertEquals("Override", ann.getName().getName());

	}

	@Test
	public void testOverrideWithArraysDimensions() throws Exception {

		String fooCode = "import java.util.List; public class Foo extends Bar<List>{ "
				+ "public void doSomething(List[] l){}" + " }";

		String barCode = "import java.util.Collection; public class Bar<T extends Collection>{ "
				+ "public void doSomething(T[][] c){}" + " }";

		CompilationUnit cu = compile(fooCode, barCode);
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNull(annotations);
	}

	@Test
	public void testOverrideWithDynamicArgs() throws Exception {

		String fooCode = "import java.util.List; public class Foo extends Bar<List>{ "
				+ "public void doSomething(List... l){}" + " }";

		String barCode = "import java.util.Collection; public class Bar<T extends Collection>{ "
				+ "public void doSomething(T... c){}" + " }";

		CompilationUnit cu = compile(fooCode, barCode);
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);
		BodyDeclaration method = cu.getTypes().get(0).getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);
		Assert.assertEquals("Override", ann.getName().getName());
	}

	@Test
	public void testOverrideOnSimpleInnerClass() throws Exception {
		CompilationUnit cu = compile("public class Foo{ class Bar {"
				+ "public String toString(){ return \"\"; }" + " }}");
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);

		BodyDeclaration innerClass = cu.getTypes().get(0).getMembers().get(0);
		BodyDeclaration method = ((ClassOrInterfaceDeclaration) innerClass)
				.getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);

		Assert.assertEquals("Override", ann.getName().getName());
	}

	@Test
	public void testOverrideOnSimpleTypeDeclarationStmt() throws Exception {
		CompilationUnit cu = compile("public class Foo{ public void something() {class Bar {"
				+ "public String toString(){ return \"\"; }" + " }}}");
		OverrideVisitor visitor = new OverrideVisitor();
		cu.accept(visitor, null);

		MethodDeclaration firstMethod = (MethodDeclaration) cu.getTypes()
				.get(0).getMembers().get(0);

		TypeDeclarationStmt stmt = (TypeDeclarationStmt) firstMethod.getBody()
				.getStmts().get(0);

		BodyDeclaration method = stmt.getTypeDeclaration().getMembers().get(0);

		List<AnnotationExpr> annotations = method.getAnnotations();
		Assert.assertNotNull(annotations);

		AnnotationExpr ann = annotations.get(0);

		Assert.assertEquals("Override", ann.getName().getName());
	}
	
	@Test
	public void testCorrectOverrideWithSubtypes() throws Exception{
	   CompilationUnit cu = compile("public class Foo { public boolean equals(Foo foo){ return false;} }");
	   OverrideVisitor visitor = new OverrideVisitor();
      cu.accept(visitor, null);

      MethodDeclaration firstMethod = (MethodDeclaration) cu.getTypes()
            .get(0).getMembers().get(0);
      List<AnnotationExpr> annotations = firstMethod.getAnnotations();
      Assert.assertNull(annotations);
	}
	
	@Test
	public void testStaticMethods() throws Exception{
	   CompilationUnit cu = compile("public class Bar extends Foo{ public static void setTestMode(boolean testMode)  {}}","public class Foo { public static void setTestMode(boolean testMode) { }}");
	   OverrideVisitor visitor = new OverrideVisitor();
      cu.accept(visitor, null);
      MethodDeclaration firstMethod = (MethodDeclaration) cu.getTypes()
            .get(0).getMembers().get(0);
      List<AnnotationExpr> annotations = firstMethod.getAnnotations();
      Assert.assertNull(annotations);
	}
	
	@Test
	public void testIssue3() throws Exception{
	   CompilationUnit cu = compile("import java.util.Hashtable; final class ThreadLocalMap extends ThreadLocal { public final Object childValue(Object parentValue) { return null; }}");
	   OverrideVisitor visitor = new OverrideVisitor();
      cu.accept(visitor, null);
      MethodDeclaration firstMethod = (MethodDeclaration) cu.getTypes()
            .get(0).getMembers().get(0);
      List<AnnotationExpr> annotations = firstMethod.getAnnotations();
      Assert.assertNull(annotations);
	}
	
	
	@Test
	public void testIssue3Generics() throws Exception{
	   CompilationUnit cu = compile("public class C extends Comparator{ public void compare(java.util.List x){}}", "public class Comparator<T>{ public void compare(T x){} }");
	   OverrideVisitor visitor = new OverrideVisitor();
      cu.accept(visitor, null);
      MethodDeclaration firstMethod = (MethodDeclaration) cu.getTypes()
            .get(0).getMembers().get(0);
      List<AnnotationExpr> annotations = firstMethod.getAnnotations();
      Assert.assertNull(annotations);
	} 
	
	@Test
	public void testAnonymousClass() throws Exception{
	   
	   CompilationUnit cu = compile("public class Foo { public void bye() {} abstract static class Scanner{ void hello(){} } static final class DefaultScanner extends Scanner { void bye() {} } }");
	   OverrideVisitor visitor = new OverrideVisitor();
      cu.accept(visitor, null);
      ClassOrInterfaceDeclaration firstMethod = (ClassOrInterfaceDeclaration) cu.getTypes()
            .get(0).getMembers().get(2);
      MethodDeclaration md = (MethodDeclaration) firstMethod.getMembers().get(0);
      Assert.assertNull(md.getAnnotations());
	}
	

}
