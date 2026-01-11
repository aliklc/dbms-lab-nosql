FROM eclipse-temurin:21-jdk
WORKDIR /app

# Maven kurulumu
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Proje dosyalarını kopyala
COPY pom.xml .
COPY src ./src

# Bağımlılıkları indir
RUN mvn dependency:go-offline

# Port
EXPOSE 8080

# Uygulamayı derle ve çalıştır
CMD ["mvn", "compile", "exec:java"]
