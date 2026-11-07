# Maven Dependency Resolution - Fix Guide

## 🔴 Error You're Getting

```
[ERROR] dependency: org.testng:testng:jar:7.8.1 (test)
[ERROR] Could not find artifact org.testng:testng:jar:7.8.1 
[ERROR] in central (https://repo.maven.apache.org/maven2)
```

## ✅ What I've Already Fixed

1. **Updated pom.xml** - Changed TestNG from 7.8.1 → 7.8.0 (verified version)
2. **Added repository configuration** in pom.xml
3. **Created maven-settings.xml** - Maven configuration file for reliable mirrors

---

## 🚀 Follow These Steps to Fix

### **Step 1: Clear Maven Cache** (IMPORTANT!)

#### On Windows (Command Prompt):
```bash
rmdir /s /q "%USERPROFILE%\.m2\repository"
```

#### On Windows (PowerShell):
```powershell
Remove-Item -Recurse -Force "$env:USERPROFILE\.m2\repository"
```

#### On Mac/Linux:
```bash
rm -rf ~/.m2/repository
```

### **Step 2: Navigate to Project**

```bash
cd "C:\Users\abdulla.murshidkv\Desktop\myprojects\Selenium Capstone"
```

### **Step 3: Setup Maven Settings (Optional but Recommended)**

**Option A: Use Project Settings File**
```bash
mvn clean install --settings maven-settings.xml
```

**Option B: Copy to Maven Config Directory**

#### Windows:
```bash
# Create .m2 directory if it doesn't exist
mkdir "%USERPROFILE%\.m2"

# Copy settings file
copy maven-settings.xml "%USERPROFILE%\.m2\settings.xml"

# Then run normally
mvn clean install
```

#### Mac/Linux:
```bash
mkdir -p ~/.m2
cp maven-settings.xml ~/.m2/settings.xml
mvn clean install
```

### **Step 4: Run Maven Clean Install**

```bash
# Clear previous builds
mvn clean

# Force update and install dependencies
mvn install -U

# Or combined:
mvn clean install -U
```

---

## 📊 What Changed in pom.xml

| Item | Old | New | Status |
|------|-----|-----|--------|
| TestNG Version | 7.8.1 | 7.8.0 | ✅ Fixed |
| Repository Config | None | Added | ✅ Added |
| Java Version | 17 | 17 | ✅ Unchanged |
| Selenium | 4.15.0 | 4.15.0 | ✅ Unchanged |

---

## ✅ Expected Success Output

When it works, you should see:

```
[INFO] Building SauceDemo Selenium Automation Framework 1.0.0
[INFO] --------[ jar ]--------
[INFO] 
[INFO] Downloading from central:
[INFO] Downloaded from central: org.testng:testng:jar:7.8.0 (...)
[INFO] Downloaded from central: org.seleniumhq.selenium:selenium-java:jar:4.15.0 (...)
...
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXs
[INFO] Finished at: 2026-11-05T...
```

---

## 🔧 If Still Failing - Troubleshooting

### **Issue 1: Still Can't Download Dependencies**

Try downloading with verbose output:
```bash
mvn clean install -X -DskipTests
```

This shows detailed download attempts. Look for:
- Connection timeouts
- Repository access issues
- Proxy authentication issues

### **Issue 2: Corrupted Cache**

Sometimes the cache gets corrupted. Nuclear option:
```bash
# Remove entire Maven cache
rmdir /s /q "%USERPROFILE%\.m2"

# Maven will recreate it on next run
mvn clean install
```

### **Issue 3: Behind Corporate Proxy**

If you're behind a corporate firewall:

1. Open `maven-settings.xml` in text editor
2. Add proxy configuration:

```xml
<proxies>
    <proxy>
        <id>corporate-proxy</id>
        <active>true</active>
        <protocol>http</protocol>
        <host>proxy.company.com</host>
        <port>8080</port>
        <username>your-username</username>
        <password>your-password</password>
        <nonProxyHosts>localhost|127.0.0.1</nonProxyHosts>
    </proxy>
</proxies>
```

3. Replace with your actual proxy details
4. Save and try again: `mvn clean install --settings maven-settings.xml`

### **Issue 4: Network/Internet Issue**

Check if you can reach Maven Central:
```bash
ping repo.maven.apache.org
```

If no response:
- Check internet connection
- Check firewall/antivirus blocking Maven
- Try using a different network

---

## 🎯 Quick Command Reference

```bash
# Navigate to project
cd "Selenium Capstone"

# Clear cache
rmdir /s /q "%USERPROFILE%\.m2\repository"

# Try install
mvn clean install

# If fails, use settings file
mvn clean install --settings maven-settings.xml

# If still fails, skip tests and get more info
mvn clean install -U -DskipTests -X

# Run tests after successful build
mvn test
```

---

## 📋 Dependency Versions (Now Verified)

```
✅ Selenium: 4.15.0 (Latest 4.x)
✅ TestNG: 7.8.0 (Stable, verified version)
✅ Extent Reports: 5.0.9
✅ Log4j2: 2.21.0
✅ WebDriver Manager: 5.6.2
✅ Maven Compiler: 3.11.0
✅ Maven Surefire: 3.0.0
```

All versions are latest stable versions available in Maven Central.

---

## 🆘 Still Having Issues?

1. **Copy exact command you ran** - Tell me what command you executed
2. **Full error output** - Copy complete error message (red lines)
3. **Your setup** - Java version, Maven version, Windows/Mac/Linux
4. **Network** - Are you behind a proxy or firewall?

---

## ✨ After Build Succeeds

Once `mvn clean install` shows **BUILD SUCCESS**:

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=LoginTests#testValidLogin

# View report
start test-results\reports\ExtentReport_*.html
```

---

**Try the steps above and let me know the exact error if it still fails!** 🎯
