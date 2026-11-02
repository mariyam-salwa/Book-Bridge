# BookBridge – Member 1: Mariyam Salwa A P

**Role:** UI/UX design and the Java Swing user interface
**Course:** PBCST304 Object-Oriented Programming – Mini Project

This folder is a complete Java project on its own. It runs without the other members' folders.

## Technology

| Part | Technology |
|---|---|
| User interface | Java Swing (AWT used for layouts, colours and events) |
| Application logic | Java OOP classes (`models` package, written by Member 2) |
| Data | In-memory sample data (`data/DataStore.java`), to be replaced by MySQL via JDBC (Member 3) |

## Folder structure

```
Mariyam_Salwa/
├── src/
│   ├── app/
│   │   └── BookBridgeApp.java      Program entry point (main method)
│   ├── models/                     Member 2's OOP classes, used unchanged
│   │   ├── User.java, Student.java, Administrator.java
│   │   ├── Material.java, DigitalMaterial.java, PhysicalMaterial.java
│   │   └── Request.java, Transaction.java
│   ├── data/
│   │   └── DataStore.java          Sample data and all data operations
│   └── ui/                         Member 1's work
│       ├── Theme.java              Colours, fonts and styled components (replaces CSS)
│       ├── FlatStyle.java          Modern flat drawing for tabs, drop-downs, headers, scrollbars
│       ├── Validator.java          Input validation (replaces validation.js)
│       ├── FormPanel.java          Reusable form layout
│       ├── BrandPanel.java         Blue branding panel with the bridge drawing
│       ├── LoginFrame.java         Login screen            (was login.html)
│       ├── RegisterFrame.java      Student registration    (was register.html)
│       ├── MainFrame.java          Main window: sidebar menu + screens
│       ├── BasePanel.java          Abstract parent of all screens
│       ├── HomePanel.java          Student home            (was index.html)
│       ├── ExplorePanel.java       Search and filters      (was explore.html)
│       ├── MaterialDetailDialog.java  Material details     (was material-detail.html)
│       ├── UploadPanel.java        Upload / list a book    (was upload.html)
│       ├── DashboardPanel.java     Student dashboard       (was dashboard.html)
│       └── AdminPanel.java         Administrator panel     (was admin.html)
├── docs/
│   └── MEMBER_1_VIVA_GUIDE.md
├── .vscode/                        VS Code run settings
├── run.bat                         Compile + run on Windows
└── run.sh                          Compile + run on macOS / Linux
```

## Software requirements

- JDK 11 or newer (JDK 17 or 21 recommended, for example Eclipse Temurin)
- VS Code with the **Extension Pack for Java** (optional, any Java IDE works)

## How to run

**Option 1 – VS Code**
1. In VS Code choose *File → Open Folder* and open the `Mariyam_Salwa` folder itself (not the whole repository).
2. Wait for the Java extension to finish loading (bottom status bar).
3. Open `src/app/BookBridgeApp.java` and click **Run** above `main`, or press F5.

**Option 2 – Windows, no IDE:** double-click `run.bat`.

**Option 3 – Terminal (any OS):**
```
javac -d bin $(find src -name "*.java")
java -cp bin app.BookBridgeApp
```

## Demo accounts

| Role | Email | Password |
|---|---|---|
| Student | shamla@college.edu | pass123 |
| Student | mariyam@college.edu | mypass456 |
| Student | arjun@college.edu | arjun123 |
| Administrator | admin@bookbridge.org | adminSecure! |

New student accounts can be created from the Login screen.
Sample data resets every time the program is restarted.

## Screens and what they do

| Screen | Who | Main functions |
|---|---|---|
| Login | everyone | Email + password check, role decides the next screen |
| Register | new students | Validated form: name, email, 10-digit phone, student ID, department, semester, password |
| Home | student | Quick search, platform numbers, recently added materials |
| Explore | everyone | Search by keyword; filter by type, price and subject; sortable results |
| Material details | everyone | Digital: file info + Download / Request access. Physical: condition + Request handover. Save for later |
| Upload / List Book | student | Digital tab (file chooser, max 20 MB) and Physical tab (condition, price) |
| My Dashboard | student | My listings, requests received (accept / decline / handed over), my requests (cancel), saved |
| Admin Panel | administrator | Remove students, remove / restore listings, close transactions |

## Main workflow (physical book)

1. Student A lists a book (Upload → Physical book).
2. Student B finds it in Explore and clicks **Request handover** → request is `PENDING`.
3. Student A accepts it in My Dashboard → book becomes `RESERVED`, a transaction is opened.
4. They meet on campus; B pays cash.
5. Student A clicks **Mark as handed over** → book becomes `SOLD`, request and transaction `COMPLETED`.

## Common errors and solutions

| Problem | Solution |
|---|---|
| `javac is not recognized` | The JDK is not installed or not on PATH. Install a JDK and reopen the terminal. |
| VS Code shows red errors on every import | Open the `Mariyam_Salwa` folder itself, not the parent repository folder. |
| No **Run** button above `main` | Install *Extension Pack for Java* and wait for it to finish loading. |
| My upload disappeared | Data is in memory only until Member 3 connects MySQL; restarting resets it. |

## Notes for teammates

- `models/` is a copy of Member 2's classes, unchanged. If Member 2 changes them, copy the new versions in.
- **Member 3:** every screen gets its data only through `DataStore`. Keep the method names, replace the code inside with JDBC + SQL, and the UI needs no changes.
