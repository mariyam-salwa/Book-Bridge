# BookBridge 📚

**Academic resource sharing and physical book exchange platform**
PBCST304 Object-Oriented Programming – Mini Project, Department of Computer Science

BookBridge helps college students share digital study materials (notes, question papers, lab manuals) and buy or sell used academic textbooks. Digital materials can be free or paid. Physical books are paid for in cash during a direct handover between students; the platform tracks requests and transaction status.

## Technology

| Part | Technology |
|---|---|
| User interface | Java Swing (with AWT for layouts and events) |
| Application logic | Java, Object-Oriented Programming |
| Database connection | JDBC |
| Permanent storage | MySQL |
| Tools | VS Code, Git and GitHub |

## Team and repository structure

As instructed, each member's work is kept in a folder named after them.

| Member | Responsibility | Folder | Status |
|---|---|---|---|
| Mariyam Salwa A P | UI/UX design and the Java Swing user interface | [`Mariyam_Salwa/`](Mariyam_Salwa/) | Completed |
| Shamla Shirin | Java OOP classes and application logic | `Shamla_Shirin/` | In progress |
| Khadeejath Naziya P M | MySQL database and JDBC data storage | `Khadeejath_Naziya/` | In progress |

The `java/`, `js/` and `backend/` folders are from the earlier web-based version of the project and will be replaced as the members upload their reworked parts.

## OOP design (summary)

- **Inheritance:** `Student` and `Administrator` extend `User`; `DigitalMaterial` and `PhysicalMaterial` extend `Material`
- **Abstraction:** `Material` is an abstract class
- **Encapsulation:** private fields accessed through getters and setters with validation
- **Polymorphism:** overridden methods behave differently for digital and physical materials

## Running the application

Member 1's folder runs on its own using sample data. See [`Mariyam_Salwa/README.md`](Mariyam_Salwa/README.md) for requirements, run steps and demo accounts.

## Documentation

Design document, user manual and test cases are in [`docs/`](docs/).
