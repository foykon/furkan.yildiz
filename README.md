# Movie Portal (Spring Boot + React)

Kişisel öğrenme ve OBSS Staj programı kapsamında geliştirilen **Film Portalı**. Backend **Spring Boot (JWT + Refresh Token)**, frontend **React** ile yazılmıştır. Proje içerisinde haftalık ilerlemeler ve PoC’ler için klasörler, ayrıca çalışır bir **frontend** uygulaması bulunur.

## Mimari ve Klasör Yapısı
.
├── FirstWeek/ # 1. hafta alıştırmalar / notlar
├── SecondWeek/ # 2. hafta alıştırmalar / notlar
├── ThirdWeek/ # 3. hafta alıştırmalar / notlar
├── Project/ # Backend ana modüller (Spring Boot)
└── frontend/
└── movie-portal/ # React istemci


## Özellikler
- **Auth:** JWT Access + Refresh Token, BCrypt şifreleme
- **Role Management:** Dinamik role entity, role-based authorization
- **Movie Modülü:** Film ekleme/silme/güncelleme (Admin), arama, filtreleme, sayfalama
- **Listeler:** Watch list & Favorite list yönetimi, sıralama
- **Diğer:** Global Exception Handling, Logging (AOP), E-mail gönderimi (SMTP/Gmail), MySQL + Docker desteği

## Teknolojiler
**Backend**
- Java 17/21, Spring Boot
- Spring Security, JPA/Hibernate
- MySQL, Lombok, Validation

**Frontend**
- React + React Router
- Ant Design UI
- Axios / Fetch API

## Kurulum ve Çalıştırma
### Veritabanı
```sql
CREATE DATABASE movie_portal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
Backend

cd Project
mvn clean package -DskipTests
mvn spring-boot:run
Frontend

cd frontend/movie-portal
npm install
npm run dev
API Örnekleri
Auth
Login


POST /auth/login
{
  "username": "furkan",
  "password": "secret"
}
Refresh



POST /auth/refresh
Movies


GET /api/movies?title=Inception&page=0&size=10


POST /api/movies   # (Admin)
Yol Haritası
 Role/permission matrisini config’den yönetilebilir hale getirmek

 E-posta doğrulama & şifre sıfırlama akışı

 Testcontainers ile integration testler

 Docker Compose (db + api + frontend)
