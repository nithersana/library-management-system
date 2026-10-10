# ⚛️ GRAVITON LIBRARY SYSTEM

<div align="center">

![Graviton Library System](https://img.shields.io/badge/GRAVITON-LIBRARY%20SYSTEM-00FFFF?style=for-the-badge&logo=atom&logoColor=black)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![TailwindCSS](https://img.shields.io/badge/Tailwind-CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![No Backend](https://img.shields.io/badge/Backend-None%20(In--Memory)-7B2FBE?style=for-the-badge)
![Single File](https://img.shields.io/badge/Deploy-Single%20HTML%20File-FF6B00?style=for-the-badge)

**A fully functional Library Management System built as a single-page web app with an Antigravity / Deep Space visual theme.**

[🚀 Live Demo](#-quick-start) · [📸 Features](#-features) · [🔐 Login Credentials](#-login-credentials)

</div>

---

## 🌌 Visual Theme — Antigravity Deep Space

- **Animated canvas** with 300 drifting star particles and gravitational lensing warp near screen center
- **Gravitational wave rings** pulsing from center every 3 seconds
- **Glassmorphism cards** with `backdrop-filter: blur(12px)` and pulsing cyan borders
- **Fonts:** Orbitron (headings) + Rajdhani (body) via Google Fonts
- **Color Palette:**

| Role | Color |
|------|-------|
| Background | `#000008` |
| Primary Glow | `#00FFFF` (Cyan) |
| Secondary | `#7B2FBE` (Purple) |
| Accent | `#FF6B00` (Orange) |
| Success | `#00FF88` |
| Danger | `#FF3860` |

---

## 🚀 Quick Start

No build step, no `npm install`, no server needed.

```bash
# Clone the repo
git clone https://github.com/YOUR_USERNAME/graviton-library-system.git

# Open in browser
start index.html          # Windows
open index.html           # macOS
xdg-open index.html       # Linux
```

Or just **double-click `index.html`** — it runs entirely in your browser.

---

## 🔐 Login Credentials

| Role | Username | Password |
|------|----------|----------|
| 👩‍💼 Librarian | `librarian` | `lib123` |
| 🎓 Student Member | `student1` | `mem123` |
| 👨‍🏫 Faculty Member | `faculty1` | `fac123` |
| 🛡️ Admin | `admin` | `adm123` |

> Quick-login buttons are also available directly on the login screen.

---

## 📚 Features

### 🔐 Authentication
- Role-based login (Librarian / Member / Admin)
- Quick-login preset buttons with credential badges
- Ripple click effect on login button
- Password update via member profile (reflected immediately in login)

### 📖 Librarian Dashboard (7 pages)
| Page | Features |
|------|----------|
| **Dashboard** | 6 real-time stat cards — Total Books, Available, Active Loans, Overdue, Members, Fines |
| **Manage Books** | Add / Edit / Delete books, live sortable + searchable table |
| **Issue Book** | Member + book dropdowns, availability check, borrow limit check, 15-day due date |
| **Return Book** | Active loans table, auto overdue detection, fine calculation (₹2/day), reservation alert on return |
| **Manage Members** | Register / delete members, active loan count + unpaid fine totals |
| **Reservations** | Pending reservation queue with cancel action |
| **Reports** | All Books · Overdue Loans (red highlight) · Fine Collection Summary |

### 👤 Member Dashboard (6 pages)
| Page | Features |
|------|----------|
| **My Dashboard** | Welcome card with membership ID, quick stats |
| **Search Books** | Live filter by title/author/category, book cards, Borrow / Reserve buttons |
| **My Loans** | Full loan history with ACTIVE / OVERDUE / RETURNED status badges |
| **My Reservations** | View and cancel reservations |
| **My Fines** | Pay fines with glowing confirmation animation |
| **My Profile** | Edit email and password |

### ⚙️ Admin Dashboard (3 pages)
| Page | Features |
|------|----------|
| **User Management** | Tab view Librarians / Members, create and delete accounts |
| **System Settings** | Toggle reservations, auto-fine, set fine rate and max books per member |
| **Reports** | Full system metrics — all data in one view |

---

## ⚡ Functional Highlights

- ✅ Loan counter starts at `LN1000`, Fine counter `FN100`, Reservation counter `RV100`
- ✅ Borrow limit validation per member type (Student: 3, Faculty: 5)
- ✅ Overdue auto-detection and fine calculation (configurable rate, default ₹2/day)
- ✅ Reservation system — triggered when book has 0 copies available
- ✅ Notification when a returned book has a pending reservation
- ✅ Column sort on every table header (toggle ▲ / ▼)
- ✅ Search/filter input above every table
- ✅ Real-time dashboard stat updates
- ✅ Form validation with red glowing error messages
- ✅ Toast notifications — success (green) / error (red) / warning (orange), auto-dismiss 3s
- ✅ Modal close on backdrop click and ✕ button
- ✅ Empty state messages with icons
- ✅ Smooth page transitions (fade in/out)
- ✅ Live clock in header (top-right)
- ✅ Star particle count in bottom-left corner
- ✅ Mobile responsive — sidebar collapses to bottom tab bar on `< 768px`

---

## 🛠 Tech Stack

| Technology | Purpose |
|------------|---------|
| **React 18** (CDN) | UI components and state management via `useState`, `useEffect`, `useReducer`, `useMemo`, `useCallback` |
| **Tailwind CSS** (CDN) | Utility styling |
| **Babel Standalone** (CDN) | In-browser JSX transpilation |
| **HTML5 Canvas** | Animated star field and gravitational wave rings |
| **Google Fonts** | Orbitron + Rajdhani |

> **No Node.js, no npm, no bundler required.**  
> All dependencies load from CDN. Works offline after first load if CDNs are cached.

---

## 📁 Project Structure

```
graviton-library-system/
├── index.html                    # ← Entire application (single file)
├── Graviton_Library_Source_Code.pdf  # Source code as PDF
├── README.md
├── .gitignore
└── LICENSE
```

---

## 📦 Pre-loaded Sample Data

### Books (8 titles)
| ISBN | Title | Author | Category | Copies |
|------|-------|--------|----------|--------|
| 978-1 | Effective Java | Joshua Bloch | Programming | 3 |
| 978-2 | Clean Code | Robert Martin | Programming | 2 |
| 978-3 | The Pragmatic Programmer | David Thomas | Programming | 4 |
| 978-4 | Design Patterns | Gang of Four | Architecture | 2 |
| 978-5 | Introduction to Algorithms | Cormen | Computer Science | 5 |
| 978-6 | Artificial Intelligence | Russell and Norvig | AI | 3 |
| 978-7 | Database Systems | Ramakrishnan | Database | 2 |
| 978-8 | Operating Systems | Silberschatz | Systems | 4 |

---

## 📄 License

MIT License — see [LICENSE](LICENSE) for details.

---

<div align="center">
  Made with 💙 and deep-space energy &nbsp;·&nbsp; ⚛️ GRAVITON LIBRARY SYSTEM
</div>
