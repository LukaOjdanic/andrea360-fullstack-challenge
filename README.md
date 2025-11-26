# Fitness Aplikacija - Andrea360 Fullstack Challenge - Luka Ojdanić

## Opis Funkcionalnosti

Ova aplikacija predstavlja kompletan sistem za upravljanje fitnes centrima sa podrškom za više lokacija, zakazivanje termina i online plaćanje putem Stripe integracije.

### Glavne Funkcionalnosti

#### 1. **Autentifikacija i Autorizacija**
- JWT-bazirana autentifikacija
- Tri nivoa korisnika: Admin, Zaposleni (Employee), i Članovi (Member)
- Sigurna registracija i prijava

#### 2. **Admin Panel**
- Kreiranje i upravljanje lokacijama fitnes centara
- Pregled i registracija zaposlenih za određene lokacije 

#### 3. **Zaposleni (Employee) Funkcionalnosti**
- Kreiranje novih članova
- Kreiranje i upravljanje uslugama (services)
- Zakazivanje termina za treninge
- Pregled dostupnih mjesta na terminima
- Pregled liste članova po lokaciji

#### 4. **Članovi (Member) Funkcionalnosti**
- Kupovina paketa treninga putem Stripe plaćanja
- Rezervacija termina za treninge
- Pregled dostupnih termina i slobodnih mjesta
- Praćenje sopstvenih rezervacija i preostalih treninga

#### 5. **Real-time Ažuriranja**
- Automatsko ažuriranje broja slobodnih mjesta nakon svake rezervacije

#### 6. **Stripe Integracija**
- Kreiranje Payment Intent-a za online plaćanja
- Payment intent može da bude primjećen preko Stripe Dashboard-a

- ```stripe listen --forward-to localhost:4242/webhook``` 
primi signalizaciju o uspješnom plaćanju ali daje error

Primjer: 
```
➜  fitness git:(main) ✗ stripe listen --forward-to localhost:4242/webhook
A newer version of the Stripe CLI is available, please update to: v1.33.0
> Ready! You are using Stripe API Version [2025-11-17.clover]. Your webhook signing secret is whsec_636ca95fc355b2c1ac9c362a8bff4c9446d4dca5859f018153c758a2e119aae3 (^C to quit)
2025-11-26 21:25:33   --> payment_intent.created [evt_3SXpHMRwmn2MoXmQ15IuQsXO]
2025-11-26 21:25:33            [ERROR] Failed to POST: Post "http://localhost:4242/webhook": dial tcp [::1]:4242: connect: connection refused
```

- Moguće je testirati payment intent sa komandom:
```
stripe trigger payment_intent.succeeded
```


## Tehnologije

### Backend
- **Spring Boot 3.5.7** - Glavni framework
- **Java 21** - Programski jezik
- **PostgreSQL** - Produkciona baza podataka
- **H2** - In-memory baza za testiranje
- **Spring Security** - Autentifikacija i autorizacija
- **JWT** - Token-bazirana autentifikacija
- **Spring Data JPA** - ORM i pristup bazi
- **WebSocket** - Real-time komunikacija
- **Stripe Java SDK** - Integracija plaćanja
- **Thymeleaf** - Server-side template engine
- **SpringDoc OpenAPI** - API dokumentacija

### Testiranje
- **JUnit 5** - Unit testovi
- **Mockito** - Mocking framework
- **Spring Boot Test** - Integracioni testovi
- **MockMvc** - Testiranje REST API-ja

## Uputstvo za Pokretanje Aplikacije

### Preduslov
- **Docker** i **Docker Compose** instalirani
- **Java 21** (za lokalni development)
- **Maven** (uključen u projekat kao Maven Wrapper)

### Pokretanje sa Docker-om (Preporučeno)

1. **Klonirajte projekat:**
```bash
git clone <repository-url>
cd fitness
```

2. **Pokrenite aplikaciju:**
```bash
docker-compose up -d --build
```

Aplikacija će biti dostupna na:
- **Backend API:** http://localhost:8080
- **Swagger UI (API Dokumentacija):** http://localhost:8080/swagger-ui.html
- **Baza podataka:** localhost:5432

3. **Zaustavljanje aplikacije:**
```bash
docker-compose down
```

### Lokalno Pokretanje (bez Docker-a)

1. **Pokrenite PostgreSQL bazu:**
```bash
docker run -d \
  --name fitness-db \
  -e POSTGRES_DB=fitness \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=pass \
  -p 5432:5432 \
  postgres:15
```

2. **Pokrenite aplikaciju (bez Docker-a i databaze):**
```bash
./mvnw spring-boot:run
```

