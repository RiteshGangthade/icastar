# iCastar Platform - Development Guide

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (optional - H2 in-memory database is configured for development)

### 1. Clone and Setup
```bash
git clone <repository-url>
cd icastar-main
```

### 2. Start Development Server
```bash
# Option 1: Use the startup script
./start-dev.sh

# Option 2: Manual start
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Access the Application
- **API Base URL**: http://localhost:8080/api
- **H2 Database Console**: http://localhost:8080/api/h2-console
  - JDBC URL: `jdbc:h2:mem:icastar_dev`
  - Username: `sa`
  - Password: (leave empty)
- **Actuator Health**: http://localhost:8080/api/actuator/health

## 🏗️ Project Structure

```
src/main/java/com/icastar/platform/
├── config/                 # Configuration classes
│   ├── SecurityConfig.java
│   └── ArtistTypeDataInitializer.java
├── controller/             # REST Controllers
│   ├── AuthController.java
│   ├── UserController.java
│   ├── ArtistController.java
│   └── PublicArtistTypeController.java
├── dto/                   # Data Transfer Objects
│   ├── auth/
│   ├── user/
│   └── artist/
├── entity/                # JPA Entities
│   ├── BaseEntity.java
│   ├── User.java
│   ├── ArtistProfile.java
│   ├── ArtistType.java
│   └── ...
├── repository/            # Data Access Layer
├── service/               # Business Logic
├── security/              # Security Configuration
├── exception/             # Exception Handling
└── IcastarPlatformApplication.java
```

## 🔧 Development Configuration

### Application Profiles
- **dev**: Development with H2 in-memory database
- **test**: Testing with H2 in-memory database
- **prod**: Production with MySQL database

### Key Configuration Files
- `application.yml`: Main configuration
- `application-dev.yml`: Development overrides
- `application-test.yml`: Test configuration

## 🎭 Dynamic Artist System

The platform supports multiple artist types with custom fields:

### Supported Artist Types
1. **Actor**: Height, weight, body type, hair color, eye color, languages, experience
2. **Dancer**: Dance styles, training background, performance experience, choreography skills
3. **Singer**: Vocal range, music genres, instruments, recording experience
4. **Director**: Directing experience, project types, equipment owned, team size
5. **Writer**: Writing experience, writing types, languages, published works
6. **DJ/RJ**: Music genres, equipment owned, venue experience, mixing skills
7. **Band**: Band size, music genres, instruments, performance experience
8. **Model**: Height, weight, body measurements, modeling types, portfolio
9. **Photographer**: Photography types, equipment owned, editing software
10. **Videographer**: Video types, equipment owned, editing software, drone license

### Adding New Artist Types
1. Add new artist type in `ArtistTypeDataInitializer.java`
2. Define custom fields for the new type
3. The system automatically handles the new type without code changes

## 🔐 Authentication System

### OTP-based Authentication
- Send OTP: `POST /api/auth/otp/send`
- Verify OTP: `POST /api/auth/otp/verify`
- Register: `POST /api/auth/register`

### JWT Token
- All authenticated requests require: `Authorization: Bearer <token>`
- Token expiration: 24 hours (configurable)

## 📊 API Endpoints

### Public APIs (No Authentication)
- `GET /api/public/artist-types` - Get all artist types
- `GET /api/public/artist-types/{id}/fields` - Get artist type fields

### Authentication APIs
- `POST /api/auth/otp/send` - Send OTP
- `POST /api/auth/otp/verify` - Verify OTP
- `POST /api/auth/register` - User registration
- `POST /api/auth/forgot-password` - Forgot password
- `POST /api/auth/reset-password` - Reset password

### User Management
- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update user profile
- `POST /api/users/verify-email` - Verify email

### Artist Management
- `POST /api/artists/profile` - Create artist profile
- `GET /api/artists/profile` - Get current artist profile
- `PUT /api/artists/profile` - Update artist profile
- `GET /api/artists/search` - Search artists
- `GET /api/artists/type/{id}` - Get artists by type
- `GET /api/artists/verified` - Get verified artists

### Admin APIs (Admin Role Required)
- `GET /api/admin/artist-types` - Manage artist types
- `POST /api/admin/artist-types` - Create artist type
- `POST /api/admin/artist-types/{id}/fields` - Create artist type field

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ArtistServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Configuration
- Uses H2 in-memory database
- Test profile: `application-test.yml`
- JWT secret: `test-secret-key`

## 🗄️ Database

### Development (H2)
- In-memory database
- Auto-creates schema on startup
- Access via H2 Console: http://localhost:8080/api/h2-console

### Production (MySQL)
- Use provided `database-schema.sql`
- Configure in `application.yml`

### Key Tables
- `users` - User accounts
- `artist_profiles` - Artist information
- `artist_types` - Artist type definitions
- `artist_type_fields` - Dynamic field definitions
- `artist_profile_fields` - Dynamic field values

## 🔧 Development Tools

### Logging
- Logs: `logs/icastar-dev.log`
- Console output with timestamps
- SQL queries logged in development

### Actuator Endpoints
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- All endpoints: `/actuator`

### H2 Console
- URL: http://localhost:8080/api/h2-console
- JDBC URL: `jdbc:h2:mem:icastar_dev`
- Username: `sa`
- Password: (empty)

## 🚀 Deployment

### Development
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production
```bash
mvn clean package
java -jar target/icastar-platform-1.0.0.jar --spring.profiles.active=prod
```

### Docker (Future)
```bash
docker build -t icastar-platform .
docker run -p 8080:8080 icastar-platform
```

## 🔍 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   # Kill process using port 8080
   lsof -ti:8080 | xargs kill -9
   ```

2. **Database connection issues**
   - Check MySQL is running
   - Verify credentials in `application.yml`
   - Use H2 for development: `spring.profiles.active=dev`

3. **JWT token issues**
   - Check JWT secret in configuration
   - Verify token format: `Bearer <token>`
   - Check token expiration

4. **Artist type not found**
   - Run application to auto-initialize artist types
   - Check `ArtistTypeDataInitializer.java`

### Debug Mode
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

## 📝 Code Style

### Naming Conventions
- Classes: PascalCase (`UserService`)
- Methods: camelCase (`findByEmail`)
- Variables: camelCase (`userName`)
- Constants: UPPER_SNAKE_CASE (`MAX_RETRY_COUNT`)

### Package Structure
- `controller`: REST endpoints
- `service`: Business logic
- `repository`: Data access
- `entity`: JPA entities
- `dto`: Data transfer objects
- `config`: Configuration classes
- `security`: Security configuration
- `exception`: Exception handling

## 🔮 Next Steps

### Immediate Development Tasks
1. ✅ Authentication system
2. ✅ User management
3. ✅ Artist type system
4. ✅ Basic artist profiles
5. 🔄 Job management system
6. 🔄 Application system
7. 🔄 Messaging system
8. 🔄 Payment integration
9. 🔄 Admin panel
10. 🔄 File upload system

### Future Enhancements
- Mobile app development
- Advanced search with Elasticsearch
- Real-time notifications
- Video interview integration
- AI-powered matching
- Advanced analytics

## 📞 Support

For development questions:
1. Check this guide
2. Review API documentation
3. Check logs in `logs/icastar-dev.log`
4. Use H2 console for database inspection
5. Create an issue in the repository

---

**Happy Coding! 🎭✨**
