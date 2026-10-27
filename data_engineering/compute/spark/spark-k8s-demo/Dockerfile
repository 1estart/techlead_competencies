# Базовый образ: официальный, с Scala 2.13 и Java 17
FROM apache/spark:3.5.1

# Копируем наш тонкий JAR
COPY app.jar /opt/spark/jobs/app.jar

# Пользователь по умолчанию уже есть в образе (не нужно создавать)
WORKDIR /opt/spark