### Pokretanje Testova

```bash
# Svi testovi
./mvnw test

# Specifičan test (na primjer MemberServiceImplTest)
./mvnw -Dtest=MemberServiceImplTest test

# Sa detaljnim izvještajem
./mvnw test -X
```

Testovi koriste H2 in-memory bazu podataka i ne zahtijevaju PostgreSQL.

## Podaci za Testiranje

### Testni Nalozi

Aplikacija se pokreće sa predefinisanim korisnicima:

#### **Admin Nalog**
- **Email:** admin@andrea360.com
- **Lozinka:** password
- **Uloga:** Administrator sistema
- **Privilegije:** Kreiranje lokacija, registracija zaposlenih

#### **Zaposleni (Employee) Nalog**
- **Email:** jelena@andrea360.com
- **Lozinka:** password
- **Uloga:** Zaposleni
- **Privilegije:** Kreiranje članova, usluga i termina

#### **Član (Member) Nalog**
- **Email:** marko@andrea360.com
- **Lozinka:** password
- **Uloga:** Član
- **Privilegije:** Kupovina paketa, rezervacija termina (nedovršeno)

### Testiranje API-ja

- Testiranje API-ja može se izvrsiti putem Swagger UI-a (http://localhost:8080/swagger-ui.html) uz pomoć postman kolekcije koja se nalazi u folderu postman.


## Struktura Projekta

```
fitness/
├── src/
│   ├── main/
│   │   ├── java/com/andrea/fitness/
│   │   │   ├── config/          # Konfiguracija (Security, WebSocket)
│   │   │   ├── controller/      # REST i View kontroleri
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── model/           # JPA entiteti
│   │   │   ├── repository/      # Spring Data repozitorijumi
│   │   │   ├── security/        # JWT i Security komponente
│   │   │   ├── service/         # Biznis logika
│   │   │   └── websocket/       # WebSocket kontroleri
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql         # Inicijalni podaci
│   │       ├── static/          # CSS, JS, slike
│   │       └── templates/       # Thymeleaf
│   └── test/
│       ├── java/                # Unit i integracioni testovi
│       └── resources/
│           └── application.properties  # Test konfiguracija
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## API Dokumentacija

Kompletan API je dokumentovan pomoću OpenAPI/Swagger specifikacije.

**Pristup dokumentaciji:** http://localhost:8080/swagger-ui.html

### Glavni Endpoint-i

#### Autentifikacija
- `POST /api/auth/login` - Prijava korisnika

#### Admin
- `POST /api/admin/locations` - Kreiranje lokacije
- `POST /api/admin/employees` - Registracija zaposlenog
- `GET /api/admin/users` - Pregled svih korisnika

#### Zaposleni
- `POST /api/employee/members` - Kreiranje člana
- `POST /api/employee/services` - Kreiranje usluge
- `POST /api/employee/appointments` - Kreiranje termina
- `GET /api/employee/appointments` - Pregled termina

#### Članovi
- `POST /api/member/payment-intent` - Kreiranje plaćanja
- `POST /api/member/reserve` - Rezervacija termina
- `GET /api/member/appointments` - Pregled dostupnih termina
- `GET /api/member/reservations` - Moje rezervacije

#### Javni
- `GET /api/public/locations` - Lista lokacija
- `GET /api/public/services` - Lista usluga
- `GET /api/public/appointments` - Dostupni termini

## Testovi

Projekat sadrži sveobuhvatan skup testova:

### Unit Testovi
- **AuthServiceImplTest** - Testiranje registracije korisnika
- **MemberServiceImplTest** - Testiranje rezervacija
- **EmployeeServiceImplTest** - Testiranje kreiranja članova i termina
- **AppointmentServiceImplTest** - Testiranje upravljanja terminima

### Integracioni Testovi
- **AuthControllerIntegrationTest** - End-to-end testiranje autentifikacije
- **FitnessApplicationTests** - Testiranje pokretanja aplikacije

**Pokrivenost:** 18 testova pokriva ključne funkcionalnosti sistema.

## Konfiguracija

### Baza Podataka
```properties
spring.datasource.url=jdbc:postgresql://db:5432/fitness
spring.datasource.username=user
spring.datasource.password=pass
```

### JWT
```properties
jwt.secret=<base64-encoded-secret>
jwt.expiration=86400000  # 24 sata
```

### Stripe
```properties
stripe.api.key=sk_test_...
```

## Napomene
- Aplikacija automatski kreira šemu baze i popunjava inicijalne podatke pri pokretanju
- Svi endpoint-i osim `/api/auth/login` i `/api/public/**` zahtijevaju autentifikaciju
