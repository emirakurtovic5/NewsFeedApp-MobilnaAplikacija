# 🗞️ NewsFeedApp — Mobilna aplikacija za vijesti

## 📝 Opis

NewsFeedApp je Android aplikacija za pregled i filtriranje vijesti, razvijena u okviru predmeta **Razvoj mobilnih aplikacija** na drugoj godini studija. Projekat je realizovan samostalno kao dio fakultetskog zadatka i demonstrira rad s vanjskim API-jem, lokalnom bazom i modernim Android komponentama.


---

## 🎬 Demo

📽️ [Klikni ovdje za video demo](https://github.com/emirakurtovic5/NewsFeedApp-MobilnaAplikacija/blob/main/assets/NewsFeedApp.mp4)

➡️ Ovaj demo prikazuje glavne funkcionalnosti aplikacije:
- Pregled i prikaz vijesti
- Filtriranje po kategorijama, datumima i nepoželjnim riječima
- Prikaz detalja vijesti sa tagovima i sličnim vijestima


---

## 🚀 Glavne funkcionalnosti

- 📥 Dohvaćanje vijesti, slika i tagova sa vanjskog API-ja
- 🗃️ Keširanje i perzistencija podataka pomoću SQLite baze (offline podrška)
- 🗂️ Više-tablična lokalna baza:
  - Tabela za vijesti
  - Tabela za tagove
  - Veza "više na više" između vijesti i tagova
- 📰 Detaljan prikaz vijesti sa tagovima i dvije slične vijesti
- 🎯 Filteri:
  - Po kategorijama
  - Po nepoželjnim riječima
  - Po rasponu datuma
- 🔘 Interaktivni "chipovi" na početnom ekranu za filtriranje po kategorijama
  - Npr. klikom na "Politika" fetchaju se 3 nove vijesti s interneta i prikazuju na vrhu kao *featured*
- 🧠 Pametan UI:
  - **Featured vijesti** se prikazuju sa većom slikom i tekstom ispod
  - **Standardne vijesti** sa manjom slikom i tekstom sa strane

---

## 🛠 Tehnologije

- 📱 Android Studio + Kotlin
- 💾 SQLite za lokalnu bazu podataka
- 🌐 REST API za dohvat sadržaja
- 🔄 Rukovanje mrežnom konekcijom (online/offline fallback)
- 🧪 Arhitektura zasnovana na MVVM (ako koristiš)

