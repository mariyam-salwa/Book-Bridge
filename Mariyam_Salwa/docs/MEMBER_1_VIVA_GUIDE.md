# Member 1 – Viva Preparation Guide (Java Swing version)

These are short answers to the questions teachers usually ask. Learn the idea behind each answer and say it in your own words. Don't memorise it word for word.

---

## 1. "What was your contribution?"

> I designed the user interface and built it in Java Swing. I made the Login and Register screens, the main window with the sidebar menu, and the Home, Explore, Upload, Dashboard and Admin screens, plus the material details window. I also wrote the `Theme` class for the colours and fonts, and the `Validator` class that checks user input.

## 2. "Why did you change from HTML/CSS to Swing?"

> The guidelines say the project must be a Java program that is menu-driven or GUI-based. With Swing, the whole program, including the interface, is written in Java and uses our OOP classes directly. We don't need a separate web server.

## 3. "What is the difference between Swing and AWT?"

> AWT is Java's older GUI toolkit. Swing is built on top of it. I use Swing for everything you see on screen: `JFrame`, `JPanel`, `JButton`, `JTextField`, `JTable`, `JOptionPane`. I use AWT only for the supporting parts: layout managers like `BorderLayout` and `GridLayout`, `Color` and `Font`, and event classes like `ActionListener`.

## 4. "Where are the OOP concepts in YOUR code?"

| Concept | Where to point |
|---|---|
| Inheritance | Every screen class extends `BasePanel`, which extends `JPanel`. `LoginFrame` extends `JFrame`. `FormPanel` extends `JPanel`. |
| Abstraction | `BasePanel` is an **abstract** class with an abstract method `refresh()`. Every screen must write its own version. |
| Polymorphism | `MainFrame.showScreen()` calls `panel.refresh()` on a `BasePanel` variable. Java runs the correct screen's version at run time. Also, `MaterialDetailDialog` shows different buttons for `DigitalMaterial` and `PhysicalMaterial`. |
| Encapsulation | Form fields are `private`. The screens never touch the data lists directly; they only call `DataStore` methods. |
| Method overriding | `BrandPanel` overrides `paintComponent()` to draw the bridge. The table renderer overrides `getTableCellRendererComponent()` to colour status words. |

## 4b. "Swing looks old. How did you make it look modern?"

> Swing's default look, called Metal, is from the 1990s. Every Swing component is drawn by a "UI delegate" class. In `FlatStyle.java` I extend Swing's basic delegates, like `BasicTabbedPaneUI` and `BasicComboBoxUI`, and override only their drawing methods. That gives flat tabs, drop-downs, headers and scrollbars without any extra library. This is also inheritance and method overriding.

## 5. "How does switching between screens work?"

> `MainFrame` uses a **CardLayout**. All the screens are stacked like a deck of cards and only one is shown at a time. Clicking a menu button calls `cardLayout.show(...)` with the screen's name. Before it is shown, I call its `refresh()` so it displays the latest data.

## 6. "How does a button click work?" (event handling)

> I add an `ActionListener` to the button. When the button is clicked, Swing calls that listener's code. I write the listener as a lambda, for example `loginButton.addActionListener(e -> handleLogin());`, which means "when clicked, run `handleLogin()`".

## 7. "What validation did you do?"

| Input | Rule (in `Validator.java`) |
|---|---|
| Name | 3–50 letters, spaces and dots |
| Email | Must look like `name@domain.com`; must not already be registered |
| Phone | Exactly 10 digits, starting with 6–9 |
| Password | At least 6 characters; Confirm password must match |
| Price | A number from 0 to 10000; letters are rejected using try/catch on `NumberFormatException` |
| File | Must be chosen; maximum 20 MB |
| Actions | You cannot request your own book, request the same item twice, or accept a request for a reserved item |

## 8. "Why does `invokeLater` appear in main?"

> Swing is not thread-safe. All screen work must run on one special thread, the Event Dispatch Thread. `SwingUtilities.invokeLater` puts our start-up code on that thread.

## 9. "Where is the data stored?"

> In my part, data is kept in memory in `DataStore`, using sample records, so the interface can be shown and tested on its own. Member 3 is connecting MySQL through JDBC. Every screen gets its data only through `DataStore` methods, so the database can replace the sample data without changing any screen.

## 10. "Why is it called a JTable / why use a table?"

> `JTable` shows rows and columns, like a spreadsheet. I made it read-only and sortable, so clicking a column header sorts it, and double-clicking a row opens the details. The Status column is coloured: green for AVAILABLE, amber for PENDING or RESERVED, red for SOLD or REMOVED.

---

## Demo order for the presentation (about 3 minutes)

1. Run the app, then try to log in with a wrong password to show the error message.
2. Register a new student, entering a wrong phone number first to show validation.
3. Log in as `shamla@college.edu` and show the Home screen.
4. Go to Explore, filter Type = Physical and Price = Paid, then double-click a book.
5. Log in as `mariyam@college.edu` and request the *Operating System Concepts* book.
6. Log in as Shamla, go to My Dashboard → Requests received, click **Accept**, then **Mark as handed over**.
7. Log in as admin and show the Students, Materials and Transactions tabs.

## Design document updates for the team

Section 15 (Development Tools) of the design document should now read:

| Technology | Purpose |
|---|---|
| Java Swing / AWT | Graphical user interface |
| Java | OOP classes and application logic |
| JDBC | Connecting Java to the database |
| MySQL | Permanent data storage |
| VS Code | Code editing |
| Git & GitHub | Version control and team collaboration |
