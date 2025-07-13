# === Makefile ===
# Test
ENV_FILE=.env

up:
	docker compose --env-file $(ENV_FILE) up --build -d

down:
	docker compose down

logs:
	docker compose logs -f

restart:
	docker compose restart

ps:
	docker compose ps

db:
	docker exec -it jcs-postgres psql -U $$(grep DB_USER $(ENV_FILE) | cut -d '=' -f2) -d $$(grep DB_NAME $(ENV_FILE) | cut -d '=' -f2)

clean:
	docker compose down -v

dev:
	docker compose --env-file $(ENV_FILE) -f docker-compose.yaml up -d

prod:
	docker compose --env-file $(ENV_FILE) -f docker-compose.prod.yaml up -d

build-prod:
	docker compose --env-file $(ENV_FILE) -f docker-compose.prod.yaml up --build -d
