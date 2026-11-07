# Java 17 Module System Fix - Lombok & Annotation Processors

## 🔴 Error You Got

```
[ERROR] java.lang.IllegalAccessError: class lombok.javac.apt.LombokProcessor
[ERROR] cannot access class com.sun.tools.javac.processing.JavacProcessingEnvironment
[ERROR] because module jdk.compiler does not export com.sun.tools.javac.processing
```

## 🎯 Root Cause

Java 17 introduced a **strict module system** that restricts access to internal compiler APIs. Libraries like Lombok that use Java annotation processors need access to these internal modules.

The error occurs because:
- ❌ Lombok (or similar) tries to access internal Java compiler modules
- ❌ Java 17 module system blocks this access by default
- ❌ Maven compiler plugin doesn't have proper module configuration

## ✅ What I Fixed

### Updated `pom.xml` Maven Compiler Plugin

Added **compiler arguments** to explicitly allow access to internal modules:

```xml
<compilerArgs>
    <arg>--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED</arg>
    <arg>--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED</arg>
    <arg>--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED</arg>
    <arg>--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED</arg>
</compilerArgs>
```

This tells Java 17 to allow annotation processors to access these internal modules.

---

## 🚀 Next Steps

### Step 1: Clear Maven Cache (Important!)

```bash
# Windows Command Prompt
rmdir /s /q "%USERPROFILE%\.m2\repository"

# Windows PowerShell
Remove-Item -Recurse -Force "$env:USERPROFILE\.m2\repository"

# Mac/Linux
rm -rf ~/.m2/repository
```

### Step 2: Rebuild Project

```bash
cd "Selenium Capstone"
mvn clean compile
```

### Step 3: Try Full Build

```bash
mvn clean install -U
```

---

## 📋 Full Updated Maven Compiler Configuration

```xml
<!-- Maven Compiler Plugin - Configured for Java 17 -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <!-- Java 17 - Latest LTS version -->
        <source>17</source>
        <target>17</target>
        <!-- Add compiler arguments for Java 17 module system compatibility -->
        <compilerArgs>
            <arg>--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED</arg>
            <arg>--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED</arg>
            <arg>--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED</arg>
            <arg>--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

---

## ⚠️ Understanding the Fix

| Item | Explanation |
|------|-------------|
| `--add-opens` | Java 17 flag to allow module access |
| `jdk.compiler` | The Java compiler module |
| `com.sun.tools.javac.*` | Internal compiler packages |
| `ALL-UNNAMED` | Allow all unnamed modules (annotation processors) |

---

## ✨ Expected Success Output

After the fix, you should see:

```
[INFO] Compiling 1 source file to target/test-classes
[INFO] Building jar: target/selenium-capstone-framework-1.0.0.jar
[INFO] 
[INFO] BUILD SUCCESS
[INFO] Total time: 01:23 min
```

---

## 🔧 If Still Failing

### Option 1: Clear Everything and Retry

```bash
# Remove Maven cache completely
rmdir /s /q "%USERPROFILE%\.m2"

# Rebuild from scratch
cd "Selenium Capstone"
mvn clean install -U -X
```

### Option 2: Run with Verbose Output

```bash
mvn clean compile -X 2>&1 | findstr /i "error"
```

This shows detailed error information.

### Option 3: Skip Tests and Build Only

```bash
mvn clean install -DskipTests
```

If this succeeds but tests fail, the issue is with test compilation specifically.

### Option 4: Check Java Version

```bash
java -version
javac -version
```

Ensure both show Java 17.x.x

---

## 📊 What These Modules Do

| Module | Purpose | Why Needed |
|--------|---------|-----------|
| `com.sun.tools.javac.processing` | Annotation processing | Annotation processors like Lombok |
| `com.sun.tools.javac.util` | Compiler utilities | Internal compiler functions |
| `com.sun.tools.javac.code` | Symbol processing | Type checking and symbol tables |
| `com.sun.tools.javac.tree` | AST manipulation | Abstract syntax tree processing |

---

## 📝 Complete Maven Compiler Plugin Block

Here's what your final `maven-compiler-plugin` should look like in pom.xml:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <compilerArgs>
            <arg>--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED</arg>
            <arg>--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED</arg>
            <arg>--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED</arg>
            <arg>--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

---

## 🎯 Step-by-Step Fix Command

Run these in order:

```bash
# 1. Navigate to project
cd "C:\Users\abdulla.murshidkv\Desktop\myprojects\Selenium Capstone"

# 2. Delete Maven cache
rmdir /s /q "%USERPROFILE%\.m2\repository"

# 3. Clean project
mvn clean

# 4. Install dependencies
mvn install -U

# 5. If successful, run tests
mvn test
```

---

## ✅ Why This Fix Works

1. **Java 17 Module System** - Restricts internal API access
2. **Lombok & Annotation Processors** - Need internal compiler access
3. **`--add-opens` Compiler Arg** - Explicitly grants module access
4. **Fixes the Illegal Access Error** - Allows compilation to proceed

This is the **standard approach** for Java 17+ when using libraries that access internal compiler modules.

---

## 🚀 Try Now

```bash
cd "Selenium Capstone"
mvn clean install -U
```

Your build should now succeed! ✅

If you still get errors, run with debug:
```bash
mvn clean install -U -X
```

And share the error output with me.

---

## 📚 Additional Resources

- [Java Module System](https://docs.oracle.com/en/java/javase/17/docs/api/)
- [Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)
- [Lombok with Java 17](https://projectlombok.org/)
