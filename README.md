# 📖 LibraryOS — Library Management System

<div align="center">

![LibraryOS](https://img.shields.io/badge/LibraryOS-Dark%20Glassmorphism-9C27B0?style=for-the-badge&logo=book&logoColor=white)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![TailwindCSS](https://img.shields.io/badge/Tailwind-CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![Java](https://img.shields.io/badge/Java-24%20OOAD-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A dual-architecture Library Management System:**
1. **Frontend:** Single-page web application featuring a Dark Glassmorphism visual theme with 5 animated HTML5 canvas floating gradient orbs and mesh grid.
2. **Backend / Architecture:** Complete Java Object-Oriented Analysis & Design (OOAD / OOSE) implementation utilizing Singleton, Strategy, and Adapter design patterns.

[🚀 Quick Start](#-quick-start) · [✨ Features](#-features) · [🔐 Login Credentials](#-login-credentials) · [☕ Java OOAD System](#-java-ooad-architecture)

</div>

---

## 🎨 Visual Theme — Dark Glassmorphism

- **Interactive Canvas Background:** 5 large gradient orbs (Deep Purple, Royal Blue, Indigo, Deep Azure, and Dark Violet) floating smoothly and bouncing dynamically across the viewport.
- **Mesh Grid:** Subtle animated grid overlay at 5% opacity.
- **Glassmorphism:** Frosted glass panels with `backdrop-filter: blur(20px) saturate(180%)`, soft purple/cyan glow borders, and hover elevations.
- **Typography:** `Sora` for modern headers and `Inter` for clean body text.
- **Palette:**
  - Base: `#0B0B1A`
  - Purple Accents: `#CE93D8`, `#9C27B0`, `#E040FB`
  - Blue Accents: `#90CAF9`, `#1565C0`, `#448AFF`
  - Indicators: Success `#69F0AE`, Danger `#FF5252`, Warning `#FFD740`

---

## 🚀 Quick Start (Web Application)

No build step, no Node.js, and no `npm install` needed!

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/library-management-system.git

# Navigate to project directory
cd library-management-system

# Open in browser
start index.html          # Windows
open index.html           # macOS
xdg-open index.html       # Linux
```
Or simply **double-click `index.html`** in your file manager.

---

## 🔐 Demo Credentials

| Role | Username | Password | Default Borrow Limit |
|---|---|---|---|
| 👩‍💼 **Librarian** | `librarian` | `lib123` | System Staff |
| 🎓 **Student Member** | `student1` | `mem123` | 3 Books Max |
| 👨‍🏫 **Faculty Member** | `faculty1` | `fac123` | 5 Books Max |
| 🛡️ **Admin** | `admin` | `adm123` | Full Access |

*(Quick one-click login buttons are also provided directly on the login screen).*

---

## 📚 Web App Core Pages

1. **🔐 Login Terminal:** Glassmorphism card with role badges and password validation.
2. **📊 Dashboard:** 6 real-time stat cards with animated count-up numbers (`Total Books`, `Available`, `Active Loans`, `Overdue Loans`, `Members`, `Fines`), activity stream, and quick shortcuts.
3. **📚 Manage Books:** Real-time search, multi-column sorting, add/edit book modal with validation, and delete with active loan safeguards.
4. **📤 Issue Book:** Member & book selectors (availability-checked), live animated loan preview card, quota limit checks, and 15-day return scheduling.
5. **📥 Return Book:** Active loan registry, live overdue calculation (₹2/day), and return modal with instant fine payment.
6. **👥 Manage Members:** Member directory with avatar initials, quota indicators, and registration modal.
7. **🔖 Reservations:** Hold queue for checked-out materials with self-cancellation option.
8. **📈 Reports & Audit:** Full catalogue status, overdue audit table, financial breakdown, and a clean print stylesheet (`🖨️ Print Report`).

---

## ☕ Java OOAD Architecture (`java_system/`)

A production-ready Object-Oriented Analysis & Design implementation in Java 24:

```
java_system/
├── src/com/library/
│   ├── model/         # User (Abstract), Member, Librarian, Admin, Book, Loan, Fine
│   ├── catalog/       # Catalog (Thread-safe Singleton using double-checked locking)
│   ├── strategy/      # FineStrategy (Student @ ₹1/d, Faculty @ ₹0.5/d, Guest @ ₹3/d)
│   ├── adapter/       # BookSource & LegacyDatabaseAdapter adapting OldLibraryDatabase (raw String[])
│   ├── exception/     # BookUnavailableException, BorrowingLimitExceededException, BookNotFoundException
│   ├── controller/    # LibraryController (orchestration & business rules)
│   └── Main.java      # Comprehensive test driver verifying all requirements
```

### Run the Java Demo:
```bash
cd java_system
javac -d bin src/com/library/*.java src/com/library/*/*.java
java -cp bin com.library.Main
```

---

## 📁 Repository Structure

```
├── index.html                    # Single-Page Web App (Dark Glassmorphism)
├── LibraryOS_Source_Code.pdf     # Formatted 7-page source code document
├── java_system/                  # Complete Java OOAD Enterprise Design
│   ├── src/                      # Java Source Code
│   └── bin/                      # Compiled Bytecode (.class)
├── README.md                     # Documentation
├── LICENSE                       # MIT License
└── .gitignore                    # Git Ignore rules
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
