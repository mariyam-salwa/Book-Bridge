# BookBridge 📚

**Academic Resource Sharing & Physical Book Exchange Platform**  
*An Object-Oriented Programming (OOP) Mini-Project in Java, Spring Boot, MySQL, and Modern Web Technologies.*

---

## 👥 Team Responsibilities Breakdown

| Member | Core Responsibilities | Status |
| :--- | :--- | :--- |
| **Member 1 (You)** | **2. UI/UX Design<br>3. HTML Structure<br>4. CSS Styling & Responsiveness** | **✅ Completed** |
| **Member 2** | 5. JavaScript (Client Interactions, Form Validation, DOM manipulation)<br>6. Java OOP Core Classes & Logic | 🔄 In Progress |
| **Member 3** | 7. Spring Boot REST Controllers & Services<br>8. MySQL Database Models & Repositories<br>9. Frontend & Backend API Integration | 🔄 Pending |

---

## 🚀 Overview of Member 1 Deliverables

The frontend architecture has been designed and implemented in this repository:

1. **Design System & Styles (`/css`)**:
   - `css/style.css`: Global typography, color palette, responsive container, header navigation, footer.
   - `css/components.css`: Buttons, material cards, status badges, forms, search boxes, modals, tabs, responsive tables.

2. **Web Pages (`/*.html`)**:
   - `index.html`: Home page featuring hero search, categories, featured books/notes, and 3-step campus workflow.
   - `explore.html`: Catalog directory with multi-criteria filter sidebar (Department, Semester, Digital vs. Physical, Free vs. Paid, Book Condition).
   - `material-detail.html`: Detailed view of a resource, featuring direct download for digital notes and a **"Request Handover"** campus meetup modal for physical books.
   - `upload.html`: Dynamic upload portal allowing students to switch between uploading digital PDF notes and listing physical textbooks for campus cash exchange.
   - `dashboard.html`: Student dashboard tracking uploads, incoming buyer requests, sent requests, and saved study materials.
   - `admin.html`: Administrator moderation portal managing students, approving/flagging materials, and auditing handover transactions.
   - `login.html` & `register.html`: Clean authentication views with student/admin role toggles.

3. **Documentation (`/docs`)**:
   - `docs/UI_UX_DESIGN_SPECIFICATION.md`: Complete design system tokens, sitemap, wireframe flows, and component specs for the academic project report.

---

## 💻 How to View & Test

You can preview the entire interface right now without any server setup:
1. Open the project folder in your file explorer: `c:\Users\shiri\OneDrive\Desktop\bookbridge`
2. Double-click **`index.html`** to open it in Chrome, Edge, or any modern web browser.
3. Test navigation between all pages, toggle filters in `explore.html`, test the modal in `material-detail.html`, and switch form tabs in `upload.html` and `dashboard.html`.

---

## 🔗 Next Steps for Teammates

- **For Member 2 (JavaScript & OOP):** Hook dynamic behaviors to the IDs in each page (`searchKeyword`, `filterCheckboxes`, `tabDigitalBtn`, `purchaseModal`), and build the Java domain classes (`User`, `Student`, `Administrator`, `Material`, `DigitalMaterial`, `PhysicalMaterial`, `Request`, `Transaction`).
- **For Member 3 (Spring Boot & MySQL):** Map the form inputs to Spring Boot entity models (`@Entity`, `@Table`) and create REST endpoints (`/api/materials`, `/api/requests`, `/api/users`).
