# Java Community Site

A modern, AT Protocol-powered forum application built with Spring Boot, featuring real-time updates via WebSocket integration with Bluesky's Jetstream service. This project demonstrates a full-stack Java application with server-side rendering, PostgreSQL database, and comprehensive Docker deployment.

## 🌟 Features

### Core Functionality
- **Question & Answer System**: Post questions, provide answers, and engage in threaded discussions
- **AT Protocol Integration**: Full integration with the AT Protocol ecosystem via Bluesky's Jetstream
- **Real-time Updates**: WebSocket connection to Jetstream for live content synchronization
- **User Authentication**: Secure authentication system with AT Protocol identity management
- **Profile Management**: User profiles with customizable avatars, display names, and bios
- **Search**: Full-text search across questions and answers
- **Notifications**: Real-time notification system for replies, mentions, and interactions
- **User Settings**: Customizable notification preferences and user preferences

### Admin Features
- **Content Moderation**: Hide/unhide posts, replies, and users
- **Role-based Access Control**: Admin role system with granular permissions
- **Tag Management**: Create and manage question tags
- **Moderation Tracking**: Audit trail for all moderation actions with reasons

### Technical Features
- **Server-side Rendering**: JTE templating engine for fast, type-safe HTML generation
- **Type-safe Database Access**: jOOQ for compile-time SQL verification
- **Markdown Support**: CommonMark for rich text formatting in posts and replies
- **Database Migrations**: MyBatis migrations for version-controlled schema changes
- **Docker Support**: Full containerization with development and production configurations
- **Security**: Spring Security with BCrypt password encoding

## 🛠️ Technology Stack

### Backend
- **Java 21** - Modern Java with latest LTS features
- **Spring Boot 3.4.4** - Application framework
  - Spring Web - REST endpoints and MVC
  - Spring Security - Authentication and authorization
  - Spring Boot jOOQ - Database integration
  - Spring DevTools - Development hot-reload

### Database
- **PostgreSQL** - Primary data store with full ACID compliance
- **jOOQ 3.20.8** - Type-safe SQL builder and code generator
- **HikariCP** - High-performance connection pooling
- **MyBatis Migrations** - Database version control

### Frontend
- **JTE 3.2.1** - Type-safe Java Template Engine for server-side rendering
- **HTMX** - Dynamic page updates without full page reloads
- **Vanilla CSS** - Custom styling without frameworks
- **Prism.js** - Syntax highlighting for code blocks

### External Services
- **AT Protocol / Bluesky** - Decentralized social protocol integration
- **Jetstream WebSocket** - Real-time event streaming from Bluesky network
- **Java-WebSocket 1.6.0** - WebSocket client implementation

### DevOps
- **Docker & Docker Compose** - Containerization
- **Maven 3.9.9** - Build automation and dependency management
- **Just** - Command runner for simplified Docker operations
- **NGINX** - Reverse proxy for production deployment

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **Docker** and **Docker Compose**
- **PostgreSQL 16** (or use Docker Compose)
- **Just** (optional, for convenient commands)
- **Git**

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Snuddles/JavaCommunitySite.git
cd JavaCommunitySite
```

### 2. Set Up Environment Variables

Create a `.env` file in the project root:

```env
DB_URL=jdbc:postgresql://your-database-host:5432/your-database-name
DB_USER=your-database-user
DB_PASSWORD=your-database-password
```

### 3. Database Setup

#### Option A: Using Docker Compose (Recommended for Development)

The development Docker Compose configuration can include a PostgreSQL container. Update `docker-compose.dev.yaml` to add a database service if needed.

#### Option B: External Database

If using an external PostgreSQL instance:

1. Create a database:
   ```sql
   CREATE DATABASE javacommunitysite;
   ```

2. Run migrations (see [Database Migrations](#database-migrations) section below)

### 4. Generate jOOQ Classes

Before running the application, generate the jOOQ database classes:

```bash
mvn clean jooq-codegen:generate -DDB_URL="${DB_URL}" -DDB_USER="${DB_USER}" -DDB_PASSWORD="${DB_PASSWORD}"
```

### 5. Run the Application

#### Using Just (Recommended)

Development mode:
```bash
just start
```

Production mode:
```bash
just create-prod
```

#### Using Docker Compose Directly

Development:
```bash
docker compose -f docker-compose.dev.yaml up -d --build
```

Production:
```bash
docker compose -f docker-compose.prod.yaml up -d --build
```

#### Using Maven (Without Docker)

```bash
mvn spring-boot:run
```

The application will be available at:
- **Development**: http://localhost:8081
- **Production**: http://localhost:8080

## 📚 Database Migrations

This project uses MyBatis Migrations for database schema management.

### Setup Migrations

1. Navigate to the migrations directory:
   ```bash
   cd migrations
   ```

2. Set up environment (one-time setup):
   
   **Windows:**
   ```powershell
   .\setup-env.ps1
   ```
   
   **macOS/Linux:**
   ```bash
   ./setup-env.sh
   ```

### Common Migration Commands

Check migration status:
```bash
migrate status --path=./migrations
```

Apply pending migrations:
```bash
migrate up --path=./migrations
```

Rollback last migration:
```bash
migrate down --path=./migrations
```

### Creating New Migrations

1. Create a new SQL file in `migrations/scripts/` following the naming convention:
   ```
   <YYYYMMDDHHMMSS>_<description>.sql
   ```
   Example: `20251120120000_add_user_badges.sql`

2. Write your SQL changes in the `-- // @UNDO` section for rollbacks

