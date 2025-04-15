# Congreso AJEF App

**Android app built for the XXXIV National AJEF Congress, held in Chiapas, Mexico from July 20–24, 2017.**

This app was created to support logistics and attendee management for the **34th AJEF National Congress**, a national youth event held in Chiapas. It was built using Firebase and Android Studio and enabled the organizing team to handle event operations efficiently.

## 🧩 Features

- Authentication via Google, Facebook, or email (Firebase Authentication)
- QR code scanning with unique user hashes (generated using name and registration date)
- Realtime registration and data management using Firebase Realtime Database
- Admin panel for staff with edit/log capabilities
- Selection of activities and event tracking
- Participants could scan their own QR to link it with their login account

Initial registration was handled via Google Forms. The collected data was manually validated and exported to JSON to populate the Firebase database.

## 🔧 Tech Stack

- Android Studio (Java)
- Firebase Realtime Database
- Firebase Authentication
- Google Forms (registration)

## 📁 Project Structure
app/
gradle/
build.gradle
settings.gradle
README.md
README-es.md
LICENSE

## 📜 License

This project is licensed under the [MIT License](LICENSE).

> This app was built for a one-time event and is no longer maintained. It remains an example of practical mobile development using Firebase and QR-based validation.
