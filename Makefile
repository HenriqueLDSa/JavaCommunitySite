# === Makefile ===
.PHONY: dev dev-local dev-remote clean logs down up restart ps db build-prod prod

# Environment files
ENV_FILE=.env

# === Development Commands ===

# Default development (local)
dev: dev-local

# Check if .env file exists
check-env:
	@if [ ! -f .env ]; then \
		echo "❌ Error: .env file not found"; \
		echo "Please create .env file: cp .env.example .env"; \
		exit 1; \
	fi

# Development with local PostgreSQL (Docker)
dev-local: check-env
	@echo "🐘 Starting with local PostgreSQL database..."
	source .env && SPRING_PROFILES_ACTIVE=local docker compose up --build -d

# Development with remote AWS RDS
dev-remote: check-env
	@echo "☁️  Starting with remote PostgreSQL database..."
	source .env && SPRING_PROFILES_ACTIVE=remote docker compose up --build -d

# === Standard Docker Commands ===

# Start with local profile (similar to dev-local but in background)
up: check-env
	@echo "Starting in background with local PostgreSQL..."
	source .env && SPRING_PROFILES_ACTIVE=local docker compose up --build -d

down:
	docker compose down

logs:
	docker compose logs -f

restart:
	docker compose restart

ps:
	docker compose ps

# === Database Commands ===

# Connect to local database (reads credentials from .env)
db: check-env
	@echo "Connecting to local PostgreSQL database..."
	@DB_USER=$$(grep '^DB_USER=' .env | cut -d '=' -f2) && \
	docker exec -it jcs-postgres psql -U $$DB_USER -d jcsdb

# === Production Commands ===

prod: check-env
	source .env && docker compose -f docker-compose.prod.yaml up -d

build-prod: check-env
	source .env && docker compose -f docker-compose.prod.yaml up --build -d

# === Cleanup Commands ===

clean:
	docker compose down -v
	docker system prune -f
