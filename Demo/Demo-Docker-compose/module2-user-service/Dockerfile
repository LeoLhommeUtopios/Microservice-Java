# ===========================================
# Dockerfile multi-stage pour User Service
# ===========================================

# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml ./
COPY .mvn .mvn

# Télécharger les dépendances (mise en cache Docker)
RUN ./mvnw dependency:go-offline -B

# Copier le code source
COPY src src

# Builder l'application (sans les tests pour la rapidité)
RUN ./mvnw package -DskipTests

# ===========================================
# Stage 2: Runtime
# ===========================================
FROM eclipse-temurin:21-jre-alpine

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Port exposé
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s \
    CMD wget -q --spider http://localhost:8081/actuator/health || exit 1

# Point d'entrée
ENTRYPOINT ["java", "-jar", "app.jar"]
