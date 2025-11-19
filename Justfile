# Util file for JCS Docker

set export
set dotenv-load

SERVICE_NAME := "jcs-backend"
COMPOSE_DEV_FILE := "docker-compose.dev.yaml"
COMPOSE_PROD_FILE := "docker-compose.prod.yaml"

list:
    just --list





########################### Development commands

# Start the container (does NOT recreate)
start:
    @echo "Starting ${SERVICE_NAME}..."
    docker compose -f ${COMPOSE_DEV_FILE} up -d --build

# Stop the running container (does NOT remove)
stop:
	@echo "Stopping ${SERVICE_NAME}..."
	docker compose -f ${COMPOSE_DEV_FILE} stop

# Restart the container (stop + start)
restart:
	@echo "Restarting ${SERVICE_NAME}..."
	docker compose -f ${COMPOSE_DEV_FILE} restart

# Clean everything (remove container, images, volumes)
clean:
	@echo "Cleaning ${SERVICE_NAME} container, images, and volumes..."
	docker compose -f ${COMPOSE_DEV_FILE} down --rmi all --volumes --remove-orphans
	mvn clean

# Show logs in real-time
logs:
	@echo "Showing logs for ${SERVICE_NAME}..."
	docker compose -f ${COMPOSE_DEV_FILE} logs -f
	
# Full rebuild inside dev container (clean + codegen + restart app)
rebuild:
    @echo "Rebuilding ${SERVICE_NAME} inside container..."
    docker compose -f ${COMPOSE_DEV_FILE} exec ${SERVICE_NAME} mvn clean
    @echo "Restarting container to run the rebuilt app..."
    docker compose -f ${COMPOSE_DEV_FILE} restart





########################### Production commands

# Generate jOOQ classes for production build (uses .env variables)
codegen-prod:
    @echo "Generating jOOQ classes for production..."
    mvn clean -DDB_URL="${DB_URL}" -DDB_USER="${DB_USER}" -DDB_PASSWORD="${DB_PASSWORD}" jooq-codegen:generate

# Create and build production container for the first time (generates jOOQ first, then builds image)
create-prod: codegen-prod
	@echo "Creating and building ${SERVICE_NAME} (prod)..."
	docker compose -f ${COMPOSE_PROD_FILE} up -d --build

# Start the existing production container (does NOT recreate)
start-prod:
	@echo "Starting ${SERVICE_NAME} (prod)..."
	docker compose -f ${COMPOSE_PROD_FILE} start

# Stop production container
stop-prod:
	@echo "Stopping ${SERVICE_NAME} (prod)..."
	docker compose -f ${COMPOSE_PROD_FILE} stop

# Restart production container
restart-prod:
	@echo "Restarting ${SERVICE_NAME} (prod)..."
	docker compose -f ${COMPOSE_PROD_FILE} restart

# Clean everything (remove container, images, volumes)
clean-prod:
	@echo "Cleaning ${SERVICE_NAME} (prod) container, images, and volumes..."
	docker compose -f ${COMPOSE_PROD_FILE} down --rmi all --volumes --remove-orphans

# Show production logs in real-time
logs-prod:
	@echo "Showing logs for ${SERVICE_NAME} (prod)..."
	docker compose -f ${COMPOSE_PROD_FILE} logs -f

# Full rebuild for production (clean containers + codegen + rebuild image)
rebuild-prod:
	@echo "Cleaning ${SERVICE_NAME} (prod) containers and images..."
	docker compose -f ${COMPOSE_PROD_FILE} down --rmi all --volumes --remove-orphans
	@echo "Generating jOOQ classes for production..."
	mvn clean -DDB_URL=${DB_URL} -DDB_USER=${DB_USER} -DDB_PASSWORD=${DB_PASSWORD} jooq-codegen:generate
	@echo "Rebuilding ${SERVICE_NAME} (prod)..."
	docker compose -f ${COMPOSE_PROD_FILE} up -d --build




		
########################### Extra commands

# Clean duplicate files created by macOS
clean-duplicates:
	@echo "Cleaning duplicate files..."
	find . -name "* 2.*" -type f -delete