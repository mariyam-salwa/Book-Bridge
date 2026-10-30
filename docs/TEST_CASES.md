# BookBridge - Test Cases Report
## PBCST304 Mini Project Deliverable 4
### Department of Computer Science

---

### Test Execution Summary
- **Module:** UI/UX, Semantic HTML, Responsive Styling & Interactive Front-End Views
- **Tested By:** Team Member 1
- **Testing Environment:** Google Chrome 120, Microsoft Edge 120, Firefox 119
- **Overall Result:** All 12 Test Cases **PASSED**

---

### Test Case Execution Table

| No. | Test Case | Test Description & Inputs | Expected Output | Result |
| :---: | :--- | :--- | :--- | :---: |
| **1** | **Navigation Bar Links** | Click each link in top navbar (Home, Explore, Upload, Dashboard). | Correct page loads smoothly with active page indicator updated in navbar. | **Pass** |
| **2** | **Hero Search Query** | Enter keyword `"Java OOP"` in `index.html` search box and submit. | User navigated to `explore.html?q=Java+OOP` with search parameters preserved. | **Pass** |
| **3** | **Explore Filter Selection** | In `explore.html`, select department *"Computer Science"* and semester *"S3"*. | Displayed card results filter to show relevant semester 3 CS resources only. | **Pass** |
| **4** | **Filter Reset Button** | Click the *"Reset All"* button on `explore.html` filter sidebar. | All checkboxes, radio buttons, and inputs clear to default state. | **Pass** |
| **5** | **Resource Detail Rendering** | Click *"View Details"* on *"Modern Operating Systems"* card. | `material-detail.html` loads with book attributes, seller profile, and price badge. | **Pass** |
| **6** | **Handover Modal Launch** | In `material-detail.html`, click *"Request Physical Handover"*. | Modal dialog opens with blurred background overlay and handover form visible. | **Pass** |
| **7** | **Handover Modal Close** | Click *"Cancel"* or the `&times;` close icon on the handover modal. | Modal closes immediately and returns focus to the details page. | **Pass** |
| **8** | **Handover Request Submission** | Fill in buyer phone number, select *"Central Library"*, and submit request form. | Success confirmation displayed; request queued for seller notification. | **Pass** |
| **9** | **Form Type Switching** | On `upload.html`, click between *"Digital Study Material"* and *"Physical Book"*. | Form instantly toggles between PDF dropzone fields and textbook condition/pricing fields. | **Pass** |
| **10** | **Dashboard Tab Switching** | On `dashboard.html`, click between Incoming Requests, Sent Requests, and Listings. | Active tab pane switches with matching table data; other panes remain hidden. | **Pass** |
| **11** | **Auth Role Toggle** | On `login.html`, click between *"Student Portal"* and *"Administrator"*. | Input label changes between *"Student ID"* and *"Admin ID"*; submit button text updates. | **Pass** |
| **12** | **Responsive Mobile Layout** | Resize browser window to viewport width `< 768px`. | Grids collapse gracefully into single column cards; tables become horizontally scrollable without breaking page layout. | **Pass** |

---

### Defect Tracking & Status
- **Total Tests Planned:** 12
- **Passed:** 12
- **Failed:** 0
- **Blocked:** 0
- **Pass Rate:** 100%
