```markdown
# 📝 Offline-First Notes

<p align="left">
  <a href="[https://github.com/Indra9555/OfflineNotes](https://github.com/Indra9555/OfflineNotes)"><img src="[https://img.shields.io/github/stars/Indra9555/OfflineNotes?style=for-the-badge&logo=github&color=007ACC](https://img.shields.io/github/stars/Indra9555/OfflineNotes?style=for-the-badge&logo=github&color=007ACC)" alt="GitHub Stars" /></a>
  <a href="[https://github.com/Indra9555/OfflineNotes/network/members](https://github.com/Indra9555/OfflineNotes/network/members)"><img src="[https://img.shields.io/github/forks/Indra9555/OfflineNotes?style=for-the-badge&logo=github&color=007ACC](https://img.shields.io/github/forks/Indra9555/OfflineNotes?style=for-the-badge&logo=github&color=007ACC)" alt="GitHub Forks" /></a>
  <a href="[https://github.com/Indra9555/OfflineNotes/issues](https://github.com/Indra9555/OfflineNotes/issues)"><img src="[https://img.shields.io/github/issues/Indra9555/OfflineNotes?style=for-the-badge&logo=github&color=007ACC](https://img.shields.io/github/issues/Indra9555/OfflineNotes?style=for-the-badge&logo=github&color=007ACC)" alt="GitHub Issues" /></a>
  <a href="[https://developer.android.com/](https://developer.android.com/)"><img src="[https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)" alt="Platform" /></a>
  <a href="[https://www.java.com/](https://www.java.com/)"><img src="[https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)" alt="Language" /></a>
  <a href="[https://material.io/](https://material.io/)"><img src="[https://img.shields.io/badge/UI-Material_Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white](https://img.shields.io/badge/UI-Material_Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white)" alt="Material Design" /></a>
</p>

A simple Android notes application designed to demonstrate important **Mobile Computing concepts**, including offline computing, intermittent connectivity, local data storage, network awareness, and synchronization.

---

## 📱 Overview

**Offline-First Notes** is an Android application that allows users to create, view, search, edit, and delete notes.

The application follows an **offline-first approach**, meaning that basic note-taking functionality remains available even when the device does not have an active Internet connection.

Notes are stored locally on the device using **SharedPreferences**. The application also detects the current network condition and displays whether the device is **Online** or **Offline**.

When a note is created, edited, or deleted, the application marks the synchronization state as **Pending Sync**. When network connectivity becomes available, the user can select **Sync Now**, after which the application displays the **Synced** state and records the latest synchronization time.

> **Note:** The current version demonstrates synchronization state locally. It does not use an actual cloud server or remote database.

---

## ✨ Features

- 📝 Create new notes
- 👀 View saved notes
- 🔍 Search notes
- ✏️ Edit existing notes
- 🗑️ Delete notes with confirmation
- 💾 Local note storage
- 📡 Network status detection
- 📴 Offline note management
- 🔄 Pending synchronization status
- ☁️ Sync Now functionality
- 🕐 Last Synced time
- 📱 Mobile-friendly user interface

---

## 🧠 Mobile Computing Concepts

The project demonstrates the following Mobile Computing concepts:

| Concept | Implementation |
|---|---|
| **Mobility** | The application runs on a mobile device and can be used from different locations. |
| **Wireless Connectivity** | The application checks the availability of the device's network connection. |
| **Intermittent Connectivity** | The application continues working when Internet connectivity is temporarily unavailable. |
| **Offline Computing** | Users can create and manage notes without an Internet connection. |
| **Local Data Storage** | Notes are stored locally using `SharedPreferences`. |
| **Network Awareness** | The application displays the current Online/Offline state. |
| **Data Synchronization** | Modified data is marked as Pending Sync and can be synchronized when the device is online. |
| **Mobile User Interface** | Touch-friendly controls are designed for smartphone interaction. |

---

## 🔄 Application Workflow

```text
                    Open Application
                           │
                           ↓
                    Load Local Notes
                           │
                           ↓
                  Check Network Status
                       ↙        ↘
                  Online        Offline
                     │              │
                     ↓              ↓
                Normal Use      Local Use
                     ↘              ↙
                       Create/Edit/Delete
                              │
                              ↓
                         Pending Sync
                              │
                              ↓
                     Network Available
                              │
                              ↓
                           Sync Now
                              │
                              ↓
                           Synced
                              │
                              ↓
                     Last Synced Time
```

---

## 🏗️ System Architecture

```text
                         USER
                           │
                           ↓
                  Android Interface
                           │
                           ↓
                    Java Activities
                     ↙           ↘
                    ↓             ↓
          SharedPreferences   ConnectivityManager
                    │             │
                    ↓             ↓
              Local Notes    Network Status
                     ↘           ↙
                       ↓       ↓
                    Sync Status
```

### Architecture Components

- **User Interface:** Provides the screens and controls through which the user interacts with the application.
- **Java Activities:** Handle application logic such as creating, editing, searching, and deleting notes.
- **SharedPreferences:** Stores note information and synchronization data locally on the device.
- **ConnectivityManager:** Checks the current network condition.
- **Synchronization Status:** Displays whether changes are synchronized, pending, or waiting for network connectivity.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Application logic and event handling |
| XML | User interface design |
| Android SDK | Android application development |
| SharedPreferences | Local data storage |
| ConnectivityManager | Network detection |
| NetworkCapabilities | Checking network capabilities |
| Android Activities | Screen management and navigation |
| Material Components | UI components |
| Git | Version control |
| GitHub | Source-code hosting |

---

## 📂 Project Structure

```text
OfflineNotes/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── ...
│           │
│           ├── res/
│           │   ├── drawable/
│           │   ├── layout/
│           │   ├── mipmap/
│           │   └── values/
│           │
│           └── AndroidManifest.xml
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── gradle.properties
└── README.md
```

---

## 💾 Local Data Storage

The application uses Android's `SharedPreferences` for lightweight local storage.

### Main Stored Values

| Key | Purpose |
|---|---|
| `notes` | Stores locally saved notes |
| `syncPending` | Stores the synchronization state |
| `lastSynced` | Stores the latest synchronization time |

### Code Examples

Fetching saved notes:

```java
String savedNotes = preferences.getString("notes", "");
```

Saving notes and setting pending status:

```java
preferences.edit()
        .putString("notes", finalNotes.toString())
        .putBoolean("syncPending", true)
        .apply();
