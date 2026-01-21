# Student Registration App (KMP + Compose Multiplatform)

Mini project aplikasi **pendaftaran siswa** menggunakan **Kotlin Multiplatform (KMP)** dengan **Compose Multiplatform** (UI di module `shared`).  
Dengan arsitektur ini, aplikasi **bisa digunakan untuk Android maupun iOS** karena UI dan business logic dibuat shared (platform-agnostic).

---

## Fitur

### Wajib
- **Login**
    - Dummy login (hardcoded): `admin / admin`
    - Validasi input (tidak boleh kosong)
    - Pesan error jelas
- **Form Pendaftaran**
    - Nama lengkap
    - NISN (10 digit)
    - Tanggal lahir (**Date Picker**)
    - Jurusan (dropdown)
    - Jika form belum lengkap → tampil pesan **“Lengkapi form …”**
- **State Management**
    - `StateFlow` + `StudentState` (unidirectional flow / Redux-style)
- **Data Handling**
    - Simpan data siswa via local datasource (abstraction)
- **List & Detail**
    - List siswa terdaftar
    - Detail siswa
- **Error Handling**
    - Validasi field kosong & format (NISN, tanggal lahir)
    - Pesan error tampil di UI

---

## Tech Stack
- Kotlin Multiplatform (KMP)
- Compose Multiplatform (UI di shared)
- Koin (Dependency Injection)
- Coroutines + StateFlow (state management)

---

## Alur Aplikasi
1. **Splash**
2. **Login** → sukses ke Register
3. **Register** → simpan data → ke List
4. **List** → klik siswa → Detail
5. **Detail** → back ke List

---

## Dummy Login
- Username: `admin`
- Password: `admin`

---

## Struktur Singkat
- `shared/`
    - `business/` (domain, repository, usecase, datasource)
    - `presentation/` (ui screens, navigation, state, viewmodel)
    - `di/` (Koin module)
- `androidApp/`
    - `MainActivity` sebagai entry point untuk menjalankan UI shared

---

## Cara Menjalankan (Android)
1. Buka project di Android Studio
2. Pilih run configuration: **androidApp**
3. Klik **Run** (emulator / device)

Output demo: **APK** dan/atau **video**.

---

## iOS
Project ini menggunakan **KMP + Compose Multiplatform**, sehingga code di `shared` **bisa dipakai untuk Android maupun iOS**.  
Untuk mini project ini fokus demo menggunakan **APK Android**, sedangkan target iOS dapat diaktifkan melalui konfigurasi target iOS pada Gradle (misal `iosArm64/iosSimulatorArm64`) bila diperlukan.

---

## Catatan Implementasi
- Navigasi Register → List hanya terjadi saat state `RegisterSuccess`
- Validasi dilakukan di UI dan juga di UseCase
- Local storage dibuat abstraction sehingga mudah diganti ke SharedPreferences/SQLite/SQLDelight

---

## Demo
- APK: [Download](https://drive.google.com/file/d/1BOF4qX_Cr3YyHoUssT_hmGZvKUgtHVCe/view?usp=drive_link)
- Video: [Watch](https://drive.google.com/file/d/1uFB4qaNboRABXX2iU0z9C8pAedlINGFD/view?usp=drive_link)
