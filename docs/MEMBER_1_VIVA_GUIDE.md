# Member 1 - Presentation & Viva Defense Guide
## PBCST304 Mini Project Evaluation Preparation
### Department of Computer Science • 30 Marks Assessment

---

## 1. Quick Summary of Your Role & Contribution

When asked: **"What was your role and individual contribution in BookBridge?"**

> **Your Answer:**  
> *"I am **Member 1** in our 3-member team. My primary responsibilities covered:  
> 1. **UI/UX Design & Wireframing:** Designing the complete user journey for academic resource sharing and direct campus book exchange.  
> 2. **Semantic HTML5 Architecture:** Structuring all application pages (`index`, `explore`, `material-detail`, `upload`, `dashboard`, `admin`, `login`, `register`) matching our OOP domain entities.  
> 3. **CSS3 Styling & Responsive Design:** Authoring our modular design system (`style.css` and `components.css`), responsive layouts, status badges, handover modals, and interactive dashboard tables.  
> 4. **Authoring Deliverables:** Preparing the Design Document, User Manual, and Test Cases for our project submission."*

---

## 2. Key Viva Questions & Recommended Answers

### Q1: How does your frontend design reflect Object-Oriented Programming (OOP) concepts?
> **Answer:**  
> *"Our user interface is directly mapped to the underlying Java OOP class hierarchy:  
> - **Inheritance:** We have a base `Material` entity with two child classes: `DigitalMaterial` and `PhysicalMaterial`. In our UI, this is reflected through the dual-mode upload form (`upload.html`) and card badges (`.badge-digital` vs. `.badge-physical`).  
> - **Polymorphism:** While both materials inherit common attributes (Title, Subject, Price), their presentation is polymorphic: a digital resource displays file size and a direct download button, whereas a physical book displays book condition (Good/Like New) and an interactive campus handover modal.  
> - **Encapsulation:** The frontend exposes clean interface hooks and form actions (`createRequest`, `uploadFile`) while abstracting internal backend processing.  
> - **User Role Specialization:** `Student` and `Administrator` inherit from `User`. Students have access to the personal dashboard (`dashboard.html`), while administrators access moderation controls (`admin.html`)."*

---

### Q2: Why is there no online payment gateway for physical books?
> **Answer:**  
> *"According to our project problem definition and scope (Section 14), BookBridge is designed specifically for student-to-student campus exchanges. Online payment gateways introduce transaction fees, verification overhead, and refund complexities. Instead, BookBridge uses **Cash on Direct Handover**. The platform manages the booking request and status tracking (Pending &rarr; Accepted &rarr; Handed Over), while the actual payment is made in cash when the two students meet in person on campus."*

---

### Q3: How did you ensure seamless collaboration with Member 2 and Member 3?
> **Answer:**  
> *"I designed the HTML and CSS with strict integration standards:  
> - **For Member 2 (JavaScript & OOP):** All interactive elements have descriptive IDs (e.g., `#tabDigitalBtn`, `#tabPhysicalBtn`, `#purchaseModal`, `#sortSelect`) so DOM event listeners can be attached without modifying HTML markup.  
> - **For Member 3 (Spring Boot & MySQL):** Form input `name` attributes strictly match the Spring Boot entity properties and MySQL table columns (`title`, `author`, `isbn`, `condition`, `price`, `subject`, `department`, `semester`). The templates can be served statically or converted directly to Thymeleaf expressions (`th:field`, `th:each`)."*

---

### Q4: How is responsive design achieved without heavy external frameworks like Bootstrap?
> **Answer:**  
> *"We built a custom, lightweight CSS architecture using modern CSS3 features:  
> - **CSS Custom Properties (Variables):** Standardized color palettes, typography scale, border-radii, and shadows defined in `:root`.  
> - **CSS Grid & Flexbox:** Dynamic grid layouts using `repeat(auto-fill, minmax(280px, 1fr))` for material cards, and multi-column flexboxes for navbar and dashboard metrics.  
> - **Media Queries:** Breakpoints at `992px` and `768px` ensure seamless adaptation across desktops, tablets, and mobile screens without the overhead of heavy third-party CSS libraries."*

---

## 3. Rubric Scoring Checklist (How You Score Full Marks)

| Rubric (Guidelines Pages 29–38) | What You Have Ready |
| :--- | :--- |
| **Rubric 1: Planning & Proposal (5 Marks)** | Detailed Problem Definition, Objectives, Scope, and Class Relationships documented in `docs/DESIGN_DOCUMENT.md`. |
| **Rubric 2: Progress Presentation & Q&A (4 Marks)** | Clear understanding of UI workflows, handover requests, and OOP mapping. |
| **Rubric 3: Team Work & Contribution (3 Marks)** | Distinct, completed Member 1 deliverable set that seamlessly feeds into Member 2 & 3's roles. |
| **Rubric 4: Execution & Implementation (10 Marks)** | 8 fully functional, semantic HTML views, 2 modular CSS stylesheets, modal popups, and tabbed switchers. |
| **Rubric 5: Final Presentation (5 Marks)** | Working browser demonstration of all pages, responsive design, and handover workflows. |
| **Rubric 6: Innovation & Creativity (3 Marks)** | Dual-mode resource sharing (notes download + campus physical book exchange with cash handover). |
