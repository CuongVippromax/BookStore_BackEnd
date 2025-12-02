# Tải maven bản 3.9.8
# ----------- STAGE 1: Build -----------
# Dùng JDK 17
FROM openjdk:17-ea-3-jdk-slim AS build

# Tạo 1 thư mục app để thc hiện những cái hành động copy các thứ ở dưới
WORKDIR /app

# Copy file pom.xml để vứt vào cái app vừa tạo ở trên
COPY pom.xml .

# Copy toàn bộ mã nguồn
COPY src ./src

# Build ứng dụng (bỏ qua test)
RUN mvn clean package -DskipTests


# ----------- STAGE 2: Runtime -----------
# Dùng Amazon Corretto 17 (JDK 17)
FROM openjdk:17-ea-3-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy file jar từ stage build
COPY --from=build /app/target/*.jar app.jar

# Lệnh chạy ứng dụng (khi chạy dòng lệnh docker run myapp )
ENTRYPOINT ["java", "-jar", "app.jar"]