```

---

## 📡 Network Awareness

The application uses Android's `ConnectivityManager` and `NetworkCapabilities` to determine the current network condition.

```java
Network network = cm.getActiveNetwork();
NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
```

The application uses the result to display:
- **Online**
- **Offline**

This allows the application to adapt its synchronization behavior according to the current network condition.

---

## 🔄 Synchronization

When a note is created, edited, or deleted, the synchronization state becomes:

```java
syncPending = true
```

The application displays: **Pending Sync**

When the device is connected and the user selects **Sync Now**, the state changes to:

```java
syncPending = false
```

The application then displays **Synced** and records the latest synchronization time.

### Synchronization Flow

```text
Create / Edit / Delete
          │
          ↓
    Save Locally
          │
          ↓
    Pending Sync
          │
          ↓
   Check Connection
          │
       ┌──┴──┐
       ↓     ↓
    Offline Online
       │     │
       ↓     ↓
    Wait   Sync Now
             │
             ↓
          Synced
             │
             ↓
      Last Synced Time
```

> **Note:** Synchronization in the current version is a local demonstration of synchronization status. No actual cloud/server data transfer is implemented.

---

## 🎨 User Interface

The application provides a simple and clean Android interface.

### Main Screen
- Application title
- Online/Offline status
- Synchronization status
- Last Synced information
- Search bar
- Saved notes
- Edit and Delete options
- Add Note button

### New/Edit Note Screen
- Note title field
- Note content field
- Save button
- Character counter
- Back button

---

## 📸 Screenshots

| Main Screen | New Note Screen |
|:---:|:---:|
| *(Insert Main Screen Screenshot)* | *(Insert New Note Screenshot)* |

| Saved Note | Edit / Delete |
|:---:|:---:|
| *(Insert Saved Note Screenshot)* | *(Insert Edit/Delete Screenshot)* |

| Offline Mode | Synchronized State |
|:---:|:---:|
| *(Insert Offline Mode Screenshot)* | *(Insert Synced State Screenshot)* |

---

## 🧪 Testing

The application was tested under normal and offline network conditions.

| Test ID | Test Case | Expected Result | Status |
|:---:|---|---|:---:|
| **TC-01** | Create Note | Note should be saved locally | ✅ PASS |
| **TC-02** | View Note | Saved note should be displayed | ✅ PASS |
| **TC-03** | Edit Note | Existing note should be updated | ✅ PASS |
| **TC-04** | Delete Note | Note should be deleted after confirmation | ✅ PASS |
| **TC-05** | Search Note | Matching notes should be displayed | ✅ PASS |
| **TC-06** | Disable Internet | Offline status should be displayed | ✅ PASS |
| **TC-07** | Modify Note Offline | Note should remain locally available | ✅ PASS |
| **TC-08** | Offline Change | Pending Sync should be displayed | ✅ PASS |
| **TC-09** | Sync When Online | Status should change to Synced | ✅ PASS |
| **TC-10** | Last Synced | Synchronization time should be displayed | ✅ PASS |

---

## 📊 Results

The application successfully performs the following operations:

- Creating notes
- Viewing notes
- Searching notes
- Editing notes
- Deleting notes
- Local data storage
- Network detection
- Offline note management
- Pending synchronization
- Synchronization status update
- Displaying the latest synchronization time

Testing confirmed that the application remains usable for basic note operations even when Internet connectivity is unavailable.

---

## ✅ Advantages

- Simple and easy to use
- Supports basic note management without Internet
- Provides fast access through local storage
- Demonstrates important Mobile Computing concepts
- Provides network awareness
- Lightweight implementation
- Easy to understand and demonstrate
- Suitable for Android mobile devices

---

## ⚠️ Limitations

- No actual cloud database is currently used.
- Synchronization is demonstrated locally.
- `SharedPreferences` is suitable for a small application but is not ideal for a large-scale notes database.
- No user authentication is implemented.
- Notes cannot currently be synchronized across multiple devices.
- Background synchronization is not implemented.

---

## 🚀 Future Scope

The application can be improved in future by adding:

- ☁️ Cloud synchronization using Firebase or a custom backend
- 🗄️ Room/SQLite database for scalable local storage
- 🔄 Background synchronization using WorkManager
- 🔐 User authentication
- 📱 Multi-device synchronization
- ⚔️ Conflict resolution for simultaneous changes
- 🏷️ Note categories and tags
- ⏰ Reminders and notifications
- 📝 Rich-text note formatting

---

## 🎓 Academic Purpose

This project demonstrates how Mobile Computing concepts can be applied to a practical Android application.

```text
Mobility → Wireless Connectivity → Intermittent Connectivity → Offline Computing → Local Data Storage → Network Awareness → Synchronization
```

---

## 👨‍💻 Author

**Indrajeet**  
B.Tech – Computer Science and Engineering  
SRMU  

GitHub: [https://github.com/Indra9555](https://github.com/Indra9555)

---

## 📄 License

This project was developed for academic and educational purposes.
```
