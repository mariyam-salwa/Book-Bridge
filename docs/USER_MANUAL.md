# BookBridge - User Manual
## PBCST304 Mini Project Deliverable 3
### Department of Computer Science

---

## 1. Software Requirements

To run and preview the BookBridge application, the following system requirements are recommended:

| Component | Minimum Specification | Recommended Specification |
| :--- | :--- | :--- |
| **Operating System** | Windows 10/11, macOS, or Linux | Any modern 64-bit OS |
| **Web Browser** | Google Chrome 90+, Mozilla Firefox 88+, MS Edge | Latest Google Chrome or Edge |
| **Code Editor** | Visual Studio Code, IntelliJ IDEA | VS Code with "Live Server" extension |
| **Java Environment** *(for Backend)* | JDK 17 or higher | OpenJDK 17 or Oracle JDK 21 |
| **Database** *(for Backend)* | MySQL 8.0 or MariaDB | MySQL Community Server 8.0 |

---

## 2. Setup & Installation Steps

### 2.1 Opening the Frontend Directly
1. Download or clone the repository to your local computer:
   ```bash
   git clone https://github.com/your-team/bookbridge.git
   ```
2. Navigate to the project root:
   ```
   c:\Users\shiri\OneDrive\Desktop\bookbridge
   ```
3. Open any `.html` file directly by double-clicking it in File Explorer, or right-clicking and selecting **Open with &rarr; Google Chrome**.

### 2.2 Running with VS Code Live Server (Recommended)
1. Open Visual Studio Code.
2. Click **File &rarr; Open Folder...** and choose the `bookbridge` folder.
3. Install the **Live Server** extension (by Ritwick Dey) from the Extensions tab (`Ctrl + Shift + X`).
4. Right-click on **`index.html`** in the Explorer pane and select **"Open with Live Server"**.
5. The web application will launch at `http://127.0.0.1:5500/index.html`.

---

## 3. Navigation & Menu Options

### 3.1 Main Navigation Bar
- **Logo (BookBridge 📚):** Clicking the logo from any page returns you to the home landing page (`index.html`).
- **Explore Materials:** Takes you to the catalog filter directory (`explore.html`) where you can search through both digital notes and physical books.
- **Physical Books:** Quick filter shortcut loading `explore.html?type=physical`.
- **Upload / Sell:** Opens the contribution portal (`upload.html`) to publish study notes or list textbooks.
- **My Dashboard:** Opens your personal student center (`dashboard.html`) to track requests and active listings.
- **Log In / Get Started:** Access authentication forms (`login.html` and `register.html`).

---

## 4. Step-by-Step Usage Guide

### 4.1 How to Search for Study Materials
1. Go to **Explore Materials** (`explore.html`).
2. In the left sidebar:
   - Type a keyword in the **Keyword** field (e.g. `Java`, `Operating Systems`, `DBMS`).
   - Check or uncheck **Resource Type** (Digital PDF vs. Physical Books).
   - Select your **Department** (e.g., *Computer Science & Eng*).
   - Click your semester pill (e.g., **S3** or **S4**).
   - Select your price criteria (*Free Only* or *Paid*).
3. Click **Apply Filters**.
4. Click **View Details** or **Request Buy** on any card.

### 4.2 How to Request a Physical Book for Cash Handover
1. On the book's details page (`material-detail.html`), click **"🤝 Request Physical Handover"**.
2. A modal dialog will appear.
3. Enter your contact phone/WhatsApp number and select your preferred on-campus meeting location (e.g. *Central Library Reading Room*, *Cafeteria*, *CS Department*).
4. Click **Send Purchase Request**.
5. A confirmation notification will appear, and the status will update on your **Dashboard**.

### 4.3 How to List a Used Book for Sale
1. Click **Upload / Sell** from the top menu (`upload.html`).
2. Click on the **"Physical Academic Book"** switcher card.
3. Fill out the book details:
   - **Book Title:** e.g. *Database System Concepts*
   - **Author:** e.g. *Silberschatz*
   - **Condition:** Select *Brand New*, *Like New*, *Good*, or *Fair*
   - **Selling Price (₹):** e.g. `350`
   - **Preferred Handover Spot:** e.g. *Central Library Lawn*
4. Click **List Book for Campus Handover**.

### 4.4 How to Manage Requests on Your Dashboard
1. Open **My Dashboard** (`dashboard.html`).
2. In the **Incoming Book Requests** tab, view requests from other students for your listed books.
3. Click **Accept** to approve the campus meeting.
4. After meeting the buyer on campus and receiving the cash, click **Mark Cash Received** to update the book status to *Sold*.

### 4.5 How Administrators Moderate the Platform
1. Go to `login.html` and click **Administrator** on the role toggle.
2. Enter your Admin credentials to open the **Administrator Control Panel** (`admin.html`).
3. Under **Student User Management**, review active students and suspend offending accounts.
4. Under **Material & Listing Moderation**, click **Approve** or **Remove** on newly listed books and notes.
5. Under **Handover Transactions Log**, view completed exchanges and cash transaction audit records.

---

## 5. Sample Input & Output

### Scenario 1: Searching for Java Notes
- **Sample Input:**
  - Keyword: `"Java"`
  - Type: `Digital Materials (PDF)`
  - Semester: `S3`
- **Expected Output:**
  - Card displayed: *"Object-Oriented Programming with Java Complete Notes"*
  - Badge: `Free`
  - Action button: `View & Download`

### Scenario 2: Requesting Physical Book Handover
- **Sample Input:**
  - Buyer Name: `"Rahul Verma"`
  - Phone: `"+91 98765 43210"`
  - Proposed Location: `"Central Library Reading Room"`
  - Proposed Cash Amount: `₹450`
- **Expected Output:**
  - Dialog closes with success confirmation alert.
  - Record added to `requests` table with status `PENDING`.
  - Notification displayed in seller's incoming requests tab.

---

## 6. Common Errors & Troubleshooting

| Issue / Error | Likely Cause | Solution |
| :--- | :--- | :--- |
| **CSS styling looks missing or broken** | Relative path broken when moving files | Ensure the `css/` folder resides in the same directory as the `.html` files (`css/style.css` and `css/components.css`). |
| **Modal doesn't close when clicking cancel** | JavaScript disabled in browser | Ensure JavaScript is enabled in your browser settings. |
| **Upload file button does not open file picker** | Hidden input element not clicked | Click directly in the center dashed area of the upload dropzone. |
| **Port 5500 already in use in Live Server** | Multiple VS Code instances active | VS Code will automatically assign port 5501 or 5502; check the bottom status bar for the active port. |
