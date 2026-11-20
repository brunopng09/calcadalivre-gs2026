# Usa a imagem oficial do Java (Eclipse Temurin) versao 21 em Alpine
FROM eclipse-temurin:21-jdk-alpine

# Define o diretorio de trabalho dentro do container
WORKDIR /app

# Copia todos os arquivos do projeto para dentro do container
COPY . ./

# Da permissao de execucao ao Maven Wrapper
RUN chmod +x mvnw

# Faz o build do projeto Spring Boot com Maven
# -DskipTests -> ignora os testes durante o deploy
RUN ./mvnw -B -DskipTests clean package

# Expoe a porta utilizada pela aplicacao
EXPOSE 8080

# Inicia a aplicacao Spring Boot
# O Spring Boot gera o JAR executavel dentro da pasta target
# -Djava.net.preferIPv4Stack=true garante bind em IPv4 (0.0.0.0), exigido pelo
# health check e pelo scanner de portas do Render
CMD ["sh", "-c", "java -Djava.net.preferIPv4Stack=true -jar target/*.jar"]