3. Apply the migration:
   ```bash
   migrate up --path=./migrations
   ```

For detailed migration documentation, see:
- `migrations/README.md`
- `migrations/How_To_Use.md`
- `migrations/Windows-Setup-Guide.md` or `migrations/macOS-Linux-Setup-Guide.md`

## 🐳 Docker Commands (Using Just)

The `Justfile` provides convenient commands for Docker operations:

### Development Commands

```bash
just list              # List all available commands
just start             # Start development container
just stop              # Stop development container
just restart           # Restart development container
just clean             # Remove containers, images, and volumes
just logs              # View container logs
just rebuild           # Full rebuild inside container
```

### Production Commands

```bash
just codegen-prod      # Generate jOOQ classes for production
just create-prod       # Build and start production container
just start-prod        # Start existing production container
just stop-prod         # Stop production container
just restart-prod      # Restart production container
just clean-prod        # Remove production containers and images
just logs-prod         # View production logs
just rebuild-prod      # Full production rebuild
```

## 🏗️ Project Structure

```
JavaCommunitySite/
├── src/
│   ├── main/
│   │   ├── java/com/jcs/javacommunitysite/
│   │   │   ├── atproto/           # AT Protocol integration
│   │   │   │   ├── jetstream/     # WebSocket event handlers
│   │   │   │   ├── records/       # AT Protocol record types
│   │   │   │   └── service/       # Session management
│   │   │   ├── database/          # Database utilities
│   │   │   ├── forms/             # Form models
│   │   │   ├── pages/             # Page controllers
│   │   │   │   ├── answerpage/    # Answer viewing
│   │   │   │   ├── askpage/       # Question posting
│   │   │   │   ├── indexpage/     # Homepage
│   │   │   │   ├── notifications/ # Notifications
│   │   │   │   ├── postpage/      # Question detail
│   │   │   │   ├── profilepage/   # User profiles
│   │   │   │   └── searchpage/    # Search functionality
│   │   │   ├── security/          # Security configuration
│   │   │   └── util/              # Utilities
│   │   ├── jte/                   # JTE templates
│   │   │   ├── components/        # Reusable UI components
│   │   │   ├── layout/            # Layout templates
│   │   │   └── pages/             # Page templates
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── atproto/           # AT Protocol lexicons
│   │       └── static/            # CSS, JS, images
│   └── test/                      # Test files
├── migrations/                    # Database migrations
│   ├── scripts/                   # Migration SQL files
│   └── environments/              # Environment configs
├── target/                        # Build output
│   └── generated-sources/
│       └── jooq/                  # Generated jOOQ classes
├── docker-compose.dev.yaml        # Development Docker config
├── docker-compose.prod.yaml       # Production Docker config
├── Dockerfile.dev                 # Development Dockerfile
├── Dockerfile.prod                # Production Dockerfile
├── Justfile                       # Command shortcuts
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

## 🔧 Configuration

### Application Properties

The application uses profile-based configuration:

- `application.properties` - Shared configuration
- `application-dev.properties` - Development overrides
- `application-prod.properties` - Production overrides

Key configuration areas:
- **Database**: Connection pooling, SSL settings
- **Security**: User credentials, authentication
- **Server**: Port configuration
- **Logging**: Log levels for different packages

### Environment Variables

Required environment variables:

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_URL` | JDBC connection URL | `jdbc:postgresql://localhost:5432/jcs` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `your-secure-password` |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `dev` or `prod` |

## 🌐 Deployment

### Local Development

