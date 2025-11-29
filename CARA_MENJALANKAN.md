# 🔧 CARA MENJALANKAN APLIKASI SETELAH PERBAIKAN

## ⚠️ PENTING - LAKUKAN LANGKAH-LANGKAH INI SECARA BERURUTAN:

### 1️⃣ Sync Gradle Project
- Di Android Studio, klik banner kuning yang muncul "Gradle files have changed"
- Atau: Klik **File** → **Sync Project with Gradle Files**
- Tunggu sampai sync selesai (lihat progress bar di bawah)

### 2️⃣ Clean Project
- Klik **Build** → **Clean Project**
- Tunggu sampai selesai

### 3️⃣ Rebuild Project
- Klik **Build** → **Rebuild Project**
- Tunggu sampai selesai (bisa 2-5 menit)

### 4️⃣ HAPUS Aplikasi Lama dari HP
**INI SANGAT PENTING!**
- Buka HP Anda
- Cari aplikasi "Hidrify" atau "Hydrify Tracker"
- Tekan dan tahan icon aplikasi
- Pilih "Uninstall" atau "Hapus"
- Konfirmasi penghapusan

**Mengapa harus dihapus?**
- Database lama corrupt/rusak
- Kalau tidak dihapus, aplikasi masih akan crash
- Setelah install ulang, database baru akan dibuat dengan benar

### 5️⃣ Install & Run
- Pastikan HP terhubung ke komputer (USB debugging ON)
- Di Android Studio, klik tombol ▶️ **Run** (hijau)
- Atau tekan **Shift + F10**
- Pilih device HP Anda
- Tunggu aplikasi ter-install dan terbuka

### 6️⃣ Test Aplikasi
Coba:
- ✅ Aplikasi terbuka tanpa crash
- ✅ Klik tombol "+100 ml", "+250 ml", dll
- ✅ Lihat angka bertambah
- ✅ Lihat progress circle berubah
- ✅ Tutup dan buka lagi aplikasi (data harus tersimpan)

---

## 🐛 Jika Masih Crash/Error:

### Cara Melihat Error di Logcat:
1. Buka tab **Logcat** di Android Studio (bagian bawah)
2. Pilih device HP Anda
3. Di kotak filter, ketik: `ka.mobile.hidrify`
4. Jalankan aplikasi
5. Lihat error message yang muncul (biasanya warna merah)
6. Screenshot error tersebut dan kirim ke saya

### Error Umum dan Solusinya:

**Error: "Application installation failed"**
- Solusi: Hapus aplikasi dari HP secara manual, lalu coba lagi

**Error: "INSTALL_FAILED_INSUFFICIENT_STORAGE"**
- Solusi: Hapus beberapa aplikasi atau file dari HP untuk free space

**Error: "Room cannot verify the data integrity"**
- Solusi: Hapus aplikasi dari HP dan install ulang

**Error: "java.lang.ClassNotFoundException"**
- Solusi: Build → Clean Project, lalu Build → Rebuild Project

---

## 📱 Persyaratan HP:

- ✅ Android versi 8.0 (Oreo) atau lebih baru
- ✅ Minimal 100 MB free storage
- ✅ USB Debugging enabled (untuk development)

---

## 🎉 Setelah Berhasil:

Aplikasi seharusnya:
1. Terbuka tanpa crash
2. Menampilkan "Hydrify Tracker" di top bar
3. Menampilkan target harian 2000 ml
4. Ada 4 tombol: +100ml, +250ml, +500ml, +600ml
5. Ketika diklik, angka bertambah dan progress circle berubah
6. Data tersimpan (tidak hilang saat tutup aplikasi)

---

## 📝 Catatan Penting:

- **JANGAN lupa hapus aplikasi lama dari HP!**
- Setelah perbaikan ini, aplikasi menggunakan Room Database yang proper
- Kalau masih ada masalah, kirim screenshot Logcat error
- Simpan file PERBAIKAN.md ini untuk referensi teknis

---

**Good luck! 🚀**