1. Use `just start` for hot-reload development environment
2. Application runs on port 8081
3. Spring DevTools enabled for auto-restart

### Production Deployment (AWS EC2 Example)

See `javacommunitysite_ec2_setup.md` for detailed AWS EC2 deployment guide including:

- EC2 instance setup
- Docker installation
- NGINX reverse proxy configuration
- SSL/TLS with Let's Encrypt
- DNS configuration
- Auto-renewal setup

Quick production deployment steps:

```bash
# 1. Generate jOOQ classes locally
just codegen-prod

# 2. Build and start production container
just create-prod

# 3. Configure NGINX as reverse proxy (see deployment guide)

# 4. Set up SSL with Certbot
sudo certbot --nginx -d yourdomain.com
```

## 🧪 Testing

Run tests with Maven:

```bash
mvn test
```

Run tests with coverage:

```bash
mvn clean test jacoco:report
```

## 📊 Database Schema

The application uses PostgreSQL with the following main tables:

- **user** - User accounts and profiles (AT Protocol DIDs)
- **post** - Questions posted to the forum
- **reply** - Answers and threaded replies
- **notification** - User notification system
- **role** - Admin role definitions
- **user_role** - User role assignments
- **tags** - Question categorization
- **hidden_user/hidden_post/hidden_reply** - Content moderation tracking
- **user_settings** - User preferences and notification settings
- **atproto_log** - AT Protocol event logging

All tables use AT Protocol URIs (ATURIs) as primary keys for distributed compatibility.

## 🔐 Security

- **Spring Security** with custom authentication
- **BCrypt** password hashing
- **CSRF protection** (disabled for API endpoints in current config)
- **Database SSL** enabled for production connections
- **Environment-based secrets** via `.env` files
- **Role-based access control** for admin features

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java naming conventions
- Write meaningful commit messages
- Create migrations for all schema changes
- Test locally before submitting PR
- Update documentation for new features

## 📝 API Endpoints

### Public Endpoints

- `GET /` - Homepage with question feed
- `GET /post/{userDid}/{postRKey}` - View question and answers
- `GET /search` - Search questions
- `GET /pfp/{did}` - View user profile
- `GET /api/heartbeat` - Health check endpoint

### Authenticated Endpoints

- `POST /login` - User authentication
- `POST /logout` - User logout
- `GET /ask` - Create question page
- `POST /answer` - Submit answer
- `GET /notifications` - View notifications
- `GET /settings` - User settings

### Admin Endpoints

- `POST /answer/htmx/hideReply` - Hide reply (admin)
- `POST /answer/htmx/unhideReply` - Unhide reply (admin)
- Admin moderation actions for users and posts

## 🐛 Troubleshooting

### jOOQ Generation Fails

Ensure database is accessible and environment variables are set correctly:
```bash
mvn clean jooq-codegen:generate -DDB_URL="${DB_URL}" -DDB_USER="${DB_USER}" -DDB_PASSWORD="${DB_PASSWORD}"
```

### Docker Container Won't Start

Check logs:
```bash
just logs
```

Verify `.env` file exists and contains correct values.

### Database Connection Issues

- Verify PostgreSQL is running
- Check firewall/security group settings
- Ensure SSL mode matches database configuration
- Test connection with psql:
  ```bash
  psql -h hostname -U username -d database
  ```

### Port Already in Use

Change port in `docker-compose.dev.yaml` or `application.properties`:
```yaml
ports:
  - '8082:8080'  # Change 8081 to 8082
```

## 📄 License

This project is currently unlicensed. All rights reserved.

## 👥 Authors

- Snuddles - [GitHub](https://github.com/Snuddles)

## 🙏 Acknowledgments

- AT Protocol team for the decentralized social protocol
- Bluesky for Jetstream WebSocket service
- Spring Boot community
- jOOQ team for excellent SQL library
- JTE team for type-safe templating

## 📞 Support

For issues, questions, or contributions:
- Open an issue on [GitHub](https://github.com/Snuddles/JavaCommunitySite/issues)
- Contact the project maintainers

## 🗺️ Roadmap

Future enhancements planned:
- [ ] Enhanced search with filters
- [ ] User reputation system
- [ ] Question voting system
- [ ] Code syntax highlighting in answers
- [ ] Email notifications
- [ ] Mobile-responsive design improvements
- [ ] API rate limiting
- [ ] GraphQL API
- [ ] Internationalization (i18n)
- [ ] Dark mode support

---

**Live Site**: [javacommunitysite.xyz](https://javacommunitysite.xyz)

Built with ☕ and ❤️ using Java
